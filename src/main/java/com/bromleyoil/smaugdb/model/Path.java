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

	public Path() {
		exits = new ArrayList<>();
	}

	public Path(Path path, Exit exit) {
		exits = new ArrayList<>(path.exits);
		sb = new StringBuilder(path.sb);
		lastDirection = path.lastDirection;
		lastDirectionCount = path.lastDirectionCount;
		addExit(exit);
	}

	public Path(Exit exit) {
		exits = new ArrayList<>();
		addExit(exit);
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

	public static void findAllPaths(Room fromRoom) {
		Queue<Path> paths = new ArrayDeque<>();
		// Set the path to the root room
		fromRoom.setPath(new Path());

		// Prime the queue with exits from the root 
		fromRoom.getExits().stream().map(Path::new).forEach(paths::add);

		// Process the queue with a BFS to get shortest paths
		while (!paths.isEmpty()) {
			Path path = paths.remove();
			expandExits(path, paths);
		}
	}

	private static void expandExits(Path path, Queue<Path> paths) {
		path.getLast().getTo().getExits().stream()
				.filter(x -> x.getTo().getPath() == null)
				.map(x -> new Path(path, x))
				.forEach(x -> {
					x.getLast().getTo().setPath(x);
					paths.add(x);
				});
	}

	@Override
	public String toString() {
		return sb.toString() + lastDirectionToString();
	}

	public Exit getLast() {
		return exits.get(exits.size() - 1);
	}
}
