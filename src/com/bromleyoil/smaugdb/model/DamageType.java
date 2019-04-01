package com.bromleyoil.smaugdb.model;

import static com.bromleyoil.smaugdb.model.WeaponSkill.*;

public enum DamageType {
	HIT(PUGILISM), SLICE(LONG_BLADES), STAB(SHORT_BLADES), SLASH(LONG_BLADES), WHIP(FLEXIBLE_ARMS), CLAW(TALONOUS_ARMS),
	BLAST(PUGILISM), POUND(BLUDGEONS), CRUSH(BLUDGEONS), GREP(WeaponSkill.NONE), BITE(PUGILISM), PIERCE(SHORT_BLADES),
	SUCTION(PUGILISM), BOLT(MISSILE_WEAPONS), ARROW(MISSILE_WEAPONS), DART(MISSILE_WEAPONS), STONE(MISSILE_WEAPONS),
	PEA(MISSILE_WEAPONS), NONE(WeaponSkill.NONE);

	private WeaponSkill weaponSkill;

	private DamageType(WeaponSkill weaponSkill) {
		this.weaponSkill = weaponSkill;
	}

	public WeaponSkill getWeaponSkill() {
		return weaponSkill;
	}
}
