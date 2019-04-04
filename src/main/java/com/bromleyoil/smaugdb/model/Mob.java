package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;

public class Mob {

	private Area area;
	int vnum;
	private String name;
	private String keywords;
	private String description;
	private List<String> longDescription;
	private List<ActFlag> actFlags;
	private List<AffectFlag> affectFlags;
	private int alignment;
	private int level;
	private int hitroll;
	private int armor;
	private Range hp;
	private Range damage;
	private int gold;
	private int experience;

	private List<Spawn> spawns = new ArrayList<>();
	private List<Pop> containedPops = new ArrayList<>();

	@Override
	public String toString() {
		return getName();
	}

	public boolean getExists() {
		return !spawns.isEmpty();
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

	public String getTitle() {
		return StringUtils.capitalize(name);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(List<String> longDescription) {
		this.longDescription = longDescription;
	}

	public List<ActFlag> getActFlags() {
		return actFlags;
	}

	public void setActFlags(List<ActFlag> actFlags) {
		this.actFlags = actFlags;
	}

	public List<AffectFlag> getAffectFlags() {
		return affectFlags;
	}

	public void setAffectFlags(List<AffectFlag> affectFlags) {
		this.affectFlags = affectFlags;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHitroll() {
		return hitroll;
	}

	public void setHitroll(int hitroll) {
		this.hitroll = hitroll;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public Range getHp() {
		return hp;
	}

	public void setHp(Range hp) {
		this.hp = hp;
	}

	public Range getDamage() {
		return damage;
	}

	public void setDamage(Range damage) {
		this.damage = damage;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public List<Spawn> getSpawns() {
		return spawns;
	}

	public void addSpawn(Spawn spawn) {
		spawns.add(spawn);
	}

	public List<Pop> getContainedPops() {
		return containedPops;
	}

	public void addContainedPop(Pop pop) {
		containedPops.add(pop);
	}
}
