package com.bromleyoil.smaugdb.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World {

	private static final Logger log = LoggerFactory.getLogger(World.class);

	private Map<String, Area> areas = new HashMap<>();
	private Map<Integer, Room> rooms = new HashMap<>();
	private Map<Integer, Mob> mobs = new HashMap<>();
	private Map<Integer, Item> items = new HashMap<>();

	public Map<String, Area> getAreas() {
		return areas;
	}

	public void addArea(Area area) {
		areas.put(area.getName(), area);
	}

	public Map<Integer, Room> getRooms() {
		return rooms;
	}

	public Room getRoom(int vnum) {
		if (!rooms.containsKey(vnum)) {
			log.warn("Room not found: {}", vnum);
		}
		return rooms.get(vnum);
	}

	public void addRoom(Room room, Area area) {
		log.trace("Adding room \"{}\" to {}", room, area);
		room.setArea(area);
		area.addRoom(room);
		rooms.put(room.getVnum(), room);
	}

	public Map<Integer, Mob> getMobs() {
		return mobs;
	}

	public Mob getMob(int vnum) {
		if (!mobs.containsKey(vnum)) {
			log.warn("Mob not found: {}", vnum);
		}
		return mobs.get(vnum);
	}

	public void addMob(Mob mob, Area area) {
		log.trace("Adding mob \"{}\" to {}", mob, area);
		mob.setArea(area);
		area.addMob(mob);
		mobs.put(mob.getVnum(), mob);
	}

	public Map<Integer, Item> getItems() {
		return items;
	}

	public Item getItem(int vnum) {
		if (!items.containsKey(vnum)) {
			log.warn("Item not found: {}", vnum);
		}
		return items.get(vnum);
	}

	public void addItem(Item item, Area area) {
		log.trace("Adding item \"{}\" to {}", item, area);
		item.setArea(area);
		area.addItem(item);
		items.put(item.getVnum(), item);
	}
}
