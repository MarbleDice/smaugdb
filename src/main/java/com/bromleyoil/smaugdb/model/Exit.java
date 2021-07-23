package com.bromleyoil.smaugdb.model;

import com.bromleyoil.smaugdb.model.enums.Direction;

public class Exit {
	private Room roomFrom;
	private Room roomTo;
	private Direction direction;
	private Item key;

	public Room getRoomFrom() {
		return roomFrom;
	}

	public void setRoomFrom(Room roomFrom) {
		this.roomFrom = roomFrom;
	}

	public Room getRoomTo() {
		return roomTo;
	}

	public void setRoomTo(Room roomTo) {
		this.roomTo = roomTo;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Item getKey() {
		return key;
	}

	public void setKey(Item key) {
		this.key = key;
	}

	public boolean isLocked() {
		return key != null;
	}
}
