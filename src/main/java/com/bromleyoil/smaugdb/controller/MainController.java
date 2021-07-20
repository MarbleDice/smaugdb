package com.bromleyoil.smaugdb.controller;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.smaugdb.form.ItemSearchForm;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.World;
import com.bromleyoil.smaugdb.model.enums.ApplyType;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.WeaponType;
import com.bromleyoil.smaugdb.model.enums.WearFlag;

@Controller
public class MainController {

	@Autowired
	private World world;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/area-list")
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
		mav.addObject("itemTypes", world.getItemTypes());
		mav.addObject("weaponTypes", world.getWeaponTypes());
		mav.addObject("wearFlags", world.getWearFlags());
		mav.addObject("applyTypes", world.getApplyTypes());
		return mav;
	}

	@GetMapping("/item-search")
	public ModelAndView itemSearchGet(ItemSearchForm form) {
		return itemSearchMav();
	}

	@PostMapping("/item-search")
	public ModelAndView itemSearchPost(ItemSearchForm form) {
		ModelAndView mav = itemSearchMav();
		Stream<Item> stream = world.getItems().stream();
		if (form.getMinLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getMax() >= form.getMinLevel());
		}
		if (form.getMaxLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getMin() <= form.getMaxLevel());
		}
		if (form.getItemType() != null) {
			if (form.getNotItemType()) {
				stream = stream.filter(x -> x.getType() != form.getItemType());
			} else {
				stream = stream.filter(x -> x.getType() == form.getItemType());
			}
		}
		if (form.getWeaponType() != null) {
			stream = stream.filter(x -> x.getWeaponType() == form.getWeaponType());
		}
		if (form.getWearFlag() != null) {
			stream = stream.filter(x -> x.hasWearFlag(form.getWearFlag()));
		}
		if (form.getApplyType() != null) {
			if (form.getApplyValue() != null) {
				stream = stream.filter(x -> x.hasApply(form.getApplyType(), form.getApplyValue()));
			} else {
				stream = stream.filter(x -> x.hasApply(form.getApplyType()));
			}
		}
		
		stream = stream.filter(Item::getExists)
				.sorted(Comparator.comparingDouble(x -> x.getLevel().getAverage()));
		mav.addObject("items", stream.collect(Collectors.toList()));
		return mav;
	}

	@RequestMapping("/item/{vnum}")
	public ModelAndView item(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("item");
		mav.addObject("item", world.getItem(vnum));
		return mav;
	}

	@RequestMapping("/mob-search")
	public ModelAndView mobSearch() {
		ModelAndView mav = new ModelAndView("mob-search");

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
}
