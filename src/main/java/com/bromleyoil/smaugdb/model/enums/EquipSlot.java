package com.bromleyoil.smaugdb.model.enums;

import static com.bromleyoil.smaugdb.model.enums.WearFlag.*;

public enum EquipSlot implements Labelable {
	LIGHT(TAKE), FINGER_L(FINGER), FINGER_R(FINGER), NECK_1(NECK), NECK_2(NECK), BODY(WearFlag.BODY),
	HEAD(WearFlag.HEAD), LEGS(WearFlag.LEGS), FEET(WearFlag.FEET), HANDS(WearFlag.HANDS), ARMS(WearFlag.ARMS),
	SHIELD(WearFlag.SHIELD), ABOUT(WearFlag.ABOUT), WAIST(WearFlag.WAIST), WRIST_L(WRIST), WRIST_R(WRIST),
	WIELD(WearFlag.WIELD), HOLD(WearFlag.HOLD), DUAL_WIELD(WearFlag.DUAL_WIELD), EARS(WearFlag.EARS),
	EYES(WearFlag.EYES), MISSILE_WIELD(WearFlag.MISSILE_WIELD), BACK(WearFlag.BACK), FACE(WearFlag.FACE),
	ANKLE_L(ANKLE), ANKLE_R(ANKLE);

	private WearFlag wearFlag;

	private EquipSlot(WearFlag wearFlag) {
		this.wearFlag = wearFlag;
	}

	public WearFlag getWearFlag() {
		return wearFlag;
	}
}
