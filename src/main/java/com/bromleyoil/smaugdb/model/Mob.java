package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.Utils;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;

/**
 * Represents a mobile.
 * 
 */
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
	private int thac0;
	private int hitroll;
	private int armor;
	private Range hp;
	private Range damage;
	private int gold;
	private int experience;

	private List<Spawn> spawns = new ArrayList<>();
	private List<Pop> containedPops = new ArrayList<>();

	public String getCssClass() {
		return hasActFlag(ActFlag.AGGRESSIVE) ? "mob aggressive" : "mob non-aggressive";
	}

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

	public boolean hasActFlag(ActFlag actFlag) {
		return actFlags.contains(actFlag);
	}

	public void setActFlags(List<ActFlag> actFlags) {
		this.actFlags = actFlags;
	}

	public List<AffectFlag> getAffectFlags() {
		return affectFlags;
	}

	public boolean hasAffectFlag(AffectFlag affectFlag) {
		return affectFlags.contains(affectFlag);
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
		// TODO fuzzed
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Mob THAC0 - Higher is better. Mobs gain +(1/32) hitroll per level for each point.
	 * Players interpolate between 18 and 6-12 based on class over 32 levels, gaining 9-18 hitroll at level 50.
	 * 
	 * 1 hitroll = 5% chance to hit
	 * Armor class = Lower is better. The best is -190. Target AC reduces hitroll at 10 AC = -1 hitroll.
	 * Weapon not visible is +1 hitroll
	 * Can't see opponent is -4 hitroll
	 * Weapon proficiency is -5 to +4 hitroll
	 * Smarter combatant gains +0.1 hitroll per logged player kill per point of int difference
	 * 
	 */
	public int getThac0() {
		return thac0;
	}

	public void setThac0(int thac0) {
		this.thac0 = thac0;
	}

	public int getHitroll() {
		return hitroll;
	}

	public void setHitroll(int hitroll) {
		this.hitroll = hitroll;
	}

	public String getAccuracy() {
		int opponentArmor = -100;

		int acc = Utils.interpolate(level, 0, thac0, 32, 0);

		return String.valueOf(acc);
	}

	public int getArmor() {
		// TODO autogen
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public Range getHp() {
		// TODO autogen
		return hp;
	}

	public void setHp(Range hp) {
		this.hp = hp;
	}

	public Range getDamage() {
		// TODO autogen
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
