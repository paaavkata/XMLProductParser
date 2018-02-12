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
	
//	@Autowired
//	private MagentoProductMaker maker;
//	
	public String parseXML(ArrayList<Element> products) {
		String result = new String();
		HashMap<String, String> stantekProperties = propertiesLoader.getStantek();
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
				if(values.containsKey(stantekProperties.get("partitionN"))){
					laptop.setSku(values.remove(stantekProperties.get("partitionN")));
				}
				if(values.containsKey(stantekProperties.get("partN")) && laptop.getSku() == null){
					laptop.setSku(values.remove(stantekProperties.get("partN")));
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
				if(values.containsKey(stantekProperties.get("weight"))){
					laptop.setWeight(values.remove(stantekProperties.get("weight")));
				}
				if(values.containsKey(stantekProperties.get("weight2")) && laptop.getWeight() == null){
					laptop.setWeight(values.remove(stantekProperties.get("weight2")));
				}
				if(laptop.getWeight().equals("")){
					laptop.setWeight("2");
				}
				//BATTERY
				String temp = new String();
				if(values.containsKey(stantekProperties.get("battery"))){
					temp = values.remove(stantekProperties.get("battery").trim());
				}
				if(values.containsKey(stantekProperties.get("battery2"))){
					temp = temp + " " + values.remove(stantekProperties.get("battery2"));
				}
				if(values.containsKey(stantekProperties.get("batteryCell"))){
					temp = temp + " " +  values.remove(stantekProperties.get("batteryCell"));
				}
				if(values.containsKey(stantekProperties.get("batteryLife"))){
					temp = temp + " " +  values.remove(stantekProperties.get("batteryLife"));
				}
				if(values.containsKey(stantekProperties.get("batteryLife2"))){
					temp = temp + " " +  values.remove(stantekProperties.get("batteryLife2"));
				}
				laptop.setBattery(temp.trim());
				//AUDIO AND SOUND
				if(values.containsKey(stantekProperties.get("audio"))){
					laptop.setAudio(values.remove(stantekProperties.get("audio")));
				}
				if(values.containsKey(stantekProperties.get("sound")) && laptop.getAudio().equals("")){
					laptop.setAudio(values.remove(stantekProperties.get("sound")));
				}
				if(laptop.getAudio().equals("")){
					laptop.setAudio("Audio Combo Jack");
				}
				//CONNECTIVITY
				temp = new String();
				if(values.containsKey(stantekProperties.get("3g"))){
					temp = "3G:" + values.remove(stantekProperties.get("3g"));
				}
				if(values.containsKey(stantekProperties.get("4g"))){
					temp = temp + " 4G:" + values.remove(stantekProperties.get("4g"));
				}
				if(values.containsKey(stantekProperties.get("4glte"))){
					temp = temp + " 4G/LTE:" +  values.remove(stantekProperties.get("4glte"));
				}
				laptop.setConnectivity(temp);
				//YES/NO FILTERS
				temp = new String();
				temp = values.remove(stantekProperties.get("bluetooth"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setBluetooth(false);
					}
					else{
						laptop.setBluetooth(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("integratedBluetooth"));
				if(temp != null && !laptop.getBluetooth()){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setBluetooth(false);
					}
					else{
						laptop.setBluetooth(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("camera2"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setWebcam(false);
					}
					else{
						laptop.setWebcam(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("integratedCamera"));
				if(temp != null && !laptop.isWebcam()){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setWebcam(false);
					}
					else{
						laptop.setWebcam(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("cardReader")) + values.remove(stantekProperties.get("cardReader2"));
				if(temp != null){
					if(temp.toLowerCase().contains("no") || temp.toLowerCase().contains("не")){
						laptop.setCardReader(false);
					}
					else{
						laptop.setCardReader(true);
					}
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("usb3"));
				if(temp != null){
					laptop.setUsb3(true);
				}
				else{
					laptop.setUsb3(false);
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("sensorScreen"));
				if(temp != null){
					laptop.setSensorScreen(true);
				}
				else{
					laptop.setSensorScreen(false);
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("keyboard"));
				if(temp != null && temp.contains("acklit")){
					laptop.setKeyboardBacklit(true);
				}
				else{
					laptop.setKeyboardBacklit(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.get("rj45"));
				if(temp != null && temp.contains("es")){
					laptop.setRj45(true);
				}
				else{
					laptop.setRj45(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.get("oneLink"));
				if(temp != null && temp.contains("es")){
					laptop.setOneLink(true);
				}
				else{
					laptop.setOneLink(false);
				}
				temp = new String();
				temp = values.get(stantekProperties.get("fingerPrint"));
				if(temp != null && temp.contains("es")){
					laptop.setFingerPrint(true);
				}
				else{
					temp = new String();
					temp = values.get(stantekProperties.get("fingerPrint2"));
					if(temp != null && temp.contains("es")){
						laptop.setFingerPrint(true);
					}
					else{
						laptop.setFingerPrint(false);
					}
				}
				temp = new String();
				temp = values.get(stantekProperties.get("hddType"));
				if(temp != null && temp.contains("SSD")){
					laptop.setSsd(true);
				}
				else{
					laptop.setSsd(false);
				}
				//COLOR
				laptop.setColor(values.remove(stantekProperties.get("color")));
				//CPU AND FILTER
				if(values.containsKey(stantekProperties.get("cpu2"))){
					laptop.setCpu(values.remove(stantekProperties.get("cpu2")));
				}
				if(values.containsKey(stantekProperties.get("cpuName")) && laptop.getCpu() == null){
					laptop.setCpu(values.remove(stantekProperties.get("cpuName")));
				}
				if(values.containsKey(stantekProperties.get("cpuSeries"))){
					laptop.setCpuFilter(values.remove(stantekProperties.get("cpuSeries")));
				}
				if(laptop.getCpuFilter().equals("")){
					laptop.setCpuFilter(laptop.getCpu());
				}
				if(values.containsKey(stantekProperties.get("cpu2")) && laptop.getCpuFilter() == null){
					laptop.setCpuFilter(values.remove(stantekProperties.get("cpu2")));
				}
				//DIMENSIONS
				if(values.containsKey(stantekProperties.get("dimensions"))){
					laptop.setDimensions(values.remove(stantekProperties.get("dimensions")));
				}
				if(values.containsKey(stantekProperties.get("productDimensions")) && laptop.getDimensions() == null){
					laptop.setDimensions(values.remove(stantekProperties.get("productDimensions")));
				}
				
				//SCREEN RESOLUTION, SIZE, INFO
				if(values.containsKey(stantekProperties.get("displayResolution"))){
					laptop.setDisplayResolution(values.remove(stantekProperties.get("displayResolution")));
				}
				if(values.containsKey(stantekProperties.get("screenResolution")) && laptop.getDisplayResolution() == null){
					laptop.setDisplayResolution(values.remove(stantekProperties.get("screenResolution")));
				}
				if(values.containsKey(stantekProperties.get("displaySize"))){
					temp = values.get(stantekProperties.get("displaySize"));
					laptop.setDisplaySize(values.remove(stantekProperties.get("displaySize")));
				}
				if(values.containsKey(stantekProperties.get("displaySize2")) && laptop.getDisplaySize().equals("")){
					temp = values.get(stantekProperties.get("displaySize2"));
					laptop.setDisplaySize(values.remove(stantekProperties.get("displaySize2")));
				}
				if(values.containsKey(stantekProperties.get("displayType"))){
					laptop.setDisplayInfo(values.remove(stantekProperties.get("displayType")));
				}
				if(values.containsKey(stantekProperties.get("displayType2")) && laptop.getDisplayInfo().equals("")){
					laptop.setDisplayInfo(values.remove(stantekProperties.get("displayType2")));
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
				if(values.containsKey(stantekProperties.get("graphicsName"))){
					laptop.setGpu((values.remove(stantekProperties.get("graphicsName"))));
				}
				if(values.containsKey(stantekProperties.get("graphics")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.get("graphics")));
				}
				if(values.containsKey(stantekProperties.get("graphics2")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.get("graphics2")));
				}
				if(values.containsKey(stantekProperties.get("graphicsMB"))){
					laptop.setGpuMemory((values.remove(stantekProperties.get("graphicsMB"))));
				}
				if(laptop.getGpu().equals("")){
					laptop.setGpu("No info");
				}
				if(laptop.getGpuMemory().equals("")){
					laptop.setGpuMemory("No info");
				}
				//Optical
				if(values.containsKey(stantekProperties.get("optical"))){
					laptop.setOptical(values.remove(stantekProperties.get("optical")));
				}
				else{
					laptop.setOptical("X");
				}
				//RAM and Info
				if(values.containsKey(stantekProperties.get("memoryMB"))){
					laptop.setMemoryRam(values.remove(stantekProperties.get("memoryMB")));
				}
				if(values.containsKey(stantekProperties.get("memoryType"))){
					laptop.setMemoryInfo(values.remove(stantekProperties.get("memoryType")));
				}
				if(values.containsKey(stantekProperties.get("memoryType2")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo(values.remove(stantekProperties.get("memoryType")));
				}
				if(values.containsKey(stantekProperties.get("memoryType3")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo(values.remove(stantekProperties.get("memoryType3")));
				}
				if(values.containsKey(stantekProperties.get("memory")) && laptop.getMemoryRam().equals("")){
					laptop.setMemoryRam(values.remove(stantekProperties.get("memory")));
				}
				if(laptop.getMemoryRam().equals(" GB") || laptop.getMemoryRam().equals("0 GB") || laptop.getMemoryRam().equals("")){
					laptop.setMemoryRam("4 GB");
				}
				if(laptop.getMemoryInfo().equals("")){
					laptop.setMemoryInfo("No Info");
				}
				//WIFI
				if(values.containsKey(stantekProperties.get("wireless"))){
					laptop.setWifi(values.remove(stantekProperties.get("wireless")));
				}
				if(values.containsKey(stantekProperties.get("wifiStandart")) && laptop.getWifi().equals("")){
					laptop.setWifi(values.remove(stantekProperties.get("wifiStandart")));
				}
				if(values.containsKey(stantekProperties.get("wifi")) && laptop.getWifi().equals("")){
					laptop.setWifi(values.remove(stantekProperties.get("wifi")));
				}
				if(laptop.getWifi().equals("")){
					laptop.setWifi("No Info");
				}
				//WARRANTY
				if(values.containsKey(stantekProperties.get("warranty2"))){
					laptop.setWarranty(values.remove(stantekProperties.get("warranty2")));
				}
				if(values.containsKey(stantekProperties.get("warranty2")) && laptop.getWarranty().equals("")){
					laptop.setWarranty(values.remove(stantekProperties.get("warranty2")));
				}
				if(laptop.getWarranty().equals("")){
					laptop.setWarranty("24 месеца");
				}
				//OS
				if(values.containsKey(stantekProperties.get("os"))){
					laptop.setOsFilter(values.remove(stantekProperties.get("os")));
				}
				if(values.containsKey(stantekProperties.get("os2")) && laptop.getOsFilter().equals("")){
					laptop.setOsFilter(values.remove(stantekProperties.get("os2")));
				}
				if(values.containsKey(stantekProperties.get("osName")) && laptop.getOsFilter().equals("")){
					laptop.setOsFilter(values.remove(stantekProperties.get("osName")));
				}
				if(laptop.getOsFilter().equals("")){
					laptop.setOsFilter("DOS");
				}
				//PORTS
				temp = "";
				if(values.containsKey(stantekProperties.get("usb"))){
					temp = values.remove(stantekProperties.get("usb"));
				}
				if(values.containsKey(stantekProperties.get("hdmiPort"))){
					temp = temp + " " + stantekProperties.get("hdmiPort") + ": " + values.remove(stantekProperties.get("hdmiPort"));
				}
				if(values.containsKey(stantekProperties.get("rj45"))){
					temp = temp + " " + stantekProperties.get("rj45") + ": " + values.remove(stantekProperties.get("rj45"));
				}
				if(values.containsKey(stantekProperties.get("rj11"))){
					temp = temp + " " + stantekProperties.get("rj11") + ": " + values.remove(stantekProperties.get("rj11"));
				}
				if(values.containsKey(stantekProperties.get("ieee1394FireWire"))){
					temp = temp + " " + stantekProperties.get("ieee1394FireWire") + ": " + values.remove(stantekProperties.get("ieee1394FireWire"));
				}
				if(values.containsKey(stantekProperties.get("mic-spk"))){
					temp = temp + " " + stantekProperties.get("mic-spk") + ": " + values.remove(stantekProperties.get("mic-spk"));
				}
				if(values.containsKey(stantekProperties.get("miniDisplayPort"))){
					temp = temp + " " + stantekProperties.get("miniDisplayPort") + ": " + values.remove(stantekProperties.get("miniDisplayPort"));
				}
				if(values.containsKey(stantekProperties.get("miniVga"))){
					temp = temp + " " + stantekProperties.get("miniVga") + ": " + values.remove(stantekProperties.get("miniVga"));
				}
				if(values.containsKey(stantekProperties.get("miniHdmi"))){
					temp = temp + " " + stantekProperties.get("miniHdmi") + ": " + values.remove(stantekProperties.get("miniHdmi"));
				}
				if(values.containsKey(stantekProperties.get("oneLink"))){
					temp = temp + " " + stantekProperties.get("oneLink") + ": " + values.remove(stantekProperties.get("oneLink"));
				}
				if(values.containsKey(stantekProperties.get("parallelPort"))){
					temp = temp + " " + stantekProperties.get("parallelPort") + ": " + values.remove(stantekProperties.get("parallelPort"));
				}
				laptop.setPorts(temp);
				
				//HDD and HDD Filter
				if(values.containsKey(stantekProperties.get("hdd"))){
					laptop.setHdd(values.remove(stantekProperties.get("hdd")));	
					System.out.println("from hdd " + laptop.getHdd());
				}
				if(values.containsKey(stantekProperties.get("hddType")) && laptop.getHdd() == null){
					laptop.setHdd(values.remove(stantekProperties.get("hddType")));
					System.out.println("from hddType " + laptop.getHdd());
				}
				temp = new String();
				temp = values.remove(stantekProperties.get("hddSize"));
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
				if(laptop.getHddSize().equals("") && values.containsKey(stantekProperties.get("hddGB"))){
					temp = values.remove(stantekProperties.get("hddGB"));
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
