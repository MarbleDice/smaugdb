package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

public enum RoomFlag implements Labelable {
	DARK("A"), DEATH, NO_MOB("C"), INDOORS("D"), HOUSE, NEUTRAL, CHAOTIC, NO_MAGIC, NOLOCATE, PRIVATE("J"), SAFE("K"),
	SOLITARY("L"), PET_SHOP("M"), NO_RECALL("N"), DONATION, NODROPALL, SILENCE, LOGSPEECH, NODROP, CLANSTOREROOM,
	NO_SUMMON, NO_ASTRAL, TELEPORT, TELESHOWDESC, NOFLOOR, NOSUPPLICATE, ARENA, NOMISSILE, AUCTION, NOHOVER, PROTOTYPE,
	DND, TRACK, LIGHT, NOLOG, COLOR, NOWHERE("T"), NOYELL, NOQUIT, NOTRACK, NS_CORPSE, NS_RECALL, IMP_ONLY("O"),
	GODS_ONLY("P"), HEROES_ONLY("Q"), NEWBIES_ONLY("R"), LAW("S");

	private String code = "";

	private RoomFlag() {
	}

	private RoomFlag(String code) {
		this.code = code;
	}

	public static Optional<RoomFlag> ofCode(String code) {
		for (RoomFlag flag : values()) {
			if (code.equals(flag.code)) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
