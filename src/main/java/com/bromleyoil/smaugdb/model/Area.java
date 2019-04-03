package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public class Area {

	private String name;
	private int lowSoftRange;
	private int highSoftRange;
	private List<Room> rooms = new ArrayList<>();
	private List<Mob> mobs = new ArrayList<>();
	private List<Item> items = new ArrayList<>();

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLowSoftRange() {
		return lowSoftRange;
	}

	public void setLowSoftRange(int lowSoftRange) {
		this.lowSoftRange = lowSoftRange;
	}

	public int getHighSoftRange() {
		return highSoftRange;
	}

	public void setHighSoftRange(int highSoftRange) {
		this.highSoftRange = highSoftRange;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public List<Mob> getMobs() {
		return mobs;
	}

	public void addMob(Mob mob) {
		mobs.add(mob);
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}
}
