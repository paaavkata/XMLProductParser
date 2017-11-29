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
	private StantekCSVMaker csvMaker;
	
	public String parseXML(ArrayList<Element> products) {
		String result = new String();
		final Properties stantekProperties = propertiesLoader.getStantek();
		try {
			HashSet<Laptop> laptops = new HashSet<Laptop>();
			for (Element e : products) {
				String name = e.getElementsByTagName("Title").item(0).getTextContent();
				String image = e.getElementsByTagName("Image").item(0).getTextContent();
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
				laptop.setName(name);
				
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

				if(!image.isEmpty()){
					if(laptop.getImages() == null){
						laptop.setImages(new ArrayList<String>());
						laptop.getImages().add(image);
					}
				}
				
				laptop.setQty(5);
				if(values.containsKey(values.containsKey(stantekProperties.getProperty("weight")))){
					laptop.setBattery(values.remove(stantekProperties.getProperty("weight")).substring(1,4));
				}
				if(values.containsKey(values.containsKey(stantekProperties.getProperty("weight2"))) && laptop.getBattery() == null){
					laptop.setBattery(values.remove(stantekProperties.getProperty("weight2")).substring(1,4));
				}
				if(laptop.getBattery() == null){
					laptop.setBattery("2");
				}
				String temp = new String();
				if(values.containsKey(stantekProperties.getProperty("battery"))){
					temp = values.remove(stantekProperties.getProperty("battery"));
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
				laptop.setBattery(temp);
				
				if(values.containsKey(stantekProperties.getProperty("audio"))){
					laptop.setAudio(values.remove(stantekProperties.getProperty("audio")));
				}
				if(values.containsKey(stantekProperties.getProperty("sound"))){
					laptop.setAudio((laptop.getAudio() != null ? (laptop.getAudio() + " ") : "") + values.remove(stantekProperties.getProperty("sound")));
				}
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
				temp = values.remove(stantekProperties.getProperty("chipset"));
				if(temp != null){
					laptop.setChipset(temp);
				}
				
				laptop.setColor(values.remove(stantekProperties.getProperty("color")));
				if(values.containsKey(stantekProperties.getProperty("cpu2"))){
					laptop.setCpu(values.remove(stantekProperties.getProperty("cpu2")));
				}
				if(values.containsKey(stantekProperties.getProperty("cpuName")) && laptop.getCpu() == null){
					laptop.setCpu(values.remove(stantekProperties.getProperty("cpuName")));
				}
				if(values.containsKey(stantekProperties.getProperty("cpuSeries"))){
					laptop.setCpuFilter(values.remove(stantekProperties.getProperty("cpuSeries")));
				}
				if(values.containsKey(stantekProperties.getProperty("cpu2")) && laptop.getCpuFilter() == null){
					laptop.setCpuFilter(values.remove(stantekProperties.getProperty("cpu2")));
				}
				if(values.containsKey(stantekProperties.getProperty("dimensions"))){
					laptop.setDimensions(values.remove(stantekProperties.getProperty("dimensions")));
				}
				if(values.containsKey(stantekProperties.getProperty("productDimensions")) && laptop.getDimensions() == null){
					laptop.setDimensions(values.remove(stantekProperties.getProperty("productDimensions")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayResolution"))){
					laptop.setDisplayResolution(values.remove(stantekProperties.getProperty("displayResolution")));
				}
				if(values.containsKey(stantekProperties.getProperty("screenResolution")) && laptop.getDisplayResolution() == null){
					laptop.setDisplayResolution(values.remove(stantekProperties.getProperty("screenResolution")));
				}
				if(values.containsKey(stantekProperties.getProperty("displaySize"))){
					laptop.setDisplaySize(values.remove(stantekProperties.getProperty("displaySize")));
				}
				if(values.containsKey(stantekProperties.getProperty("displaySize2")) && laptop.getDisplaySize().equals("")){
					laptop.setDisplaySize(values.remove(stantekProperties.getProperty("displaySize2")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType"))){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType2")) && laptop.getDisplayInfo().equals("")){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType2")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType"))){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType")));
				}
				if(values.containsKey(stantekProperties.getProperty("displayType2")) && laptop.getDisplayInfo().equals("")){
					laptop.setDisplayInfo(values.remove(stantekProperties.getProperty("displayType2")));
				}
				if(values.containsKey(stantekProperties.getProperty("graphics"))){
					laptop.setGpu((values.remove(stantekProperties.getProperty("graphics"))));
				}
				if(values.containsKey(stantekProperties.getProperty("graphics2")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.getProperty("graphics2")));
				}
				if(values.containsKey(stantekProperties.getProperty("graphicsName")) && laptop.getGpu().equals("")){
					laptop.setGpu(values.remove(stantekProperties.getProperty("graphicsName")));
				}
				if(values.containsKey(stantekProperties.getProperty("graphicsMB"))){
					laptop.setGpuMemory((values.remove(stantekProperties.getProperty("graphicsMB"))));
				}
				if(values.containsKey(stantekProperties.getProperty("optical"))){
					laptop.setOptical(values.remove(stantekProperties.getProperty("optical")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryMB"))){
					laptop.setMemoryRam(values.remove(stantekProperties.getProperty("memoryMB")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType"))){
					laptop.setMemoryType(values.remove(stantekProperties.getProperty("memoryType")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType2")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryType(values.remove(stantekProperties.getProperty("memoryType2")));
				}
				if(values.containsKey(stantekProperties.getProperty("memoryType3")) && laptop.getMemoryInfo().equals("")){
					laptop.setMemoryType(values.remove(stantekProperties.getProperty("memoryType3")));
				}
				if(values.containsKey(stantekProperties.getProperty("memory")) && laptop.getMemoryRam().equals("")){
					laptop.setMemoryRam(values.remove(stantekProperties.getProperty("memory")));
				}
				temp = new String();
				temp = values.remove(stantekProperties.getProperty("hddGB"));
				
				if(temp != null){
					StringBuilder st2 = new StringBuilder();
					for(int i = 0; i < temp.length(); i ++ ){
						if(temp.charAt(i) == 'G'){
							break;
						}
						st2.append(temp.charAt(i));
					}
					st2.append("GB");
					laptop.setHddSize(st2.toString());
				}
				temp = new String();
				if(laptop.getHddSize() == null){
					temp = values.remove(stantekProperties.getProperty("hddSize"));
					if(temp != null){
						StringBuilder st2 = new StringBuilder();
						for(int i = 0; i < temp.length(); i ++ ){
							if(temp.charAt(i) == 'G'){
								break;
							}
							st2.append(temp.charAt(i));
						}
						st2.append("GB");
						laptop.setHddSize(st2.toString());
					}
				}
				if(values.containsKey(stantekProperties.getProperty("hdd"))){
					laptop.setHdd(values.remove(stantekProperties.getProperty("hdd")));
				}
				if(values.containsKey(stantekProperties.getProperty("hddType")) && laptop.getHdd() == null){
					laptop.setHdd(values.remove(stantekProperties.getProperty("hddType")));
				}
				if(values.containsKey(stantekProperties.getProperty("hddType2")) && laptop.getHdd() == null){
					laptop.setHdd(values.remove(stantekProperties.getProperty("hddType2")));
				}
				st1 = new StringBuilder();
				for(Entry<String, String> entry : values.entrySet()){
					st1.append(entry.getKey() + ": " + entry.getValue() + "<br>");
				}
				laptop.setOtherInfo(st1.toString());
//				System.out.println("=====================================" +
//						"\nGpu " + laptop.getGpu() +
//						"\nGpuMemory " + laptop.getGpuMemory() +
//						"\nOptical " + laptop.getOptical() +
//						"\nMemory " + laptop.getMemory() +
//						"\nMemoryType: " + laptop.getMemoryType() +
//						"\nCPUFilter " + laptop.getCpuFilter() +
//						"\nHDDSize " + laptop.getHddSize() + 
//						"\nHdd " + laptop.getHdd() + 
//						"\n-----Other Info------\n" + laptop.getOtherInfo());
//				
				laptops.add(laptop);
				
			}
			System.out.println("Laptops: " + laptops.size());
			result = csvMaker.makeLaptopCSV(laptops);
			
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
					values.put(st1.toString(), st2.toString());
				}
			}
		return values;
	}
}
