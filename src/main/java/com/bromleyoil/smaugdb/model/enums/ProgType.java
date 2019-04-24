package com.bromleyoil.smaugdb.model.enums;

public enum ProgType {
	ACT, SPEECH, RAND, FIGHT, DEATH, HITPRCNT, ENTRY, GREET, ALL_GREET, GIVE, BRIBE, HOUR, TIME, WEAR, REMOVE, SAC,
	LOOK, EXA, ZAP, GET, DROP, DAMAGE, REPAIR, RANDIW, SPEECHIW, PULL, PUSH, SLEEP, REST, LEAVE, SCRIPT, USE, LOAD,
	LOGIN, VOID, TELL, IMMINFO, GREET_IN_FIGHT, MOVE, CMD, SELL, EMOTE;

	public static ProgType of(String value) {
		return valueOf(ProgType.class, value.toUpperCase().replace("_PROG", ""));
	}

	public String getLabel() {
		return String.format("on %s", name().toLowerCase());
	}
}
