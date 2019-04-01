package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public enum ActFlag {
	IS_NPC, SENTINEL, SCAVENGER, NOLOCATE, AGGRESSIVE, STAY_AREA, WIMPY, PET, TRAIN, PRACTICE, IMMORTAL, DEADLY,
	POLYSELF, META_AGGR, GUARDIAN, RUNNING, NOWANDER, MOUNTABLE, MOUNTED, SCHOLAR, SECRETIVE, HARDHAT, MOBINVIS,
	NOASSIST, AUTONOMOUS, PACIFIST, NOATTACK, ANNOYING, STATSHIELD, PROTOTYPE, NOSUMMON, NOSTEAL, INFEST, BLOCKING,
	IS_CLONE, IS_DREAMFORM, IS_SPIRITFORM, IS_PROJECTION, STOP_SCRIPT;

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 * @return
	 */
	public static List<ActFlag> of(int bitVector) {
		List<ActFlag> flags = new ArrayList<>();

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
