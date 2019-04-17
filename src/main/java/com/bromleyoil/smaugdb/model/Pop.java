package com.bromleyoil.smaugdb.model;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.model.enums.PopType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

/**
 * Represents a location where an item appears.
 * 
 */
public class Pop {

	private static final Logger log = LoggerFactory.getLogger(Pop.class);

	private static final AtomicInteger idGenerator = new AtomicInteger();

	public static final Comparator<Pop> EQUIP_ORDER = Comparator
			// Equip location ordinal, with inventory at the end 
			.comparing((Pop x) -> x.getWearFlag() != null ? x.getWearFlag().ordinal() : WearFlag.values().length)
			// Items sorted by name
			.thenComparing(x -> x.getItem().getName())
			// Finally break ties with the id
			.thenComparing(Pop::getId);

	private int id;
	private Range itemLevel = Range.of(0, 0);
	private Item item;
	private PopType type;

	// TODO mpoload

	private Spawn spawn;
	private WearFlag wearFlag;
	private Item container;
	private Room room;

	private Pop() {
		// Private constructor
		this.id = idGenerator.getAndIncrement();
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

	public static Pop held(Item item, Spawn spawn) {
		log.debug("Giving {} to {}", item, spawn.getMob());
		Pop pop = new Pop();
		pop.setType(PopType.HELD);
		pop.setItem(item);
		pop.setSpawn(spawn);
		item.addPop(pop);
		spawn.addContainedPop(pop);
		return pop;
	}

	public static Pop worn(Item item, Spawn spawn, WearFlag wearFlag) {
		log.debug("Equipping {} to {}", item, spawn.getMob());
		Pop pop = new Pop();
		pop.setType(PopType.WORN);
		pop.setItem(item);
		pop.setSpawn(spawn);
		pop.setWearFlag(wearFlag);
		item.addPop(pop);
		spawn.addContainedPop(pop);
		return pop;
	}

	@Override
	public String toString() {
		if (type == PopType.FOUND) {
			return String.format("%s found in %s", item, room);
		} else if (type == PopType.CONTAINED) {
			return String.format("%s contained in %s", item, container);
		} else if (type == PopType.WORN) {
			return String.format("%s worn by %s", item, spawn.getMob());
		} else if (type == PopType.HELD) {
			return String.format("%s held by %s", item, spawn.getMob());
		} else {
			return String.format("%s appears in an unknown location", item);
		}
	}

	public String getMobDescription() {
		if (type == PopType.WORN) {
			return "Equipped with ";
		} else if (type == PopType.HELD) {
			return "Carrying ";
		} else {
			return "Unrelated to ";
		}
	}

	public String getItemDescription() {
		if (type == PopType.WORN) {
			return "Equipped by ";
		} else if (type == PopType.HELD) {
			return "Carried by ";
		} else if (type == PopType.CONTAINED) {
			return "Contained in ";
		} else if (type == PopType.FOUND) {
			return "Found in ";
		} else {
			return "Unrelated to ";
		}
	}

	/** The unique ID of this pop record for consistency with equals */
	public int getId() {
		return id;
	}

	/** Level range the item can appear at this location */
	public Range getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(Range itemLevel) {
		this.itemLevel = itemLevel;
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

	/** The specific spawn to which the item is assigned */
	public Spawn getSpawn() {
		return spawn;
	}

	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}

	/** The spawned mob the item pops on or in */
	public Mob getMob() {
		return spawn.getMob();
	}

	/** The location the item is equipped */
	public WearFlag getWearFlag() {
		return wearFlag;
	}

	public void setWearFlag(WearFlag wearFlag) {
		this.wearFlag = wearFlag;
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
