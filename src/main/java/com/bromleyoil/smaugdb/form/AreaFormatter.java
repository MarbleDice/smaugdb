package com.bromleyoil.smaugdb.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.World;

@Component
public class AreaFormatter implements Formatter<Area>  {
	@Autowired
	private World world;

	@Override
	public String print(Area area, Locale locale) {
		return area.getUrlSafeName();
	}

	@Override
	public Area parse(String urlSafeName, Locale locale) throws ParseException {
		return world.getArea(urlSafeName);
	}
}
