package com.scm.promotion.service.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.scm.promotion.model.Product;
import com.scm.promotion.model.Rule;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

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

	public static void main(String[] args) {

		List<String> list1 = Arrays.asList("A", "B", "C", "D");
		List<String> list2 = Arrays.asList("A", "D", "E");
		String result = list1.stream().reduce((first, second) -> second).orElse("no last element");

		System.out.println(result);

		Map<String, Integer> map1 = new HashMap<>();
		map1.put("A", 3);// 5 5 1 370
		map1.put("B", 2);// 3 5 1 1 280
		map1.put("C", 1);// 1 1 1 100
		map1.put("D", 1);// 3 5 2 1 300
		// 130 + 120+ 300+100

		RuleEngine ruleEngine = new RuleEngine();
		ruleEngine.loadItemPrices();
		ruleEngine.registerRule(new Rule("Offer for A", Arrays.asList(new Product("A", 3)), 130.0));
		ruleEngine.registerRule(new Rule("Offer for B", Arrays.asList(new Product("B", 2)), 45.0));
		ruleEngine.registerRule(
				new Rule("Offer for C & D", Arrays.asList(new Product("C", 1), new Product("D", 1)), 30.0));
		ruleEngine.fireRules(map1);
		// .forEach((key, val) -> System.out.println(key + "-->>" + val));
		int val1 = 10;

		Map<Predicate<Integer>, Supplier<String>> ruleMap = new HashMap<Predicate<Integer>, Supplier<String>>() {
			{
				put((i) -> i < 10, () -> "Less than 10!");
				put((i) -> i < 100, () -> "Less than 100!");
				put((i) -> i < 1000, () -> "Less than 1000!");
			}
		};

		ruleMap.keySet().stream().filter((keyCondition) -> keyCondition.test(23)).findFirst()
				.ifPresent((e) -> System.out.print(ruleMap.get(e).get()));

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

	public void fireRules2() {
		/*Map<String, Integer> map2 = new HashMap<>();
		map2.put("A", 3);// 5 5 1 370
		map2.put("B", 2);// 3 5 1 1 280
		map2.put("C", 1);// 1 1 1 100
		// map2.put("D", 1);// 3 5 2 1 300
		Map<Predicate<Map<String, Integer>>, Supplier<Map<String, Product>>> ruleMap1 = new HashMap<Predicate<Map<String, Integer>>, Supplier<Map<String, Product>>>() {
			{
				put((map) -> map.get("A") != null && map.get("A") >= 3, () -> new HashMap<String, Product>() {
					{
						put("A", Product.builder().skuCode("A").quantity(3).price(130.0).build());
					}
				});
				put((map) -> map.get("B") != null && map.get("B") >= 2, () -> new HashMap<String, Product>() {
					{
						put("B", Product.builder().skuCode("B").quantity(2).price(45.0).build());
					}
				});
				put((map) -> map.get("C") != null && map.get("D") != null && map.get("C") >= 1 && map.get("D") >= 1,
						() -> new HashMap<String, Product>() {
							{
								put("C", Product.builder().skuCode("C").quantity(1).price(30.0).build());
								put("D", Product.builder().skuCode("D").quantity(1).price(30.0).build());
							}
						});
			}
		};

		List<Map<String, Product>> offersApplied = ruleMap1.keySet().stream()
				.filter((keyCondition) -> keyCondition.test(map2)).map(e -> ruleMap1.get(e).get())
				.collect(Collectors.toList());
		AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
		map2.forEach((key, value) -> {
			Double nonDiscountedItems = offersApplied.stream().filter(offr -> offr.keySet().contains(key))
					.peek(action -> {
						Product offrProduct = action.get(key);
						Double offrPrice = offrProduct.getPrice() * Math.floor(value / offrProduct.getQuantity());
						totalPrice.set(totalPrice.get() + offrPrice);

					});

		});*/

	}

}
