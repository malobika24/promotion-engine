package com.scm.promotion.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.promotion.service.ProductService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductPriceController {
	private final ProductService productService;

	@PostMapping("/price")
	public ResponseEntity<Double> calculatePrice(@RequestBody Map<String, Integer> items) {
		Double totalPrice = productService.calculatePrice(items);
		return new ResponseEntity<Double>(totalPrice, HttpStatus.OK);
	}
}
