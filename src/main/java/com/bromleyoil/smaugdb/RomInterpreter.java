package com.bromleyoil.smaugdb;

import static com.bromleyoil.smaugdb.model.enums.ItemType.ARMOR;
import static com.bromleyoil.smaugdb.model.enums.ItemType.CONTAINER;
import static com.bromleyoil.smaugdb.model.enums.ItemType.PILL;
import static com.bromleyoil.smaugdb.model.enums.ItemType.POTION;
import static com.bromleyoil.smaugdb.model.enums.ItemType.SCROLL;
import static com.bromleyoil.smaugdb.model.enums.ItemType.STAFF;
import static com.bromleyoil.smaugdb.model.enums.ItemType.WAND;
import static com.bromleyoil.smaugdb.model.enums.ItemType.WEAPON;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.PopType;
import com.bromleyoil.smaugdb.model.enums.WeaponType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

/**
 * Interprets all the code-level adjustments that ROM makes and applies them to the data model. Various code paths
 * are responsible for this:
 * 
 * load_objects (db.c)
 * reset_area (reset.c)
 * create_object (db.c)
 * 
 */
public class RomInterpreter {

	private static final Logger log = LoggerFactory.getLogger(RomInterpreter.class);

	private static final int LEVEL_AVATAR = 50;

	private static final Pattern mploadPattern = Pattern.compile("mp(o|m)load\\s+(\\d+)(\\s+(\\d+))?");

	private World world;

	private RomInterpreter(World world) {
		this.world = world;
	}

	public static void process(World world) {
		RomInterpreter interpreter = new RomInterpreter(world);

		interpreter.processProgs();

		for (Area area : world.getAreas()) {
			interpreter.processArea(area);
		}

		for (Item item : world.getItems()) {
			interpreter.processItem(item);
		}

		for (Mob mob : world.getMobs()) {
			interpreter.processMob(mob);
		}
	}

	private void processProgs() {
		for (Mob producer : world.getMobs()) {
			processProgs(producer.getProgs(),
					(p, m) -> Spawn.produced(m, p, producer),
					(p, i, l) -> Pop.produced(i, p, producer, l));
		}

		for (Item producer : world.getItems()) {
			processProgs(producer.getProgs(),
					(p, m) -> Spawn.produced(m, p, producer),
					(p, i, l) -> Pop.produced(i, p, producer, l));
		}

		for (Room producer : world.getRooms()) {
			processProgs(producer.getProgs(),
					(p, m) -> Spawn.produced(m, p, producer),
					(p, i, l) -> Pop.produced(i, p, producer, l));
		}
	}

	private void processProgs(Collection<Prog> progs, BiConsumer<Prog, Mob> mpmLoader,
			TriConsumer<Prog, Item, Integer> mpoLoader) {
		for (Prog prog : progs) {
			Matcher matcher = mploadPattern.matcher(String.join(" ", prog.getDefinition()));
			while (matcher.find()) {
				if (matcher.group(1).equals("m")) {
					Optional.ofNullable(world.getMob(Integer.parseInt(matcher.group(2))))
							.ifPresent(m -> mpmLoader.accept(prog, m));
				} else if (matcher.group(1).equals("o")) {
					int level = matcher.groupCount() > 4 ? Integer.parseInt(matcher.group(5)) : 0;
					Optional.ofNullable(world.getItem(Integer.parseInt(matcher.group(2))))
							.ifPresent(i -> mpoLoader.accept(prog, i, level));
				}
			}
		}
	}

	private void processArea(Area area) {
	}

	private void processItem(Item item) {
		calculateItemLevel(item);
		interpretValues(item);
	}

	private void calculateItemLevel(Item item) {
		if (item.getLevel() != null) {
			return;
		}

		// Calculate the item level from each pop
		for (Pop pop : item.getPops()) {
			calculatePopItemLevel(pop);
		}

		// Calculate the item's overall level range
		item.setLevel(item.getPops().stream().map(Pop::getItemLevel).collect(Range.unionCollector()));
	}

