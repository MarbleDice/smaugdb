package com.bromleyoil.smaugdb.model;

import java.util.List;

public class Item {

	private int vnum;
	private String name;
	private String keywords;
	private String shortDescription;
	private ItemType itemType;
	private DamageType damageType;
	private List<WearFlag> wearLocations;
	private int minDamage;
	private int maxDamage;
	private int damRoll;

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

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public WeaponSkill getWeaponSkill() {
		return damageType.getWeaponSkill();
	}

	public List<WearFlag> getWearLocations() {
		return wearLocations;
	}

	public void setWearLocations(List<WearFlag> wearLocations) {
		this.wearLocations = wearLocations;
	}

	public int getMinDamage() {
		return minDamage;
	}

	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}

	public int getDamRoll() {
		return damRoll;
	}

	public void setDamRoll(int damRoll) {
		this.damRoll = damRoll;
	}
}
