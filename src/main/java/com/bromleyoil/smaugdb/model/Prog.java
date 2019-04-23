package com.bromleyoil.smaugdb.model;

import java.util.List;

import com.bromleyoil.smaugdb.model.enums.ProgType;

public class Prog {

	private ProgType type;
	private String trigger;
	private List<String> definition;

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
}
