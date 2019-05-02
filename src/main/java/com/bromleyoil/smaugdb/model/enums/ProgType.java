package com.bromleyoil.smaugdb.model.enums;

import java.util.Optional;

import org.thymeleaf.util.StringUtils;

public enum ProgType {
	ACT("when acted upon"), SPEECH("when spoken to"), RAND("when nearby"), FIGHT("during combat"), DEATH("when slain"),
	HITPRCNT("when injured"), ENTRY("upon entry"), GREET("when encountered"), ALL_GREET("when encountered"),
	GIVE("when given an item"), BRIBE("when paid"), HOUR("at a certain time"), TIME("at a certain time"),
	WEAR("when worn"), REMOVE("when removed"), SAC("when sacrificed"), LOOK("when looked at"), EXA("when examined"),
	ZAP("when zapped"), GET("when picked up"), DROP("when dropped"), DAMAGE("when damaged"), REPAIR("when repaired"),
	RANDIW("when nearby"), SPEECHIW("when spoken to"), PULL("when pulled"), PUSH("when pushed"),
	SLEEP("upon sleeping"), REST("upon resting"), LEAVE("upon leaving"), SCRIPT("constantly"), USE("when used"),
	LOAD, LOGIN, VOID, TELL("when spoken to directly"), IMMINFO, GREET_IN_FIGHT, MOVE, CMD, SELL("when sold"),
	EMOTE("when acted upon");

	private String triggerLabel;

	private ProgType() {

	}

	private ProgType(String triggerLabel) {
		this.triggerLabel = triggerLabel;
	}

	public static ProgType of(String value) {
		return valueOf(ProgType.class, value.toUpperCase().replace("_PROG", ""));
	}

	public String getLabel() {
		return String.format("on %s", name().toLowerCase());
	}

	public String getTriggerLabel() {
		return StringUtils.capitalize(Optional.ofNullable(triggerLabel).orElse("upon " + name().toLowerCase()));
	}
}
