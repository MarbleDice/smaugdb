package com.bromleyoil.smaugdb.parser;

import java.util.ArrayList;
import java.util.List;

public class SmaugParser {

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 * @return
	 */
	public static <T extends Enum<?>> List<T> of(Class<T> enumType, int bitVector) {
		List<T> flags = new ArrayList<>();

		for (int i = enumType.getEnumConstants().length - 1; i >= 0; i--) {
			double bitFlag = Math.pow(2, i);
			if (bitVector >= bitFlag) {
				bitVector -= bitFlag;
				flags.add(enumType.getEnumConstants()[i]);
			}
		}

		return flags;
	}

}
