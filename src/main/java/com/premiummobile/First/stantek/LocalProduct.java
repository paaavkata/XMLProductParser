package com.premiummobile.First.stantek;

import java.util.ArrayList;

public class LocalProduct {

	private int qty;
	private long id;
	private double price;
	private String name;
	private String vendor;
	private String brand;
	private String model;
	private String sku;
	private String productId;
	private String weight;
	private String warranty;
	private String color;
	private String url;
	private ArrayList<String> images;
	
	
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		if(price > 70){
			if(price % 10 == 0){
				this.price = price - 1;
			}
			else{
				if(price % 10 > 5){
					this.price = (price - (price % 10) + 9);
				}
				else{
					this.price = (price - (price % 10) - 1);
				}
			}
		}
		else{
			this.price = price;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getBrand() {
		return brand != null ? brand : "";
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getWeight() {
		return weight != null ? weight : "";
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWarranty() {
		return warranty != null ? warranty : "";
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public ArrayList<String> getImages() {
		return images;
	}
	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
