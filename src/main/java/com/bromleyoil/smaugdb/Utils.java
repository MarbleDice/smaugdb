package com.bromleyoil.smaugdb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.form.MobSearchForm;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.Labelable;

/**
 * Contains various helper methods for use in thymeleaf templates.
 * 
 */
@Component
public class Utils {

	/** A publicly accessible static log for debugging and being lazy */
	public static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	/**
	 * Partitions a list in multiple smaller lists.
	 * 
	 * @param list
	 * @param partitionSize
	 * @return
	 */
	public <T> List<List<T>> partition(List<T> list, int partitionSize) {
		return ListUtils.partition(list, partitionSize);
	}

	public static int constrain(int min, int max, int value) {
		return Integer.max(min, Integer.min(max, value));
	}

	/**
	 * Given a linear function defined by two points (0, y1) and (32, y2), returns the value f(x). Gets a little fuzzy
	 * due to integer rounding.
	 * 
	 * @param x
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static int interpolate(int x, int y1, int y2) {
		return y1 + x * (y2 - y1) / 32;
	}

	/**
	 * Given two points (x1, y1) and (x2, y2) which define a linear function f(x) = mx + b, returns the value f(x3).
	 * Emulates fuzzy integer rounding.
	 * 
	 * @param x3
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return f(x3)
	 */
	public static int interpolate(int x3, int x1, int y1, int x2, int y2) {
		// m = (y2 - y1) / (x2 - x1)
		BigDecimal m = BigDecimal.valueOf((long) y2 - y1).divide(BigDecimal.valueOf((long) x2 - x1));
		// b = y1 - m * x1
		BigDecimal b = BigDecimal.valueOf(y1).subtract(m.multiply(BigDecimal.valueOf(x1)));
		// y3 = m * x3 + b
		return m.multiply(BigDecimal.valueOf(x3)).intValue() + b.intValue();
	}

	/**
	 * Produces a list of flags from a bit vector, where each bit of the number corresponds to one flag in this list.
	 * 
	 * @param bitVector
	 *     A bit vector of 9 would be 1001 in binary, which results in a list of the first and fourth enums.
	 * @return
	 */
	public static <T extends Enum<?>> List<T> convertBitVector(Class<T> enumType, int bitVector) {
		List<T> flags = new ArrayList<>();

		for (int i = enumType.getEnumConstants().length - 1; i >= 0; i--) {
			double bitFlag = Math.pow(2, i);
			if (bitVector >= bitFlag) {
				bitVector -= bitFlag;
				flags.add(enumType.getEnumConstants()[i]);
			}
		}

		return flags;
	}

	public String label(Collection<Labelable> items) {
		return items.stream().map(Labelable::getLabel).collect(Collectors.joining(", "));
	}

	public static double calcExp(Mob mob, MobSearchForm form) {
		// Base exp based on level diff ranges, from -9 to +4 inclusive
		List<Integer> baseExpTable = Arrays.asList(1, 2, 5, 9, 11, 22, 33, 50, 66, 83, 99, 121, 143, 165);
		int exp = 0;
		int diff = ((int) mob.getLevel().getAverage()) - form.getPlayerLevel();

		// Calculate base experience
		if (diff >= -9 && diff <= 4) {
			exp = baseExpTable.get(diff + 9);
		} else if (diff > 4) {
			exp = 160 + 20 * (diff - 4);
		}

		// Adjust for alignment
		if (form.getPlayerAlignment() != null && !mob.hasActFlag(ActFlag.NOALIGN)) {
			if (form.getPlayerAlignment() > 500) {
				// Very good player
				if (mob.getAlignment() < -750) {
					exp = exp * 4 / 3;
				} else if (mob.getAlignment() < -500) {
					exp = exp * 5 / 4;
				} else if (mob.getAlignment() > 750) {
					exp = exp / 4;
				} else if (mob.getAlignment() > 500) {
					exp = exp / 2;
				} else if (mob.getAlignment() > 250) {
					exp = exp * 3 / 4;
				}
			} else if (form.getPlayerAlignment() > 200) {
				// Good player
				if (mob.getAlignment() < -500) {
					exp = exp * 6 / 5;
				} else if (mob.getAlignment() > 750) {
					exp = exp / 2;
				} else if (mob.getAlignment() > 0) {
					exp = exp * 3 / 4;
				}
			} else if (form.getPlayerAlignment() < -200) {
				// Evil player
				if (mob.getAlignment() < -750) {
					exp = exp / 2;
				} else if (mob.getAlignment() < 0) {
					exp = exp * 3 / 4;
				} else if (mob.getAlignment() > 500) {
					exp = exp * 6 / 5;
				}
			} else if (form.getPlayerAlignment() < -500) {
				// Very evil player
				if (mob.getAlignment() < -750) {
					exp = exp / 2;
				} else if (mob.getAlignment() < -500) {
					exp = exp * 3 / 4;
				} else if (mob.getAlignment() < -250) {
					exp = exp * 9 / 10;
				} else if (mob.getAlignment() > 750) {
					exp = exp * 5 / 4;
				} else if (mob.getAlignment() > 500) {
					exp = exp * 11 / 10;
				}
			} else {
				// Neutral player
				if (mob.getAlignment() < -500 || mob.getAlignment() > 500) {
					exp = exp * 4 / 3;
				} else if (mob.getAlignment() > -200 || mob.getAlignment() < 200) {
					exp = exp / 2;
				}
			}
		}

		// Adjust for level
		if (form.getPlayerLevel() < 6) {
			exp = 10 * exp / (form.getPlayerLevel() + 4);
		} else if (form.getPlayerLevel() > 35) {
			exp = 15 * exp / (form.getPlayerLevel() - 25);
		}

		// Adjust for party size
		if (form.getTotalPartyLevel() != null && form.getTotalPartyLevel() > form.getPlayerLevel()) {
			exp = exp * form.getPlayerLevel() / (form.getTotalPartyLevel() - 1);
		}

		// Calculate the Exp/100HP
		return 100d * exp / mob.getHp().getAverage();
	}
	
	public static Comparator<Mob> getExpComparator(MobSearchForm form) {
		return Comparator.comparingDouble(x -> Utils.calcExp(x, form));
	}
}
