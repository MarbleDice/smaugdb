package com.bromleyoil.smaugdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bromleyoil.smaugdb.form.ApplyTypeFormatter;
import com.bromleyoil.smaugdb.form.ItemTypeFormatter;
import com.bromleyoil.smaugdb.form.WeaponTypeFormatter;
import com.bromleyoil.smaugdb.form.WearFlagFormatter;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	public ItemTypeFormatter itemTypeFormatter() {
		return new ItemTypeFormatter();
	}

	@Bean
	public WeaponTypeFormatter weaponTypeFormatter() {
		return new WeaponTypeFormatter();
	}

	@Bean
	public WearFlagFormatter wearFlagFormatter() {
		return new WearFlagFormatter();
	}

	@Bean
	public ApplyTypeFormatter applyTypeFormatter() {
		return new ApplyTypeFormatter();
	}

}