	private void calculatePopItemLevel(Pop pop) {
		if (pop.getType() == PopType.FOUND) {
			pop.setItemLevel(calculateGeneratedItemLevel(pop.getArea(), pop.getItem())
					.extend(1)
					.constrainMin(1));
		} else if (pop.getType() == PopType.CONTAINED) {
			calculateItemLevel(pop.getContainer());
			pop.setItemLevel(calculateGeneratedItemLevel(pop.getArea(), pop.getItem())
					.max(pop.getContainer().getLevel())
					.extend(1)
					.constrainMin(1));
		} else if (pop.getType() == PopType.HELD || pop.getType() == PopType.WORN) {
			calculateMobLevel(pop.getMob());
			pop.setItemLevel(Range.of(pop.getMob().getLevel())
					.subtract(2)
					.constrain(0, LEVEL_AVATAR)
					.extend(1)
					.constrainMin(1));
		} else if (pop.getType() == PopType.SOLD) {
			calculateMobLevel(pop.getMob());
			pop.setItemLevel(Range.of(pop.getMob().getLevel())
					.subtract(2)
					.constrain(0, LEVEL_AVATAR)
					.min(calculateGeneratedItemLevel(pop.getArea(), pop.getItem())));
		} else if (pop.getType() == PopType.PRODUCED_ITEM
				|| pop.getType() == PopType.PRODUCED_MOB
				|| pop.getType() == PopType.PRODUCED_ROOM) {
			if (pop.getProducedLevel() > 0) {
				pop.setItemLevel(Range.of(pop.getProducedLevel()));
			} else if (pop.getMob() != null) {
				calculateMobLevel(pop.getMob());
				pop.setItemLevel(pop.getMob().getLevel());
			} else {
				pop.setItemLevel(Range.of(LEVEL_AVATAR));
			}
		} else {
			log.error("Don't know how to set the level range for: {}", pop.getType());
		}
	}

	/**
	 * Returns the generated item level range for an item. Roughly equivalent to generate_itemlevel in reset.c
	 * 
	 * @param item
	 * @return
	 */
	private Range calculateGeneratedItemLevel(Area area, Item item) {
		if (item.getSuggestedLevel() > 0) {
			return Range.of(item.getSuggestedLevel());
		} else if (item.getType() == SCROLL) {
			return Range.of(item.getValue(0));
		} else if (item.getType() == PILL || item.getType() == POTION) {
			return Range.of(area.getGeneratedRange());
		} else if (item.getType() == ARMOR || item.getType() == WAND || item.getType() == WEAPON) {
			return Range.of(area.getGeneratedRange()).adjust(4, 1);
		} else if (item.getType() == STAFF) {
			return Range.of(area.getGeneratedRange()).adjust(9, 5);
		}

		return Range.of(0);
	}

	private void interpretValues(Item item) {
		if (item.getType() == CONTAINER) {
			item.setCapacity(item.getValue(0));
		} else if (item.getType() == WEAPON) {
			// type num size verb flags
			item.setWeaponType(WeaponType.valueOf(item.getStringValue(0).toUpperCase()));
			item.setDamage(Range.of(item.getValue(1), item.getValue(1) * item.getValue(2)));
			int bonusDam = item.getApplies().stream()
					.filter(x -> x.getType() == ApplyType.DAMROLL)
					.collect(Collectors.summingInt(Apply::getValue));
			item.getDamage().add(bonusDam);
			item.setSummary(String.format("%.1f damage", item.getDamage().getAverage()));
			item.setTooltip(String.format("%dd%d+%d", item.getValue(1), item.getValue(2), bonusDam));
			// 3 verb
			// TODO 4 weapon flag (burning, etc)

		} else if (item.getType() == ARMOR) {
			// pierce bash slash exotic bulk
			item.setPierceArmor(item.getValue(0));
			item.setBashArmor(item.getValue(1));
			item.setSlashArmor(item.getValue(2));
			item.setMagicArmor(item.getValue(3));
			// calculateArmor yields a total armor of average * 4
			calculateArmor(item);
			item.setSummary(String.format("%.1f armor", item.getTotalArmor().getAverage() / 4));
			item.setTooltip(String.format("%d/%d/%d/%d", item.getSlashArmor(), item.getBashArmor(),
					item.getPierceArmor(), item.getMagicArmor()));

		} else {
			if (!item.getStringValues().stream().map(x -> x.replace("0", "")).allMatch(StringUtils::isBlank)) {
				item.setSummary(String.join(", ", item.getStringValues()));
			}
		}
	}

	private void calculateArmor(Item item) {
		Range totalArmor = null;

		// Establish an armor multiplier range for the slots it can be equipped in
		int mult = 1;
		for (WearFlag wearFlag : item.getWearFlags()) {
			if (wearFlag == WearFlag.BODY) {
				mult = Integer.max(mult, 3);
			} else if (wearFlag == WearFlag.HEAD || wearFlag == WearFlag.LEGS || wearFlag == WearFlag.ABOUT) {
				mult = Integer.max(mult, 2);
			}
		}

		int applyAc = 0 - item.getApplies().stream()
				.filter(a -> a.getType() == ApplyType.AC)
				.collect(Collectors.summingInt(Apply::getValue));
		totalArmor = Range.of(4 * applyAc + mult * (item.getSlashArmor() + item.getBashArmor()
				+ item.getPierceArmor() + item.getMagicArmor()));

		item.setTotalArmor(totalArmor);
	}

	private void processMob(Mob mob) {
		calculateMobLevel(mob);
		int gold = mob.getGold().getMax();
		mob.setGold(Range.of(gold / 2, 3 * gold / 2));
	}

	private void calculateMobLevel(Mob mob) {
		mob.setLevel(Range.of(mob.getSuggestedLevel()));
	}
}
