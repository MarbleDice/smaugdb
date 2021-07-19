package com.bromleyoil.smaugdb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UtilsTest {

	@Test
	public void interpolate_yEqualsX_accurate() {
		assertThat(Utils.interpolate(10, 0, 0, 1, 1)).as("f(10); f(x) = x").isEqualTo(10);
	}

	@Test
	public void interpolate_yEquals2X_accurate() {
		assertThat(Utils.interpolate(10, 0, 0, 1, 2)).as("f(10); f(x) = 2x").isEqualTo(20);
	}

	@Test
	public void interpolate_yEquals10Minus2X_accurate() {
		assertThat(Utils.interpolate(10, 0, 10, 1, 8)).as("f(10); f(x) = 10 - 2x").isEqualTo(-10);
	}

	@Test
	public void interpolate_first50Levels_matchesLegacy() {

		for (int i = 0; i <= 50; i++) {
			assertThat(Utils.interpolate(i, 0, 18, 32, 6)).as("Fighter Lv " + i)
					.isEqualTo(Utils.interpolate(i, 18, 6));
		}
	}
}