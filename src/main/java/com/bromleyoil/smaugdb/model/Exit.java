package com.bromleyoil.smaugdb.model;

import com.bromleyoil.smaugdb.model.enums.Direction;

public class Exit {
	private Room roomFrom;
	private Room roomTo;
	private Direction direction;
	private boolean isDoor;
	private boolean isLocked;
	private boolean isPickProof;
	private boolean isPassProof;
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

	public boolean isDoor() {
		return isDoor;
	}

	public void setIsDoor(boolean isDoor) {
		this.isDoor = isDoor;
	}

	public boolean isClosed() {
		return true;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isPickProof() {
		return isPickProof;
	}

	public void setIsPickProof(boolean isPickProof) {
		this.isPickProof = isPickProof;
	}

	public boolean isPassProof() {
		return isPassProof;
	}

	public void setIsPassProof(boolean isPassProof) {
		this.isPassProof = isPassProof;
	}

	public Item getKey() {
		return key;
	}

	public void setKey(Item key) {
		this.key = key;
	}
}
