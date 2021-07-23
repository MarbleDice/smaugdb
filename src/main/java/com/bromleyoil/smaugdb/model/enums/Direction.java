package com.bromleyoil.smaugdb.model.enums;

public enum Direction implements Labelable {
	NORTH, EAST, SOUTH, WEST, UP, DOWN;

	public String getCode() {
		return name().substring(0, 1).toLowerCase();
	}
}
