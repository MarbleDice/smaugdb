package com.bromleyoil.smaugdb.form;

public class MobSearchForm {
	private String name;
	private Integer minLevel;
	private Integer maxLevel;
	private Integer alignment;
	private Integer spawnCount;

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
}
