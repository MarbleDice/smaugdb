package com.bromleyoil.smaugdb.model;

public class Reset {

	private int minItemLevel;
	private int maxItemLevel;
	private Item item;
	private Mob mob;
	private Item container;
	private Room room;

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
