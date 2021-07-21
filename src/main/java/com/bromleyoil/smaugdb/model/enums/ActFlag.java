package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

public enum ActFlag implements Labelable {
	IS_NPC("A"), SENTINEL("B"), SCAVENGER("C"), NOLOCATE, AGGRESSIVE("F"), STAY_AREA("G"), WIMPY("H"), PET("I"),
	TRAIN("J"), PRACTICE("K"), IMMORTAL, DEADLY, POLYSELF, META_AGGR, GUARDIAN, RUNNING, NOWANDER, MOUNTABLE, MOUNTED,
	SCHOLAR, SECRETIVE, HARDHAT, MOBINVIS, NOASSIST, AUTONOMOUS, PACIFIST, NOATTACK, ANNOYING, STATSHIELD, PROTOTYPE,
	NOSUMMON, NOSTEAL, INFEST, BLOCKING, IS_CLONE, IS_DREAMFORM, IS_SPIRITFORM, IS_PROJECTION, STOP_SCRIPT, UNDEAD("O"),
	CLERIC("Q"), MAGE("R"), THIEF("S"), WARRIOR("T"), NOALIGN("U"), NOPURGE("V"), OUTDOORS("W"), INDOORS("Y"),
	HEALER("a"), GAIN("b"), UPDATE("c"), CHANGER("d");

	private String code = "";

	private ActFlag() {
	}

	private ActFlag(String code) {
		this.code = code;
	}

	public static Optional<ActFlag> ofCode(String code) {
		for (ActFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
