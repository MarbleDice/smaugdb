package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.model.enums.DamageType;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.WeaponSkill;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class Item {

	private Area area;
	private int vnum;
	private String name;
	private String keywords;
	private String description;
	private ItemType type;
	private List<ExtraFlag> extraFlags = new ArrayList<>();
	private List<WearFlag> wearFlags = new ArrayList<>();
	private int weight;
	private int cost;

	private DamageType damageType;
	private int minDamage;
	private int maxDamage;
	private int damRoll;

	// Wands, staves, potions, pills
	private int spellLevel;
	private List<Skill> skills = new ArrayList<>();

	private List<Pop> pops = new ArrayList<>();
	private List<Pop> containedPops = new ArrayList<>();

	public String getCssClass() {
		if (!hasWearFlag(WearFlag.TAKE)) {
			return "item decoration";
		} else if (isEquipment()) {
			return "item equipment";
		} else {
			return "item object";
		}
	}

	public boolean isEquipment() {
		for (WearFlag wearFlag : wearFlags) {
			if (WearFlag.EQUIP_FLAGS.contains(wearFlag)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

	public boolean getExists() {
		return !pops.isEmpty();
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

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public List<ExtraFlag> getExtraFlags() {
		return extraFlags;
	}

	public boolean hasExtraFlag(ExtraFlag extraFlag) {
		return extraFlags.contains(extraFlag);
	}

	public void setExtraFlags(List<ExtraFlag> extraFlags) {
		this.extraFlags = extraFlags;
	}

	public List<WearFlag> getWearFlags() {
		return wearFlags;
	}

	public boolean hasWearFlag(WearFlag wearFlag) {
		return wearFlags.contains(wearFlag);
	}

	public void setWearFlags(List<WearFlag> wearFlags) {
		this.wearFlags = wearFlags;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
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
