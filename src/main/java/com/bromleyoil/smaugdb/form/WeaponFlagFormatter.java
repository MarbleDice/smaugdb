package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.WeaponFlag;

@Component
public class WeaponFlagFormatter implements Formatter<WeaponFlag> {

	@Override
	public String print(WeaponFlag object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public WeaponFlag parse(String text, Locale locale) throws ParseException {
		return WeaponFlag.valueOf(text.toUpperCase());
	}
}
