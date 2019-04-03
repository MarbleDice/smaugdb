package com.bromleyoil.smaugdb.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class Area {

	private String name;
	private String author;
	private int lowSoftRange;
	private int highSoftRange;
	private Collection<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getName));
	private Collection<Mob> mobs = new TreeSet<>(Comparator.comparing(Mob::getName));
	private Collection<Item> items = new TreeSet<>(Comparator.comparing(Item::getName));

	@Override
	public String toString() {
		return getUrlSafeName();
	}

	public String getName() {
		return name;
	}

	public String getUrlSafeName() {
		return String.join("-", name.split("[^a-zA-Z]+")).toLowerCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public Collection<Room> getRooms() {
		return Collections.unmodifiableCollection(rooms);
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public Collection<Mob> getMobs() {
		return Collections.unmodifiableCollection(mobs);
	}

	public void addMob(Mob mob) {
		mobs.add(mob);
	}

	public Collection<Item> getItems() {
		return Collections.unmodifiableCollection(items);
	}

	public void addItem(Item item) {
		items.add(item);
	}
}
