package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.model.enums.Direction;
import com.bromleyoil.smaugdb.model.enums.RoomFlag;
import com.bromleyoil.smaugdb.model.enums.SectorType;

public class Room {

	private boolean isLoaded;
	private Area area;
	private int vnum;
	private String name;
	private List<String> description;
	private List<Exit> exits = new ArrayList<>();
	private int randomExitCount;
	private List<RoomFlag> roomFlags = new ArrayList<>();
	private SectorType sectorType;
	private List<String> extras = new ArrayList<>();

	private List<Pop> containedPops = new ArrayList<>();
	private List<Spawn> containedSpawns = new ArrayList<>();

	private List<Prog> progs = new ArrayList<>();

	private Path path;

	@Override
	public String toString() {
		return getName();
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
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

	public String getTitle() {
		return StringUtils.capitalize(name);
	}

	public String getStyledName() {
		return "[" + name + "]";
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public List<Exit> getExits() {
		return exits;
	}

	public void setExits(List<Exit> exits) {
		this.exits = exits;
	}

	public Exit getExit(Direction direction) {
		return exits.stream().filter(x -> x.getDirection() == direction).findAny().orElse(null);
	}

	public int getRandomExitCount() {
		return randomExitCount;
	}

	public void setRandomExitCount(int randomExitCount) {
		this.randomExitCount = randomExitCount;
	}

	public boolean isMaze() {
		return randomExitCount > 0;
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

	public List<Direction> getDirections () {
		return exits.stream().map(Exit::getDirection).collect(Collectors.toList());
	}
	public Collection<Pop> getContainedPops() {
		return Collections.unmodifiableCollection(containedPops);
	}

	public void addContainedPop(Pop containedPop) {
		containedPops.add(containedPop);
	}

	public Collection<Spawn> getContainedSpawns() {
		return Collections.unmodifiableCollection(containedSpawns);
	}

	public void addContainedSpawn(Spawn containedSpawn) {
		containedSpawns.add(containedSpawn);
	}

	public Collection<Prog> getProgs() {
		return Collections.unmodifiableCollection(progs);
	}

	public void setProgs(List<Prog> progs) {
		progs.stream().forEach(p -> p.setOwner(this));
		this.progs = progs;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
