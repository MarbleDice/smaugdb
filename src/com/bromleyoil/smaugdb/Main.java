package com.bromleyoil.smaugdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Pop;
import com.bromleyoil.smaugdb.model.World;
import com.bromleyoil.smaugdb.parser.SmaugParser;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		SmaugParser parser = new SmaugParser();
		World world = parser.parseWorld("C:\\Users\\Billy\\Downloads\\smaug1.8\\");
		// World world = parser.parseWorld("/Users/moorwi/Downloads/smaug1.8b/");

		for (Item item : world.getItems().values()) {
			log.info("{}", item);
			for (Pop pop : item.getPops()) {
				log.info("    {}", pop);
			}
		}
	}
}
