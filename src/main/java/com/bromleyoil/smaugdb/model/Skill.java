package com.bromleyoil.smaugdb.model;

public class Skill {

	private String name;
	private int slot;

	public Skill(int slot, String name) {
		this.slot = slot;
		this.name = name;
	}

	public static Skill of(int slot) {
		return new Skill(slot, "Spell " + slot);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
}
