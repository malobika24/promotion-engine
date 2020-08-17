package com.scm.promotion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scm.promotion.model.Item;

@Service
public class ProductService {

	/**Method to calculate item price based on the Item Sku Code and Quantity
	 * @param items List of items supplied by user with Item Sku Code and quantiy
	 * @return
	 */
	public Item calculatePrice(List<Item> items) {
		//TODO: Fetch matching Rules
		//TODO: Calculate Price
		return null;
	}
}
