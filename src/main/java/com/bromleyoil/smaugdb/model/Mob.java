package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.smaugdb.Utils;
import com.bromleyoil.smaugdb.model.enums.ActFlag;
import com.bromleyoil.smaugdb.model.enums.AffectFlag;
import com.bromleyoil.smaugdb.model.enums.AttackFlag;
import com.bromleyoil.smaugdb.model.enums.DamageType;
import com.bromleyoil.smaugdb.model.enums.DamageVerb;
import com.bromleyoil.smaugdb.model.enums.DefenseFlag;
import com.bromleyoil.smaugdb.model.enums.ExtraFlag;
import com.bromleyoil.smaugdb.model.enums.ItemType;
import com.bromleyoil.smaugdb.model.enums.ResistFlag;
import com.bromleyoil.smaugdb.model.enums.Special;

/**
 * Represents a mobile.
 * 
 */
public class Mob {

	private Area area;
	int vnum;
	private String name;
	private String keywords;
	private String description;
	private String race;
	private List<String> longDescription = new ArrayList<>();
	private List<ActFlag> actFlags = new ArrayList<>();
	private List<AffectFlag> affectFlags = new ArrayList<>();
	private int alignment;
	private String assistGroup;
	private int suggestedLevel;
	private Range level;
	private Range gold;
	private int experience;
	private Range hp;
	private Range mana;
	private int armor;
	private int pierceArmor;
	private int bashArmor;
	private int slashArmor;
	private int magicArmor;
	private int thac0;
	private int hitroll;
	private Range damage;
	private int damroll;
	private DamageVerb damageVerb;
	private List<ExtraFlag> extraFlags = new ArrayList<>();
	private List<ResistFlag> resistFlags = new ArrayList<>();
	private List<ResistFlag> immuneFlags = new ArrayList<>();
	private List<ResistFlag> vulnerableFlags = new ArrayList<>();
	private List<AttackFlag> attackFlags = new ArrayList<>();
	private List<DefenseFlag> defenseFlags = new ArrayList<>();
	private String size;

	private boolean isShopkeeper;
	private List<ItemType> purchasedTypes = new ArrayList<>();
	private int sellPercent;
	private int buyPercent;
	private int openHour;
	private int closeHour;

	private Special special;
	private List<Spawn> spawns = new ArrayList<>();
	private int maxSpawnCount;

	private List<Prog> progs = new ArrayList<>();

	public String getCssClass() {
		return isAggressive() ? "mob aggressive" : "mob non-aggressive";
	}

	public String getAccuracy() {
		int accuracy = hitroll - Utils.interpolate(suggestedLevel, 0, thac0, 32, 0);
		accuracy = Utils.constrain(1, 19, accuracy);
		return String.valueOf(accuracy);
	}

	@Override
	public String toString() {
		return getName();
	}

	public boolean getExists() {
		return !spawns.isEmpty();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int vnum) {
		this.vnum = vnum;
	}

	public String getName() {
		return name;
	}

