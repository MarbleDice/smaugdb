package com.bromleyoil.smaugdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.parser.SmaugParser;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		log.info("Going!");
		SmaugParser parser = new SmaugParser();
		parser.parseWorld("C:\\Users\\Billy\\Downloads\\smaug1.8\\");
	}
}
