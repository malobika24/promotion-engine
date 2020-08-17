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
	private Integer offerQuantity;
	private Double offerPrice;
	private Double totalPrice;
	
	
	
	public Item(String skuCode, Integer reqQuantity) {
		super();
		this.skuCode = skuCode;
		this.quantity = reqQuantity;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getOfferQuantity() {
		return offerQuantity;
	}
	public void setOfferQuantity(Integer offerQuantity) {
		this.offerQuantity = offerQuantity;
	}
	public Double getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
	
}
