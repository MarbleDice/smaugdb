package com.bromleyoil.smaugdb.form;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractSearchForm<T> {
	public List<T> search(Collection<T> collection) {
		return applyFilters(collection.stream())
				.sorted(getComparator())
				.collect(Collectors.toList());
	}

	protected abstract Stream<T> applyFilters(Stream<T> stream);

	protected abstract Comparator<T> getComparator();

	protected Stream<T> maybeFilter(Stream<T> stream, boolean isFiltered, Predicate<T> pred) {
		return isFiltered ? stream.filter(pred) : stream;
	}

	protected Stream<T> maybeInvertFilter(Stream<T> stream, boolean isFiltered, boolean isInverted, Predicate<T> pred) {
		if (isFiltered) {
			return isInverted ? stream.filter(pred.negate()) : stream.filter(pred);
		} else {
			return stream;
		}
	}
}
