package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.WearFlag;

@Component
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
