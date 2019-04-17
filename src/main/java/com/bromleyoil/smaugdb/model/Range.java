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
		conform();
	}

	public static Range of(int value) {
		return new Range(value, value);
	}

	public static Range of(int min, int max) {
		return new Range(min, max);
	}

	public static Range of(Range other) {
		return new Range(other.min, other.max);
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

	public static RangeCollector unionCollector() {
		return new RangeCollector(Range::union);
	}

	private static class RangeCollector implements Collector<Range, RangeCollector, Range> {

		private Range range;
		private BiConsumer<Range, Range> accumulatingFunctor;

		public RangeCollector(BiConsumer<Range, Range> accumulatingFunctor) {
			this.accumulatingFunctor = accumulatingFunctor;
		}

		@Override
		public Supplier<RangeCollector> supplier() {
			return () -> new RangeCollector(accumulatingFunctor);
		}

		@Override
		public BiConsumer<RangeCollector, Range> accumulator() {
			return (c, r) -> {
				if (c.range == null) {
					c.range = new Range(r.getMin(), r.getMax());
				} else {
					c.accumulatingFunctor.accept(c.range, r);
				}
			};
		}

		@Override
		public BinaryOperator<RangeCollector> combiner() {
			return (c1, c2) -> {
				c1.accumulatingFunctor.accept(c1.range, c2.range);
				return c1;
			};
		}

		@Override
		public Function<RangeCollector, Range> finisher() {
			return c -> c.range == null ? Range.of(0) : c.range;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return EnumSet.of(Characteristics.UNORDERED);
		}
	}

	public Range union(Range range) {
		min = range.getMin() > getMin() ? range.getMin() : getMin();
		max = range.getMax() < getMax() ? range.getMax() : getMax();
		conform();
		return this;
	}

	public Range intersect(Range range) {
		min = range.getMin() < getMin() ? range.getMin() : getMin();
		max = range.getMax() > getMax() ? range.getMax() : getMax();
		conform();
		return this;
	}

	public Range min(Range range) {
		min = range.getMin() < getMin() ? range.getMin() : getMin();
		max = range.getMax() < getMax() ? range.getMax() : getMax();
		conform();
		return this;
	}

	public Range max(Range range) {
		min = range.getMin() > getMin() ? range.getMin() : getMin();
		max = range.getMax() > getMax() ? range.getMax() : getMax();
		conform();
		return this;
	}

	public Range add(int amount) {
		min = getMin() + amount;
		max = getMax() + amount;
		conform();
		return this;
	}

	public Range subtract(int amount) {
		min = getMin() - amount;
		max = getMax() - amount;
		conform();
		return this;
	}

	public Range extend(int amount) {
		min = getMin() - amount;
		max = getMax() + amount;
		conform();
		return this;
	}

	public Range adjust(int minAdjustBy, int maxAdjustBy) {
		min = getMin() + minAdjustBy;
		max = getMax() + maxAdjustBy;
		conform();
		return this;
	}

	public Range constrainMin(int range) {
		if (min < max - range) {
			min = max - range;
		}
		conform();
		return this;
	}

	public Range constrainMax(int range) {
		if (max > min + range) {
			max = min + range;
		}
		conform();
		return this;
	}

	private void conform() {
		if (min > max) {
			max = min;
		}
	}

	@Override
	public String toString() {
		return getMin() == getMax() ? String.valueOf(getMin()) : String.format("%d - %d", getMin(), getMax());
	}

	public double getAverage() {
		return (getMin() + getMax()) / 2d;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
