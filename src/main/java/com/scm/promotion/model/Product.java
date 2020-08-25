package com.scm.promotion.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Product {
	private String skuCode;
	private Integer quantity;
	private Double price;

	public Product(String skuCode, Integer quantity) {
		super();
		this.skuCode = skuCode;
		this.quantity = quantity;
	}
	
	public Product(String skuCode, Integer quantity, Double price) {
		super();
		this.skuCode = skuCode;
		this.quantity = quantity;
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skuCode == null) ? 0 : skuCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (skuCode == null) {
			if (other.skuCode != null)
				return false;
		} else if (!skuCode.equals(other.skuCode))
			return false;
		return true;
	}

}
