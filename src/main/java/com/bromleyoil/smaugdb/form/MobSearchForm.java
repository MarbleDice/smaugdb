package com.bromleyoil.smaugdb.form;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.enums.ActFlag;

public class MobSearchForm {
	private String name;
	private Area area;
	private Integer minLevel;
	private Integer maxLevel;
	private Integer alignment;
	private Integer spawnCount;
	private Double damage;
	private ActFlag actFlag;

	private Integer playerLevel;
	private Integer playerAlignment;
	private Integer totalPartyLevel;

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

	public Integer getAlignment() {
		return alignment;
	}

	public void setAlignment(Integer alignment) {
		this.alignment = alignment;
	}

	public Integer getSpawnCount() {
		return spawnCount;
	}

	public void setSpawnCount(Integer spawnCount) {
		this.spawnCount = spawnCount;
	}

	public Double getDamage() {
		return damage;
	}

	public void setDamage(Double damage) {
		this.damage = damage;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ActFlag getActFlag() {
		return actFlag;
	}

	public void setActFlag(ActFlag actFlag) {
		this.actFlag = actFlag;
	}

	public Integer getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(Integer playerLevel) {
		this.playerLevel = playerLevel;
	}

	public Integer getPlayerAlignment() {
		return playerAlignment;
	}

	public void setPlayerAlignment(Integer playerAlignment) {
		this.playerAlignment = playerAlignment;
	}

	public Integer getTotalPartyLevel() {
		return totalPartyLevel;
	}

	public void setTotalPartyLevel(Integer totalPartyLevel) {
		this.totalPartyLevel = totalPartyLevel;
	}
}
