package com.bromleyoil.smaugdb.model.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ApplyType implements Labelable {
	NONE, STR(1), DEX(2), INT(3), WIS(4), CON(5), SEX(6), CLASS, LEVEL, AGE, HEIGHT, WEIGHT, MANA(12), HP(13), MOVE, GOLD, EXP, AC, HITROLL,
	DAMROLL, SAVING_POISON, SAVING_ROD, SAVING_PARA, SAVING_BREATH, SAVING_SPELL, CHA(7), AFFECT, RESISTANT, IMMUNE,
	SUSCEPTIBLE, WEAPONSPELL, LCK, BACKSTAB, PICK, TRACK, STEAL, SNEAK, HIDE, PALM, DETRAP, DODGE, PEEK, SCAN, GOUGE,
	SEARCH, MOUNT, DISARM, KICK, PARRY, BASH, STUN, PUNCH, CLIMB, GRIP, SCRIBE, BREW, WEARSPELL, REMOVESPELL, EMOTION,
	MENTALSTATE, STRIPSN, REMOVE, DIG, FULL, THIRST, DRUNK, BLOOD, COOK, RECURRINGSPELL, CONTAGIOUS, EXT_AFFECT, ODOR,
	ROOMFLAG, SECTORTYPE, ROOMLIGHT, TELEVNUM, TELEDELAY;

	private static final Logger LOG = LoggerFactory.getLogger(ExtraFlag.class);

	private int code = 0;

	private ApplyType() {
	}

	private ApplyType(int code) {
		this.code = code;
	}

	public static Optional<ApplyType> ofCode(int code) {
		for (ApplyType flag : values()) {
			if (code > 0 && code == flag.code) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}

	public static List<ApplyType> convertCodes(String charVector) {
		List<ApplyType> flags = new ArrayList<>();
		for (int i = 0; i < charVector.length(); i++) {
			int code = Integer.parseInt(charVector.substring(i, i + 1));
			ofCode(code).ifPresentOrElse(flags::add, () -> {
				if (code != 0) LOG.warn("Unrecognized ApplyType: {}", code);
			});
		}
		return flags;
	}
}
