package com.scm.promotion.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.scm.promotion.model.Item;
import com.scm.promotion.service.rules.RulesService;
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	@Mock
	private RulesService rulesService;
	
	@InjectMocks
	private ProductService productService;
	@Before
	public void setup() {
		productService.init();
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
		//when(rulesService.fireRules(items)).thenReturn(1);

		Double totalPrice= productService.calculatePrice(items);

		assertEquals("Total Price of all Items", "100.0",""+totalPrice);
		
		assertEquals("No of Items in List", 3, items.size());
		assertEquals("Total Price of Item A", "50.0", "" + items.get(0).getTotalPrice());
		assertEquals("Total Price of Item B", "30.0", "" + items.get(1).getTotalPrice());
		assertEquals("Total Price of Item C", "20.0", "" + items.get(2).getTotalPrice());

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());

	}

	@Test
	public void testWithOfferItems() {
		// Scenario 2 tests
		List<Item> items = new ArrayList<>();
		Item item1 = new Item("A", 5);
		item1.setOfferPrice(130.0);
		item1.setOfferQuantity(3);
		Item item2 = new Item("B", 5);
		item2.setOfferPrice(45.0);
		item2.setOfferQuantity(2);
		Item item3 = new Item("C", 1);
		items.add(item1);
		items.add(item2);
		items.add(item3);

		Double totalPrice= productService.calculatePrice(items);

		assertEquals("Total Price of all Items", "370.0",""+totalPrice);
		
		assertEquals("No of Items in List", 3, items.size());
		assertEquals("Total Price of Item A", "230.0", "" + items.get(0).getTotalPrice());
		assertEquals("Total Price of Item B", "120.0", "" + items.get(1).getTotalPrice());
		assertEquals("Total Price of Item C", "20.0", "" + items.get(2).getTotalPrice());

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());

	}

	@Test
	public void testWithCombinedOfferItems() {
		// Scenario 3 tests
		List<Item> items = new ArrayList<>();
		Item item1 = new Item("A", 3);
		item1.setOfferPrice(130.0);
		item1.setOfferQuantity(3);
		Item item2 = new Item("B", 5);
		item2.setOfferPrice(45.0);
		item2.setOfferQuantity(2);
		Item item3 = new Item("C", 1);
		item3.setOfferPrice(0.0);
		item3.setOfferQuantity(1);
		Item item4 = new Item("D", 1);
		item4.setOfferPrice(30.0);
		item4.setOfferQuantity(1);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);

		rulesService.fireRules(items);

		Double totalPrice= productService.calculatePrice(items);

		assertEquals("Total Price of all Items", "280.0",""+totalPrice);
		
		assertEquals("No of Items in List", 4, items.size());
		assertEquals("Total Price of Item A", "130.0", "" + items.get(0).getTotalPrice());
		assertEquals("Total Price of Item B", "120.0", "" + items.get(1).getTotalPrice());
		assertEquals("Total Price of Item C", "0.0", "" + items.get(2).getTotalPrice());
		assertEquals("Total Price of Item D", "30.0", "" + items.get(3).getTotalPrice());

		assertEquals("Item Sku Code", "A", items.get(0).getSkuCode());
		assertEquals("Item Sku Code", "B", items.get(1).getSkuCode());
		assertEquals("Item Sku Code", "C", items.get(2).getSkuCode());
		assertEquals("Item Sku Code", "D", items.get(3).getSkuCode());

	}
}
