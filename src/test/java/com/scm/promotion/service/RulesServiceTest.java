package com.scm.promotion.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.promotion.model.Item;
import com.scm.promotion.service.rules.RulesService;

@SpringBootTest
public class RulesServiceTest {
	private RulesService rulesService = new RulesService();

	@Before
	public void setup() {
		rulesService.initializeRules();
	}

	@Test
	public void testWithoutOfferItems() {
		// Scenario 1 tests
		List<Item> items = new ArrayList<>();
		Item item1 = new Item("A", 1);
		Item item2 = new Item("B", 1);
		Item item3 = new Item("C", 1);
		items.add(item1);
		items.add(item2);
		items.add(item3);

		rulesService.fireRules(items);

		assertEquals("No of Items in List", 3, items.size());

		assertNull("Offer Price of Item 1", items.get(0).getOfferPrice());
		assertNull("Offer Price of Item 2", items.get(1).getOfferPrice());
		assertNull("Offer Price of Item 3", items.get(2).getOfferPrice());

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());

	}

	@Test
	public void testWithOfferItems() {
		// Scenario 2 tests
		List<Item> items = new ArrayList<>();
		Item item1 = new Item("A", 5);
		Item item2 = new Item("B", 5);
		Item item3 = new Item("C", 1);
		items.add(item1);
		items.add(item2);
		items.add(item3);

		rulesService.fireRules(items);

		assertEquals("No of Items in List", 3, items.size());

		assertEquals("Offer Price of Item 1", "130.0", "" + items.get(0).getOfferPrice());
		assertEquals("Offer Price of Item 2", "45.0", "" + items.get(1).getOfferPrice());
		assertEquals("Offer Quantity of Item 2", "2", "" + items.get(1).getOfferQuantity());
		assertNull("Offer Price of Item 3", items.get(2).getOfferPrice());// No Matching Offer

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());

	}

	@Test
	public void testWithCombinedOfferItems() {
		// Scenario 3 tests
		List<Item> items = new ArrayList<>();
		Item item1 = new Item("A", 3);
		Item item2 = new Item("B", 5);
		Item item3 = new Item("C", 1);
		Item item4 = new Item("D", 1);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);

		rulesService.fireRules(items);

		assertEquals("No of Items in List", 4, items.size());

		assertEquals("Offer Price of Item 1", "130.0", "" + items.get(0).getOfferPrice());
		assertEquals("Offer Quantity of Item 1", "3", "" + items.get(0).getOfferQuantity());
		assertEquals("Offer Price of Item 2", "45.0", "" + items.get(1).getOfferPrice());
		assertEquals("Offer Quantity of Item 2", "2", "" + items.get(1).getOfferQuantity());
		assertEquals("Offer Price of Item 3", "0.0", "" + items.get(2).getOfferPrice());
		assertEquals("Offer Quantity of Item 3", "1", "" + items.get(2).getOfferQuantity());
		assertEquals("Offer Price of Item 4", "30.0", "" + items.get(3).getOfferPrice());
		assertEquals("Offer Quantity of Item 4", "1", "" + items.get(3).getOfferQuantity());

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());
		assertEquals("Item Sku Code", "D", items.get(3).getSkuCode());

	}
}
