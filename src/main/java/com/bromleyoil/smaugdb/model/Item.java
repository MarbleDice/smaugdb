package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

import com.bromleyoil.smaugdb.model.enums.DamageType;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.WeaponSkill;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class Item {

	private Area area;
	private int vnum;
	private String name;
	private String keywords;
	private String shortDescription;
	private ItemType itemType;
	private DamageType damageType;
	private List<WearFlag> wearLocations = new ArrayList<>();
	private int minDamage;
	private int maxDamage;
	private int damRoll;

	// Wands, staves, potions, pills
	private int spellLevel;
	private List<Skill> skills = new ArrayList<>();

	private List<Pop> pops = new ArrayList<>();
	private List<Pop> containedPops = new ArrayList<>();

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

	public int getSpellLevel() {
		return spellLevel;
	}

	public void setSpellLevel(int spellLevel) {
		this.spellLevel = spellLevel;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Pop> getPops() {
		return pops;
	}

	public void addPop(Pop pop) {
		pops.add(pop);
	}

	public List<Pop> getContainedPops() {
		return containedPops;
	}

	public void addContainedPop(Pop pop) {
		containedPops.add(pop);
	}
}