package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

public enum AttackFlag implements Labelable {
	BITE, CLAWS, TAIL("M"), STING, PUNCH, KICK("I"), TRIP("N"), BASH("C"), STUN, GOUGE, BACKSTAB("B"), FEED, DRAIN,
	FIREBREATH, FROSTBREATH, ACIDBREATH, LIGHTNBREATH, GASBREATH, POISON, NASTYPOISON, GAZE, BLINDNESS, CAUSESERIOUS,
	EARTHQUAKE, CAUSECRITICAL, CURSE, FLAMESTRIKE, HARM, FIREBALL, COLORSPRAY, WEAKEN, SPIRALBLAST, POUNCE,
	AREA_ATTACK("A"), BERSERK("D"), DISARM("E"), DODGE("F"), FADE("G"), FAST("H"), DIRT_KICK("J"), PARRY("K"),
	RESCUE("L"), CRUSH("O"), ASSIST_ALL("P"), ASSIST_ALIGN("Q"), ASSIST_RACE("R"), ASSIST_PLAYERS("S"),
	ASSIST_GUARD("T"), ASSIST_VNUM("U");

	private String code = "";

	private AttackFlag() {
	}

	private AttackFlag(String code) {
		this.code = code;
	}
	public static Optional<AttackFlag> ofCode(String code) {
		for (AttackFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
