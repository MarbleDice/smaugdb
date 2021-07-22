package com.bromleyoil.smaugdb.model.enums;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum ExtraFlag implements Labelable {
	GLOW("A"), HUM("B"), DARK("C"), LOYAL, EVIL("E"), INVIS("F"), MAGIC("G"), NODROP("H"), BLESS("I"), ANTI_GOOD("J"),
	ANTI_EVIL("K"), ANTI_NEUTRAL("L"), NOREMOVE("M"), INVENTORY("N"), ANTI_MAGE, ANTI_THIEF, ANTI_WARRIOR, ANTI_CLERIC,
	ORGANIC, METAL, DONATION, CLANOBJECT, CLANCORPSE, ANTI_VAMPIRE, ANTI_DRUID, HIDDEN, POISONED, COVERING,
	DEATHROT("P"), BURIED, PROTOTYPE, NOLOCATE("T"), GROUNDROT, LOOTABLE, PERMANENT, MULTI_INVOKE, DEATHDROP, SKINNED,
	NOFILL, BLACKENED, NOSCAVANGE, NOPURGE("O"), VISDEATH("Q"), NOSAC("R"), MELT_DROP("U"), SELL_EXTRACT("W"),
	BURN_PROOF("Y"), NOUNCURSE("Z");

	private static EnumSet<ExtraFlag> alignments;
	private static EnumSet<ExtraFlag> classes;
	private static EnumSet<ExtraFlag> requirements;
	private static EnumSet<ExtraFlag> nonRequirements;

	private String code = "";

	private ExtraFlag() {
	}

	private ExtraFlag(String code) {
		this.code = code;
	}

	public static Optional<ExtraFlag> ofCode(String code) {
		for (ExtraFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}

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