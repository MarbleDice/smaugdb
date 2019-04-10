package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.Utils;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;
import com.bromleyoil.smaugdb.model.enums.AttackFlag;
import com.bromleyoil.smaugdb.model.enums.DefenseFlag;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ResistFlag;

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
	private List<String> longDescription = new ArrayList<>();
	private List<ActFlag> actFlags = new ArrayList<>();
	private List<AffectFlag> affectFlags = new ArrayList<>();
	private int alignment;
	private int level;
	private int gold;
	private int experience;
	private Range hp;
	private int armor;
	private int thac0;
	private int hitroll;
	private Range damage;
	private int damroll;
	private List<ExtraFlag> extraFlags = new ArrayList<>();
	private List<ResistFlag> resistFlags = new ArrayList<>();
	private List<ResistFlag> immuneFlags = new ArrayList<>();
	private List<ResistFlag> vulnerableFlags = new ArrayList<>();
	private List<AttackFlag> attackFlags = new ArrayList<>();
	private List<DefenseFlag> defenseFlags = new ArrayList<>();
	private List<Spawn> spawns = new ArrayList<>();
	private List<Pop> containedPops = new ArrayList<>();

	public String getCssClass() {
		return hasActFlag(ActFlag.AGGRESSIVE) ? "mob aggressive" : "mob non-aggressive";
	}

	public String getAccuracy() {
		int accuracy = hitroll - Utils.interpolate(level, 0, thac0, 32, 0);
		accuracy = Utils.constrain(1, 19, accuracy);
		return String.valueOf(accuracy);
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

	public String getTruncatedName() {
		return name.length() <= 45 ? name : name.substring(0, 40) + "...";
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

	public int getDamroll() {
		return damroll;
	}

	public void setDamroll(int damroll) {
		this.damroll = damroll;
	}

	public List<ExtraFlag> getExtraFlags() {
		return Collections.unmodifiableList(extraFlags);
	}

	public void setExtraFlags(List<ExtraFlag> extraFlags) {
		this.extraFlags = extraFlags;
	}

	public List<ResistFlag> getResistFlags() {
		return Collections.unmodifiableList(resistFlags);
	}

	public void setResistFlags(List<ResistFlag> resistFlags) {
		this.resistFlags = resistFlags;
	}

	public List<ResistFlag> getImmuneFlags() {
		return Collections.unmodifiableList(immuneFlags);
	}

	public void setImmuneFlags(List<ResistFlag> immuneFlags) {
		this.immuneFlags = immuneFlags;
	}

	public List<ResistFlag> getVulnerableFlags() {
		return Collections.unmodifiableList(vulnerableFlags);
	}

	public void setVulnerableFlags(List<ResistFlag> vulnerableFlags) {
		this.vulnerableFlags = vulnerableFlags;
	}

	public List<AttackFlag> getAttackFlags() {
		return Collections.unmodifiableList(attackFlags);
	}

	public void setAttackFlags(List<AttackFlag> attackFlags) {
		this.attackFlags = attackFlags;
	}

	public List<DefenseFlag> getDefenseFlags() {
		return Collections.unmodifiableList(defenseFlags);
	}

	public void setDefenseFlags(List<DefenseFlag> defenseFlags) {
		this.defenseFlags = defenseFlags;
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
