package com.bromleyoil.smaugdb.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.bromleyoil.smaugdb.model.enums.Direction;

public class Path {
	private List<Exit> exits;
	private StringBuilder sb = new StringBuilder("#");
	private Direction lastDirection;
	private int lastDirectionCount;
	private boolean requiresSwim;
	private boolean requiresFlight;
	private int hostileTo;
	private int hostileToDetect;

	private Path() {
		exits = new ArrayList<>();
	}

	private Path(Path path, Exit exit) {
		exits = new ArrayList<>(path.exits);
		sb = new StringBuilder(path.sb);
		lastDirection = path.lastDirection;
		lastDirectionCount = path.lastDirectionCount;
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

	private static void expandExits(Path path, Queue<Path> paths) {
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

	private String lastDirectionToString() {
		if (lastDirectionCount == 0) {
			return "";
		} else {
			return String.format("%s%s", lastDirectionCount > 1 ? lastDirectionCount : "", lastDirection.getCode());
		} 
	}
}
