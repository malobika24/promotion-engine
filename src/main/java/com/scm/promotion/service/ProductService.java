package com.scm.promotion.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.promotion.model.Item;
import com.scm.promotion.service.rules.RulesService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	RulesService rulesService;

	Map<String, Double> itemPriceMap;

	@PostConstruct
	public void init() {
		// Load all rules into Kie session.
		this.rulesService.initializeRules();
		loadItemPrices();
	}

	private void loadItemPrices() {
		itemPriceMap = new HashMap<>();

		itemPriceMap.put("A", 50.00);
		itemPriceMap.put("B", 30.00);
		itemPriceMap.put("C", 20.00);
		itemPriceMap.put("D", 15.00);
	}

	/**
	 * Method to calculate item price based on the Item Sku Code and Quantity
	 * 
	 * @param items List of items supplied by user with Item Sku Code and quantiy
	 * @return
	 */
	public Double calculatePrice(List<Item> items) {
		// Fetch matching Rules
		fetchMatchingRulesAndCalculatePrice(items);
		// Calculate Total price
		items.parallelStream().forEach(item -> setPrice(item));
		Double totalPrice = items.parallelStream().collect(Collectors.summingDouble(Item::getTotalPrice));
		return totalPrice;
	}

	private void setPrice(Item item) {
		Double nonOfferQty = 0.00 + item.getQuantity();
		Double totalOfferPrice =0.00;
		Double totalPrice = 0.00;
		if (item.getOfferQuantity()!=null && item.getOfferQuantity() > 0) {
			Double offerOnQty = Math.floor(item.getQuantity() / item.getOfferQuantity());
			totalOfferPrice = offerOnQty * item.getOfferPrice();
			nonOfferQty = nonOfferQty - (offerOnQty*item.getOfferQuantity());
		}
		totalPrice = (nonOfferQty * itemPriceMap.get(item.getSkuCode())) + totalOfferPrice;
		item.setTotalPrice(totalPrice);
	}

	private void fetchMatchingRulesAndCalculatePrice(List<Item> items) {
		int fired = this.rulesService.fireRules(items);
	}
}
