package com.bromleyoil.smaugdb;

import static com.bromleyoil.smaugdb.model.enums.ItemType.*;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.Pop;
import com.bromleyoil.smaugdb.model.Range;
import com.bromleyoil.smaugdb.model.World;
import com.bromleyoil.smaugdb.model.enums.ContainerFlag;
import com.bromleyoil.smaugdb.model.enums.PopType;

/**
 * Interprets all the code-level adjustments that Smaug makes and applies them to the data model. Various code paths
 * are responsible for this:
 * 
 * load_objects (db.c)
 * reset_area (reset.c)
 * generate_itemlevel (reset.c)
 * create_object (db.c)
 * 
 */
public class SmaugInterpreter {

	private static final int LEVEL_AVATAR = 50;

	private World world;

	private SmaugInterpreter(World world) {
		this.world = world;
	}

	public static void process(World world) {
		SmaugInterpreter interpreter = new SmaugInterpreter(world);

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

	private void processArea(Area area) {
		// Calculate the range for generated item levels
		area.setGeneratedRange(Range.of(area.getSoftRange())
				.max(Range.of(1, area.getSoftRange().getMax()))
				.constrainMaxRange(15));
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
			if (pop.getType() == PopType.FOUND) {
				pop.setItemLevel(calculateGeneratedItemLevel(pop.getArea(), item)
						.extend(1)
						.constrainMin(1));
			} else if (pop.getType() == PopType.CONTAINED) {
				calculateItemLevel(pop.getContainer());
				pop.setItemLevel(calculateGeneratedItemLevel(pop.getArea(), item)
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
						.min(calculateGeneratedItemLevel(pop.getArea(), item)));
			} else {
				throw new UnsupportedOperationException("Don't know how to set the level range for: " + pop.getType());
			}
		}

		// Calculate the item's overall level range
		item.setLevel(item.getPops().stream().map(Pop::getItemLevel).collect(Range.unionCollector()));
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
			item.setContainerFlags(Utils.convertBitVector(ContainerFlag.class, item.getValue(1)));
			item.setKey(world.getItem(item.getValue(2)));
		} else if (item.getType() == WEAPON) {
			item.setDamage(Range.of(item.getValue(1), item.getValue(1) * item.getValue(2)));
		}
	}

	private void processMob(Mob mob) {
		calculateMobLevel(mob);
	}

	private void calculateMobLevel(Mob mob) {
		if (mob.getLevel() != null) {
			return;
		}

		mob.setLevel(Range.of(mob.getSuggestedLevel()).extend(1).constrainMin(1));
	}
}
