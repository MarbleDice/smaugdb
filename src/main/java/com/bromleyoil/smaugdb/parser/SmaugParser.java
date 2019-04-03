package com.bromleyoil.smaugdb.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.Pop;
import com.bromleyoil.smaugdb.model.Room;
import com.bromleyoil.smaugdb.model.Spawn;
import com.bromleyoil.smaugdb.model.World;

public class SmaugParser {

	private static final Logger log = LoggerFactory.getLogger(SmaugParser.class);

	private static final Pattern vnumPattern = Pattern.compile("^\\s*#\\s*([1-9]\\d*)\\s*$");
	private static final Pattern stringPattern = Pattern.compile("^(.+)~$");
	private static final Pattern resetPattern = Pattern.compile("^\\s*([OPMEG])\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*(\\d+)?");

	private World world;
	private Area area;

	/** The current line of text from the area file */
	private String line;

	/** The last mob processed while parsing resets */
	private Mob lastMob;

	private SmaugParser(World world) {
		// Private constructor
		this.world = world;
	}

	public static void parseWorld(World world, String path) {
		// Create a parser to store the parsing context
		SmaugParser parser = new SmaugParser(world);

		String areaPath = path + File.separator + "area" + File.separator + "manor.are";

		parser.parseArea(areaPath);
	}

	private void parseArea(String areaPath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(areaPath))) {
			nextLine(reader);

			while (line != null) {

				if (line.startsWith("#AREA")) {
					parseArea(reader);
				} else if (line.startsWith("#RANGES")) {
					parseRanges(reader);
				} else if (line.startsWith("#MOBILES")) {
					parseVnumBlock(reader, this::parseMobile);
				} else if (line.startsWith("#OBJECTS")) {
					parseVnumBlock(reader, this::parseObject);
				} else if (line.startsWith("#ROOMS")) {
					parseVnumBlock(reader, this::parseRoom);
				} else if (line.startsWith("#RESETS")) {
					parseResets(reader);
				} else {
					nextLine(reader);
				}
			}
		} catch (IOException e) {
			log.info("Could not read: " + areaPath, e);
		}
	}

	private void parseArea(BufferedReader reader) {
		Matcher matcher = matches(line, "#AREA\\s+([^~]+)~")
				.orElseThrow(() -> new ParseException("Invalid #AREA line: " + line));

		area = new Area();
		area.setName(matcher.group(1));
		world.getAreas().put(area.getName(), area);

		nextLine(reader);
	}

	private void parseRanges(BufferedReader reader) {
		nextLine(reader);

		Matcher matcher = matches(line, "(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)")
				.orElseThrow(() -> new ParseException("Invalid #RANGES line: " + line));

		area.setLowSoftRange(Integer.valueOf(matcher.group(1)));
		area.setHighSoftRange(Integer.valueOf(matcher.group(2)));
	}

	private void parseVnumBlock(BufferedReader reader, BiConsumer<BufferedReader, Integer> vnumParser) {
		nextLine(reader);

		while (line != null) {
			Matcher matcher = vnumPattern.matcher(line);
			if (matcher.matches()) {
				// Found a vnum, read a mob
				vnumParser.accept(reader, Integer.valueOf(matcher.group(1)));
			} else if (line.startsWith("#")) {
				// We hit the end of the section
				return;
			} else {
				// Keep moving down to look for the next vnum
				nextLine(reader);
			}
		}
	}

	private void parseMobile(BufferedReader reader, int vnum) {
		Mob mob = new Mob();
		mob.setVnum(vnum);
		nextLine(reader);
		mob.setKeywords(matchString(line)
				.orElseThrow(() -> new ParseException("Invalid keywords: " + line)));
		nextLine(reader);
		mob.setName(matchString(line)
				.orElseThrow(() -> new ParseException("Invalid name: " + line)));

		world.addMob(mob, area);
	}

	private void parseObject(BufferedReader reader, int vnum) {
		Item item = new Item();
		item.setVnum(vnum);
		nextLine(reader);
		item.setKeywords(matchString(line)
				.orElseThrow(() -> new ParseException("Invalid keywords: " + line)));
		nextLine(reader);
		item.setName(matchString(line)
				.orElseThrow(() -> new ParseException("Invalid name: " + line)));

		world.addItem(item, area);
	}

	private void parseRoom(BufferedReader reader, int vnum) {
		Room room = new Room();
		room.setVnum(vnum);

		nextLine(reader);
		room.setName(matchString(line)
				.orElseThrow(() -> new ParseException("Invalid name: " + line)));

		world.addRoom(room, area);
	}

	private void parseResets(BufferedReader reader) {
		nextLine(reader);

		while (line != null) {
			Matcher matcher = resetPattern.matcher(line);
			if (matcher.matches()) {
				log.trace("Got \"{}\": v2={} v3={} v4={} n={}", line, matcher.group(3), matcher.group(4),
						matcher.group(5), matcher.groupCount());
				// Found a reset. Groups are 1=code, 2=ignored, 3=vnum, 4=limit/ignored, 5=vnum/absent
				parseReset(matcher.group(1).charAt(0),
						Integer.valueOf(matcher.group(3)),
						Integer.valueOf(matcher.group(4)),
						Optional.ofNullable(matcher.group(5)).map(Integer::valueOf).orElse(0));
				nextLine(reader);
			} else if (line.startsWith("#")) {
				// We hit the end of the section
				return;
			} else {
				// Keep moving down to look for the next reset
				nextLine(reader);
			}
		}
	}

	private void parseReset(char code, int vnum1, int limit, int vnum2) {
		if (code == 'O') {
			Pop.found(world.getItem(vnum1), world.getRoom(vnum2));
		} else if (code == 'P') {
			Pop.contained(world.getItem(vnum1), world.getItem(vnum2));
		} else if (code == 'M') {
			lastMob = Spawn.in(world.getMob(vnum1), world.getRoom(vnum2), limit);
		} else if (code == 'E') {
			Pop.worn(world.getItem(vnum1), lastMob);
		} else if (code == 'G') {
			Pop.held(world.getItem(vnum1), lastMob);
		} else {
			throw new ParseException("Unknown reset code: " + code);
		}
	}

	/**
	 * 
	 * @param reader
	 * @throws UncheckedIOException
	 */
	private void nextLine(BufferedReader reader) {
		try {
			line = reader.readLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private Optional<String> matchString(String input) {
		Matcher matcher = stringPattern.matcher(input);
		return matcher.matches() ? Optional.of(matcher.group(1)) : Optional.empty();
	}

	private Optional<Matcher> matches(String input, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(input);
		return matcher.matches() ? Optional.of(matcher) : Optional.empty();
	}

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 *        A bit vector of 9 would be 1001 in binary, which results in a list of the first and fourth enums.
	 * @return
	 */
	public static <T extends Enum<?>> List<T> createFlagList(Class<T> enumType, int bitVector) {
		List<T> flags = new ArrayList<>();

		for (int i = enumType.getEnumConstants().length - 1; i >= 0; i--) {
			double bitFlag = Math.pow(2, i);
			if (bitVector >= bitFlag) {
				bitVector -= bitFlag;
				flags.add(enumType.getEnumConstants()[i]);
			}
		}

		return flags;
	}

}