	public String getTruncatedName() {
		return name.length() <= 45 ? name : name.substring(0, 40) + "...";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return StringUtils.capitalize(name);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public List<String> getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(List<String> longDescription) {
		this.longDescription = longDescription;
	}

	public List<ActFlag> getActFlags() {
		return actFlags;
	}

	public boolean hasActFlag(ActFlag actFlag) {
		return actFlags.contains(actFlag);
	}

	public boolean isAggressive() {
		return hasActFlag(ActFlag.AGGRESSIVE) && !hasActFlag(ActFlag.WIMPY);
	}

	public void setActFlags(List<ActFlag> actFlags) {
		this.actFlags = actFlags;
	}

	public boolean canKill() {
		return !isShopkeeper && !hasActFlag(ActFlag.HEALER) && !hasActFlag(ActFlag.TRAIN) && !hasActFlag(ActFlag.GAIN)
				&& !hasActFlag(ActFlag.PRACTICE) && !hasActFlag(ActFlag.CHANGER);
	}

	public List<AffectFlag> getAffectFlags() {
		return affectFlags;
	}

	public boolean hasAffectFlag(AffectFlag affectFlag) {
		return affectFlags.contains(affectFlag);
	}

	public void setAffectFlags(List<AffectFlag> affectFlags) {
		this.affectFlags = affectFlags;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public String getAssistGroup() {
		return assistGroup;
	}

	public void setAssistGroup(String assistGroup) {
		this.assistGroup = assistGroup;
	}

	public int getSuggestedLevel() {
		return suggestedLevel;
	}

	public void setSuggestedLevel(int suggestedLevel) {
		this.suggestedLevel = suggestedLevel;
	}

	public Range getLevel() {
		return level;
	}

	public void setLevel(Range level) {
		this.level = level;
	}

	/**
	 * Mob THAC0 - Higher is better. Mobs gain +(1/32) hitroll per level for each point.
	 * Players interpolate between 18 and 6-12 based on class over 32 levels, gaining 9-18 hitroll at level 50.
	 * 
	 * 1 hitroll = 5% chance to hit
	 * Armor class = Lower is better. The best is -190. Target AC reduces hitroll at 10 AC = -1 hitroll.
	 * Weapon not visible is +1 hitroll
	 * Can't see opponent is -4 hitroll
	 * Weapon proficiency is -5 to +4 hitroll
	 * Smarter combatant gains +0.1 hitroll per logged player kill per point of int difference
	 * 
	 */
	public int getThac0() {
		return thac0;
	}

	public void setThac0(int thac0) {
		this.thac0 = thac0;
	}

	public int getHitroll() {
		return hitroll;
	}

	public void setHitroll(int hitroll) {
		this.hitroll = hitroll;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getPierceArmor() {
		return pierceArmor;
	}

	public void setPierceArmor(int pierceArmor) {
		this.pierceArmor = pierceArmor;
	}

	public int getBashArmor() {
		return bashArmor;
	}

	public void setBashArmor(int bashArmor) {
		this.bashArmor = bashArmor;
	}

	public int getSlashArmor() {
		return slashArmor;
	}

	public void setSlashArmor(int slashArmor) {
		this.slashArmor = slashArmor;
	}

	public int getMagicArmor() {
		return magicArmor;
	}

	public void setMagicArmor(int magicArmor) {
		this.magicArmor = magicArmor;
	}

	public Range getHp() {
		return hp;
	}

	public void setHp(Range hp) {
		this.hp = hp;
	}

	public Range getEhp() {
		return Range.of(hp).multiply(hasAffectFlag(AffectFlag.SANCTUARY) ? 2 : 1);
	}

	public Range getMana() {
		return mana;
	}

	public void setMana(Range mana) {
		this.mana = mana;
	}

	public Range getDamage() {
		return damage;
	}

	public void setDamage(Range damage) {
		this.damage = damage;
	}

	public Range getDamagePerRound() {
		double multiple = 1;
		if (hasAffectFlag(AffectFlag.HASTE) || hasAttackFlag(AttackFlag.FAST)) {
			multiple += 1;
		}
		if (hasActFlag(ActFlag.THIEF) || hasActFlag(ActFlag.WARRIOR)) {
			// chance for 2nd attack = rand[0, 100) < (10 + 3 * ch->level) / 2
			multiple += (getLevel().getAverage() * 3 + 10) / 200;
		}
		if (hasActFlag(ActFlag.WARRIOR)) {
			// chance for 3rd attack = rand[0, 100) < (4 * ch->level - 40) / 4)
			multiple += (getLevel().getAverage() * 4 - 40) / 400;
		}
		return Range.of(damage).multiply((int)(100 * multiple)).divide(100);
	}

	public double getThreat() {
		return getEhp().getAverage() * getDamagePerRound().getAverage() / 10;
	}

	public DamageType getDamageType() {
		return Optional.ofNullable(damageVerb).map(DamageVerb::getDamageType).orElse(null);
	}

	public Range getGold() {
		return gold;
	}

	public void setGold(Range gold) {
		this.gold = gold;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getDamroll() {
		return damroll;
	}

	public void setDamroll(int damroll) {
		this.damroll = damroll;
	}

	public DamageVerb getDamageVerb() {
		return damageVerb;
	}

	public void setDamageVerb(DamageVerb damageVerb) {
		this.damageVerb = damageVerb;
	}

	public List<ExtraFlag> getExtraFlags() {
		return Collections.unmodifiableList(extraFlags);
	}

	public void setExtraFlags(List<ExtraFlag> extraFlags) {
		this.extraFlags = extraFlags;
	}

	public List<ResistFlag> getResistFlags() {
		return Collections.unmodifiableList(resistFlags);
	}

	public void setResistFlags(List<ResistFlag> resistFlags) {
		this.resistFlags = resistFlags;
	}

	public List<ResistFlag> getImmuneFlags() {
		return Collections.unmodifiableList(immuneFlags);
	}

	public void setImmuneFlags(List<ResistFlag> immuneFlags) {
		this.immuneFlags = immuneFlags;
	}

	public List<ResistFlag> getVulnerableFlags() {
		return Collections.unmodifiableList(vulnerableFlags);
	}

	public void setVulnerableFlags(List<ResistFlag> vulnerableFlags) {
		this.vulnerableFlags = vulnerableFlags;
	}

	public List<AttackFlag> getAttackFlags() {
		return Collections.unmodifiableList(attackFlags);
	}

	public void setAttackFlags(List<AttackFlag> attackFlags) {
		this.attackFlags = attackFlags;
	}

	public boolean hasAttackFlag(AttackFlag attackFlag) {
		return attackFlags.contains(attackFlag);
	}

	public List<DefenseFlag> getDefenseFlags() {
		return Collections.unmodifiableList(defenseFlags);
	}

	public void setDefenseFlags(List<DefenseFlag> defenseFlags) {
		this.defenseFlags = defenseFlags;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<Spawn> getSpawns() {
		return spawns;
	}

	public void addSpawn(Spawn spawn) {
		spawns.add(spawn);
	}

	public int getMaxSpawnCount() {
		return maxSpawnCount;
	}

	public void setMaxSpawnCount(int maxSpawnCount) {
		this.maxSpawnCount = maxSpawnCount;
	}

	public boolean isShopkeeper() {
		return isShopkeeper;
	}

	public void setShopkeeper(boolean isShopkeeper) {
		this.isShopkeeper = isShopkeeper;
	}

	public List<ItemType> getPurchasedTypes() {
		return purchasedTypes;
	}

	public void setPurchasedTypes(List<ItemType> purchasedTypes) {
		this.purchasedTypes = purchasedTypes;
	}

	public int getSellPercent() {
		return sellPercent;
	}

	public void setSellPercent(int sellPercent) {
		this.sellPercent = sellPercent;
	}

	public int getBuyPercent() {
		return buyPercent;
	}

	public void setBuyPercent(int buyPercent) {
		this.buyPercent = buyPercent;
	}

	public int getOpenHour() {
		return openHour;
	}

	public void setOpenHour(int openHour) {
		this.openHour = openHour;
	}

	public int getCloseHour() {
		return closeHour;
	}

	public void setCloseHour(int closeHour) {
		this.closeHour = closeHour;
	}

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

	public Collection<Prog> getProgs() {
		return Collections.unmodifiableCollection(progs);
	}

	public void setProgs(List<Prog> progs) {
		progs.stream().forEach(p -> p.setOwner(this));
		this.progs = progs;
	}
}
