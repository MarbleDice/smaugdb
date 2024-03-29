package com.bromleyoil.smaugdb.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ObjIntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.SmaugInterpreter;
import com.bromleyoil.smaugdb.Utils;
import com.bromleyoil.smaugdb.ValueList;
import com.bromleyoil.smaugdb.model.Apply;
import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.Pop;
import com.bromleyoil.smaugdb.model.Prog;
import com.bromleyoil.smaugdb.model.Range;
import com.bromleyoil.smaugdb.model.Room;
import com.bromleyoil.smaugdb.model.Spawn;
import com.bromleyoil.smaugdb.model.World;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;
import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.AttackFlag;
import com.bromleyoil.smaugdb.model.enums.DefenseFlag;
import com.bromleyoil.smaugdb.model.enums.EquipSlot;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.ProgType;
import com.bromleyoil.smaugdb.model.enums.ResistFlag;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class SmaugParser {

	private static final Logger log = LoggerFactory.getLogger(SmaugParser.class);

	private static final Pattern vnumPattern = Pattern.compile("^\\s*#\\s*([1-9]\\d*)\\s*$");
	private static final Pattern resetPattern = Pattern.compile("^\\s*([OPMEG])\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*(\\d+)?");
	private static final Pattern shopPattern = Pattern.compile("^\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)"
			+ "\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
	private static final Pattern doorPattern = Pattern.compile("^\\s*D(\\d+)\\s*$");
	private static final Pattern progPattern = Pattern.compile("^>\\s+(\\w+)\\s+(.+)~$");

	private World world;
	private Area area;

	/** The current line of text from the area file */
	private String line;

	/** The last mob spawn processed while parsing resets */
	private Spawn lastSpawn;

	private SmaugParser(World world) {
		// Private constructor
		this.world = world;
	}

	/**
	 * Loads all areas in the area.list file contained inside the mudPath into the given world.
	 * 
	 * @param world
	 * @param mudPath
	 */
	public static void loadWorld(World world, String mudPath) {
		String areaPath = mudPath + File.separator + "area";

		// Read the area.list file
		List<String> areaFiles;
		try {
			areaFiles = Files.readAllLines(Paths.get(areaPath + File.separator + "area.lst"));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		// Load all areas
		SmaugParser parser;
		for (String areaFile : areaFiles) {
			String areaFilePath = areaPath + File.separator + areaFile;
			if (Paths.get(areaFilePath).toFile().isFile()) {
				log.info("Loading {}", areaFilePath);
				// Create a new parser to store the parsing context
				parser = new SmaugParser(world);
				parser.parseArea(areaFilePath);
			} else if (!"$".equals(areaFile)) {
				log.warn("Listed area file does not exist at {}", areaFilePath);
			}
		}

		// Interpret loaded data
		SmaugInterpreter.process(world);
	}

	/**
	 * Parses an area file, loading all data contained within.
	 * 
	 * @param areaFilePath
	 */
	private void parseArea(String areaFilePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(areaFilePath))) {
			nextLine(reader);

			while (line != null) {

				if (line.startsWith("#AREA")) {
					parseAreaTag(reader);
				} else if (line.startsWith("#AUTHOR")) {
					parseAuthorTag(reader);
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
				} else if (line.startsWith("#SHOPS")) {
					parseShops(reader);
				} else {
					nextLine(reader);
				}
			}
		} catch (IOException e) {
			log.info("Could not read: " + areaFilePath, e);
		}
	}

	/**
	 * Parses the #AREA tag
	 * 
	 * @param reader
	 */
	private void parseAreaTag(BufferedReader reader) {
		Matcher matcher = matches(line, "#AREA\\s+([^~]+)~")
				.orElseThrow(() -> new ParseException("Invalid #AREA line: " + line));

		area = new Area();
		area.setName(matcher.group(1));
		world.addArea(area);

		nextLine(reader);
	}

	/**
	 * Parses the #AUTHOR tag
	 * 
	 * @param reader
	 */
	private void parseAuthorTag(BufferedReader reader) {
		Matcher matcher = matches(line, "#AUTHOR\\s+([^~]+)~")
				.orElseThrow(() -> new ParseException("Invalid #AUTHOR line: " + line));
		area.setAuthor(matcher.group(1));
		nextLine(reader);
	}

	/**
	 * Parses the #RANGES tag
	 * 
	 * @param reader
	 */
	private void parseRanges(BufferedReader reader) {
		nextLine(reader);

		Matcher matcher = matches(line, "(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)")
				.orElseThrow(() -> new ParseException("Invalid #RANGES line: " + line));

		area.setSoftRange(Range.of(Integer.valueOf(matcher.group(1)),
				Integer.valueOf(matcher.group(2))));
		area.setHardRange(Range.of(Integer.valueOf(matcher.group(3)),
				Integer.valueOf(matcher.group(4))));
	}

	/**
	 * Parses a block of vnum (#MOBILES, #OBJECTS, or #ROOMS) deferring to the given handler for processing a single
	 * entry.
	 * 
	 * @param reader
	 * @param handler
	 *        Accepts a buffered reader and the vnum of the object being parsed
	 */
	private void parseVnumBlock(BufferedReader reader, ObjIntConsumer<BufferedReader> handler) {
		nextLine(reader);

		while (line != null) {
			Matcher matcher = vnumPattern.matcher(line);
			if (matcher.matches()) {
				// Found a vnum, read a mob
				handler.accept(reader, Integer.valueOf(matcher.group(1)));
			} else if ("#0".equals(line)) {
				// We hit the end of the section
				return;
			} else {
				// Keep moving down to look for the next vnum
				nextLine(reader);
			}
		}
	}

	/**
	 * Parses a single mobile.
	 * 
	 * @param reader
	 * @param vnum
	 */
	private void parseMobile(BufferedReader reader, int vnum) {
		log.trace("Reading mobile {}", vnum);
		char recordType;
		List<String> strings;
		List<Integer> values;

		Mob mob = new Mob();
		mob.setVnum(vnum);
		mob.setKeywords(convertKeywords(nextString(reader)));
		mob.setName(nextString(reader));
		mob.setDescription(nextString(reader));
		mob.setLongDescription(nextBlock(reader));

		// act affected alignment recordType
		strings = nextStringValues(reader);
		mob.setActFlags(Utils.convertBitVector(ActFlag.class, Integer.parseInt(strings.get(0))));
		mob.setAffectFlags(Utils.convertBitVector(AffectFlag.class, Integer.parseInt(strings.get(1))));
		mob.setAlignment(Integer.parseInt(strings.get(2)));
		recordType = strings.get(3).charAt(0);

		if (recordType != 'C' && recordType != 'S') {
			log.info("Got a mob with record type {}: {}", recordType, mob.getName());
		}

		// level thac0 armor hpdice damdice
		strings = nextStringValues(reader);
		mob.setSuggestedLevel(Integer.parseInt(strings.get(0)));
		mob.setThac0(Integer.parseInt(strings.get(1)));
		mob.setArmor(Integer.parseInt(strings.get(2)));
		mob.setHp(Range.of(strings.get(3)));
		mob.setDamage(Range.of(strings.get(4)));

		// gold exp
		values = nextValues(reader);
		mob.setGold(Range.of(values.get(0)));
		if (values.size() > 1) {
			mob.setExperience(values.get(1));
		}

		// position defposition gender
		nextValues(reader);

		// Complex and very complex records have extra fields
		if (recordType == 'C' || recordType == 'V') {
			// str int wis dex con cha lck
			nextValues(reader);

			// Saves vs poison/death wand para/petrify breath spell/staff
			nextValues(reader);

			// race class height weight speaks speaking numattacks
			nextValues(reader);

			// hitroll damroll xflags resist immune susceptible attacks defenses
			values = nextValues(reader);
			mob.setHitroll(values.get(0));
			mob.setDamroll(values.get(1));
			// values[2] is xflags, which are body parts
			mob.setResistFlags(Utils.convertBitVector(ResistFlag.class, values.get(3)));
			mob.setImmuneFlags(Utils.convertBitVector(ResistFlag.class, values.get(4)));
			mob.setVulnerableFlags(Utils.convertBitVector(ResistFlag.class, values.get(5)));
			mob.setAttackFlags(Utils.convertBitVector(AttackFlag.class, values.get(6)));
			mob.setDefenseFlags(Utils.convertBitVector(DefenseFlag.class, values.get(7)));
		}

		// Progs
		nextLine(reader);
		mob.setProgs(nextProgs(reader));

		world.addMob(mob, area);
	}

	/**
	 * Parses a single object.
	 * 
	 * @param reader
	 * @param vnum
	 */
	private void parseObject(BufferedReader reader, int vnum) {
		log.trace("Reading object {}", vnum);
		List<Integer> values;

		Item item = new Item();
		item.setVnum(vnum);
		item.setKeywords(convertKeywords(nextString(reader)));
		item.setName(nextString(reader));
		item.setDescription(nextString(reader));
		// The next "action description" is unused
		nextBlock(reader);

		// type extra wear [layers [level]]
		values = nextValues(reader);
		item.setType(ItemType.values()[values.get(0)]);
		item.setExtraFlags(Utils.convertBitVector(ExtraFlag.class, values.get(1)));
		item.setWearFlags(Utils.convertBitVector(WearFlag.class, values.get(2)));
		item.setSuggestedLevel(values.get(4));
		if (values.size() > 3) {
			log.info("Got an object with layers/levels: {} has \"{}\"", item, line);
		}

		// "the values line" requires interpretation by type
		values = nextValues(reader);
		item.setValues(values);

		// weight cost rent
		values = nextValues(reader);
		item.setWeight(values.get(0));
		item.setCost(values.get(1));

		// Extra/Apply sections
		nextLine(reader);
		while ("E".equals(line) || "A".equals(line)) {
			if ("A".equals(line)) {
				values = nextValues(reader);
				item.addApply(new Apply(ApplyType.values()[values.get(0)], values.get(1)));
			} else if ("E".equals(line)) {
				nextString(reader);
				nextString(reader);
			}
			nextLine(reader);
		}

		// Progs
		item.setProgs(nextProgs(reader));

		world.addItem(item, area);
	}

	/**
	 * Parses a single room.
	 * 
	 * @param reader
	 * @param vnum
	 */
	private void parseRoom(BufferedReader reader, int vnum) {
		log.trace("Reading room {}", vnum);
		List<Integer> values;
		Matcher matcher;

		Room room = world.addRoom(area, vnum);
		room.setVnum(vnum);

		room.setName(nextString(reader));

		// description
		nextString(reader);

		// area roomflags sectorflags [teledelay televnum]
		nextValues(reader);

		// Extra/door sections
		nextLine(reader);
		matcher = doorPattern.matcher(line);
		while ("E".equals(line) || matcher.matches()) {
			if (matcher.matches()) {
				// Door line: D[0-10]
				nextString(reader);
				nextString(reader);
				values = nextValues(reader);
				Item key = world.getItem(values.get(1));
				if (key != null) {
					key.addKeyDoor(room);
				}
			} else if ("E".equals(line)) {
				// Extra line: E
				nextString(reader);
				nextString(reader);
			}
			nextLine(reader);
			matcher = doorPattern.matcher(line);
		}

		// Progs
		room.setProgs(nextProgs(reader));
	}

	/**
	 * Parses the #RESETS tag.
	 * 
	 * @param reader
	 */
	private void parseResets(BufferedReader reader) {
		nextLine(reader);

		while (line != null) {
			Matcher matcher = resetPattern.matcher(line);
			if (matcher.find()) {
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

	/**
	 * Parses a single reset.
	 * 
	 * @param code
	 *        The type of reset.
	 * @param vnum1
	 *        The object or mob being reset.
	 * @param limit
	 *        The mob spawning limit (only used for mob resets)
	 * @param arg
	 *        The containing room, mob, object, or wear location of the mob/object being reset.
	 */
	private void parseReset(char code, int vnum1, int limit, int arg) {
		if (code == 'O') {
			Pop.found(world.getItem(vnum1), area, world.getRoom(arg));
		} else if (code == 'P') {
			Pop.contained(world.getItem(vnum1), area, world.getItem(arg));
		} else if (code == 'M') {
			lastSpawn = Spawn.in(world.getMob(vnum1), world.getRoom(arg), limit);
		} else if (code == 'E') {
			Pop.worn(world.getItem(vnum1), area, lastSpawn, EquipSlot.values()[arg].getWearFlag());
		} else if (code == 'G') {
			Pop.held(world.getItem(vnum1), area, lastSpawn);
		} else {
			throw new ParseException("Unknown reset code: " + code);
		}
	}

	/**
	 * Parses the #SHOPS tag.
	 * 
	 * @param reader
	 */
	private void parseShops(BufferedReader reader) {
		nextLine(reader);

		while (line != null) {
			Matcher matcher = shopPattern.matcher(line);
			if (matcher.find()) {
				// Found a shop. Values are 0=vnum, 1-5=purchased types, 6=sell pct, 7=buy pct, 8=open hr, 9=close hr
				List<Integer> values = IntStream.range(1, 11)
						.mapToObj(matcher::group)
						.map(Integer::valueOf)
						.collect(Collectors.toList());
				parseShop(values.get(0), values.subList(1, 6), values.get(6), values.get(7), values.get(8),
						values.get(9));
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

	/**
	 * Parses a single shop definition.
	 * 
	 * @param shopkeeperVnum
	 * @param purchasedItemTypes
	 * @param sellPercent
	 * @param buyPercent
	 * @param openHour
	 * @param closeHour
	 */
	private void parseShop(int shopkeeperVnum, List<Integer> purchasedItemTypes, int sellPercent, int buyPercent,
			int openHour, int closeHour) {
		log.trace("Got a shopkeeper {}", shopkeeperVnum);
		Mob mob = world.getMob(shopkeeperVnum);
		mob.setShopkeeper(true);
		mob.setPurchasedTypes(purchasedItemTypes.stream().filter(i -> i > 0).map(i -> ItemType.values()[i])
				.collect(Collectors.toList()));
		mob.setSellPercent(sellPercent);
		mob.setBuyPercent(buyPercent);
		mob.setOpenHour(openHour);
		mob.setCloseHour(closeHour);
	}

	/**
	 * Advances the reader by one line.
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

	/**
	 * Reads the next line from the file as a string, such as the following:
	 * 
	 * Some short description ended with a tilde~
	 * 
	 * @param reader
	 */
	private String nextString(BufferedReader reader) {
		return nextBlock(reader).stream()
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.joining(" "));
	}

	/**
	 * Reads the next set of lines as a multi-line text block, such as the following:
	 * 
	 * A longer description with
	 * lines breaks that's terminated
	 * by a tilde on a new line
	 * ~
	 * 
	 * @param reader
	 * @return
	 */
	private List<String> nextBlock(BufferedReader reader) {
		List<String> lines = new ArrayList<>();

		while (line != null) {
			nextLine(reader);

			if (line.endsWith("~")) {
				// Last line
				if (line.length() > 1) {
					// Drop the tilde before adding the last content
					lines.add(line.substring(0, line.length() - 1));
				}
				break;
			} else {
				lines.add(line);
			}
		}

		return lines;
	}

	/**
	 * Reads the next line as a list of values, such as the following:
	 * 
	 * 1014 0 1212
	 * 
	 * @param reader
	 * @return
	 */
	private List<Integer> nextValues(BufferedReader reader) {
		nextLine(reader);
		return new ValueList(Stream.of(line.split("\\s+")).map(Integer::valueOf).collect(Collectors.toList()));
	}

	private List<String> nextStringValues(BufferedReader reader) {
		nextLine(reader);
		return Stream.of(line.split("\\s+")).collect(Collectors.toList());
	}

	private List<Prog> nextProgs(BufferedReader reader) {
		List<Prog> progs = new ArrayList<>();

		Matcher matcher = progPattern.matcher(line);
		while (matcher.matches()) {
			Prog prog = new Prog();
			prog.setType(ProgType.of(matcher.group(1)));
			prog.setTrigger(matcher.group(1) + " " + matcher.group(2));
			prog.setDefinition(nextBlock(reader));

			log.trace("Got prog {}: {}", prog.getTrigger(), prog.getType());

			progs.add(prog);

			nextLine(reader);
			matcher = progPattern.matcher(line);
		}

		return progs;
	}

	private Optional<Matcher> matches(String input, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(input);
		return matcher.matches() ? Optional.of(matcher) : Optional.empty();
	}

	/**
	 * Converts a set of whitespace-delimited keywords into a comma-separated list of keywords.
	 * 
	 * @param keywords
	 * @return
	 */
	public String convertKeywords(String keywords) {
		return String.join(", ", keywords.split("\\s+"));
	}
}
