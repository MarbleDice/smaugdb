package com.bromleyoil.smaugdb.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.RomInterpreter;
import com.bromleyoil.smaugdb.ValueList;
import com.bromleyoil.smaugdb.model.Apply;
import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Exit;
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
import com.bromleyoil.smaugdb.model.enums.Direction;
import com.bromleyoil.smaugdb.model.enums.EquipSlot;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.ProgType;
import com.bromleyoil.smaugdb.model.enums.ResistFlag;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class RomParser {

	private static final Logger log = LoggerFactory.getLogger(RomParser.class);

	private static final Pattern vnumPattern = Pattern.compile("^\\s*#\\s*([1-9]\\d*)\\s*$");
	private static final Pattern resetPattern = Pattern.compile("^\\s*([RDOPMEG])\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s*(-?\\d+)?\\s*(-?\\d+)?");
	private static final Pattern shopPattern = Pattern.compile("^\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)"
			+ "\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
	private static final Pattern doorPattern = Pattern.compile("^\\s*D(\\d+)\\s*$");
	private static final Pattern progPattern = Pattern.compile("^>\\s+(\\w+)\\s+(.+)~$");

	private Set<String> warnings = new HashSet<>();

	private World world;
	private Area area;

	/** The current line of text from the area file */
	private String line;

	/** The last mob spawn processed while parsing resets */
	private Spawn lastSpawn;

	private RomParser(World world) {
		// Private constructor
		this.world = world;
	}
	
	private void warnOnce(String warning) {
		if (!warnings.contains(warning)) {
			log.warn(warning);
			warnings.add(warning);
		}
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
		RomParser parser;
		for (String areaFile : areaFiles) {
			String areaFilePath = areaPath + File.separator + areaFile;
			if (Paths.get(areaFilePath).toFile().isFile()) {
				log.info("Loading {}", areaFilePath);
				// Create a new parser to store the parsing context
				parser = new RomParser(world);
				parser.parseArea(areaFilePath);
			} else if (!"$".equals(areaFile)) {
				log.warn("Listed area file does not exist at {}", areaFilePath);
			}
		}

		// Remove unloaded entities
		world.removeUnloaded();

		// Interpret loaded data
		RomInterpreter.process(world);
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
		Matcher matcher = matches(line, "#AREA\\s*")
				.orElseThrow(() -> new ParseException("Invalid #AREA line: " + line));
		// Just shut up SonarLint
		matcher.hashCode();
		area = new Area();

		// Discard filename
		nextString(reader);

		// Discard jank area name
		nextString(reader);

		// Range Author Name
		matcher = matches(nextString(reader), "\\{\\s*(All|None|(\\d+)\\s*-?\\s*(\\d+))\\s*\\}\\s+([a-zA-Z]+)\\s+(.+)")
				.orElseThrow(() -> new ParseException("Invalid levels line: " + line));
		area.setName(matcher.group(5));
		area.setAuthor(matcher.group(4));
		if (matcher.group(3) != null && matcher.group(4) != null) {
			area.setSoftRange(Range.of(Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3))));
		}

		// Vnums
		nextLine(reader);

		world.addArea(area);
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
		List<String> strings;
		List<Integer> values;

		Mob mob = new Mob();
		mob.setVnum(vnum);
		mob.setKeywords(convertKeywords(nextString(reader)));
		mob.setName(nextString(reader));
		mob.setDescription(nextString(reader));
		mob.setLongDescription(nextBlock(reader));
		mob.setRace(nextString(reader));

		// act affected alignment recordType
		strings = nextStringValues(reader);
		mob.setActFlags(convertCharVector(ActFlag.class, ActFlag::ofCode, strings.get(0)));
		mob.setAffectFlags(convertCharVector(AffectFlag.class, AffectFlag::ofCode, strings.get(1)));
		mob.setAlignment(Integer.parseInt(strings.get(2)));
		mob.setAssistGroup(strings.get(3));

		// level thac0 hphpdice manadice damdice damtype
		strings = nextStringValues(reader);
		mob.setSuggestedLevel(Integer.parseInt(strings.get(0)));
		mob.setThac0(Integer.parseInt(strings.get(1)));
		mob.setHp(Range.of(strings.get(2)));
		mob.setMana(Range.of(strings.get(3)));
		mob.setDamage(Range.of(strings.get(4)));
		mob.setDamageType(strings.get(5));

		// pierce bash slash magic
		values = nextValues(reader);
		mob.setPierceArmor(values.get(0));
		mob.setBashArmor(values.get(0));
		mob.setSlashArmor(values.get(0));
		mob.setMagicArmor(values.get(0));
		
		// attack_flags imm_flags res_flags vuln_flags
		strings = nextStringValues(reader);
		mob.setAttackFlags(convertCharVector(AttackFlag.class, AttackFlag::ofCode, strings.get(0)));
		mob.setImmuneFlags(convertCharVector(ResistFlag.class, ResistFlag::ofCode, strings.get(1)));
		mob.setResistFlags(convertCharVector(ResistFlag.class, ResistFlag::ofCode, strings.get(2)));
		mob.setVulnerableFlags(convertCharVector(ResistFlag.class, ResistFlag::ofCode, strings.get(3)));

		// start_pos return_pos gender gold
		strings = nextStringValues(reader);
		mob.setGold(Range.of(Integer.valueOf(strings.get(3))));

		// form part size material
		strings = nextStringValues(reader);
		mob.setSize(strings.get(3));

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
		List<String> strings;

		Item item = new Item();
		item.setVnum(vnum);
		item.setKeywords(convertKeywords(nextString(reader)));
		item.setName(nextString(reader));
		item.setDescription(nextString(reader));
		// material
		nextString(reader);

		// type extra wear
		strings = nextStringValues(reader);
		item.setType(ItemType.valueOf(strings.get(0).toUpperCase()));
		item.setExtraFlags(convertCharVector(ExtraFlag.class, ExtraFlag::ofCode, strings.get(1)));
		item.setWearFlags(convertCharVector(WearFlag.class, WearFlag::ofCode, strings.get(2)));

		// "the values line" requires interpretation by type
		strings = nextStringValues(reader);
		item.setStringValues(strings);

		// level weight cost condition(unused)
		strings = nextStringValues(reader);
		item.setLevel(Range.of(Integer.valueOf(strings.get(0))));
		item.setWeight(Integer.valueOf(strings.get(1)));
		item.setCost(Integer.valueOf(strings.get(2)));

		// Extra/Apply sections
		nextLine(reader);
		while ("E".equals(line) || "A".equals(line)) {
			if ("A".equals(line)) {
				values = nextValues(reader);
				int applyTypeCode = values.get(0);
				int applyValue = values.get(1);
				ApplyType.ofCode(applyTypeCode).ifPresentOrElse(
						x -> item.addApply(new Apply(x, applyValue)),
						() -> warnOnce(String.format("Unrecognized ApplyType: %d", applyTypeCode)));
			} else if ("E".equals(line)) {
				nextString(reader);
				nextBlock(reader);
			}
			nextLine(reader);
		}

		world.addItem(item, area);
	}

	private Room reserveRoom(int vnum) {
		if (!world.hasRoom(vnum)) {
			Room room = new Room();
			room.setVnum(vnum);
			room.setName("");
			world.addRoom(room, area);
		}
		return world.getRoom(vnum);
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

		Room room = reserveRoom(vnum);
		room.setIsLoaded(true);

		room.setName(nextString(reader));

		// description
		nextString(reader);

		// unused room_flags sector_flags
		nextStringValues(reader);

		// Door sections
		nextLine(reader);
		matcher = doorPattern.matcher(line);
		while ("E".equals(line) || matcher.matches()) {
			if (matcher.matches()) {
				Exit exit = new Exit();
				exit.setFrom(room);

				// Door line: D[0-5]
				exit.setDir(Direction.values()[Integer.parseInt(matcher.group(1))]);
	
				// Description
				nextBlock(reader);
	
				// Door name
				nextString(reader);
	
				// door_state key_vnum to_vnum
				values = nextValues(reader);
				Item key = world.getItem(values.get(1));
				if (key != null) {
					key.addKeyDoor(room);
					exit.setKey(key);
				}
				exit.setTo(reserveRoom(values.get(2)));
				if (exit.getTo().getVnum() > 0) {
					// TODO not needed if exits are pruned
					room.getExits().add(exit);
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

		world.addRoom(room, area);
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
				// Found a reset. Groups are 1=code(MOPGEDR), 2=ignored, 3=arg1, 4=arg2, 5=arg3(!GR), 6=arg4(MP)
				parseReset(matcher.group(1).charAt(0),
						Integer.valueOf(matcher.group(3)),
						Integer.valueOf(matcher.group(4)),
						Optional.ofNullable(matcher.group(5)).map(Integer::valueOf).orElse(0),
						Optional.ofNullable(matcher.group(6)).map(Integer::valueOf).orElse(0));
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
	 * @param arg1
	 *        The object or mob being reset.
	 * @param arg2
	 *        The mob spawning limit (only used for mob resets)
	 * @param arg3
	 *        The containing room, mob, object, or wear location of the mob/object being reset.
	 */
	private void parseReset(char code, int arg1, int arg2, int arg3, int arg4) {
		if (code == 'O') {
			Pop.found(world.getItem(arg1), area, world.getRoom(arg3));
		} else if (code == 'P') {
			Pop.contained(world.getItem(arg1), area, world.getItem(arg3));
		} else if (code == 'M') {
			lastSpawn = Spawn.in(world.getMob(arg1), world.getRoom(arg3), arg2, arg4);
		} else if (code == 'E') {
			Pop.worn(world.getItem(arg1), area, lastSpawn, EquipSlot.values()[arg3].getWearFlag());
		} else if (code == 'G') {
			Pop.held(world.getItem(arg1), area, lastSpawn);
		} else if (code == 'D') {
			// Skip door resets
		} else if (code == 'R') {
			// Skip door randomization
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
			line = line == null ? null : line.trim();
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

	public <T extends Enum<T>> List<T> convertCharVector(Class<T> clazz, Function<String, Optional<T>> func,
			String charVector) {
		List<T> flags = new ArrayList<>();

		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			if (code.equals("0")) {
				continue;
			}
			func.apply(code).ifPresentOrElse(
					flags::add,
					() -> warnOnce(String.format("Unrecognized %s: %s", clazz.getSimpleName(), code)));
		}

		return flags;
	}
}
