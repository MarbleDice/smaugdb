package com.bromleyoil.smaugdb.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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

	public static final Comparator<Pop> TYPE_ORDER = Comparator.comparing(Pop::getType)
			// Then by mob name
			.thenComparing(p -> p.getMob() == null ? "" : p.getMob().getName())
			// Finally break ties with the id
			.thenComparing(Pop::getId);

	public static final Comparator<Pop> EQUIP_ORDER = Comparator.comparing(Pop::getType)
			// Then by equip location ordinal
			.thenComparing(p -> p.getWearFlag() == null ? 0 : p.getWearFlag().ordinal())
			// Then by item name
			.thenComparing(x -> x.getItem().getName())
			// Finally break ties with the id
			.thenComparing(Pop::getId);

	private int id;
	private Item item;
	private Range itemLevel = Range.of(0, 0);

	/** PopType.SOLD is handled in the getter */
	private PopType type;

	private Object owner;

	private Area area;
	private WearFlag wearFlag;
	private Prog prog;
	private int producedLevel;

	/** Items inside containers are assigned to the Pop */
	private Set<Pop> containedPops = new TreeSet<>(Pop.EQUIP_ORDER);

	private Pop() {
		id = idGenerator.getAndIncrement();
	}

	public static Pop found(Item item, Area area, Room room) {
		log.debug("Dropping {} in {}", item, room);
		Pop pop = new Pop();
		pop.setArea(area);
		pop.setType(PopType.FOUND);
		pop.setItem(item);
		pop.setOwner(room);
		item.addPop(pop);
		room.addContainedPop(pop);
		return pop;
	}

	public static Pop contained(Item item, Area area, Item container) {
		Pop containerPop = container.getPops().get(container.getPops().size() - 1);
		log.debug("Putting {} inside {}", item, container);
		Pop pop = new Pop();
		pop.setArea(area);
		pop.setType(PopType.CONTAINED);
		pop.setItem(item);
		pop.setOwner(containerPop);
		item.addPop(pop);
		containerPop.addContainedPop(pop);
		return pop;
	}

	public static Pop held(Item item, Area area, Spawn spawn) {
		log.debug("Giving {} to {}", item, spawn.getMob());
		Pop pop = new Pop();
		pop.setArea(area);
		pop.setType(PopType.HELD);
		pop.setItem(item);
		pop.setOwner(spawn);
		item.addPop(pop);
		spawn.addContainedPop(pop);
		return pop;
	}

	public static Pop worn(Item item, Area area, Spawn spawn, WearFlag wearFlag) {
		log.debug("Equipping {} to {}", item, spawn.getMob());
		Pop pop = new Pop();
		pop.setArea(area);
		pop.setType(PopType.WORN);
		pop.setItem(item);
		pop.setOwner(spawn);
		pop.setWearFlag(wearFlag);
		item.addPop(pop);
		spawn.addContainedPop(pop);
		return pop;
	}

	public static Pop produced(Item item, Prog prog, Mob producer, int level) {
		log.debug("Producing {} from mob {}", item, producer);
		Pop pop = new Pop();
		pop.setType(PopType.PRODUCED_MOB);
		pop.setProg(prog);
		pop.setItem(item);
		pop.setProducedLevel(level);
		pop.setOwner(prog);
		item.addPop(pop);
		prog.addContainedPop(pop);
		return pop;
	}

	public static Pop produced(Item item, Prog prog, Item producer, int level) {
		log.debug("Producing {} from item {}", item, producer);
		Pop pop = new Pop();
		pop.setType(PopType.PRODUCED_ITEM);
		pop.setProg(prog);
		pop.setItem(item);
		pop.setProducedLevel(level);
		pop.setOwner(prog);
		item.addPop(pop);
		prog.addContainedPop(pop);
		return pop;
	}

	public static Pop produced(Item item, Prog prog, Room producer, int level) {
		log.debug("Producing {} from room {}", item, producer);
		Pop pop = new Pop();
		pop.setType(PopType.PRODUCED_ROOM);
		pop.setProg(prog);
		pop.setItem(item);
		pop.setProducedLevel(level);
		pop.setOwner(prog);
		item.addPop(pop);
		prog.addContainedPop(pop);
		return pop;
	}

	@Override
	public String toString() {
		if (getType() == PopType.FOUND) {
			return String.format("%s found in %s", item, owner);
		} else if (getType() == PopType.CONTAINED) {
			return String.format("%s contained in %s", item, owner);
		} else if (getType() == PopType.WORN) {
			return String.format("%s worn by %s", item, owner);
		} else if (getType() == PopType.HELD) {
			return String.format("%s held by %s", item, owner);
		} else if (getType() == PopType.SOLD) {
			return String.format("%s sold by %s", item, owner);
		} else if (getType() == PopType.PRODUCED_MOB) {
			return String.format("%s produced %s by mob %s", item, prog.getType().getLabel(), owner);
		} else if (getType() == PopType.PRODUCED_ITEM) {
			return String.format("%s produced %s by item %s", item, prog.getType().getLabel(), owner);
		} else if (getType() == PopType.PRODUCED_ROOM) {
			return String.format("%s produced %s by room %s", item, prog.getType().getLabel(), owner);
		} else {
			return String.format("%s appears in an unknown location", item);
		}
	}

	/** Gets a description of this pop from the item's perspective. */
	public String getDescription() {
		if (getType() == PopType.WORN) {
			return "Equipped by ";
		} else if (getType() == PopType.HELD) {
			return "Carried by ";
		} else if (getType() == PopType.CONTAINED) {
			return "Contained in ";
		} else if (getType() == PopType.FOUND) {
			return "Found in ";
		} else if (getType() == PopType.SOLD) {
			return "Sold by ";
		} else if (getType() == PopType.PRODUCED_MOB
				|| getType() == PopType.PRODUCED_ITEM
				|| getType() == PopType.PRODUCED_ROOM) {
			return String.format("May be produced %s by", prog.getType().getLabel());
		} else {
			return "Unrelated to ";
		}
	}

	/** Gets a description of this pop from the container's perspective. */
	public String getContainedDescription() {
		if (getType() == PopType.FOUND) {
			return getItem().hasWearFlag(WearFlag.TAKE) ? "May contain" : "Contains";
		} else if (getType() == PopType.WORN) {
			return "Equipped with ";
		} else if (getType() == PopType.HELD) {
			return "Carrying ";
		} else if (getType() == PopType.SOLD) {
			return "Sells ";
		} else if (getType() == PopType.CONTAINED) {
			return "Containing ";
		} else if (getType() == PopType.PRODUCED_MOB) {
			return "May produce";
		} else {
			return "Unrelated to ";
		}
	}

	/** The unique ID of this pop record for consistency with equals */
	public int getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	/** The item that pops */
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	/** The pop type */
	public PopType getType() {
		return type == PopType.HELD && Optional.ofNullable(getMob()).map(Mob::isShopkeeper).orElse(false)
				? PopType.SOLD
				: type;
	}

	public boolean isSold() {
		return getType() == PopType.SOLD;
	}

	public void setType(PopType type) {
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
		} else if (getOwner() instanceof Pop) {
			return ((Pop) getOwner()).getItem();
		} else if (getOwner() instanceof Spawn) {
			return ((Spawn) getOwner()).getMob();
		} else if (getOwner() instanceof Prog) {
			return ((Prog) getOwner()).getOwner();
		} else {
			throw new UnsupportedOperationException("Unknown owner type: " + getOwner());
		}
	}

	/** Level range the item can appear at this location */
	public Range getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(Range itemLevel) {
		this.itemLevel = itemLevel;
	}

	/** The specific spawn to which the item is assigned */
	public Spawn getSpawn() {
		return owner instanceof Spawn ? (Spawn) owner : null;
	}

	/** The spawned mob the item pops on or in */
	public Mob getMob() {
		if (getOwner() instanceof Spawn) {
			return ((Spawn) getOwner()).getMob();
		} else if (getOwner() instanceof Prog) {
			Prog owningProg = (Prog) getOwner();
			return owningProg.getOwner() instanceof Mob ? (Mob) owningProg.getOwner() : null;
		} else {
			return null;
		}
	}

	/** The location the item is equipped */
	public WearFlag getWearFlag() {
		return wearFlag;
	}

	public void setWearFlag(WearFlag wearFlag) {
		this.wearFlag = wearFlag;
	}

	public int getCost() {
		return getType() == PopType.SOLD ? getItem().getCost() * getSpawn().getMob().getSellPercent() / 100 : 0;
	}

	/** The container the item pops in */
	public Item getContainer() {
		return owner instanceof Pop ? ((Pop) owner).getItem() : null;
	}

	/** The room the item pops in, or the room containing the mob or container the item pops */
	public Room getRoom() {
		return owner instanceof Room ? (Room) owner : null;
	}

	public Prog getProg() {
		return prog;
	}

	public void setProg(Prog prog) {
		this.prog = prog;
	}

	public Collection<Pop> getContainedPops() {
		return containedPops;
	}

	public void addContainedPop(Pop pop) {
		containedPops.add(pop);
	}

	public int getProducedLevel() {
		return producedLevel;
	}

	public void setProducedLevel(int producedLevel) {
		this.producedLevel = producedLevel;
	}

}
