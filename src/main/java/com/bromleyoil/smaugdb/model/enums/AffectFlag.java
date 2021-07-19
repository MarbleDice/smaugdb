package com.bromleyoil.smaugdb.model.enums;

public enum AffectFlag implements Labelable {
	BLIND, INVISIBLE("B"), DETECT_EVIL("C"), DETECT_INVIS("D"), DETECT_MAGIC("E"), DETECT_HIDDEN("F"), HOLD,
	SANCTUARY("H"), FAERIE_FIRE("I"), INFRARED("J"), CURSE, FLAMING, POISON, PROTECT, PARALYSIS, SNEAK("P"), HIDE("Q"),
	SLEEP, CHARM, FLYING("T"), PASS_DOOR("U"), FLOATING, TRUESIGHT, DETECTTRAPS, SCRYING, FIRESHIELD, SHOCKSHIELD,
	HAUS1, ICESHIELD, POSSESS, BERSERK, AQUA_BREATH, RECURRINGSPELL, CONTAGIOUS, ACIDMIST, VENOMSHIELD, ITEM_AURAS,
	PEOPLE_AURAS, SENSE_DEAD, HEAR_DEAD, SEE_DEAD, FADE, CHAIN_AGONY, INFEST, GRAPPLE, DETECT_GOOD("G"),
	PROTECT_EVIL("N"), PROTECT_GOOD("O"), HASTE("V"), DARK_VISION("Z"), SWIMMING("b"), REGENERATION("c");

	private String code = "";

	private AffectFlag() {
	}

	private AffectFlag(String code) {
		this.code = code;
	}

	public static AffectFlag ofCode(String code) {
		for (AffectFlag affectFlag : values()) {
			if (affectFlag.getCode().equals(code)) {
				return affectFlag;
			}
		}
		throw new IllegalArgumentException(code);
	}

	/**
	 * @return The ROM character code
	 */
	public String getCode() {
		return code;
	}
}
