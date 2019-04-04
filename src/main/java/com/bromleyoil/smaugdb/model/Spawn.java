package com.bromleyoil.smaugdb.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.model.enums.ActFlag;

public class Spawn {

	private static final Logger log = LoggerFactory.getLogger(Spawn.class);

	private Mob mob;
	private Room room;
	private int limit;
	private Set<Pop> containedPops = new TreeSet<>(Pop.EQUIP_ORDER);

	private Spawn(Mob mob, Room room, int limit) {
		this.mob = mob;
		this.room = room;
		this.limit = limit;
	}

	public static Spawn in(Mob mob, Room room, int limit) {
		log.debug("Spawning {} in {} with limit {}", mob, room, limit);
		Spawn spawn = new Spawn(mob, room, limit);
		mob.addSpawn(spawn);
		room.addSpawn(spawn);
		return spawn;
	}

	@Override
	public String toString() {
		return String.format("%s in %s%s", mob, room, limit == 1 ? "" : (" (max " + limit + ")"));
	}

	/** Gets a description of this spawn from the mob's perspective. */
	public String getMobDescription() {
		return "May be found " + (mob.hasActFlag(ActFlag.SENTINEL) ? "in " : "around ");
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

	public Collection<Pop> getContainedPops() {
		return Collections.unmodifiableCollection(containedPops);
	}

	public void addContainedPop(Pop containedPop) {
		containedPops.add(containedPop);
	}
}
