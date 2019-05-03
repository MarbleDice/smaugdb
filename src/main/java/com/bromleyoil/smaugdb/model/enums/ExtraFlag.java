package com.bromleyoil.smaugdb.model.enums;

import java.util.EnumSet;
import java.util.Set;

public enum ExtraFlag implements Labelable {
	GLOW, HUM, DARK, LOYAL, EVIL, INVIS, MAGIC, NODROP, BLESS, ANTI_GOOD, ANTI_EVIL, ANTI_NEUTRAL, NOREMOVE, INVENTORY,
	ANTI_MAGE, ANTI_THIEF, ANTI_WARRIOR, ANTI_CLERIC, ORGANIC, METAL, DONATION, CLANOBJECT, CLANCORPSE, ANTI_VAMPIRE,
	ANTI_DRUID, HIDDEN, POISONED, COVERING, DEATHROT, BURIED, PROTOTYPE, NOLOCATE, GROUNDROT, LOOTABLE, PERMANENT,
	MULTI_INVOKE, DEATHDROP, SKINNED, NOFILL, BLACKENED, NOSCAVANGE;

	private static EnumSet<ExtraFlag> alignments;
	private static EnumSet<ExtraFlag> classes;
	private static EnumSet<ExtraFlag> requirements;
	private static EnumSet<ExtraFlag> nonRequirements;

	private static void constructSubsets() {
		if (alignments == null) {
			alignments = EnumSet.of(ANTI_EVIL, ANTI_NEUTRAL, ANTI_GOOD);
			classes = EnumSet.of(ANTI_WARRIOR, ANTI_THIEF, ANTI_MAGE, ANTI_CLERIC, ANTI_DRUID, ANTI_VAMPIRE);
			requirements = EnumSet.copyOf(alignments);
			requirements.addAll(classes);
			nonRequirements = EnumSet.complementOf(requirements);
		}
	}

	public static Set<ExtraFlag> getAlignments() {
		constructSubsets();
		return alignments;
	}

	public static Set<ExtraFlag> getClasses() {
		constructSubsets();
		return classes;
	}

	public static Set<ExtraFlag> getRequirements() {
		constructSubsets();
		return requirements;
	}

	public static Set<ExtraFlag> getNonRequirements() {
		constructSubsets();
		return nonRequirements;
	}
}