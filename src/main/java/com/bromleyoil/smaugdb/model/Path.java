package com.bromleyoil.smaugdb.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.bromleyoil.smaugdb.model.enums.AffectFlag;
import com.bromleyoil.smaugdb.model.enums.Direction;
import com.bromleyoil.smaugdb.model.enums.SectorType;

public class Path {
	private List<Exit> exits;
	private StringBuilder sb = new StringBuilder("#");
	private Direction lastDirection;
	private int lastDirectionCount;
	private boolean requiresBoat;
	private boolean requiresFlight;
	private int hostileTo;
	private int hostileToInvis;

	private Path() {
		exits = new ArrayList<>();
	}

	private Path(Path path, Exit exit) {
		// Copy constructor
		exits = new ArrayList<>(path.exits);
		sb = new StringBuilder(path.sb);
		lastDirection = path.lastDirection;
		lastDirectionCount = path.lastDirectionCount;
		requiresBoat = path.requiresBoat;
		requiresFlight = path.requiresFlight;
		hostileTo = path.hostileTo;
		hostileToInvis = path.hostileToInvis;

		// Add the new exit
		addExit(exit);
	}

	private Path(Exit exit) {
		exits = new ArrayList<>();
		addExit(exit);
	}

	public static void setAllPaths(Room fromRoom) {
		Queue<Path> paths = new ArrayDeque<>();
		// Set the path to the root room
		fromRoom.setPath(new Path());

		// Prime the queue with exits from the root room
		fromRoom.getExits().stream().map(Path::new).forEach(paths::add);

		// Process the queue with a BFS to get shortest paths
		while (!paths.isEmpty()) {
			Path path = paths.remove();
			expandExits(path, paths);
		}
	}

	protected static void expandExits(Path path, Queue<Path> paths) {
		// Expand every exit from the last step in the path
		path.getLastExit().getRoomTo().getExits().stream()
				// Filter for exits to rooms without a path
				.filter(x -> x.getRoomTo().getPath() == null)
				// Convert each exit to a new, slightly longer path
				.map(x -> new Path(path, x))
				// Set the path on the destination room and add it to the queue
				.forEach(x -> {
					Room roomTo = x.getLastExit().getRoomTo();
					roomTo.setPath(x);
					paths.add(x);
					// Check for area entrances
					if (!x.getLastExit().getRoomFrom().getArea().equals(roomTo.getArea())) {
						roomTo.getArea().addEntrance(x);
					}
				});
	}

	@Override
	public String toString() {
		return sb.toString() + lastDirectionToString();
	}

	public int getLength() {
		return exits.size();
	}

	public Exit getLastExit() {
		return exits.get(exits.size() - 1);
	}

	protected void addExit(Exit exit) {
		exits.add(exit);
		// Check for boat/flight requirements
		Room roomTo = exit.getRoomTo();
		if (roomTo.getSectorType() == SectorType.WATER_NOSWIM) {
			requiresBoat = true;
		} else if (roomTo.getSectorType() == SectorType.AIR) {
			requiresFlight = true;
		}

		// Check for aggressive mobs
		roomTo.getContainedSpawns().stream().map(Spawn::getMob).filter(Mob::isAggressive).forEach(x -> {
			if (x.hasAffectFlag(AffectFlag.DETECT_INVIS)) {
				hostileToInvis = Integer.max(hostileToInvis, x.getLevel().getMax() + 5);
			} else {
				hostileTo = Integer.max(hostileTo, x.getLevel().getMax() + 5);
			}
		});

		// Accumulate the directions
		if (lastDirection == null) {
			lastDirection = exit.getDirection();
			lastDirectionCount = 1;
		} else {
			if (lastDirection == exit.getDirection()) {
				lastDirectionCount++;
			} else {
				sb.append(lastDirectionToString());
				lastDirection = exit.getDirection();
				lastDirectionCount = 1;
			}
		}
	}

	protected String lastDirectionToString() {
		if (lastDirectionCount == 0) {
			return "";
		} else {
			return String.format("%s%s", lastDirectionCount > 1 ? lastDirectionCount : "", lastDirection.getCode());
		}
	}

	public List<String> getRequirements() {
		List<String> rv = new ArrayList<>();
		if (hostileToInvis > 0) {
			rv.add(String.format("Hostile to level %d and below (enemies detect invis)", hostileToInvis));
		}
		if (hostileTo > 0 && hostileTo > hostileToInvis) {
			rv.add(String.format("Hostile to level %d and below (invis ok)", hostileTo));
		}
		if (requiresFlight) {
			rv.add("Requires flight");
		} else if (requiresBoat) {
			rv.add("Requires a boat");
		}
		return rv;
	}
}
