package com.premiummobile.First.magento;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MagentoStockItem extends ExtensionAttribute{
	
	@JsonProperty("stock_id")
	private Integer stockId;
	private Integer qty;
	@JsonProperty("is_in_stock")
	private boolean isInStock;

	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public boolean isInStock() {
		return isInStock;
	}
	public void setInStock(boolean isInStock) {
		this.isInStock = isInStock;
	}
}
