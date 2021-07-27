package com.bromleyoil.smaugdb.form;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.Labelable;
import com.bromleyoil.smaugdb.model.enums.WeaponFlag;
import com.bromleyoil.smaugdb.model.enums.WeaponType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class ItemSearchForm {
	private String name;
	private Area area;
	private Integer minLevel;
	private Integer maxLevel;
	private boolean notItemType;
	private ItemType itemType;
	private ExtraFlag extraFlag;
	private WeaponType weaponType;
	private WeaponFlag weaponFlag;
	private WearFlag wearFlag;
	private ApplyType applyType;
	private Integer applyValue;

	private Format format;

	public enum Format implements Labelable {
		DEFAULT, VALUE;
	}

	public List<Item> apply(Collection<Item> items) {
		Stream<Item> stream = items.stream();
		if (getName() != null) {
			stream = stream.filter(x -> x.getName().toLowerCase().contains(getName().toLowerCase()));
		}
		if (getArea() != null) {
			stream = stream.filter(x -> x.getArea().equals(getArea()));
		}
		if (getMinLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getMax() >= getMinLevel());
		}
		if (getMaxLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getMin() <= getMaxLevel());
		}
		if (getItemType() != null) {
			if (getNotItemType()) {
				stream = stream.filter(x -> x.getType() != getItemType());
			} else {
				stream = stream.filter(x -> x.getType() == getItemType());
			}
		}
		if (extraFlag != null) {
			stream = stream.filter(x -> x.hasExtraFlag(extraFlag));
		}
		if (weaponType != null) {
			stream = stream.filter(x -> x.getWeaponType() == weaponType);
		}
		if (weaponFlag != null) {
			stream = stream.filter(x -> x.hasWeaponFlag(weaponFlag));
		}
		if (getWearFlag() != null) {
			stream = stream.filter(x -> x.hasWearFlag(getWearFlag()));
		}
		if (getApplyType() != null) {
			if (getApplyValue() != null) {
				stream = stream.filter(x -> x.hasApply(getApplyType(), getApplyValue()));
			} else {
				stream = stream.filter(x -> x.hasApply(getApplyType()));
			}
		}

		return stream.filter(Item::getExists)
				.sorted(getComparator())
				.collect(Collectors.toList());
	}

	protected Comparator<Item> getComparator() {
		if (format == Format.VALUE) {
			return Comparator.comparingInt(Item::getCost).reversed();
		} else {
			return Comparator.comparingDouble(x -> x.getLevel().getAverage());
		}
	}

	public boolean showWeapon() {
		return (itemType == ItemType.WEAPON && !notItemType)
				|| weaponType != null || weaponFlag != null;
	}

	public boolean showWear() {
		return !showWeapon() && format == Format.DEFAULT;
	}

	public boolean showSummary() {
		return format == Format.DEFAULT;
	}

	public boolean showCost() {
		return format == Format.VALUE;
	}

	public boolean showApply() {
		return format == Format.DEFAULT;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Integer minLevel) {
		this.minLevel = minLevel;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public boolean getNotItemType() {
		return notItemType;
	}

	public void setNotItemType(boolean notItemType) {
		this.notItemType = notItemType;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public ExtraFlag getExtraFlag() {
		return extraFlag;
	}

	public void setExtraFlag(ExtraFlag extraFlag) {
		this.extraFlag = extraFlag;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public WeaponFlag getWeaponFlag() {
		return weaponFlag;
	}

	public void setWeaponFlag(WeaponFlag weaponFlag) {
		this.weaponFlag = weaponFlag;
	}

	public WearFlag getWearFlag() {
		return wearFlag;
	}

	public void setWearFlag(WearFlag wearFlag) {
		this.wearFlag = wearFlag;
	}

	public ApplyType getApplyType() {
		return applyType;
	}

	public void setApplyType(ApplyType applyType) {
		this.applyType = applyType;
	}

	public Integer getApplyValue() {
		return applyValue;
	}

	public void setApplyValue(Integer applyValue) {
		this.applyValue = applyValue;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}
}
