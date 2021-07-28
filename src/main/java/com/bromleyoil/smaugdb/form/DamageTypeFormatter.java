package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.DamageType;

@Component
public class DamageTypeFormatter implements Formatter<DamageType> {

	@Override
	public String print(DamageType object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public DamageType parse(String text, Locale locale) throws ParseException {
		return DamageType.valueOf(text.toUpperCase());
	}
}
