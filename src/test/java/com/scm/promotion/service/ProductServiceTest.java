package com.scm.promotion.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.scm.promotion.service.rules.RuleEngine;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	@Mock
	private RuleEngine ruleEngine;
	@InjectMocks
	private ProductService productService;

	@Test
	public void testSuccess() {
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 1);
		items.put("B", 1);
		items.put("C", 1);
		when(ruleEngine.fireRules(items)).thenReturn(100.0);

		Double totalPrice = productService.calculatePrice(items);

		assertEquals("Total Price of all Items", "100.0", "" + totalPrice);

	}

	@Test(expected=RuntimeException.class)
	public void testFailure() {
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 1);
		items.put("B", 1);
		items.put("C", 1);
		when(ruleEngine.fireRules(items)).thenThrow(new RuntimeException("No Value"));

		productService.calculatePrice(items);

	}

}
