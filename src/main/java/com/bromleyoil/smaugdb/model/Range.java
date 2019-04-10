package com.bromleyoil.smaugdb.model;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

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

	public static Range of(int anchor, int below, int above) {
		return new Range(anchor - below, anchor + above);
	}

	public static Range of(String dice) {
		Matcher matcher = dicePattern.matcher(dice);
		if (matcher.matches()) {
			int n = Integer.parseInt(matcher.group(1));
			int s = Integer.parseInt(matcher.group(2));
			int p = Integer.parseInt(matcher.group(3));
			return new Range(n + p, n * s + p);
		} else {
			throw new IllegalArgumentException("Invalid dice format: " + dice);
		}
	}

	public static RangeCollector collector() {
		return new RangeCollector();
	}

	private static class RangeCollector implements Collector<Range, RangeCollector, Range> {

		private Range range;

		@Override
		public Supplier<RangeCollector> supplier() {
			return RangeCollector::new;
		}

		@Override
		public BiConsumer<RangeCollector, Range> accumulator() {
			return (c, r) -> {
				if (range == null) {
					range = new Range(r.getMin(), r.getMax());
				} else {
					range.extend(r);
				}
			};
		}

		@Override
		public BinaryOperator<RangeCollector> combiner() {
			return (c1, c2) -> {
				c1.range.extend(c2.range);
				return c1;
			};
		}

		@Override
		public Function<RangeCollector, Range> finisher() {
			return c -> c.range;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return EnumSet.of(Characteristics.UNORDERED);
		}
	}

	public void extend(Range range) {
		setMin(range.getMin() < getMin() ? range.getMin() : getMin());
		setMax(range.getMax() > getMax() ? range.getMax() : getMax());
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
