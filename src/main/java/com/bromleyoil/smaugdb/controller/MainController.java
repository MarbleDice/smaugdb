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
import com.bromleyoil.smaugdb.form.MobSearchForm;
import com.bromleyoil.smaugdb.model.Item;
import com.bromleyoil.smaugdb.model.Mob;
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
		if (form.getName() != null) {
			stream = stream.filter(x -> x.getName().contains(form.getName()));
		}
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

	protected ModelAndView getMovSearchMav() {
		ModelAndView mav = new ModelAndView("mob-search");
		return mav;
	}
	@GetMapping("/mob-search")
	public ModelAndView mobSearchGet(MobSearchForm form) {
		return getMovSearchMav();
	}

	@PostMapping("/mob-search")
	public ModelAndView mobSearchPost(MobSearchForm form) {
		ModelAndView mav = getMovSearchMav();
		Stream<Mob> stream = world.getMobs().stream();
		if (form.getName() != null) {
			stream = stream.filter(x -> x.getName().contains(form.getName()));
		}
		if (form.getMinLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getAverage() >= form.getMinLevel());
		}
		if (form.getMaxLevel() != null) {
			stream = stream.filter(x -> x.getLevel().getAverage() <= form.getMaxLevel());
		}
		if (form.getAlignment() != null) {
			stream = stream.filter(x -> x.getAlignment() <= form.getAlignment());
		}
		if (form.getSpawnCount() != null) {
			stream = stream.filter(x -> x.getSpawnCount() >= form.getSpawnCount());
		}
		stream = stream.filter(Mob::getExists)
				.sorted(Comparator.comparingDouble(x -> x.getLevel().getAverage()));
		mav.addObject("mobs", stream.collect(Collectors.toList()));
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
