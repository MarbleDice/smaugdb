package com.bromleyoil.smaugdb.model.enums;

import static com.bromleyoil.smaugdb.model.enums.WeaponSkill.*;
import static com.bromleyoil.smaugdb.model.enums.DamageType.*;

public enum DamageVerb implements Labelable {
	HIT(BASH, PUGILISM), SLICE(DamageType.SLASH, LONG_BLADES), STAB(DamageType.PIERCE, SHORT_BLADES),
	SLASH(DamageType.SLASH, LONG_BLADES), WHIP(DamageType.SLASH, FLEXIBLE_ARMS), CLAW(DamageType.SLASH, TALONOUS_ARMS),
	BLAST(BASH, PUGILISM), POUND(BASH, BLUDGEONS), CRUSH(BASH, BLUDGEONS), GREP(DamageType.SLASH, WeaponSkill.NONE),
	BITE(DamageType.PIERCE, PUGILISM), PIERCE(DamageType.PIERCE, SHORT_BLADES), SUCTION(BASH, PUGILISM),
	BOLT(MISSILE_WEAPONS), ARROW(MISSILE_WEAPONS), DART(MISSILE_WEAPONS), STONE(MISSILE_WEAPONS), PEA(MISSILE_WEAPONS),
	NONE(BASH, WeaponSkill.NONE), BEATING(BASH), DIGESTION(DamageType.MAGIC), CHARGE(BASH), SLAP(BASH), PUNCH(BASH),
	WRATH(DamageType.MAGIC), MAGIC(DamageType.MAGIC), DIVINE(DamageType.MAGIC), CLEAVE(DamageType.SLASH),
	SCRATCH(DamageType.PIERCE), PECK(DamageType.PIERCE), PECKB(BASH), CHOP(DamageType.SLASH), STING(DamageType.PIERCE),
	SMASH(BASH), SHBITE(DamageType.MAGIC), FLBITE(DamageType.MAGIC), FRBITE(DamageType.MAGIC), ACBITE(DamageType.MAGIC),
	CHOMP(DamageType.PIERCE), DRAIN(DamageType.MAGIC), THRUST(DamageType.PIERCE), SLIME(DamageType.MAGIC),
	SHOCK(DamageType.MAGIC), THWACK(BASH), FLAME(DamageType.MAGIC), CHILL(DamageType.MAGIC);

	private DamageType damageType;
	private WeaponSkill weaponSkill;

	private DamageVerb() {
		this.damageType = DamageType.BASH;
		this.weaponSkill = WeaponSkill.NONE;
	}

	private DamageVerb(WeaponSkill weaponSkill) {
		this.damageType = DamageType.BASH;
		this.weaponSkill = weaponSkill;
	}

	private DamageVerb(DamageType damageType) {
		this.damageType = damageType;
		this.weaponSkill = WeaponSkill.NONE;
	}

	private DamageVerb(DamageType damageType, WeaponSkill weaponSkill) {
		this.damageType = damageType;
		this.weaponSkill = weaponSkill;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public WeaponSkill getWeaponSkill() {
		return weaponSkill;
	}
}
