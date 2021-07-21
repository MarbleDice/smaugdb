package com.bromleyoil.smaugdb.model.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum WearFlag implements Labelable {
	TAKE("A"), FINGER("B"), NECK("C"), BODY("D"), HEAD("E"), LEGS("F"), FEET("G"), HANDS("H"), ARMS("I"), SHIELD("J"),
	ABOUT("K"), WAIST("L"), WRIST("M"), WIELD("N"), HOLD("O"), DUAL_WIELD, EARS, EYES, MISSILE_WIELD, BACK, FACE, ANKLE,
	FLOAT("Q");

	public static final List<WearFlag> EQUIP_FLAGS = Collections.unmodifiableList(
			Arrays.asList(values()).subList(1, values().length));

	private String code = "";

	private WearFlag() {
	}

	private WearFlag(String code) {
		this.code = code;
	}

	public static Optional<WearFlag> ofCode(String code) {
		for (WearFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
