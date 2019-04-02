package com.bromleyoil.smaugdb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a location where an item appears.
 * 
 * @author moorwi
 */
public class Pop {

	private static final Logger log = LoggerFactory.getLogger(Pop.class);

	private int minItemLevel;
	private int maxItemLevel;
	private Item item;
	private PopType type;

	private Mob mob;
	private Item container;
	private Room room;

	private Pop() {
		// Private constructor
	}

	public static Pop found(Item item, Room room) {
		log.debug("Dropping {} in {}", item, room);
		Pop pop = new Pop();
		pop.setType(PopType.FOUND);
		pop.setItem(item);
		pop.setRoom(room);
		item.addPop(pop);
		room.addPop(pop);
		return pop;
	}

	public static Pop contained(Item item, Item container) {
		log.debug("Putting {} inside {}", item, container);
		Pop pop = new Pop();
		pop.setType(PopType.CONTAINED);
		pop.setItem(item);
		pop.setContainer(container);
		item.addPop(pop);
		container.addContainedPop(pop);
		return pop;
	}

	public static Pop held(Item item, Mob mob) {
		log.debug("Giving {} to {}", item, mob);
		Pop pop = new Pop();
		pop.setType(PopType.HELD);
		pop.setItem(item);
		pop.setMob(mob);
		item.addPop(pop);
		mob.addContainedPop(pop);
		return pop;
	}

	public static Pop worn(Item item, Mob mob) {
		log.debug("Equipping {} to {}", item, mob);
		Pop pop = new Pop();
		pop.setType(PopType.WORN);
		pop.setItem(item);
		pop.setMob(mob);
		item.addPop(pop);
		mob.addContainedPop(pop);
		return pop;
	}

	@Override
	public String toString() {
		if (type == PopType.FOUND) {
			return String.format("%s found in %s", item, room);
		} else if (type == PopType.CONTAINED) {
			return String.format("%s contained in %s", item, container);
		} else if (type == PopType.WORN) {
			return String.format("%s worn by %s", item, mob);
		} else if (type == PopType.HELD) {
			return String.format("%s held by %s", item, mob);
		} else {
			return String.format("%s appears in an unknown location", item);
		}
	}

	/** Minimum level the item can appear at this location */
	public int getMinItemLevel() {
		return minItemLevel;
	}

	public void setMinItemLevel(int minItemLevel) {
		this.minItemLevel = minItemLevel;
	}

	/** Maximum level the item can appear at this location */
	public int getMaxItemLevel() {
		return maxItemLevel;
	}

	public void setMaxItemLevel(int maxItemLevel) {
		this.maxItemLevel = maxItemLevel;
	}

	/** The item that pops */
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public PopType getType() {
		return type;
	}

	public void setType(PopType type) {
		this.type = type;
	}

	/** The mob the item pops on or in */
	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}

	/** The contain the item pops in */
	public Item getContainer() {
		return container;
	}

	public void setContainer(Item container) {
		this.container = container;
	}

	/** The room the item pops in, or the room containing the mob or container the item pops */
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
