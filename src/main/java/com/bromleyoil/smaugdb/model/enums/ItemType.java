package com.bromleyoil.smaugdb.model.enums;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

public enum ItemType {
	NONE, LIGHT, SCROLL, WAND, STAFF, WEAPON, FIREWEAPON, MISSILE, TREASURE, ARMOR, POTION, WORN, FURNITURE, TRASH,
	OLDTRAP, CONTAINER, NOTE, DRINK_CON, KEY, FOOD, MONEY, PEN, BOAT, CORPSE_NPC, CORPSE_PC, FOUNTAIN, PILL, BLOOD,
	BLOODSTAIN, SCRAPS, PIPE, HERB_CON, HERB, INCENSE, FIRE, BOOK, SWITCH, LEVER, PULLCHAIN, BUTTON, DIAL, RUNE,
	RUNEPOUCH, MATCH, TRAP, MAP, PORTAL, PAPER, TINDER, LOCKPICK, SPIKE, DISEASE, OIL, FUEL, PUDDLE, ABACUS,
	MISSILE_WEAPON, PROJECTILE, QUIVER, SHOVEL, SALVE, COOK, KEYRING, ODOR, CHANCE, PIECE, HOUSEKEY, JOURNAL;

	public static final Collection<ItemType> SPELL_ITEMS = Collections.unmodifiableSet(
			EnumSet.of(POTION, SCROLL, PILL, SALVE, WAND, STAFF));

	public static final Collection<ItemType> MAGICAL_DEVICES = Collections.unmodifiableSet(
			EnumSet.of(WAND, STAFF, SALVE));

	public static final Collection<ItemType> CONTAINERS = Collections.unmodifiableSet(
			EnumSet.of(DRINK_CON, CONTAINER));
}
