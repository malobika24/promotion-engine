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
		Map<String, Integer> orders = new HashMap<>();
		orders.put("A", 1);
		orders.put("B", 1);
		orders.put("C", 1);
		when(ruleEngine.fireRules(orders)).thenReturn(100.0);

		Double totalPrice = productService.calculatePrice(orders);

		assertEquals("Total Price of all orders", "100.0", "" + totalPrice);

	}

	@Test(expected=RuntimeException.class)
	public void testFailure() {
		Map<String, Integer> orders = new HashMap<>();
		orders.put("A", 1);
		orders.put("B", 1);
		orders.put("C", 1);
		when(ruleEngine.fireRules(orders)).thenThrow(new RuntimeException("No Value"));

		productService.calculatePrice(orders);

	}

}
