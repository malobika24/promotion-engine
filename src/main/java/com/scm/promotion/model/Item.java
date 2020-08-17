package com.scm.promotion.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Item {

	private String skuCode;
	private Integer quantity;
	private Double price;
}
