package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.ApplyType;

@Component
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
