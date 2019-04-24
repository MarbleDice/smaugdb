package com.bromleyoil.smaugdb.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.SpawnType;

public class Spawn {

	private static final Logger log = LoggerFactory.getLogger(Spawn.class);

	private static final AtomicInteger idGenerator = new AtomicInteger();

	private int id;
	private Mob mob;
	private SpawnType type;

	private Mob containingMob;
	private Item containingItem;
	private Room containingRoom;
	private int limit;
	private Prog prog;

	/** Equipment and inventory are assigned to the Spawn */
	private Set<Pop> containedPops = new TreeSet<>(Pop.EQUIP_ORDER);

	private Spawn() {
		id = idGenerator.getAndIncrement();
	}

	public static Spawn in(Mob mob, Room room, int limit) {
		log.debug("Spawning {} in {} with limit {}", mob, room, limit);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.APPEARS);
		spawn.setRoom(room);
		spawn.setLimit(limit);
		mob.addSpawn(spawn);
		room.addContainedSpawn(spawn);
		return spawn;
	}

	public static Spawn produced(Mob mob, Prog prog, Mob producer) {
		log.debug("Producing {} from mob {}", mob, producer);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.PRODUCED_MOB);
		spawn.setProg(prog);
		spawn.setContainingMob(producer);
		mob.addSpawn(spawn);
		producer.addContainedSpawn(spawn);
		return spawn;
	}

	public static Spawn produced(Mob mob, Prog prog, Item producer) {
		log.debug("Producing {} from item {}", mob, producer);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.PRODUCED_ITEM);
		spawn.setProg(prog);
		spawn.setContainingItem(producer);
		mob.addSpawn(spawn);
		producer.addContainedSpawn(spawn);
		return spawn;
	}

	public static Spawn produced(Mob mob, Prog prog, Room producer) {
		log.debug("Producing {} from room {}", mob, producer);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.PRODUCED_ROOM);
		spawn.setProg(prog);
		spawn.setRoom(producer);
		mob.addSpawn(spawn);
		producer.addContainedSpawn(spawn);
		return spawn;
	}

	@Override
	public String toString() {
		return String.format("%s in %s%s", mob, containingRoom, limit == 1 ? "" : (" (max " + limit + ")"));
	}

	/** Gets a description of this spawn from the mob's perspective. */
	public String getDescription() {
		if (getType() == SpawnType.APPEARS) {
			return String.format("May be found %s", (mob.hasActFlag(ActFlag.SENTINEL) ? "in " : "around "));
		} else if (getType() == SpawnType.PRODUCED_MOB
				|| getType() == SpawnType.PRODUCED_ITEM
				|| getType() == SpawnType.PRODUCED_ROOM) {
			return String.format("May be summoned %s by", getProg().getType().getLabel());
		} else {
			return "Unrelated to";
		}
	}

	/** Gets a description of this spawn from the container's perspective. */
	public String getContainedDescription() {
		if (getType() == SpawnType.PRODUCED_MOB
				|| getType() == SpawnType.PRODUCED_ITEM
				|| getType() == SpawnType.PRODUCED_ROOM) {
			return String.format("May summon %s", getProg().getType().getLabel());
		} else {
			return "Unrelated to";
		}
	}

	public int getId() {
		return id;
	}

	public SpawnType getType() {
		return type;
	}

	public void setType(SpawnType type) {
		this.type = type;
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
		return containingRoom;
	}

	public void setRoom(Room room) {
		this.containingRoom = room;
	}

	public Mob getContainingMob() {
		return containingMob;
	}

	public void setContainingMob(Mob containingMob) {
		this.containingMob = containingMob;
	}

	public Item getContainingItem() {
		return containingItem;
	}

	public void setContainingItem(Item containingItem) {
		this.containingItem = containingItem;
	}

	/** The maximum number of this mob that can spawn in the area */
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Prog getProg() {
		return prog;
	}

	public void setProg(Prog prog) {
		this.prog = prog;
	}

	public Collection<Pop> getContainedPops() {
		return Collections.unmodifiableCollection(containedPops);
	}

	public void addContainedPop(Pop containedPop) {
		containedPops.add(containedPop);
	}
}
