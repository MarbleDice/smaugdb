package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.bromleyoil.smaugdb.model.enums.WearFlag;

public class WearFlagFormatter implements Formatter<WearFlag> {

	@Override
	public String print(WearFlag object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public WearFlag parse(String text, Locale locale) throws ParseException {
		return WearFlag.valueOf(text.toUpperCase());
	}
}
