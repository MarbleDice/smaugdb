package com.bromleyoil.smaugdb.form;

import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.WeaponType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class ItemSearchForm {
	private String name;
	private Integer minLevel;
	private Integer maxLevel;
	private boolean notItemType;
	private ItemType itemType;
	private WeaponType weaponType;
	private WearFlag wearFlag;
	private ApplyType applyType;
	private Integer applyValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
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
}
