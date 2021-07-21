package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

public enum ApplyType implements Labelable {
	NONE, STR(1), DEX(2), INT(3), WIS(4), CON(5), SEX(6), CLASS(7), LEVEL(8), AGE(9), HEIGHT(10), WEIGHT(11), MANA(12),
	HP(13), MOVE(14), GOLD(15), EXP(16), AC(17), HITROLL(18), DAMROLL(19), SAVING_POISON, SAVING_ROD(21), SAVING_PARA,
	SAVING_BREATH(23), SAVING_SPELL(24), CHA(7), AFFECT(25), RESISTANT, IMMUNE, SUSCEPTIBLE, WEAPONSPELL, LCK, BACKSTAB,
	PICK, TRACK, STEAL, SNEAK, HIDE, PALM, DETRAP, DODGE, PEEK, SCAN, GOUGE, SEARCH, MOUNT, DISARM, KICK, PARRY, BASH,
	STUN, PUNCH, CLIMB, GRIP, SCRIBE, BREW, WEARSPELL, REMOVESPELL, EMOTION, MENTALSTATE, STRIPSN, REMOVE, DIG, FULL,
	THIRST, DRUNK, BLOOD, COOK, RECURRINGSPELL, CONTAGIOUS, EXT_AFFECT, ODOR, ROOMFLAG, SECTORTYPE, ROOMLIGHT, TELEVNUM,
	TELEDELAY, SAVES(20), SAVING_PETRI(22);

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
}
