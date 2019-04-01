package com.bromleyoil.smaugdb.model;

public class Mob {

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

}
