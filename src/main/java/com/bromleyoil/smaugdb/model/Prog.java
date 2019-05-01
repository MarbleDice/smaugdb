package com.bromleyoil.smaugdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bromleyoil.smaugdb.model.enums.ProgType;

public class Prog {

	private ProgType type;
	private String trigger;
	private List<String> definition;

	private List<Pop> containedPops = new ArrayList<>();
	private List<Spawn> containedSpawns = new ArrayList<>();

	public ProgType getType() {
		return type;
	}

	public void setType(ProgType type) {
		this.type = type;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public List<String> getDefinition() {
		return definition;
	}

	public void setDefinition(List<String> definition) {
		this.definition = definition;
	}

	public Collection<Pop> getContainedPops() {
		return Collections.unmodifiableCollection(containedPops);
	}

	public void addContainedPop(Pop pop) {
		containedPops.add(pop);
	}

	public Collection<Spawn> getContainedSpawns() {
		return Collections.unmodifiableCollection(containedSpawns);
	}

	public void addContainedSpawn(Spawn containedSpawn) {
		containedSpawns.add(containedSpawn);
	}
}
