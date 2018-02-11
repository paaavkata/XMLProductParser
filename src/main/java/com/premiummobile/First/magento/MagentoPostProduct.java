package com.premiummobile.First.magento;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagentoPostProduct {
	
	@JsonProperty("product")
	private MagentoProductRequest magentoProduct;

	public MagentoProductRequest getMagentoProduct() {
		return magentoProduct;
	}

	public void setMagentoProduct(MagentoProductRequest magentoProduct) {
		this.magentoProduct = magentoProduct;
	}
	
}
