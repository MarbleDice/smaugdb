package com.bromleyoil.smaugdb.form;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.DamageType;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.Labelable;
import com.bromleyoil.smaugdb.model.enums.Special;

public class MobSearchForm extends AbstractSearchForm<Mob> {
	private String name;
	private Area area;
	private Integer minLevel;
	private Integer maxLevel;
	private Integer alignment;
	private Integer spawnCount;
	private Integer gold;
	private Double damage;
	private Double threat;
	private DamageType damageType;
	private Special special;
	private ActFlag actFlag;

	private Integer playerLevel;
	private Integer playerAlignment;
	private Integer totalPartyLevel;

	private Boolean isShopkeeper;
	private ItemType buysItem;

	private Format format;

	public enum Format implements Labelable {
		DEFAULT, COINS, EXP_HP, EXP_THREAT;
	}

	@Override
	public Stream<Mob> applyFilters(Stream<Mob> stream) {
		stream = maybeFilter(stream, name != null, x -> x.getName().toLowerCase().contains(name.toLowerCase()));
		stream = maybeFilter(stream, area != null, x -> x.getArea().equals(area));
		stream = maybeFilter(stream, minLevel != null, x -> x.getLevel().getAverage() >= minLevel);
		stream = maybeFilter(stream, maxLevel != null, x -> x.getLevel().getAverage() <= maxLevel);
		stream = maybeFilter(stream, alignment != null, x -> x.getAlignment() <= alignment);
		stream = maybeFilter(stream, spawnCount != null, x -> x.getMaxSpawnCount() >= spawnCount);
		stream = maybeFilter(stream, gold != null, x -> x.getGold().getAverage() >= gold);
		stream = maybeFilter(stream, damage != null, x -> x.getDamagePerRound().getAverage() <= damage);
		stream = maybeFilter(stream, threat != null, x -> x.getThreat() <= threat);
		stream = maybeFilter(stream, damageType != null, x -> x.getDamageType() == damageType);
		stream = maybeFilter(stream, actFlag != null, x -> x.hasActFlag(getActFlag()));
		stream = maybeFilter(stream, isShopkeeper != null, x -> x.isShopkeeper() == isShopkeeper);
		stream = maybeFilter(stream, buysItem != null, x -> x.getPurchasedTypes().contains(buysItem));
		stream = maybeFilter(stream, showExp(), Mob::canKill);
		stream = maybeFilter(stream, showExp(), x -> x.getLevel().getMax() >= playerLevel - 9);
		stream = maybeFilter(stream, special != null, x -> x.getSpecial() == special);

		return stream;
	}

	@Override
	protected Comparator<Mob> getComparator() {
		if (format == Format.COINS) {
			return Comparator.comparing(x ->  x.getGold().getAverage(), Comparator.reverseOrder());
		} else if (format == Format.EXP_HP) {
			return Comparator.comparingDouble(this::getExpPerHp).reversed();
		} else if (format == Format.EXP_THREAT) {
			return Comparator.comparingDouble(this::getExpPerThreat).reversed();
		} else if (showExp()) {
			return Comparator.comparingDouble(this::calcExp).reversed();
		} else {
			return Comparator.comparingDouble(x -> x.getLevel().getAverage());
		}
	}

	public boolean showExp() {
		return playerLevel != null;
	}

	public boolean showShop() {
		return buysItem != null || (isShopkeeper != null && isShopkeeper);
	}

	public boolean showCombat() {
		return !showShop();
	}

