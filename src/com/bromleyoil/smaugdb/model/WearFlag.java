package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public enum WearFlag {
	TAKE, FINGER, NECK, BODY, HEAD, LEGS, FEET, HANDS, ARMS, SHIELD, ABOUT, WAIST, WRIST, WIELD, HOLD, DUAL_WIELD, EARS,
	EYES, MISSILE_WIELD, BACK, FACE, ANKLE;

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 * @return
	 */
	public static List<WearFlag> of(int bitVector) {
		List<WearFlag> flags = new ArrayList<>();

		for (int i = values().length - 1; i >= 0; i--) {
			double bitFlag = Math.pow(2, i);
			if (bitVector >= bitFlag) {
				bitVector -= bitFlag;
				flags.add(values()[i]);
			}
		}

		return flags;
	}
}
