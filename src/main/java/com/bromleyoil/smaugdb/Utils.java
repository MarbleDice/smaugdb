package com.bromleyoil.smaugdb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;
import com.bromleyoil.smaugdb.model.enums.AttackFlag;
import com.bromleyoil.smaugdb.model.enums.Labelable;
import com.bromleyoil.smaugdb.model.enums.ResistFlag;

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

	public static List<ActFlag> convertActFlagCodes(String charVector) {
		List<ActFlag> flags = new ArrayList<>();

		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			try {
				flags.add(ActFlag.ofCode(code));
			} catch (IllegalArgumentException e) {
				if (!e.getMessage().equals("0")) {
					LOG.warn("Unrecognized ActFlag: {}", e.getMessage());
				}
			}
		}

		return flags;
	}

	public static List<AffectFlag> convertAffectFlagCodes(String charVector) {
		List<AffectFlag> flags = new ArrayList<>();

		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			try {
				flags.add(AffectFlag.ofCode(code));
			} catch (IllegalArgumentException e) {
				if (!e.getMessage().equals("0")) {
					LOG.warn("Unrecognized AffectFlag: {}", e.getMessage());
				}
			}
		}

		return flags;
	}

	public static List<AttackFlag> convertAttackFlagCodes(String charVector) {
		List<AttackFlag> flags = new ArrayList<>();

		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			try {
				flags.add(AttackFlag.ofCode(code));
			} catch (IllegalArgumentException e) {
				if (!e.getMessage().equals("0")) {
					LOG.warn("Unrecognized AttackFlag: {}", e.getMessage());
				}
			}
		}

		return flags;
	}

	public static List<ResistFlag> convertResistFlagCodes(String charVector) {
		List<ResistFlag> flags = new ArrayList<>();

		for (int i = 0; i < charVector.length(); i++) {
			String code = charVector.substring(i, i + 1);
			try {
				flags.add(ResistFlag.ofCode(code));
			} catch (IllegalArgumentException e) {
				if (!e.getMessage().equals("0")) {
					LOG.warn("Unrecognized ResistFlag: {}", e.getMessage());
				}
			}
		}

		return flags;
	}

	public String label(Collection<Labelable> items) {
		return items.stream().map(Labelable::getLabel).collect(Collectors.joining(", "));
	}
}
