package com.bromleyoil.smaugdb;

import java.util.ArrayList;
import java.util.Collection;

public class ValueList extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;

	public ValueList(Collection<Integer> collection) {
		super(collection);
	}

	@Override
	public Integer get(int i) {
		return size() > i ? super.get(i) : 0;
	}
}
