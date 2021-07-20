package com.bromleyoil.smaugdb.model;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.WeaponType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;
import com.bromleyoil.smaugdb.parser.RomParser;

@Component
public class World {

	private static final Logger log = LoggerFactory.getLogger(World.class);

	@Value("${mud.path:}")
	private String mudPath;

	private Map<String, Area> areas = new TreeMap<>();
	private Map<Integer, Room> rooms = new HashMap<>();
	private Map<Integer, Mob> mobs = new HashMap<>();
	private Map<Integer, Item> items = new HashMap<>();

	private List<ItemType> itemTypes;
	private List<WeaponType> weaponTypes;
	private List<WearFlag> wearFlags;
	private List<ApplyType> applyTypes;

	@PostConstruct
	public void postConstruct() {
		if (StringUtils.isBlank(mudPath)) {
			throw new IllegalArgumentException("You must specify --mud.path=<PATH TO MUD> as an argument!");
		} else if (!Paths.get(mudPath).toFile().isDirectory()) {
			throw new IllegalArgumentException("Invalid mud.path: " + mudPath);
		}

		// SmaugParser.loadWorld(this, mudPath);
		RomParser.loadWorld(this, mudPath);
	}

	public Collection<Area> getAreas() {
		return Collections.unmodifiableCollection(areas.values());
	}

	public Area getArea(String urlSafeName) {
		return areas.get(urlSafeName);
	}

	public void addArea(Area area) {
		areas.put(area.getUrlSafeName(), area);
	}

	public Collection<Room> getRooms() {
		return Collections.unmodifiableCollection(rooms.values());
	}

	public Room getRoom(int vnum) {
		if (vnum > 0 && !rooms.containsKey(vnum)) {
			log.warn("Room not found: {}", vnum);
		}
		return rooms.get(vnum);
	}

	public void addRoom(Room room, Area area) {
		log.trace("Adding room \"{}\" to {}", room, area);
		room.setArea(area);
		area.addRoom(room);
		rooms.put(room.getVnum(), room);
	}

	public Collection<Mob> getMobs() {
		return Collections.unmodifiableCollection(mobs.values());
	}

	public Mob getMob(int vnum) {
		if (vnum > 0 && !mobs.containsKey(vnum)) {
			log.warn("Mob not found: {}", vnum);
		}
		return mobs.get(vnum);
	}

	public void addMob(Mob mob, Area area) {
		log.trace("Adding mob \"{}\" to {}", mob, area);
		mob.setArea(area);
		area.addMob(mob);
		mobs.put(mob.getVnum(), mob);
	}

	public Collection<Item> getItems() {
		return Collections.unmodifiableCollection(items.values());
	}

	public Item getItem(int vnum) {
		if (vnum > 0 && !items.containsKey(vnum)) {
			log.warn("Item not found: {}", vnum);
		}
		return items.get(vnum);
	}

	public void addItem(Item item, Area area) {
		log.trace("Adding item \"{}\" to {}", item, area);
		item.setArea(area);
		area.addItem(item);
		items.put(item.getVnum(), item);
	}
	
	public List<ItemType> getItemTypes() {
		if (itemTypes == null) {
			itemTypes = items.values().stream()
					.map(Item::getType)
					.distinct()
					.sorted(Comparator.comparing(ItemType::name))
					.collect(Collectors.toList());
		}
		return itemTypes;
	}

	public List<WeaponType> getWeaponTypes() {
		if (weaponTypes == null) {
			weaponTypes = items.values().stream()
					.map(Item::getWeaponType)
					.filter(Objects::nonNull)
					.distinct()
					.sorted(Comparator.comparing(WeaponType::name))
					.collect(Collectors.toList());
		}
		return weaponTypes;
	}

	public List<WearFlag> getWearFlags() {
		if (wearFlags == null) {
			wearFlags = items.values().stream()
					.flatMap(x -> x.getWearFlags().stream())
					.distinct()
					.sorted(Comparator.comparing(WearFlag::name))
					.collect(Collectors.toList());
		}
		return wearFlags;
	}

	public List<ApplyType> getApplyTypes() {
		if (applyTypes == null) {
			applyTypes = items.values().stream()
					.flatMap(x -> x.getApplies().stream())
					.map(Apply::getType)
					.distinct()
					.sorted(Comparator.comparing(ApplyType::name))
					.collect(Collectors.toList());
		}
		return applyTypes;
	}
}
