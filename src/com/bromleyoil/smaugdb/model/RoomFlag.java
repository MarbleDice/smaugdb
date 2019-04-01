package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public enum RoomFlag {
	DARK, DEATH, NO_MOB, INDOORS, HOUSE, NEUTRAL, CHAOTIC, NO_MAGIC, NOLOCATE, PRIVATE, SAFE, SOLITARY, PET_SHOP,
	NO_RECALL, DONATION, NODROPALL, SILENCE, LOGSPEECH, NODROP, CLANSTOREROOM, NO_SUMMON, NO_ASTRAL, TELEPORT,
	TELESHOWDESC, NOFLOOR, NOSUPPLICATE, ARENA, NOMISSILE, AUCTION, NOHOVER, PROTOTYPE, DND, TRACK, LIGHT, NOLOG, COLOR,
	NOWHERE, NOYELL, NOQUIT, NOTRACK, NS_CORPSE, NS_RECALL;

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 * @return
	 */
	public static List<RoomFlag> of(int bitVector) {
		List<RoomFlag> flags = new ArrayList<>();

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
