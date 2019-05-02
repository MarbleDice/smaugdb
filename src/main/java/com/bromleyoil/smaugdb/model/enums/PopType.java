package com.bromleyoil.smaugdb.model.enums;

public enum PopType implements Labelable {
	/** Found in a room */
	FOUND,
	/** Contained in another item */
	CONTAINED,
	/** Sold by a mob */
	SOLD,
	/** Worn by a mob */
	WORN,
	/** Held by a mob */
	HELD,
	/** Produced by a mob */
	PRODUCED_MOB,
	/** Produced by a room */
	PRODUCED_ROOM,
	/** Produced by an item */
	PRODUCED_ITEM;
}
