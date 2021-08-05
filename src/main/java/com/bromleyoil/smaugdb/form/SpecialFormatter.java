package com.bromleyoil.smaugdb.form;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.Special;
import com.bromleyoil.smaugdb.parser.ParseException;

@Component
public class SpecialFormatter implements Formatter<Special> {

	@Override
	public String print(Special object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public Special parse(String text, Locale locale) throws ParseException {
		return Special.valueOf(text.toUpperCase());
	}
}
