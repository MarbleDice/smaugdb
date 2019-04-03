package com.bromleyoil.smaugdb;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bromleyoil.smaugdb.model.World;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
public class Main implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!args.containsOption("mud.path")) {
			throw new IllegalArgumentException("You must specify --mud.path=<PATH TO MUD> as an argument!");
		}
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	public World world() {
		return new World();
	}
}
