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

	private Object owner;

	private int worldLimit;
	private int roomLimit;
	private int maxSpawnCount;
	private Prog prog;

	/** Equipment and inventory are assigned to the Spawn */
	private Set<Pop> containedPops = new TreeSet<>(Pop.EQUIP_ORDER);

	private Spawn() {
		id = idGenerator.getAndIncrement();
	}

	public static Spawn in(Mob mob, Room room, int limit) {
		return in(mob, room, limit, limit);
	}

	public static Spawn in(Mob mob, Room room, int worldLimit, int roomLimit) {
		log.debug("Spawning {} in {} with limit {}", mob, room, worldLimit);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.APPEARS);
		spawn.setOwner(room);
		spawn.setWorldLimit(worldLimit);
		spawn.setRoomLimit(roomLimit);
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
		spawn.setOwner(prog);
		mob.addSpawn(spawn);
		prog.addContainedSpawn(spawn);
		return spawn;
	}

	public static Spawn produced(Mob mob, Prog prog, Item producer) {
		log.debug("Producing {} from item {}", mob, producer);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.PRODUCED_ITEM);
		spawn.setProg(prog);
		spawn.setOwner(prog);
		mob.addSpawn(spawn);
		prog.addContainedSpawn(spawn);
		return spawn;
	}

	public static Spawn produced(Mob mob, Prog prog, Room producer) {
		log.debug("Producing {} from room {}", mob, producer);
		Spawn spawn = new Spawn();
		spawn.setMob(mob);
		spawn.setType(SpawnType.PRODUCED_ROOM);
		spawn.setProg(prog);
		spawn.setOwner(prog);
		mob.addSpawn(spawn);
		prog.addContainedSpawn(spawn);
		return spawn;
	}

	@Override
	public String toString() {
		return String.format("%s in %s%s", mob, owner, worldLimit == 1 ? "" : (" (max " + worldLimit + ")"));
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
		if (getType() == SpawnType.APPEARS) {
			return "May contain";
		} else if (getType() == SpawnType.PRODUCED_MOB
				|| getType() == SpawnType.PRODUCED_ITEM
				|| getType() == SpawnType.PRODUCED_ROOM) {
			return "May summon";
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

	public Object getOwner() {
		return owner;
	}

	public void setOwner(Object owner) {
		this.owner = owner;
	}

	public Object getLinkableOwner() {
		if (getOwner() instanceof Room) {
			return getOwner();
		} else if (getOwner() instanceof Prog) {
			return ((Prog) getOwner()).getOwner();
		} else {
			throw new UnsupportedOperationException("Unknown owner type: " + getOwner());
		}
	}

	/** The mob that spawns */
	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}

	/** The maximum number of this mob that can spawn in the area */
	public int getWorldLimit() {
		return worldLimit;
	}

	public void setWorldLimit(int worldLimit) {
		this.worldLimit = worldLimit;
	}

	public int getRoomLimit() {
		return roomLimit;
	}

	public void setRoomLimit(int roomLimit) {
		this.roomLimit = roomLimit;
	}

	public int getMaxSpawnCount() {
		return maxSpawnCount;
	}

	public void setMaxSpawnCount(int maxSpawnCount) {
		this.maxSpawnCount = maxSpawnCount;
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
