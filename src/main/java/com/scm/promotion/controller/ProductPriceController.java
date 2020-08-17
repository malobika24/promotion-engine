package com.scm.promotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.promotion.model.Item;
import com.scm.promotion.service.ProductService;


@RestController
@RequestMapping("/products")
public class ProductPriceController {
	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Item> calculatePrice(@RequestBody List<Item> items) {
		Item item = productService.calculatePrice(items);
		return new ResponseEntity<Item>(item, HttpStatus.OK);
	}
}
