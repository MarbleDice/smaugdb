package com.bromleyoil.smaugdb;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.Range;
import com.bromleyoil.smaugdb.model.World;
import com.bromleyoil.smaugdb.model.enums.ContainerFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;

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

	private World world;

	private SmaugInterpreter(World world) {
		this.world = world;
	}

	public static void process(World world) {
		SmaugInterpreter interpreter = new SmaugInterpreter(world);

		for (Area area : world.getAreas().values()) {
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
		Range range = Range.of(
				area.getSoftRange().getMin() == 0 ? 1 : area.getSoftRange().getMin(),
				area.getSoftRange().getMax());
		range.setMax(Integer.min(range.getMin() + 15, range.getMax()));
		area.setGeneratedRange(range);
	}

	private void processItem(Item item) {
		if (item.getType() == ItemType.CONTAINER) {
			item.setCapacity(item.getValue(0));
			item.setContainerFlags(Utils.convertBitVector(ContainerFlag.class, item.getValue(1)));
			item.setKey(world.getItem(item.getValue(2)));
		} else if (item.getType() == ItemType.WEAPON) {
			item.setDamage(Range.of(item.getValue(1), item.getValue(1) * item.getValue(2)));
		}
	}

	private void processMob(Mob mob) {

	}
}
