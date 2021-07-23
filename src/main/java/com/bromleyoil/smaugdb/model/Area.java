package com.bromleyoil.smaugdb.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class Area {

	private String name;
	private String author;
	private Range softRange;
	private Range hardRange;
	private Range generatedRange;

	private Collection<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getName)
			.thenComparing(Comparator.comparing(Room::getVnum)));
	private Collection<Mob> mobs = new TreeSet<>(Comparator.comparing(Mob::getName)
			.thenComparing(Comparator.comparing(Mob::getVnum)));
	private Collection<Item> items = new TreeSet<>(Comparator.comparing(Item::getName)
			.thenComparing(Comparator.comparing(Item::getVnum)));
	private Collection<Path> entrances = new TreeSet<>(Comparator.comparingInt(Path::getLength));

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

	public Range getSoftRange() {
		return softRange;
	}

	public void setSoftRange(Range softRange) {
		this.softRange = softRange;
	}

	public Range getHardRange() {
		return hardRange;
	}

	public void setHardRange(Range hardRange) {
		this.hardRange = hardRange;
	}

	public Range getGeneratedRange() {
		return generatedRange;
	}

	public void setGeneratedRange(Range generatedRange) {
		this.generatedRange = generatedRange;
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
	
	public Collection<Path> getEntrances() {
		return Collections.unmodifiableCollection(entrances);
	}
	
	public void addEntrance(Path path) {
		entrances.add(path);
	}
}
