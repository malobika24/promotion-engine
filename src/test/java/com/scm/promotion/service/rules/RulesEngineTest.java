package com.scm.promotion.service.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.promotion.model.Product;
import com.scm.promotion.model.Rule;

@SpringBootTest
public class RulesEngineTest {
	private RuleEngine ruleEngine = new RuleEngine();

	public void loadRules() {
		ruleEngine.registerRule(new Rule("Offer for A", Arrays.asList(new Product("A", 3)), 130.0));
		ruleEngine.registerRule(new Rule("Offer for B", Arrays.asList(new Product("B", 2)), 45.0));
		ruleEngine.registerRule(
				new Rule("Offer for C & D", Arrays.asList(new Product("C", 1), new Product("D", 1)), 30.0));

	}

	@Test
	public void testWithoutAnyRulesLoaded() {
		// Scenario 2 tests
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 5);
		items.put("B", 5);
		items.put("C", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(420.0, totalValue);

	}
	
	@Test
	public void testWithFewRulesLoaded() {
		ruleEngine.registerRule(new Rule("Offer for A", Arrays.asList(new Product("A", 3)), 130.0));
		// Scenario 2 tests
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 5);
		items.put("B", 5);
		items.put("C", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(400.0, totalValue);

	}

	@Test
	public void test_registerRules() {
		ruleEngine.registerRules(Arrays.asList(new Rule("Offer for B", Arrays.asList(new Product("B", 2)), 45.0),new Rule("Offer for C & D", Arrays.asList(new Product("C", 1), new Product("D", 1)), 30.0)));
		// Scenario 2 tests
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 5);
		items.put("B", 5);
		items.put("C", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(390.0, totalValue);

	}

	@Test
	public void testWithoutOfferItems() {
		loadRules();
		// Scenario 1 tests
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 1);
		items.put("B", 1);
		items.put("C", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(100, totalValue);

	}

	@Test
	public void testWithOfferItems() {
		loadRules();
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 5);
		items.put("B", 5);
		items.put("C", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(370, totalValue);

	}

	@Test
	public void testWithCombinedOfferItems() {
		loadRules();
		// Scenario 3 tests
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 3);
		items.put("B", 5);
		items.put("C", 1);
		items.put("D", 1);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(280, totalValue);

	}

	@Test
	public void testWithCombinedOfferAlongWithSingle() {
		loadRules();
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 3);
		items.put("B", 5);
		items.put("C", 15);
		items.put("D", 10);

		Double totalValue = ruleEngine.fireRules(items);

		assertEquals(650, totalValue);

	}

}
