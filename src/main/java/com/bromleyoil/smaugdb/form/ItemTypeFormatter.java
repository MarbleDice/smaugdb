package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.ItemType;

@Component
public class ItemTypeFormatter implements Formatter<ItemType> {

	@Override
	public String print(ItemType object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public ItemType parse(String text, Locale locale) throws ParseException {
		return ItemType.valueOf(text.toUpperCase());
	}
}
