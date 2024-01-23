package com.bromleyoil.smaugdb;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Exit;
import com.bromleyoil.smaugdb.model.World;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@SpringBootApplication
public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static final StringBuilder rawOutput = new StringBuilder();

	@Value("${application.title:SMAUG MUD DB}")
	private String applicationTitle;

	@Autowired
	private World world;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	private static void print(String format, Object... args) {
		rawOutput.append(String.format(format, args));
	}

	private static void println(String format, Object... args) {
		rawOutput.append(String.format(format, args)).append(System.lineSeparator());
	}

	private static void dumpExits(String indent, Stream<Exit> stream) {
		AtomicInteger i = new AtomicInteger();
		stream.forEach(x -> print("%d->%d;%s", x.getRoomFrom().getVnum(), x.getRoomTo().getVnum(),
				i.incrementAndGet() % 10 == 0 ? System.lineSeparator() + indent : ""));
	}

	public void dumpWorldGraphViz() {
		println("digraph ROM {");
		// Dump every area
		for (Area area : world.getAreas()) {
			println("\tsubgraph %s {", area.getUrlSafeName().replace("-", "_"));
			print("\t\t");
			dumpExits("\t\t", area.getRooms().stream().flatMap(x -> x.getExits().stream())
					.filter(x -> x.getRoomFrom().getArea().equals(x.getRoomTo().getArea())));
			println("%n\t}");
		}
		// Dump all cross-area exits
		print("\t");
		dumpExits("\t", world.getAreas().stream()
				.flatMap(x -> x.getRooms().stream())
				.flatMap(x -> x.getExits().stream())
				.filter(x -> !x.getRoomFrom().getArea().equals(x.getRoomTo().getArea())));
		println("%n}");
	}

	public void dumpMazeGraphViz() {
		println("digraph ROM {");
		// Dump every area
		for (Area area : world.getAreas()) {
			println("\tsubgraph %s {", area.getUrlSafeName().replace("-", "_"));
			print("\t\t");
			dumpExits("\t\t", area.getRooms().stream().flatMap(x -> x.getExits().stream())
					.filter(x -> x.getRoomFrom().getArea().equals(x.getRoomTo().getArea()))
					.filter(x -> x.getRoomFrom().isMaze() || x.getRoomTo().isMaze()));
			println("%n\t}");
		}
		// Dump all cross-area exits
		print("\t");
		dumpExits("\t", world.getAreas().stream()
				.flatMap(x -> x.getRooms().stream())
				.flatMap(x -> x.getExits().stream())
				.filter(x -> !x.getRoomFrom().getArea().equals(x.getRoomTo().getArea()))
				.filter(x -> x.getRoomFrom().isMaze() || x.getRoomTo().isMaze()));
		println("%n}");
	}

	@Bean
	ApplicationRunner appRunner() {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) throws Exception {
				// Execute start-up tasks


				// Log any raw output produced
				if (rawOutput.length() > 0) {
					log.info("App runner produced raw output:\n{}", rawOutput);
				} else {
					log.info("App runner produced no raw output");
				}
			}
		};
	}

	@Bean
	String applicationTitle() {
		return applicationTitle;
	}

	@Bean
	LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}
}
