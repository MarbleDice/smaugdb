package com.bromleyoil.smaugdb.model.enums;

public enum ResistFlag implements Labelable {
	FIRE("H"), COLD("I"), ELECTRICITY("J"), ENERGY("O"), BLUNT("E"), PIERCE("F"), SLASH("G"), ACID("K"), POISON("L"),
	DRAIN("M"), SLEEP, CHARM("B"), HOLD, NONMAGIC("D"), PLUS1, PLUS2, PLUS3, PLUS4, PLUS5, PLUS6, MAGIC("C"), PARALYSIS,
	SUMMON("A"), HOLY("N"), MENTAL("P"), DISEASE("Q"), DROWNING("R"), LIGHT("S"), SOUND("T"), WOOD("X"), SILVER("Y"),
	IRON("Z");

	private String code = "";

	private ResistFlag() {
	}

	private ResistFlag(String code) {
		this.code = code;
	}

	public static ResistFlag ofCode(String code) {
		for (ResistFlag resistFlag : values()) {
			if (resistFlag.getCode().equals(code)) {
				return resistFlag;
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
