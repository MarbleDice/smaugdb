package com.bromleyoil.smaugdb.form;

import java.util.Comparator;
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

public class ItemSearchForm extends AbstractSearchForm<Item> {

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
		DEFAULT, HIGHEST_LEVEL, COST;
	}

	@Override
	public Stream<Item> applyFilters(Stream<Item> stream) {
		stream = maybeFilter(stream, name!= null, x -> x.getName().toLowerCase().contains(name));
		stream = maybeFilter(stream, area != null, x -> x.getArea().equals(area));
		stream = maybeFilter(stream, minLevel != null, x -> x.getLevel().getMax() >= minLevel);
		stream = maybeFilter(stream, maxLevel != null, x -> x.getLevel().getMin() <= maxLevel);
		stream = maybeInvertFilter(stream, itemType != null, notItemType, x -> x.getType() == itemType);
		stream = maybeFilter(stream, extraFlag != null, x -> x.hasExtraFlag(extraFlag));
		stream = maybeFilter(stream, weaponType != null, x -> x.getWeaponType() == weaponType);
		stream = maybeFilter(stream, weaponFlag != null, x -> x.hasWeaponFlag(weaponFlag));
		stream = maybeFilter(stream, wearFlag != null, x -> x.hasWearFlag(wearFlag));
		stream = maybeFilter(stream, applyType != null,	applyValue != null
				? x -> x.hasApply(applyType, applyValue)
				: x -> x.hasApply(applyType));
		stream = maybeFilter(stream, true, Item::getExists);

		return stream;
	}

	@Override
	protected Comparator<Item> getComparator() {
		if (format == Format.COST) {
			return Comparator.comparingInt(Item::getCost).reversed();
		} else if (format == Format.HIGHEST_LEVEL) {
			return Comparator.comparingDouble((Item x) -> x.getLevel().getAverage()).reversed();
		} else if (showWeapon()) {
			return Comparator.comparingDouble((Item x) -> x.getDamage().getAverage()).reversed();
		} else {
			return Comparator.comparingDouble(x -> x.getLevel().getAverage());
		}
	}

	public boolean showWeapon() {
		return (itemType == ItemType.WEAPON && !notItemType)
				|| weaponType != null || weaponFlag != null;
	}

	public boolean showWear() {
		return !showWeapon();
	}

	public boolean showSummary() {
		return true;
	}

	public boolean showCost() {
		return format == Format.COST;
	}

	public boolean showApply() {
		return true;
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
