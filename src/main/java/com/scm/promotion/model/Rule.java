package com.scm.promotion.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Rule {
	private String name;
	private List<Product> conditions;
	private Double outcome;

	public Rule(String name, List<Product> conditions, Double outcome) {
		super();
		this.conditions = conditions;
		this.outcome = outcome;
		this.name = name;
	}
}
