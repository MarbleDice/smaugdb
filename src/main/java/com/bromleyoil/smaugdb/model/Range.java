package com.bromleyoil.smaugdb.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Range {

	private static final Pattern dicePattern = Pattern.compile("(\\d+)\\s*d\\s*(\\d+)\\s*\\+\\s*(\\d+)");

	private int min;
	private int max;

	private Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public static Range of(int min, int max) {
		return new Range(min, max);
	}

	public static Range of(String dice) {
		Matcher matcher = dicePattern.matcher(dice);
		if (matcher.matches()) {
			int n = Integer.parseInt(matcher.group(1));
			int s = Integer.parseInt(matcher.group(2));
			int p = Integer.parseInt(matcher.group(3));
			return new Range(n + p, n * s + p);
		} else {
			// TODO lol ugh
			throw new RuntimeException("Invalid dice format: " + dice);
		}
	}

	@Override
	public String toString() {
		return min == max ? String.valueOf(min) : String.format("%d - %d", min, max);
	}
	// TODO lol nope
	public double getAverage() {
		return (min + max) / 2d;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
