package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

	private Area area;
	private int vnum;
	private String name;
	private String description;
	private List<RoomFlag> roomFlags = new ArrayList<>();
	private SectorType sectorType;
	private List<String> extras = new ArrayList<>();
	// teleports
	// exits
	private List<Reset> resets = new ArrayList<>();

	@Override
	public String toString() {
		return getName();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int vnum) {
		this.vnum = vnum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoomFlag> getRoomFlags() {
		return roomFlags;
	}

	public void setRoomFlags(List<RoomFlag> roomFlags) {
		this.roomFlags = roomFlags;
	}

	public SectorType getSectorType() {
		return sectorType;
	}

	public void setSectorType(SectorType sectorType) {
		this.sectorType = sectorType;
	}

	public List<String> getExtras() {
		return extras;
	}

	public void setExtras(List<String> extras) {
		this.extras = extras;
	}

	public List<Reset> getResets() {
		return resets;
	}

	public void setResets(List<Reset> resets) {
		this.resets = resets;
	}
}
