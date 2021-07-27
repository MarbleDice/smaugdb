package com.bromleyoil.smaugdb.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.smaugdb.form.ItemSearchForm;
import com.bromleyoil.smaugdb.form.MobSearchForm;
import com.bromleyoil.smaugdb.model.World;

@Controller
public class MainController {

	@Autowired
	private World world;

	@RequestMapping(path = {"/", "/area-list"})
	public ModelAndView areaList() {
		ModelAndView mav = new ModelAndView("area-list");
		mav.addObject("areas", world.getAreas());
		return mav;
	}

	@RequestMapping("/area/{urlSafeName}")
	public ModelAndView area(@PathVariable String urlSafeName) {
		ModelAndView mav = new ModelAndView("area");
		mav.addObject("area", world.getArea(urlSafeName));
		return mav;
	}

	protected ModelAndView itemSearchMav() {
		ModelAndView mav = new ModelAndView("item-search");
		mav.addObject("areas", world.getAreas());
		mav.addObject("itemTypes", world.getItemTypes());
		mav.addObject("extraFlags", world.getItems().stream()
				.flatMap(x -> x.getExtraFlags().stream())
				.distinct()
				.collect(Collectors.toList()));
		mav.addObject("weaponTypes", world.getWeaponTypes());
		mav.addObject("weaponFlags", world.getItems().stream()
				.flatMap(x -> x.getWeaponFlags().stream())
				.distinct()
				.collect(Collectors.toList()));
		mav.addObject("wearFlags", world.getWearFlags());
		mav.addObject("applyTypes", world.getApplyTypes());
		mav.addObject("formats", ItemSearchForm.Format.values());
		return mav;
	}

	@GetMapping("/item-search")
	public ModelAndView itemSearchGet(ItemSearchForm form) {
		return itemSearchMav();
	}

	@PostMapping("/item-search")
	public ModelAndView itemSearchPost(ItemSearchForm form) {
		ModelAndView mav = itemSearchMav();
		mav.addObject("items", form.apply(world.getItems()));
		return mav;
	}

	@RequestMapping("/item/{vnum}")
	public ModelAndView item(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("item");
		mav.addObject("item", world.getItem(vnum));
		return mav;
	}

	protected ModelAndView getMobSearchMav() {
		ModelAndView mav = new ModelAndView("mob-search");
		mav.addObject("areas", world.getAreas());
		mav.addObject("actFlags", world.getActFlags());
		mav.addObject("itemTypes", world.getMobs().stream()
				.flatMap(x -> x.getPurchasedTypes().stream())
				.distinct()
				.collect(Collectors.toList()));
		mav.addObject("formats", MobSearchForm.Format.values());
		return mav;
	}
	@GetMapping("/mob-search")
	public ModelAndView mobSearchGet(MobSearchForm form) {
		return getMobSearchMav();
	}

	@PostMapping("/mob-search")
	public ModelAndView mobSearchPost(MobSearchForm form) {
		ModelAndView mav = getMobSearchMav();
		mav.addObject("mobs", form.apply(world.getMobs()));
		return mav;
	}

	@RequestMapping("/mob/{vnum}")
	public ModelAndView mob(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("mob");
		mav.addObject("mob", world.getMob(vnum));
		return mav;
	}

	@RequestMapping("/limbo")
	public ModelAndView limbo() {
		ModelAndView mav = new ModelAndView("limbo");
		mav.addObject("world", world);
		return mav;
	}

	@RequestMapping("/room/{vnum}")
	public ModelAndView room(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("room");
		mav.addObject("room", world.getRoom(vnum));
		return mav;
	}
}
