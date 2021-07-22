package com.bromleyoil.smaugdb;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bromleyoil.smaugdb.form.ApplyTypeFormatter;
import com.bromleyoil.smaugdb.form.ItemTypeFormatter;
import com.bromleyoil.smaugdb.form.WeaponTypeFormatter;
import com.bromleyoil.smaugdb.form.WearFlagFormatter;
import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Exit;
import com.bromleyoil.smaugdb.model.World;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
public class Main {

	@Value("${application.title:SMAUG MUD DB}")
	private String applicationTitle;

	@Autowired
	private World world;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	private static void print(String format, Object... args) {
		System.out.print(String.format(format, args));
	}

	private static void println(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	private static void dumpExits(String indent, Stream<Exit> stream) {
		AtomicInteger i = new AtomicInteger();
		stream.forEach(x -> print("%d->%d;%s", x.getFrom().getVnum(), x.getTo().getVnum(),
				i.incrementAndGet() % 10 == 0 ? System.lineSeparator() + indent: ""));
	}

	public ApplicationRunner graphvizDump() {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) throws Exception {
				println("digraph ROM {");
				// Dump every area
				for (Area area : world.getAreas()) {
					println("\tsubgraph %s {", area.getUrlSafeName().replace("-", "_"));
					print("\t\t");
					dumpExits("\t\t", area.getRooms().stream().flatMap(x -> x.getExits().stream())
							.filter(x -> x.getFrom().getArea().equals(x.getTo().getArea())));
					println("%n\t}");
				}
				// Dump all cross-area exits
				print("\t");
				dumpExits("\t", world.getAreas().stream()
						.flatMap(x -> x.getRooms().stream())
						.flatMap(x -> x.getExits().stream())
						.filter(x -> !x.getFrom().getArea().equals(x.getTo().getArea())));
				println("%n}");
			}
		};
	}

	@Bean
	public String applicationTitle() {
		return applicationTitle;
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
