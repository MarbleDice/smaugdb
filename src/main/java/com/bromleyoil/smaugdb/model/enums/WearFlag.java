package com.bromleyoil.smaugdb.model.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum WearFlag {
	TAKE, FINGER, NECK, BODY, HEAD, LEGS, FEET, HANDS, ARMS, SHIELD, ABOUT, WAIST, WRIST, WIELD, HOLD, DUAL_WIELD, EARS,
	EYES, MISSILE_WIELD, BACK, FACE, ANKLE;

	public static final List<WearFlag> EQUIP_FLAGS = Collections.unmodifiableList(
			Arrays.asList(values()).subList(1, values().length));
}
