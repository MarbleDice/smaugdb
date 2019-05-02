package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
	private Range level;
	private int weight;
	private int cost;
	private List<Apply> applies = new ArrayList<>();

	private List<Integer> values = new ArrayList<>();
	private Range totalArmor;
	private Range damage;
	private int capacity;
	private List<ContainerFlag> containerFlags = new ArrayList<>();

	private Item key;
	private List<Item> keyItems = new ArrayList<>();
	private List<Room> keyDoors = new ArrayList<>();

	private List<Pop> pops = new ArrayList<>();

	private List<Prog> progs = new ArrayList<>();

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
		String subType = isWeapon()
				? String.format(" / %s / %s", getWeaponSkill().getLabel(), getDamageType().getLabel())
				: "";
		return String.format("%s%s", type.getLabel(), subType);
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

	public Collection<WearFlag> getWearFlags() {
		return wearFlags;
	}

	public boolean hasWearFlag(WearFlag wearFlag) {
		return wearFlags.contains(wearFlag);
	}

	public void setWearFlags(List<WearFlag> wearFlags) {
		this.wearFlags = wearFlags;
	}

	public Collection<WearFlag> getEquipFlags() {
		return wearFlags.stream().filter(f -> f != WearFlag.TAKE).collect(Collectors.toList());
	}

	/** The item's declared level (only used sometimes) */
	public int getSuggestedLevel() {
		return suggestedLevel;
	}

	public void setSuggestedLevel(int suggestedLevel) {
		this.suggestedLevel = suggestedLevel;
	}

	public Range getLevel() {
		return level;
	}

	public void setLevel(Range level) {
		this.level = level;
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

	public Range getDamage() {
		return damage;
	}

	public void setDamage(Range damage) {
		this.damage = damage;
	}

	public DamageType getDamageType() {
		return type == ItemType.WEAPON ? DamageType.values()[values.get(3)] : DamageType.NONE;
	}

	public WeaponSkill getWeaponSkill() {
		return getDamageType().getWeaponSkill();
	}

	public int getArmor() {
		return type == ItemType.ARMOR ? values.get(1) : 0;
	}

	public Range getTotalArmor() {
		return totalArmor;
	}

	public void setTotalArmor(Range totalArmor) {
		this.totalArmor = totalArmor;
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

	public boolean isUsableKey() {
		return !keyItems.isEmpty() || !keyDoors.isEmpty();
	}

	public Item getKey() {
		return key;
	}

	public void setKey(Item key) {
		this.key = key;
		if (key != null) {
			key.keyItems.add(this);
		}
	}

	public Collection<Item> getKeyItems() {
		return Collections.unmodifiableCollection(keyItems);
	}

	public Collection<Room> getKeyDoors() {
		return Collections.unmodifiableCollection(keyDoors);
	}

	public void addKeyDoor(Room keyDoor) {
		keyDoors.add(keyDoor);
	}

	public List<Pop> getPops() {
		return pops;
	}

	public Collection<Pop> getSortedPops() {
		Set<Pop> rv = new TreeSet<>(Pop.TYPE_ORDER);
		rv.addAll(pops);
		return rv;
	}

	public void addPop(Pop pop) {
		pops.add(pop);
	}

	public Collection<Prog> getProgs() {
		return Collections.unmodifiableCollection(progs);
	}

	public void setProgs(List<Prog> progs) {
		progs.stream().forEach(p -> p.setOwner(this));
		this.progs = progs;
	}
}
