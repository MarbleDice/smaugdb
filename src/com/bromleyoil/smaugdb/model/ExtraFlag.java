package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.List;

public enum ExtraFlag {
	GLOW, HUM, DARK, LOYAL, EVIL, INVIS, MAGIC, NODROP, BLESS, ANTI_GOOD, ANTI_EVIL, ANTI_NEUTRAL, NOREMOVE, INVENTORY,
	ANTI_MAGE, ANTI_THIEF, ANTI_WARRIOR, ANTI_CLERIC, ORGANIC, METAL, DONATION, CLANOBJECT, CLANCORPSE, ANTI_VAMPIRE,
	ANTI_DRUID, HIDDEN, POISONED, COVERING, DEATHROT, BURIED, PROTOTYPE, NOLOCATE, GROUNDROT, LOOTABLE, PERMANENT,
	MULTI_INVOKE, DEATHDROP, SKINNED, NOFILL, BLACKENED, NOSCAVANGE;

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 * @return
	 */
	public static List<ExtraFlag> of(int bitVector) {
		List<ExtraFlag> flags = new ArrayList<>();

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
