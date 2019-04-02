package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public class Mob {

	private Area area;
	int vnum;
	private String name;
	private String keywords;
	private String shortDescription;
	// private String longDescription;
	// Act flags bitv
	// Affected by bitv
	// private int alignment;
	// private int level;
	// private int thac0;
	// private int ac;
	// private String hitDice;
	// private String damageDice;
	// private int gold;
	// private int experience;
	private List<Reset> resets = new ArrayList<>();

	@Override
	public String toString() {
		return getName();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int vnum) {
		this.vnum = vnum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public List<Reset> getResets() {
		return resets;
	}

	public void setResets(List<Reset> resets) {
		this.resets = resets;
	}
}