	/**
	 * @return The XP awarded for the slaying the given Mob.
	 */
	public double calcExp(Mob mob) {
		// Base exp based on level diff ranges, from -9 to +4 inclusive
		List<Integer> baseExpTable = Arrays.asList(1, 2, 5, 9, 11, 22, 33, 50, 66, 83, 99, 121, 143, 165);
		int exp = 0;
		int diff = ((int) mob.getLevel().getAverage()) - getPlayerLevel();

		// Calculate base experience
		if (diff >= -9 && diff <= 4) {
			exp = baseExpTable.get(diff + 9);
		} else if (diff > 4) {
			exp = 160 + 20 * (diff - 4);
		}

		// Adjust for alignment
		if (getPlayerAlignment() != null && !mob.hasActFlag(ActFlag.NOALIGN)) {
			if (getPlayerAlignment() > 500) {
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
			} else if (getPlayerAlignment() > 200) {
				// Good player
				if (mob.getAlignment() < -500) {
					exp = exp * 6 / 5;
				} else if (mob.getAlignment() > 750) {
					exp = exp / 2;
				} else if (mob.getAlignment() > 0) {
					exp = exp * 3 / 4;
				}
			} else if (getPlayerAlignment() < -200) {
				// Evil player
				if (mob.getAlignment() < -750) {
					exp = exp / 2;
				} else if (mob.getAlignment() < 0) {
					exp = exp * 3 / 4;
				} else if (mob.getAlignment() > 500) {
					exp = exp * 6 / 5;
				}
			} else if (getPlayerAlignment() < -500) {
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
		if (getPlayerLevel() < 6) {
			exp = 10 * exp / (getPlayerLevel() + 4);
		} else if (getPlayerLevel() > 35) {
			exp = 15 * exp / (getPlayerLevel() - 25);
		}

		// Adjust for party size
		if (getTotalPartyLevel() != null && getTotalPartyLevel() > getPlayerLevel()) {
			exp = exp * getPlayerLevel() / (getTotalPartyLevel() - 1);
		}

		return exp;
	}

	/**
	 * @return The expected average HP for a mob of the given level
	 */
	protected static double calcAverageHp(int level) {
		// Values determined through manual regression analysis
		if (level <= 15) {
			return -8.8178 + 14.6767 * level;
		} else {
			return 61.1928 * Math.pow(Math.E, 0.0875 * level);
		}
	}

	/**
	 * @return The XP awarded per expected health pool, for slaying the given Mob
	 */
	public double getExpPerHp(Mob mob) {
		// Add 1 to make 1 hp monsters behave more reasonably
		double modifiedHp = mob.getEhp().getAverage() + 1d;
		double exp = calcExp(mob);
		return calcAverageHp(playerLevel) * exp / modifiedHp;
	}

	public double getExpPerThreat(Mob mob) {
		double exp = calcExp(mob);
		return 20d * calcAverageHp(playerLevel) * exp / mob.getThreat();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Integer minLevel) {
		this.minLevel = minLevel;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public Integer getAlignment() {
		return alignment;
	}

	public void setAlignment(Integer alignment) {
		this.alignment = alignment;
	}

	public Integer getSpawnCount() {
		return spawnCount;
	}

	public void setSpawnCount(Integer spawnCount) {
		this.spawnCount = spawnCount;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Double getDamage() {
		return damage;
	}

	public void setDamage(Double damage) {
		this.damage = damage;
	}

	public Double getThreat() {
		return threat;
	}

	public void setThreat(Double threat) {
		this.threat = threat;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ActFlag getActFlag() {
		return actFlag;
	}

	public void setActFlag(ActFlag actFlag) {
		this.actFlag = actFlag;
	}

	public Integer getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(Integer playerLevel) {
		this.playerLevel = playerLevel;
	}

	public Integer getPlayerAlignment() {
		return playerAlignment;
	}

	public void setPlayerAlignment(Integer playerAlignment) {
		this.playerAlignment = playerAlignment;
	}

	public Integer getTotalPartyLevel() {
		return totalPartyLevel;
	}

	public void setTotalPartyLevel(Integer totalPartyLevel) {
		this.totalPartyLevel = totalPartyLevel;
	}

	public Boolean getIsShopkeeper() {
		return isShopkeeper;
	}

	public void setIsShopkeeper(Boolean isShopkeeper) {
		this.isShopkeeper = isShopkeeper;
	}

	public ItemType getBuysItem() {
		return buysItem;
	}

	public void setBuysItem(ItemType buysItem) {
		this.buysItem = buysItem;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}
}
