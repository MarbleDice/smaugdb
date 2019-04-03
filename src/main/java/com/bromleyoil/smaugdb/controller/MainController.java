package com.bromleyoil.smaugdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.smaugdb.model.World;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private World world;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/area-list")
	public ModelAndView areaList() {
		ModelAndView mav = new ModelAndView("area-list");
		mav.addObject("areas", world.getAreas().values());
		return mav;
	}

	@RequestMapping("/area/{urlSafeName}")
	public ModelAndView area(@PathVariable String urlSafeName) {
		ModelAndView mav = new ModelAndView("area");
		mav.addObject("area", world.getArea(urlSafeName));
		return mav;
	}

	@RequestMapping("/item/{vnum}")
	public ModelAndView item(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("item");
		mav.addObject("item", world.getItem(vnum));
		return mav;
	}

	@RequestMapping("/mob/{vnum}")
	public ModelAndView mob(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("mob");
		mav.addObject("mob", world.getMob(vnum));
		return mav;
	}
}
