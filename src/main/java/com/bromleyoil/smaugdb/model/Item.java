package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.model.enums.ContainerFlag;
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
	private int suggestedLevel;
	private int weight;
	private int cost;
	private List<Apply> applies = new ArrayList<>();
	private List<Integer> values = new ArrayList<>();
	private int capacity;
	private List<ContainerFlag> containerFlags = new ArrayList<>();
	private Item key;
	// TODO also track doors unlocked
	private List<Item> keyTo = new ArrayList<>();
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

	public boolean isItem() {
		return hasWearFlag(WearFlag.TAKE);
	}

	public boolean isEquipment() {
		for (WearFlag wearFlag : wearFlags) {
			if (WearFlag.EQUIP_FLAGS.contains(wearFlag)) {
				return true;
			}
		}
		return false;
	}

	public boolean isWeapon() {
		return type == ItemType.WEAPON;
	}

	public boolean isArmor() {
		return type == ItemType.ARMOR;
	}

	public boolean isContainer() {
		return type == ItemType.CONTAINER;
	}

	public Range getLevelRange() {
		return getPops().stream().map(Pop::getItemLevel).collect(Range.unionCollector());
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

	public ItemType getType() {
		return type;
	}

	public String getSubType() {
		String subType = isWeapon() ? String.format(" / %s / %s", getWeaponSkill(), getDamageType()) : "";
		return String.format("%s%s", type, subType);
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

	/** The item's declared level (only used sometimes) */
	public int getSuggestedLevel() {
		return suggestedLevel;
	}

	public void setSuggestedLevel(int suggestedLevel) {
		this.suggestedLevel = suggestedLevel;
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

	public List<Apply> getApplies() {
		return applies;
	}

	public void addApply(Apply apply) {
		applies.add(apply);
	}

	public void setApplies(List<Apply> applies) {
		this.applies = applies;
	}

	public List<Integer> getValues() {
		return values;
	}

	public Integer getValue(int index) {
		return values.get(index);
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

	public int getLightHours() {
		return type == ItemType.LIGHT ? values.get(2) : 0;
	}

	public int getSpellLevel() {
		return ItemType.SPELL_ITEMS.contains(type)
				? values.get(0)
				: 0;
	}

	public List<Skill> getSkills() {
		if (type == ItemType.SALVE) {
			return Stream.of(Skill.of(values.get(4)), Skill.of(values.get(5))).collect(Collectors.toList());
		} else if (ItemType.MAGICAL_DEVICES.contains(type)) {
			// Wands and staves
			return Stream.of(Skill.of(values.get(3))).collect(Collectors.toList());
		} else if (ItemType.SPELL_ITEMS.contains(type)) {
			// Potions, scrolls, pills
			return Stream.of(Skill.of(values.get(1)), Skill.of(values.get(2)), Skill.of(values.get(3)))
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	public int getMinDamage() {
		return type == ItemType.WEAPON ? values.get(1) : 0;
	}

	public int getMaxDamage() {
		return type == ItemType.WEAPON ? values.get(1) * values.get(2) : 0;
	}

	public DamageType getDamageType() {
		return type == ItemType.WEAPON ? DamageType.values()[values.get(3)] : DamageType.NONE;
	}

	public WeaponSkill getWeaponSkill() {
		return getDamageType().getWeaponSkill();
	}

	public String getDamage() {
		// TODO range
		return String.format("%d - %d", getMinDamage(), getMaxDamage());
	}

	public int getArmor() {
		return type == ItemType.ARMOR ? values.get(1) : 0;
	}

	public List<ContainerFlag> getContainerFlags() {
		return containerFlags;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setContainerFlags(List<ContainerFlag> containerFlags) {
		this.containerFlags = containerFlags;
	}

	public Item getKey() {
		return key;
	}

	public void setKey(Item key) {
		this.key = key;
		if (key != null) {
			key.keyTo.add(this);
		}
	}

	public List<Item> getKeyTo() {
		return keyTo;
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
