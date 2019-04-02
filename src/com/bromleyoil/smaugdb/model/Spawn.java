package com.bromleyoil.smaugdb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spawn {

	private static final Logger log = LoggerFactory.getLogger(Spawn.class);

	private Mob mob;
	private Room room;
	private int limit;

	private Spawn(Mob mob, Room room, int limit) {
		this.mob = mob;
		this.room = room;
		this.limit = limit;
		mob.addSpawn(this);
		room.addSpawn(this);
	}

	public static Mob in(Mob mob, Room room, int limit) {
		log.trace("Adding {} to {} with limit {}", mob, room, limit);
		Spawn spawn = new Spawn(mob, room, limit);
		mob.addSpawn(spawn);
		room.addSpawn(spawn);
		return mob;
	}

	/** The mob the item pops on or in */
	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}

	/** The room the item pops in, or the room containing the mob or container the item pops */
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	/** The maximum number of this mob that can spawn in the area */
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
