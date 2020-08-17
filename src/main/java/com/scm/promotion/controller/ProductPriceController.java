package com.scm.promotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping
	public ResponseEntity<Double> calculatePrice(@RequestBody List<Item> items) {
		Double totalPrice = productService.calculatePrice(items);
		return new ResponseEntity<Double>(totalPrice, HttpStatus.OK);
	}
}
