package com.bromleyoil.smaugdb.model.enums;

import org.apache.commons.lang3.StringUtils;

public interface Labelable {

	default String name() {
		return "";
	}

	default String getCustomLabel() {
		return "";
	}

	default String getLabel() {
		return StringUtils.isBlank(getCustomLabel())
				? name().toLowerCase().replace("_", " ")
				: getCustomLabel();
	}
}
