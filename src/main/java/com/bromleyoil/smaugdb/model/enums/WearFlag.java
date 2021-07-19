package com.bromleyoil.smaugdb.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum WearFlag implements Labelable {
	TAKE("A"), FINGER("B"), NECK("C"), BODY("D"), HEAD("E"), LEGS("F"), FEET("G"), HANDS("H"), ARMS("I"), SHIELD("J"),
	ABOUT("K"), WAIST("L"), WRIST("M"), WIELD("N"), HOLD("O"), DUAL_WIELD, EARS, EYES, MISSILE_WIELD, BACK, FACE, ANKLE,
	FLOAT("Q");

	private static final Logger LOG = LoggerFactory.getLogger(ExtraFlag.class);

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

	public static List<WearFlag> convertCodes(String charVector) {
		List<WearFlag> flags = new ArrayList<>();
		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			ofCode(code).ifPresentOrElse(flags::add, () -> {
				if (!"0".equals(code)) LOG.warn("Unrecognized WearFlag: {}", code);
			});
		}
		return flags;
	}
}
