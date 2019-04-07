package com.bromleyoil.smaugdb.model;

import com.bromleyoil.smaugdb.model.enums.ApplyType;

public class Apply {

	private ApplyType type;
	private int value;

	public Apply(ApplyType type, int value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s %s%d", type, value >= 0 ? "+" : "", value);
	}
	public ApplyType getType() {
		return type;
	}

	public void setType(ApplyType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
