package com.premiummobile.First.stantek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.premiummobile.First.util.PropertiesLoader;

@Component
public class StantekLaptopsBuilder {
	
	@Autowired
	private PropertiesLoader propertiesLoader;
	
	@Autowired
	private CsvMaker csvMaker;
	
	@Autowired
	private MagentoProductMaker maker;
	
	public String parseXML(ArrayList<Element> products) {
		String result = new String();
		final Properties stantekProperties = propertiesLoader.getStantek();
		try {
			HashSet<Laptop> laptops = new HashSet<Laptop>();
			for (Element e : products) {
				String name = e.getElementsByTagName("Title").item(0).getTextContent();
				
				String description = e.getElementsByTagName("Description").item(0).getTextContent();
				Double clientPrice = Double.parseDouble(e.getElementsByTagName("RetailPriceWithVAT").item(0).getTextContent());
//				Double myPrice = Double.parseDouble(e.getElementsByTagName("YourPriceWithoutVAT").item(0).getTextContent());
				HashMap<String, String> values = getValues(description);
				Laptop laptop = new Laptop();
				laptop.setPrice(clientPrice);
				StringBuilder st = new StringBuilder();
				StringBuilder st1 = new StringBuilder();
				if(values.containsKey(stantekProperties.getProperty("partitionN"))){
					laptop.setSku(values.remove(stantekProperties.getProperty("partitionN")));
				}
				if(values.containsKey(stantekProperties.getProperty("partN")) && laptop.getSku() == null){
					laptop.setSku(values.remove(stantekProperties.getProperty("partN")));
				}
				
				if(laptop.getSku() == null){
					for(int i = name.length()-1; i >= name.length()-40; i--){
						if(name.charAt(i) == ' '){
							break;
						}
						st.append(name.charAt(i));
					}
					String sku = st.reverse().toString();
					if(name.length() > 40){
						name = name.substring(0,name.length()-sku.length()-1);
					}
					st = new StringBuilder();
					for(int i = 0; i < sku.length(); i++){
						if(sku.charAt(i) == '_'){
							break;
						}
						st.append(sku.charAt(i));
					}
					laptop.setSku(st.toString());
				}
				st = new StringBuilder();
				if(!name.isEmpty()){
					for(int i = 0; i < name.length(); i++){
						if(name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == '(' || name.charAt(i) == (char) 047 ){
							break;
						}
						st.append(name.charAt(i));
					}
				}
				name = st.toString();
				name = name.replaceAll("&quot", "");
				int spaces = 0;
				st = new StringBuilder();
				for(int i = 0; i < name.length(); i++){
					if(name.charAt(i) == ' '){
						boolean shouldBrake = false;
						if(spaces > 3){
							for(int j = i; j < name.length(); j++){
								if(name.charAt(j) == ' '){
									break;
								}
								if(j > 10){ 
									shouldBrake = true;
								}
							}
						}
						spaces++;
						if(spaces == 5 || shouldBrake){
							break;
						}
					}
					st.append(name.charAt(i));
				}
				String newName = st.toString();
				laptop.setName(newName);
				StringBuilder st2 = new StringBuilder();
				for(int i = 0; i < newName.length(); i++) {
					if(newName.charAt(i) == ' ' || newName.charAt(i) == (char) 92 || newName.charAt(i) == (char) 47) {
						st2.append("-");
						continue;
					}
					st2.append(newName.charAt(i));
				}
				st2.append("-top-cena-na-izplashtane");
				laptop.setUrl(st2.toString());
				st = new StringBuilder();
				if(!name.substring(0, 4).equals("Hewl")){
					for(int i = 0; i <= name.length(); i++){
						if(name.charAt(i) == ' '){
							break;
						}
						st.append(name.charAt(i));
					}
					laptop.setBrand(st.toString());
				}
				else{
					laptop.setBrand("HP");
				}
				//IMAGES
				String image = e.getElementsByTagName("Image").item(0).getTextContent();
				if(!image.isEmpty()){
					if(laptop.getImages() == null){
						laptop.setImages(new ArrayList<String>());
						laptop.getImages().add(image);
					}
				}
				
				laptop.setQty(5);
				//WEIGHT
				if(values.containsKey(stantekProperties.getProperty("weight"))){
					laptop.setWeight(values.remove(stantekProperties.getProperty("weight")));
				}
				if(values.containsKey(stantekProperties.getProperty("weight2")) && laptop.getWeight() == null){
					laptop.setWeight(values.remove(stantekProperties.getProperty("weight2")));
				}
				if(laptop.getWeight().equals("")){
					laptop.setWeight("2");
				}
				//BATTERY
				String temp = new String();
				if(values.containsKey(stantekProperties.getProperty("battery"))){
					temp = values.remove(stantekProperties.getProperty("battery").trim());
				}
				if(values.containsKey(stantekProperties.getProperty("battery2"))){
					temp = temp + " " + values.remove(stantekProperties.getProperty("battery2"));
				}
				if(values.containsKey(stantekProperties.getProperty("batteryCell"))){
					temp = temp + " " +  values.remove(stantekProperties.getProperty("batteryCell"));
				}
				if(values.containsKey(stantekProperties.getProperty("batteryLife"))){
					temp = temp + " " +  values.remove(stantekProperties.getProperty("batteryLife"));
				}
				if(values.containsKey(stantekProperties.getProperty("batteryLife2"))){
					temp = temp + " " +  values.remove(stantekProperties.getProperty("batteryLife2"));
				}
				laptop.setBattery(temp.trim());
				//AUDIO AND SOUND
				if(values.containsKey(stantekProperties.getProperty("audio"))){
					laptop.setAudio(values.remove(stantekProperties.getProperty("audio")));
				}
				if(values.containsKey(stantekProperties.getProperty("sound")) && laptop.getAudio().equals("")){
					laptop.setAudio(values.remove(stantekProperties.getProperty("sound")));
				}
				if(laptop.getAudio().equals("")){
					laptop.setAudio("Audio Combo Jack");
				}
				//CONNECTIVITY
				temp = new String();
				if(values.containsKey(stantekProperties.getProperty("3g"))){
					temp = "3G:" + values.remove(stantekProperties.getProperty("3g"));
				}
				if(values.containsKey(stantekProperties.getProperty("4g"))){
					temp = temp + " 4G:" + values.remove(stantekProperties.getProperty("4g"));
				}
				if(values.containsKey(stantekProperties.getProperty("4glte"))){
					temp = temp + " 4G/LTE:" +  values.remove(stantekProperties.getProperty("4glte"));
				}
				laptop.setConnectivity(temp);
				//YES/NO FILTERS
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("bluetooth"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setBluetooth(false);
					}
					else{
						laptop.setBluetooth(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("integratedBluetooth"));
				if(temp != null && !laptop.getBluetooth()){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setBluetooth(false);
					}
					else{
						laptop.setBluetooth(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("camera2"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setWebcam(false);
					}
					else{
						laptop.setWebcam(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("integratedCamera"));
				if(temp != null && !laptop.isWebcam()){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setWebcam(false);
					}
					else{
						laptop.setWebcam(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("cardReader")) + values.remove(stantekProperties.getProperty("cardReader2"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setCardReader(false);
					}
					else{
						laptop.setCardReader(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("usb3"));
				if(temp != null){
					laptop.setUsb3(true);
				}
				else{
					laptop.setUsb3(false);
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("sensorScreen"));
				if(temp != null){
					laptop.setSensorScreen(true);
				}
				else{
					laptop.setSensorScreen(false);
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("keyboard"));
				if(temp != null && temp.contains("acklit")){
					laptop.setKeyboardBacklit(true);
				}
				else{
					laptop.setKeyboardBacklit(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.getProperty("rj45"));
				if(temp != null && temp.contains("es")){
					laptop.setRj45(true);
				}
				else{
					laptop.setRj45(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.getProperty("oneLink"));
				if(temp != null && temp.contains("es")){
					laptop.setOneLink(true);
				}
				else{
					laptop.setOneLink(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.getProperty("fingerPrint"));
				if(temp != null && temp.contains("es")){
					laptop.setFingerPrint(true);
				}
				else{
					temp = new String();
					temp = values.get(stantekProperties.getProperty("fingerPrint2"));
					if(temp != null && temp.contains("es")){
						laptop.setFingerPrint(true);
					}
					else{
						laptop.setFingerPrint(false);
					}
				}
				temp = new String();
				temp = values.get(stantekProperties.getProperty("hddType"));
				if(temp != null && temp.contains("SSD")){
					laptop.setSsd(true);
				}
				else{
					laptop.setSsd(false);
				}
				//COLOR
				laptop.setColor(values.remove(stantekProperties.getProperty("color")));
				//CPU AND FILTER
				if(values.containsKey(stantekProperties.getProperty("cpu2"))){
					laptop.setCpu(values.remove(stantekProperties.getProperty("cpu2")));
				}
				if(values.containsKey(stantekProperties.getProperty("cpuName")) && laptop.getCpu() == null){
					laptop.setCpu(values.remove(stantekProperties.getProperty("cpuName")));
				}
				if(values.containsKey(stantekProperties.getProperty("cpuSeries"))){
					laptop.setCpuFilter(values.remove(stantekProperties.getProperty("cpuSeries")));
				}
				if(laptop.getCpuFilter().equals("")){
					laptop.setCpuFilter(laptop.getCpu());
				}
				if(values.containsKey(stantekProperties.getProperty("cpu2")) && laptop.getCpuFilter() == null){
					laptop.setCpuFilter(values.remove(stantekProperties.getProperty("cpu2")));
				}
				//DIMENSIONS
				if(values.containsKey(stantekProperties.getProperty("dimensions"))){
					laptop.setDimensions(values.remove(stantekProperties.getProperty("dimensions")));
				}
				if(values.containsKey(stantekProperties.getProperty("productDimensions")) && laptop.getDimensions() == null){
					laptop.setDimensions(values.remove(stantekProperties.getProperty("productDimensions")));
				}
				
				//SCREEN RESOLUTION, SIZE, INFO
				if(values.containsKey(stantekProperties.getProperty("displayResolution"))){
					laptop.setDisplayResolution(values.remove(stantekProperties.getProperty("displayResolution")));
				}
				if(values.containsKey(stantekProperties.getProperty("screenResolution")) && laptop.getDisplayResolution() == null){
					laptop.setDisplayResolution(values.remove(stantekProperties.getProperty("screenResolution")));
				}
				if(values.containsKey(stantekProperties.getProperty("displaySize"))){
					temp = values.get(stantekProperties.getProperty("displaySize"));
					laptop.setDisplaySize(values.remove(stantekProperties.getProperty("displaySize")));
				}
				if(values.containsKey(stantekProperties.getProperty("displaySize2")) && laptop.getDisplaySize().equals("")){
					temp = values.get(stantekProperties.getProperty("displaySize2"));
					laptop.setDisplaySize(values.remove(stantekProperties.getProperty("displaySize2")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType"))){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType2")) && laptop.getDisplayInfo().equals("")){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType2")));
				}
				if(laptop.getDisplaySize().equals("")){
					laptop.setDisplaySize("15.6");
				}
				if(laptop.getDisplayResolution().equals("")){
					st = new StringBuilder();
					if(temp != null){
						int starter = 0;
						if(temp.charAt(0) == '1'){
							if(temp.charAt(4) == '&'){
								if(temp.charAt(10) == '&'){
									starter = 15;
								}
								else{
									starter = 9;
								}
							}
							else{
								if(temp.charAt(5) == '”'){
									starter = 6;
								}
								else{
									starter = 4;
								}
							}
						}
						for(int i = starter; i < temp.length(); i++){
							if(temp.charAt(i) == '('){
								i++;
								st2 = new StringBuilder();
								for(int j = i; i < temp.length(); j++){
									if(temp.charAt(j) == ')'){
										break;
									}
									st2.append(temp.charAt(j));
									i++;
								}
								if(laptop.getDisplayResolution().equals("")){
									laptop.setDisplayResolution(st2.toString());
								}
								i+=2;
							}
							if(i >= temp.length()-1){
								break;
							}
							st.append(temp.charAt(i));
						}
					}
					if(laptop.getDisplayInfo().equals("")){
						laptop.setDisplayInfo(st.toString().replaceAll("”", "") );
					}
				}
				if(laptop.getDisplayResolution().equals("")){
					laptop.setDisplayResolution("1366x768");
				}
				//VIDEO
				if(values.containsKey(stantekProperties.getProperty("graphicsName"))){
					laptop.setGpu((values.remove(stantekProperties.getProperty("graphicsName"))));
				}
				if(values.containsKey(stantekProperties.getProperty("graphics")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.getProperty("graphics")));
				}
				if(values.containsKey(stantekProperties.getProperty("graphics2")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.getProperty("graphics2")));
				}
				if(values.containsKey(stantekProperties.getProperty("graphicsMB"))){
					laptop.setGpuMemory((values.remove(stantekProperties.getProperty("graphicsMB"))));
				}
				if(laptop.getGpu().equals("")){
					laptop.setGpu("No info");
				}
				if(laptop.getGpuMemory().equals("")){
					laptop.setGpuMemory("No info");
				}
				//Optical
				if(values.containsKey(stantekProperties.getProperty("optical"))){
					laptop.setOptical(values.remove(stantekProperties.getProperty("optical")));
				}
				else{
					laptop.setOptical("X");
				}
				//RAM and Info
				if(values.containsKey(stantekProperties.getProperty("memoryMB"))){
					laptop.setMemoryRam(values.remove(stantekProperties.getProperty("memoryMB")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType"))){
					laptop.setMemoryInfo(values.remove(stantekProperties.getProperty("memoryType")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType2")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo(values.remove(stantekProperties.getProperty("memoryType")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType3")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo(values.remove(stantekProperties.getProperty("memoryType3")));
				}
				if(values.containsKey(stantekProperties.getProperty("memory")) && laptop.getMemoryRam().equals("")){
					laptop.setMemoryRam(values.remove(stantekProperties.getProperty("memory")));
				}
				if(laptop.getMemoryRam().equals(" GB") || laptop.getMemoryRam().equals("0 GB") || laptop.getMemoryRam().equals("")){
					laptop.setMemoryRam("4 GB");
				}
				if(laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo("No Info");
				}
				//WIFI
				if(values.containsKey(stantekProperties.getProperty("wireless"))){
					laptop.setWifi(values.remove(stantekProperties.getProperty("wireless")));
				}
				if(values.containsKey(stantekProperties.getProperty("wifiStandart")) && laptop.getWifi().equals("")){
					laptop.setWifi(values.remove(stantekProperties.getProperty("wifiStandart")));
				}
				if(values.containsKey(stantekProperties.getProperty("wifi")) && laptop.getWifi().equals("")){
					laptop.setWifi(values.remove(stantekProperties.getProperty("wifi")));
				}
				if(laptop.getWifi().equals("")){
					laptop.setWifi("No Info");
				}
				//WARRANTY
				if(values.containsKey(stantekProperties.getProperty("warranty2"))){
					laptop.setWarranty(values.remove(stantekProperties.getProperty("warranty2")));
				}
				if(values.containsKey(stantekProperties.getProperty("warranty2")) && laptop.getWarranty().equals("")){
					laptop.setWarranty(values.remove(stantekProperties.getProperty("warranty2")));
				}
				if(laptop.getWarranty().equals("")){
					laptop.setWarranty("24 месеца");
				}
				//OS
				if(values.containsKey(stantekProperties.getProperty("os"))){
					laptop.setOsFilter(values.remove(stantekProperties.getProperty("os")));
				}
				if(values.containsKey(stantekProperties.getProperty("os2")) && laptop.getOsFilter().equals("")){
					laptop.setOsFilter(values.remove(stantekProperties.getProperty("os2")));
				}
				if(values.containsKey(stantekProperties.getProperty("osName")) && laptop.getOsFilter().equals("")){
					laptop.setOsFilter(values.remove(stantekProperties.getProperty("osName")));
				}
				if(laptop.getOsFilter().equals("")){
					laptop.setOsFilter("DOS");
				}
				//PORTS
				temp = "";
				if(values.containsKey(stantekProperties.getProperty("usb"))){
					temp = values.remove(stantekProperties.getProperty("usb"));
				}
				if(values.containsKey(stantekProperties.getProperty("hdmiPort"))){
					temp = temp + " " + stantekProperties.getProperty("hdmiPort") + ": " + values.remove(stantekProperties.getProperty("hdmiPort"));
				}
				if(values.containsKey(stantekProperties.getProperty("rj45"))){
					temp = temp + " " + stantekProperties.getProperty("rj45") + ": " + values.remove(stantekProperties.getProperty("rj45"));
				}
				if(values.containsKey(stantekProperties.getProperty("rj11"))){
					temp = temp + " " + stantekProperties.getProperty("rj11") + ": " + values.remove(stantekProperties.getProperty("rj11"));
				}
				if(values.containsKey(stantekProperties.getProperty("ieee1394FireWire"))){
					temp = temp + " " + stantekProperties.getProperty("ieee1394FireWire") + ": " + values.remove(stantekProperties.getProperty("ieee1394FireWire"));
				}
				if(values.containsKey(stantekProperties.getProperty("mic-spk"))){
					temp = temp + " " + stantekProperties.getProperty("mic-spk") + ": " + values.remove(stantekProperties.getProperty("mic-spk"));
				}
				if(values.containsKey(stantekProperties.getProperty("miniDisplayPort"))){
					temp = temp + " " + stantekProperties.getProperty("miniDisplayPort") + ": " + values.remove(stantekProperties.getProperty("miniDisplayPort"));
				}
				if(values.containsKey(stantekProperties.getProperty("miniVga"))){
					temp = temp + " " + stantekProperties.getProperty("miniVga") + ": " + values.remove(stantekProperties.getProperty("miniVga"));
				}
				if(values.containsKey(stantekProperties.getProperty("miniHdmi"))){
					temp = temp + " " + stantekProperties.getProperty("miniHdmi") + ": " + values.remove(stantekProperties.getProperty("miniHdmi"));
				}
				if(values.containsKey(stantekProperties.getProperty("oneLink"))){
					temp = temp + " " + stantekProperties.getProperty("oneLink") + ": " + values.remove(stantekProperties.getProperty("oneLink"));
				}
				if(values.containsKey(stantekProperties.getProperty("parallelPort"))){
					temp = temp + " " + stantekProperties.getProperty("parallelPort") + ": " + values.remove(stantekProperties.getProperty("parallelPort"));
				}
				laptop.setPorts(temp);
				
				//HDD and HDD Filter
				if(values.containsKey(stantekProperties.getProperty("hdd"))){
					laptop.setHdd(values.remove(stantekProperties.getProperty("hdd")));	
					System.out.println("from hdd " + laptop.getHdd());
				}
				if(values.containsKey(stantekProperties.getProperty("hddType")) && laptop.getHdd() == null){
					laptop.setHdd(values.remove(stantekProperties.getProperty("hddType")));
					System.out.println("from hddType " + laptop.getHdd());
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("hddSize"));
				if(temp != null){
					st2 = new StringBuilder();
					for(int i = 0; i < temp.length(); i++){
						if(temp.charAt(i) < 48 || temp.charAt(i) > 57){
							break;
						}
						st2.append(temp.charAt(i));
					}
					if(st2.length() == 1){
						st2.append("000 GB");
					}
					else{
						st2.append(" GB");
					}
					laptop.setHddSize(st2.toString());
				}
				temp = new String();
				if(laptop.getHddSize().equals("") && values.containsKey(stantekProperties.getProperty("hddGB"))){
					temp = values.remove(stantekProperties.getProperty("hddGB"));
					if(temp != null){
						st2 = new StringBuilder();
						for(int i = 0; i < temp.length(); i++){
							if(temp.charAt(i) < 48 || temp.charAt(i) > 57){
								break;
							}
							st2.append(temp.charAt(i));
						}
						if(st2.length() == 1){
							st2.append("000 GB");
						}
						else{
							st2.append(" GB");
						}
						laptop.setHddSize(st2.toString());
					}
				}
				if(laptop.getHddSize().equals("")){
					temp = laptop.getHdd();
					if(!temp.equals("")){
						st2 = new StringBuilder();
						for(int i = 0; i < temp.length(); i++){
							if(temp.charAt(i) < 48 || temp.charAt(i) > 57){
								break;
							}
							st2.append(temp.charAt(i));
						}
						if(st2.length() == 1){
							st2.append("000 GB");
						}
						else{
							st2.append(" GB");
						}
						laptop.setHddSize(st2.toString());
					}
				}
				if(laptop.getHddSize().equals("")){
					laptop.setHddSize("500 GB");
				}
				laptop.setHddFilter(laptop.getHddSize());
				st1 = new StringBuilder();
				for(Entry<String, String> entry : values.entrySet()){
					st1.append(entry.getKey() + ": " + entry.getValue() + "<br>");
				}
				laptop.setOtherInfo(st1.toString());
				laptops.add(laptop);
				
			}
			System.out.println("Laptops: " + laptops.size());
//			result = csvMaker.makeLaptopCSV(laptops);
//			result = maker.uploadProductsToProduction(laptops);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private HashMap<String, String> getValues(String desc){
		HashMap<String, String> values = new HashMap<String, String>();
		for(int i = 0; i < desc.length(); i++){
			if(desc.charAt(i) == '['){
				i++;
				StringBuilder st1 = new StringBuilder();
				while(true){
					if(desc.charAt(i) == ']' || desc.charAt(i) == ':'){
						break;
					}
					st1.append(desc.charAt(i));
					i++;
				}
				i+=2;
				StringBuilder st2 = new StringBuilder();
				while(true){
					if(i == desc.length() || desc.charAt(i) == '\n'){
						break;
					}
					st2.append(desc.charAt(i));
					i++;
				}
				values.put(st1.toString().trim(), st2.toString().trim());
			}
		}
		return values;
	}
}
