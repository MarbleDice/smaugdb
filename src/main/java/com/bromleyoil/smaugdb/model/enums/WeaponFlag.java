package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

public enum WeaponFlag implements Labelable {
	FLAMING("A"), FROST("B"), VAMPIRIC("C"), SHARP("D"), VORPAL("E"), TWO_HANDS("F"), SHOCKING("G"), POISON("H");

	private String code;

	private WeaponFlag(String code) {
		this.code = code;
	}

	public static Optional<WeaponFlag> ofCode(String code) {
		for (WeaponFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
