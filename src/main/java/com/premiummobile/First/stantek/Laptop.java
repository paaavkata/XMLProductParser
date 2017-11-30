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
	private String warranty;
	private String chipset;
	private String color;
	private String ports;
	private String weightFilter;
	private String hddFilter;
	private String url;
	
	
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
		return battery != null ? battery : "";
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getBatteryFilter() {
		return batteryFilter != null ? batteryFilter : "";
	}

	public void setBatteryFilter(String batteryFilter) {
		this.batteryFilter = batteryFilter;
	}

	public String getDimensions() {
		return dimensions != null ? dimensions : "";
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getWeight() {
		return weight != null ? weight : "";
	}

	public void setWeight(String weight) {
		
		this.weight = weight;
		
	}

	public String getCpu() {
		return cpu != null ? cpu : "";
	}

	public void setCpu(String cpu) {
		this.cpu = cpu.trim();
	}

	public String getCpuFilter() {
		return cpuFilter != null ? cpuFilter : "";
	}

	public void setCpuFilter(String cpuFilter) {
		cpuFilter = cpuFilter.trim();
		StringBuilder st = new StringBuilder();
		int spaces = 0;
		for(int i = 0; i < cpuFilter.length(); i++) {
			if(cpuFilter.charAt(i) == ' ') {
				spaces++;
				if(spaces >= 3) {
					break;
				}
			}
			st.append(cpuFilter.charAt(i));
		}
		this.cpuFilter = st.toString();
	}

	public String getGpu() {
		return gpu != null ? gpu : "";
	}

	public void setGpu(String gpu) {
		this.gpu = gpu;
	}

	public String getGpuMemory() {
		return gpuMemory != null ? gpuMemory : "";
	}

	public void setGpuMemory(String gpuMemory) {
		this.gpuMemory = gpuMemory;
	}

	public String getMemoryRam() {
		return memoryRam != null ? memoryRam : "";
	}

	public void setMemoryRam(String memory) {
		if(memory != null && !memory.equals(""))
		memory = memory.trim();
		StringBuilder st = new StringBuilder();
		for(int i = 0; i < memory.length(); i++) {
			if(memory.charAt(i) > 57 || memory.charAt(i) < 49) {
				break;
			}
			st.append(memory.charAt(i));
		}
		if(st.length() >= 3) {
			int memoryInt = Integer.parseInt(st.toString());
			this.memoryRam = String.valueOf(memoryInt/1024) + "GB";
		}
		else {
			this.memoryRam = st.toString() + "GB";
		}
	}

	public String getMemoryInfo() {
		return memoryInfo != null ? memoryInfo : "";
	}

	public void setMemoryType(String memoryType) {
		this.memoryInfo = memoryType;
	}

	public String getHdd() {
		return hdd != null ? hdd : "";
	}

	public void setHdd(String hdd) {
		this.hdd = hdd;
	}

	public String getHddSize() {
		return hddSize != null ? hddSize : "";
	}

	public void setHddSize(String hddSize) {
		this.hddSize = hddSize;
	}

	public String getDisplayInfo() {
		return displayInfo != null ? displayInfo : "";
	}

	public void setDisplayInfo(String displayInfo) {
		this.displayInfo = displayInfo;
	}

	public String getDisplaySize() {
		return displaySize != null ? displaySize : "";
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public String getDisplayResolution() {
		return displayResolution != null ? displayResolution : "";
	}

	public void setDisplayResolution(String displayResolution) {
		this.displayResolution = displayResolution;
	}

	public String getOptical() {
		return optical != null ? optical : "";
	}

	public void setOptical(String optical) {
		this.optical = optical;
	}

	public String getWifi() {
		return wifi != null ? wifi : "";
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
		return otherInfo != null ? otherInfo : "";
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getBrand() {
		return brand != null ? brand : "";
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
		return audio != null ? audio : "";
	}

	public void setConnectivity(String connectivity) {
		this.connectivity = connectivity;
	}
	
	public String getConnectivity() {
		return connectivity != null ? connectivity : "";
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
		return osFilter != null ? osFilter : "";
	}

	public void setOsFilter(String osFilter) {
		this.osFilter = osFilter;
	}

	public void setChipset(String chipset) {
		
		this.chipset = chipset;
	}
	
	public String getChipset() {
		return chipset != null ? chipset : "";
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color != null ? color : "";
	}

	public String getWarranty() {
		return warranty;
	}
	
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	
	public String getPorts() {
		
		return ports;
	}
	
	public void setPorts(String ports) {
		this.ports = ports;
	}
	
	public String getWeightFilter() {
		return weightFilter;
	}

	public void setWeightFilter(String weightFilter) {
		this.weightFilter = weightFilter;
	}

	public String getHddFilter() {
		return hddFilter;
	}

	public void setHddFilter(String hddFilter) {
		this.hddFilter = hddFilter;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}