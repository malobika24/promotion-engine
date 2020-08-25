package com.scm.promotion.service;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.scm.promotion.model.Product;
import com.scm.promotion.model.Rule;
import com.scm.promotion.service.rules.RuleEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final RuleEngine ruleEngine;

	@PostConstruct
	public void init() {
		// Load all rules into session.
		ruleEngine.registerRule(Rule.builder().name("Offer for A").conditions(Arrays.asList(new Product("A", 3))).outcome(130.0).build());
		ruleEngine.registerRule(Rule.builder().name("Offer for B").conditions(Arrays.asList(new Product("B", 2))).outcome(45.0).build());
		ruleEngine.registerRule(
				Rule.builder().name("Offer for C & D").conditions( Arrays.asList(new Product("C", 1), new Product("D", 1))).outcome( 30.0).build());
	}

	/**
	 * Method to calculate item price based on the Item Sku Code and Quantity
	 * 
	 * @param items List of items supplied by user with Item Sku Code and quantiy
	 * @return
	 */
	public Double calculatePrice(Map<String, Integer> orders) {
		return ruleEngine.fireRules(orders);
	}
	
}
