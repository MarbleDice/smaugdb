package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.bromleyoil.smaugdb.model.enums.WeaponType;

public class WeaponTypeFormatter implements Formatter<WeaponType> {

	@Override
	public String print(WeaponType object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public WeaponType parse(String text, Locale locale) throws ParseException {
		return WeaponType.valueOf(text.toUpperCase());
	}
}
