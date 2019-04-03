package com.bromleyoil.smaugdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableAutoConfiguration
@ComponentScan
@Controller
public class MainController {

	public static void main(String[] args) {
		SpringApplication.run(MainController.class, args);
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/item/{vnum}")
	public ModelAndView skill(@PathVariable Integer vnum) {
		ModelAndView mav = new ModelAndView("item");

		return mav;
	}
}
