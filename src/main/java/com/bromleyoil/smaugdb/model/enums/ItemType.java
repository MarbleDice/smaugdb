package com.bromleyoil.smaugdb.model.enums;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;

public enum ItemType implements Labelable {
	NONE, LIGHT(1), SCROLL(2), WAND(3), STAFF(4), WEAPON(5), FIREWEAPON, MISSILE, TREASURE(8), ARMOR(9), POTION(10),
	WORN, FURNITURE(12), TRASH(13), OLDTRAP, CONTAINER(15), NOTE, DRINK_CON(17), KEY(18), FOOD(19), MONEY(20), PEN,
	BOAT(22), CORPSE_NPC(23), CORPSE_PC(24), FOUNTAIN(25), PILL(26), BLOOD, BLOODSTAIN, SCRAPS, PIPE, HERB_CON, HERB,
	INCENSE, FIRE, BOOK, SWITCH, LEVER, PULLCHAIN, BUTTON, DIAL, RUNE, RUNEPOUCH, MATCH, TRAP, MAP(28), PORTAL(29),
	PAPER, TINDER, LOCKPICK, SPIKE, DISEASE, OIL, FUEL, PUDDLE, ABACUS, MISSILE_WEAPON, PROJECTILE, QUIVER, SHOVEL,
	SALVE, COOK, KEYRING, ODOR, CHANCE, PIECE, HOUSEKEY, JOURNAL, JEWELRY(33), GEM(32), DRINK, JUKEBOX(34),
	CLOTHING(11), WARP_STONE(30), NPC_CORPSE, PC_CORPSE, PROTECT(27), ROOM_KEY(31);

	public static final Collection<ItemType> SPELL_ITEMS = Collections.unmodifiableSet(
			EnumSet.of(POTION, SCROLL, PILL, SALVE, WAND, STAFF));

	public static final Collection<ItemType> MAGICAL_DEVICES = Collections.unmodifiableSet(
			EnumSet.of(WAND, STAFF, SALVE));

	public static final Collection<ItemType> CONTAINERS = Collections.unmodifiableSet(
			EnumSet.of(DRINK_CON, CONTAINER));

	private int code = 0;

	private ItemType() {
	}

	private ItemType(int code) {
		this.code = code;
	}

	public static Optional<ItemType> ofCode(int code) {
		for (ItemType flag : values()) {
			if (code > 0 && code == flag.code) {
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}
}
