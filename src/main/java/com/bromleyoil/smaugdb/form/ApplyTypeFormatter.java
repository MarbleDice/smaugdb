package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.bromleyoil.smaugdb.model.enums.ApplyType;

public class ApplyTypeFormatter implements Formatter<ApplyType> {

	@Override
	public String print(ApplyType object, Locale locale) {
		return object.name().toLowerCase();
	}

	@Override
	public ApplyType parse(String text, Locale locale) throws ParseException {
		return ApplyType.valueOf(text.toUpperCase());
	}
}
