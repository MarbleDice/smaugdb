package com.bromleyoil.smaugdb.model;

import com.bromleyoil.smaugdb.model.enums.Direction;

public class Exit {
	private Room from;
	private Room to;
	private Direction dir;
	private Item key;

	public Room getFrom() {
		return from;
	}

	public void setFrom(Room from) {
		this.from = from;
	}

	public Room getTo() {
		return to;
	}

	public void setTo(Room to) {
		this.to = to;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
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
