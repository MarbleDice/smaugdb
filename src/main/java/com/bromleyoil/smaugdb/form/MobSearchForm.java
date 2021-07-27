package com.bromleyoil.smaugdb.form;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.bromleyoil.smaugdb.model.Area;
import com.bromleyoil.smaugdb.model.Mob;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.Labelable;

public class MobSearchForm extends AbstractSearchForm<Mob> {
	private String name;
	private Area area;
	private Integer minLevel;
	private Integer maxLevel;
	private Integer alignment;
	private Integer spawnCount;
	private Integer gold;
	private Double damage;
	private ActFlag actFlag;

	private Integer playerLevel;
	private Integer playerAlignment;
	private Integer totalPartyLevel;

	private Boolean isShopkeeper;
	private ItemType buysItem;

	private Format format;

	public enum Format implements Labelable {
		DEFAULT, COINS, EXP;
	}

	@Override
	public Stream<Mob> applyFilters(Stream<Mob> stream) {
		stream = maybeFilter(stream, name != null, x -> x.getName().toLowerCase().contains(getName().toLowerCase()));
		stream = maybeFilter(stream, area != null, x -> x.getArea().equals(getArea()));
		stream = maybeFilter(stream, minLevel != null, x -> x.getLevel().getAverage() >= getMinLevel());
		stream = maybeFilter(stream, maxLevel != null, x -> x.getLevel().getAverage() <= getMaxLevel());
		stream = maybeFilter(stream, alignment != null, x -> x.getAlignment() <= getAlignment());
		stream = maybeFilter(stream, spawnCount != null, x -> x.getMaxSpawnCount() >= getSpawnCount());
		stream = maybeFilter(stream, gold != null, x -> x.getGold().getAverage() >= gold);
		stream = maybeFilter(stream, damage != null, x -> x.getDamagePerRound().getAverage() <= getDamage());
		stream = maybeFilter(stream, actFlag != null, x -> x.hasActFlag(getActFlag()));
		stream = maybeFilter(stream, isShopkeeper != null, x -> x.isShopkeeper() == isShopkeeper);
		stream = maybeFilter(stream, buysItem != null, x -> x.getPurchasedTypes().contains(buysItem));

		return stream;
	}

	@Override
	protected Comparator<Mob> getComparator() {
		if (format == Format.EXP && playerLevel != null) {
			return Comparator.comparingDouble(this::getExpPerHp).reversed();
		} else if (format == Format.COINS) {
			return Comparator.comparing(x ->  x.getGold().getAverage(), Comparator.reverseOrder());
		} else {
			return Comparator.comparingDouble(x -> x.getLevel().getAverage());
		}
	}

	public boolean showExp() {
		return format == Format.EXP && playerLevel != null;
	}

	public boolean showShop() {
		return buysItem != null || (isShopkeeper != null && isShopkeeper);
	}

	public boolean showCombat() {
		return !showShop();
	}

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

	public double getExpPerHp(Mob mob) {
		// Add 1 to make 1 hp monsters behave more reasonably
		double modifiedHp = mob.getEhp().getAverage() + 1d;
		double exp = calcExp(mob);
		return 10d * playerLevel * exp / modifiedHp;
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
