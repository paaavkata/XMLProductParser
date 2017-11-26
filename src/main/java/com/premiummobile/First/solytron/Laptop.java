package com.premiummobile.First.solytron;

import java.util.List;

public class Laptop {
	
	private String name;
	private String vendor;
	private String model;
	private String sku;
	private String productId;
	private String procesor;
	private String procesorDetails;
	private String video;
	private String videoDetails;
	private String memory;
	private String memoryType;
	private String memoryMax;
	private String memorySlots;
	private String disk;
	private String diskDetails;
	private String displayType;
	private String optical;
	private String lan;
	private String wifi;
	private String osname;
	private String os;
	private String bluetooth;
	private int usb2;
	private int usb3;
	private int usbc;
	private boolean rj45;
	private boolean vga;
	private boolean hdmi;
	private String audio;
	private boolean card;
	private Double battery;
	private Double weight;
	private String color;
	private int warranty;
	private String camera;
	private List<String> images;
	private Double price;
	private Double userPrice;
	private int quantity;
	private String stockInfo;
	
	public Laptop(){
		
	}
	
	public Laptop(String sku, 
					String productId, 
					String name, 
					String vendor, 
					double price, 
					double userPrice, 
					String stockInfo, 
					int quantity){
		
		this.sku = sku;
		this.productId = productId;
		this.name = name;
		this.vendor = vendor;
		this.price = price;
		this.userPrice = userPrice;
		this.stockInfo = stockInfo;
		this.quantity = quantity;
		
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

	public String getProcesor() {
		return procesor;
	}

	public void setProcesor(String procesor) {
		this.procesor = procesor;
	}

	public String getProcesorDetails() {
		return procesorDetails;
	}

	public void setProcesorDetails(String procesorDetails) {
		this.procesorDetails = procesorDetails;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getVideoDetails() {
		return videoDetails;
	}

	public void setVideoDetails(String videoDetails) {
		this.videoDetails = videoDetails;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getMemoryType() {
		return memoryType;
	}

	public void setMemoryType(String memoryType) {
		this.memoryType = memoryType;
	}

	public String getMemoryMax() {
		return memoryMax;
	}

	public void setMemoryMax(String memoryMax) {
		this.memoryMax = memoryMax;
	}

	public String getMemorySlots() {
		return memorySlots;
	}

	public void setMemorySlots(String memorySlots) {
		this.memorySlots = memorySlots;
	}

	public String getDisk() {
		return disk;
	}

	public void setDisk(String disk) {
		this.disk = disk;
	}

	public String getDiskDetails() {
		return diskDetails;
	}

	public void setDiskDetails(String diskDetails) {
		this.diskDetails = diskDetails;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getOptical() {
		return optical;
	}

	public void setOptical(String optical) {
		this.optical = optical;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(String bluetooth) {
		this.bluetooth = bluetooth;
	}

	public int getUsb2() {
		return usb2;
	}

	public void setUsb2(int usb2) {
		this.usb2 = usb2;
	}

	public int getUsb3() {
		return usb3;
	}

	public void setUsb3(int usb3) {
		this.usb3 = usb3;
	}

	public int getUsbc() {
		return usbc;
	}

	public void setUsbc(int usbc) {
		this.usbc = usbc;
	}

	public boolean isRj45() {
		return rj45;
	}

	public void setRj45(boolean rj45) {
		this.rj45 = rj45;
	}

	public boolean isVga() {
		return vga;
	}

	public void setVga(boolean vga) {
		this.vga = vga;
	}

	public boolean isHdmi() {
		return hdmi;
	}

	public void setHdmi(boolean hdmi) {
		this.hdmi = hdmi;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public boolean isCard() {
		return card;
	}

	public void setCard(boolean card) {
		this.card = card;
	}

	public Double getBattery() {
		return battery;
	}

	public void setBattery(Double battery) {
		this.battery = battery;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getWarranty() {
		return warranty;
	}

	public void setWarranty(int warranty) {
		this.warranty = warranty;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(Double userPrice) {
		this.userPrice = userPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStockInfo() {
		return stockInfo;
	}

	public void setStockInfo(String stockInfo) {
		this.stockInfo = stockInfo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	
}
