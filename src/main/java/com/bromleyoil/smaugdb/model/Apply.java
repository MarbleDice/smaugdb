package com.bromleyoil.smaugdb.model;

import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.Labelable;

public class Apply implements Labelable {

	private ApplyType type;
	private int value;

	public Apply(ApplyType type, int value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String getCustomLabel() {
		return String.format("%s %s%d", type.getLabel(), value >= 0 ? "+" : "", value);
	}

	@Override
	public String toString() {
		return getCustomLabel();
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
