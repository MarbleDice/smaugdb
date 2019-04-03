package com.bromleyoil.smaugdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.Pop;
import com.bromleyoil.smaugdb.model.World;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private World world;

	@RequestMapping("/")
	public String index() {
		logMobs();
		return "index";
	}

	@RequestMapping("/item/{vnum}")
	public ModelAndView skill(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("item");
		mav.addObject("item", world.getItem(vnum));
		return mav;
	}

	public void logItems() {
		for (Item item : world.getItems().values()) {
			log.info("{}", item);
			for (Pop pop : item.getPops()) {
				log.info("    {}", pop);
			}
		}
	}

	public void logMobs() {
		for (Mob mob : world.getMobs().values()) {
			log.info("{}", mob);
			for (Pop pop : mob.getContainedPops()) {
				log.info("    {}", pop);
			}
		}
	}
}
