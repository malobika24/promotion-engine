package com.scm.promotion.service.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.scm.promotion.model.Product;
import com.scm.promotion.model.Rule;

import lombok.extern.java.Log;

@Component
@Log
public class RuleEngine implements RuleInterface<Map<String, Integer>, Double> {

	private List<Rule> rules;

	Map<String, Double> itemPriceMap;

	private void loadItemPrices() {
		itemPriceMap = new HashMap<>();

		itemPriceMap.put("A", 50.00);
		itemPriceMap.put("B", 30.00);
		itemPriceMap.put("C", 20.00);
		itemPriceMap.put("D", 15.00);
		itemPriceMap.put("F", 25.00);
	}

	public RuleEngine() {
		super();
		this.rules = new ArrayList<>();
		loadItemPrices();
	}

	public void registerRule(Rule rule) {
		this.rules.add(rule);
	}
	public void registerRules(List<Rule> rules) {
		this.rules.addAll(rules);
	}

	@Override
	public Double fireRules(Map<String, Integer> input) {

		AtomicReference<Double> output = new AtomicReference<>(0.0);
		rules.stream().forEach(rule -> {
			List<Product> ruleCondition = rule.getConditions();
			// If all Sku Code Conditions present in the listed items
			Set<String> matchingProducts = ruleCondition.parallelStream().map(Product::getSkuCode)
					.collect(Collectors.toSet());
			boolean isRulePassed = input.keySet().containsAll(matchingProducts);
			int minReqValue = input.entrySet().parallelStream().filter(e -> matchingProducts.contains(e.getKey()))
					.min((x, y) -> Integer.compare(x.getValue(), y.getValue())).get().getValue();
			
			if (isRulePassed) {
				ruleCondition.stream().forEach(cond -> {
					boolean isRuleMatched = false;
					Integer reqQuantity = input.get(cond.getSkuCode());
					if ((reqQuantity != null && reqQuantity >= cond.getQuantity())) {
						isRuleMatched = true;
					}
					log.info("Rule Passed" + isRuleMatched + " " + cond.getSkuCode());
					// Apply offer
					if (isRuleMatched && ruleCondition.lastIndexOf(cond) == (ruleCondition.size() - 1)) {

						ruleCondition.stream().forEach(condRule -> {
							input.put(condRule.getSkuCode(),
									input.get(condRule.getSkuCode()) - ((Double) (condRule.getQuantity()
											* Math.floor(minReqValue / condRule.getQuantity()))).intValue());

						});

						output.set(output.get() + Math.floor(minReqValue / cond.getQuantity()) * rule.getOutcome());
						log.info("Offer Applied: Rule Name=" + rule.getName()+" Price=" + output.get() );
					}
				});
			}

		});
		// Calculate Non-Discounted Products
		for (Map.Entry<String, Integer> entry : input.entrySet()) {
			if (entry.getValue() > 0) {
				output.set(output.get() + entry.getValue() * itemPriceMap.get(entry.getKey()));
			}
		}
		log.info("Final price=" + output.get());
		return output.get();
	}

}
