package com.premiummobile.First.magento;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtensionAttribute {
	
	@JsonProperty("stock_item")
	private MagentoStockItemRequest item;

	public MagentoStockItemRequest getItem() {
		return item;
	}

	public void setItem(MagentoStockItemRequest item) {
		this.item = item;
	}
}
