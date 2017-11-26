package com.premiummobile.First.stantek;

import java.util.ArrayList;

public class Laptop {
	
	private long id;
	private String name;
	private String vendor;
	private String brand;
	private String model;
	private String sku;
	private String productId;
	private String battery;
	private String batteryFilter;
	private String dimensions;
	private String weight;
	private String cpu;
	private String cpuFilter;
	private String gpu;
	private String gpuMemory;
	private String memoryRam;
	private String memoryInfo;
	private String hdd;
	private String hddSize;
	private String displayInfo;
	private String displaySize;
	private String displayResolution;
	private String optical;
	private String wifi;
	private String osFilter;
	private String otherInfo;
	private double price;
	private int qty;
	private ArrayList<String> images;
	private String audio;
	private String connectivity;
	private boolean bluetooth;
	private boolean webcam;
	private boolean ssd;
	private boolean cardReader;
	private boolean fingerPrint;
	private boolean hdmi;
	private boolean oneLink;
	private boolean usb3;
	private boolean rj45;
	private boolean sensorScreen;
	private boolean keyboardBacklit;
	private String chipset;
	private String color;
	
	public Laptop(){
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getBatteryFilter() {
		return batteryFilter;
	}

	public void setBatteryFilter(String batteryFilter) {
		this.batteryFilter = batteryFilter;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getCpuFilter() {
		return cpuFilter;
	}

	public void setCpuFilter(String cpuFilter) {
		this.cpuFilter = cpuFilter;
	}

	public String getGpu() {
		return gpu;
	}

	public void setGpu(String gpu) {
		this.gpu = gpu;
	}

	public String getGpuMemory() {
		return gpuMemory;
	}

	public void setGpuMemory(String gpuMemory) {
		this.gpuMemory = gpuMemory;
	}

	public String getMemory() {
		return memoryRam;
	}

	public void setMemory(String memory) {
		this.memoryRam = memory;
	}

	public String getMemoryType() {
		return memoryInfo;
	}

	public void setMemoryType(String memoryType) {
		this.memoryInfo = memoryType;
	}

	public String getHdd() {
		return hdd;
	}

	public void setHdd(String hdd) {
		this.hdd = hdd;
	}

	public String getHddSize() {
		return hddSize;
	}

	public void setHddSize(String hddSize) {
		this.hddSize = hddSize;
	}

	public String getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(String displayInfo) {
		this.displayInfo = displayInfo;
	}

	public String getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public String getDisplayResolution() {
		return displayResolution;
	}

	public void setDisplayResolution(String displayResolution) {
		this.displayResolution = displayResolution;
	}

	public String getOptical() {
		return optical;
	}

	public void setOptical(String optical) {
		this.optical = optical;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public boolean getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public void setAudio(String audio) {
		this.audio = audio;
		
	}
	public String getAudio() {
		return audio;
	}

	public void setConnectivity(String connectivity) {
		this.connectivity = connectivity;
	}
	
	public String getConnectivity() {
		return connectivity;
	}

	public boolean isWebcam() {
		return webcam;
	}

	public void setWebcam(boolean webcam) {
		this.webcam = webcam;
	}

	public boolean isSsd() {
		return ssd;
	}

	public void setSsd(boolean ssd) {
		this.ssd = ssd;
	}

	public boolean isCardReader() {
		return cardReader;
	}

	public void setCardReader(boolean cardReader) {
		this.cardReader = cardReader;
	}

	public boolean isFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(boolean fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public boolean isHdmi() {
		return hdmi;
	}

	public void setHdmi(boolean hdmi) {
		this.hdmi = hdmi;
	}

	public boolean isOneLink() {
		return oneLink;
	}

	public void setOneLink(boolean oneLink) {
		this.oneLink = oneLink;
	}

	public boolean isUsb3() {
		return usb3;
	}

	public void setUsb3(boolean usb3) {
		this.usb3 = usb3;
	}

	public boolean isRj45() {
		return rj45;
	}

	public void setRj45(boolean rj45) {
		this.rj45 = rj45;
	}

	public boolean isSensorScreen() {
		return sensorScreen;
	}

	public void setSensorScreen(boolean sensorScreen) {
		this.sensorScreen = sensorScreen;
	}

	public boolean isKeyboardBacklit() {
		return keyboardBacklit;
	}

	public void setKeyboardBacklit(boolean keyboardBacklit) {
		this.keyboardBacklit = keyboardBacklit;
	}

	public String getOsFilter() {
		return osFilter;
	}

	public void setOsFilter(String osFilter) {
		this.osFilter = osFilter;
	}

	public void setChipset(String chipset) {
		this.chipset = chipset;
	}
	
	public String getChipset() {
		return chipset;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
}