package com.premiummobile.First.stantek;

import java.util.ArrayList;

public class Laptop extends LocalProduct{
	
	private String battery;
	private String batteryFilter;
	private String dimensions;
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
	private String audio;
	private String connectivity;
	private String hddFilter;
	private String ports;
	private String color;
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
	private ArrayList<String> images;
	
	
	public Laptop(){
		
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
		cpuFilter = st.toString();
		if(cpuFilter.contains("i7")){
			this.cpuFilter = "Intel Core i7";
		}
		if(cpuFilter.contains("i5")){
			this.cpuFilter = "Intel Core i5";
		}
		if(cpuFilter.contains("i3")){
			this.cpuFilter = "Intel Core i3";
		}
		if(cpuFilter.contains("entium")){
			this.cpuFilter = "Intel Pentium";
		}
		if(cpuFilter.contains("AMD")){
			this.cpuFilter = "AMD";
		}
		if(cpuFilter.contains("eleron")){
			this.cpuFilter = "Intel Celeron";
		}
		if(cpuFilter.contains("eon")){
			this.cpuFilter = "Intel Xeon";
		}
		if(cpuFilter.contains("tom")){
			this.cpuFilter = "Intel Atom";
		}
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
		if(memory != null && !memory.equals("")){
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
				this.memoryRam = String.valueOf(memoryInt/1024) + " GB";
			}
			else {
				this.memoryRam = st.toString() + " GB";
			}
		}
	}

	public String getMemoryInfo() {
		return memoryInfo != null ? memoryInfo : "";
	}

	public void setMemoryInfo(String memoryInfo) {
		this.memoryInfo = memoryInfo;
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
		if(displaySize != null && !displaySize.equals("")){
			if(displaySize.contains("15.6")){
				this.displaySize = "15.6 inch (39.62 cm)";
			}
			if(displaySize.contains("14")){
				this.displaySize = "14.0 inch (35.56 cm)";
			}
			if(displaySize.contains("17")){
				this.displaySize = "17.3 inch (43.94 cm)";
			}
			if(displaySize.contains("12")){
				this.displaySize = "12.0 inch (30.48 cm)";
			}
			if(displaySize.contains("13.3")){
				this.displaySize = "13.3 inch (33.78 cm)";
			}
			if(displaySize.contains("13.0")){
				this.displaySize = "13.0 inch (33.02 cm)";
			}
			if(displaySize.contains("11.6")){
				this.displaySize = "11.6 inch (29.46 cm)";
			}
			if(displaySize.contains("15.4")){
				this.displaySize = "15.4 inch (39.12 cm)";
			}
		}
	}

	public String getDisplayResolution() {
		return displayResolution != null ? displayResolution : "";
	}

	public void setDisplayResolution(String displayResolution) {
		if(displayResolution != null && !displayResolution.equals("")){
			if(displayResolution.contains("1366x768")){
				this.displayResolution = "1366x768";
				return;
			}
			if(displayResolution.contains("1280×720")){
				this.displayResolution = "HD (1280x720)";
				return;
			}
			if(displayResolution.contains("1600x1080")){
				this.displayResolution = "HD+ (1600x1080)";
				return;
			}
			if(displayResolution.contains("1920x1080")){
				this.displayResolution = "Full HD (1920x1080)";
				return;
			}
			if(displayResolution.contains("2560x1440")){
				this.displayResolution = "Quad HD (2560x1440)";
				return;
			}
			if(displayResolution.contains("3200x1800")){
				this.displayResolution = "Quad HD+ (3200x1800)";
				return;
			}
			if(displayResolution.contains("3640x2160")){
				this.displayResolution = "Ultra HD - 4K (3640x2160)";
				return;
			}
			if(displayResolution.contains("2304x1440")){
				this.displayResolution = "Retina (2304x1440)";
				return;
			}
			if(displayResolution.contains("2880x1800")){
				this.displayResolution = "Retina (2880x1800)";
				return;
			}
		}
		this.displayResolution = "1366x768";
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
	
	public void setBrand(String brand) {
		if(brand.contains("enovo") || brand.contains("ENOVO")){
			super.setBrand("Lenovo");
		}
		if(brand.contains("cer")){
			super.setBrand("ACER");
		}
		if(brand.contains("HP")){
			super.setBrand("HP");
		}
		if(brand.contains("ell")){
			super.setBrand("DELL");
		}
		if(brand.contains("sus")){
			super.setBrand("ASUS");
		}
		if(brand.contains("pple")){
			super.setBrand("Apple");
		}
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
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
		if(osFilter.contains("DOS")){
			this.osFilter = "MS-Dos";
			return;
		}
		if(osFilter.contains("Windows")){
			this.osFilter = "Windows 10";
			return;
		}
		if(osFilter.contains("Linux")){
			this.osFilter = "Linux";
			return;
		}
	}

	public void setColor(String color) {
		if(color != null && !color.equals("")){
			if(color.contains("lack")){
				this.color = "Черен";
				return;
			}
			if(color.contains("lue")){
				this.color = "Син";
				return;
			}
			if(color.contains("Red")){
				this.color = "Червен";
				return;
			}
			if(color.contains("hite")){
				this.color = "Бял";
				return;
			}
			if(color.contains("Grey")){
				this.color = "Сив";
				return;
			}
			if(color.contains("ilver")){
				this.color = "Сребрист";
				return;
			}
			if(color.contains("Gold")){
				this.color = "Златист";
				return;
			}
			if(color.contains("Purple")){
				this.color = "Лилав";
				return;
			}
		}
		this.color = "Черен";
	}
	
	public String getColor() {
		return color != null ? color : "";
	}

	public String getPorts() {
		
		return ports;
	}
	
	public void setPorts(String ports) {
		this.ports = ports;
	}
	
	public String getHddFilter() {
		return hddFilter;
	}

	public void setHddFilter(String hddFilter) {
		hddFilter = hddFilter.replaceAll("GB", "").trim();
		if(hddFilter != null && !hddFilter.equals("")){
			int gb = Integer.parseInt(hddFilter);
			if(gb <= 200){
				if(gb <= 5){
					this.hddFilter = "1001-1500 GB";
				}
				else{
					this.hddFilter = "под 200 GB";
				}
				return;
			}
			if(gb > 200 && gb <= 400){
				this.hddFilter = "200-400 GB";
				return;
			}
			if(gb > 400 && gb <= 600){
				this.hddFilter = "401-600 GB";
				return;
			}
			if(gb > 600 && gb <= 800){
				this.hddFilter = "601-800 GB";
				return;
			}
			if(gb > 800 && gb <= 1000){
				this.hddFilter = "801-1000 GB";
				return;
			}
			if(gb > 1000 && gb <= 1500){
				this.hddFilter = "1001-1500 GB";
				return;
			}
			if(gb > 1500){
				this.hddFilter = "над 1500 GB";
				return;
			}
		}
	}
}