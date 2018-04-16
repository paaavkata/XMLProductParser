package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.KeyListAttribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.solytron.Model.Property;
import com.premiummobile.First.util.PropertiesLoader;

@Component
public class MagentoMappingHelper {
	
PropertiesLoader loader;
	
	HashMap<String, String> solytronLaptop;
	
	HashMap<String, String> magentoAttributes;
	
	HashMap<String, String> magentoAttributesReversed;
	
	HashMap<String, String> colors;
	
	HashMap<String, String> brands;
	
	@Autowired
	public MagentoMappingHelper(PropertiesLoader loader){
		this.loader = loader;
		this.solytronLaptop = loader.getSolytronLaptop();
		this.brands = loader.getBrands();
		this.colors = loader.getColors();
		this.magentoAttributes = loader.getMagentoAttributes();
		this.magentoAttributesReversed = loader.getMagentoAttributesReversed();
	}
	
	public String generateShortDescription(String displaySize, String cpu, String ram, String hdd, String battery) {
		StringBuilder st = new StringBuilder();
		st.append("<ul class=\"short-description-list smartphone\"><div class=\"row\"><div class=\"col-md-2 col-md-offset-1\"><li class=\"display-size\">");
		st.append(displaySize);
		st.append("</li></div><div class=\"col-md-2\"><li class=\"processor\">");
		st.append(cpu);
		st.append("</li></div><div class=\"col-md-2\"><li class=\"memory\">");
		st.append(ram);
		st.append("</li></div><div class=\"col-md-2\"><li class=\"hdd\">");
		st.append(hdd);
		st.append("</li></div><div class=\"col-md-2\"><li class=\"battery\">");
		st.append(battery);
		st.append("</li></div></div></ul>");
		return st.toString();
	}

	public List<Attribute> generateLaptopAttributes(HashMap<Integer, Property> productProperties) {
		HashMap<Integer, String> properties = new HashMap<Integer, String>();
		for(Property property : productProperties.values()){
			properties.put(property.getPropertyId(), property.getValue().get(0).getText());
		}
		List<Attribute> customAttributes = new ArrayList<Attribute>();
		
		KeyListAttribute hddFilter = new KeyListAttribute();
		hddFilter.setAttributeCode("hdd_razmer_filt_r_laptop");
		hddFilter.setValue(new ArrayList<String>());
		hddFilter.getValue().add(generateHddFilter(properties.get(11)));
		customAttributes.add(hddFilter);
		
		KeyValueAttribute battery = new KeyValueAttribute();
		battery.setAttributeCode("laptop_battery");
		battery.setValue((properties.get(43) != null ? properties.remove(43) : "" + properties.get(44) != null ? " " + properties.remove(44) : ""));
		customAttributes.add(battery);
		
		KeyValueAttribute color = new KeyValueAttribute();
		color.setAttributeCode("color");
		color.setValue(generateColorFilter(properties.remove(71)));
		customAttributes.add(color);
		
		KeyListAttribute cpuFilter = new KeyListAttribute();
		cpuFilter.setAttributeCode("laptop_cpu_filter");
		cpuFilter.setValue(new ArrayList<String>());
		cpuFilter.getValue().add(generateCpuFilter(properties.remove(55), properties.get(2)));
		customAttributes.add(cpuFilter);
		
		KeyValueAttribute dimensions = new KeyValueAttribute();
		dimensions.setAttributeCode("laptop_dimensions");
		dimensions.setValue(properties.remove(47));
		customAttributes.add(dimensions);
		
		KeyValueAttribute displayInfo = new KeyValueAttribute();
		displayInfo.setAttributeCode("laptop_display_info");
		displayInfo.setValue((properties.get(1) + " " + properties.get(9)).trim());
		customAttributes.add(displayInfo);
		
		KeyListAttribute displayResolution = new KeyListAttribute();
		displayResolution.setAttributeCode("laptop_display_resolution");
		displayResolution.setValue(new ArrayList<String>());
		displayResolution.getValue().add(generateDisplayResolution(properties.get(1), properties.remove(70)).trim());
		customAttributes.add(displayResolution);
		
		KeyListAttribute displaySize = new KeyListAttribute();
		displaySize.setAttributeCode("laptop_display_size");
		displaySize.setValue(new ArrayList<String>());
		displaySize.getValue().add(generateDisplaySize(properties.remove(1)));
		customAttributes.add(displaySize);
		
		KeyValueAttribute gpu = new KeyValueAttribute();
		gpu.setAttributeCode("laptop_gpu");
		gpu.setValue(properties.remove(10));
		customAttributes.add(gpu);
		
		KeyValueAttribute hddInfo = new KeyValueAttribute();
		hddInfo.setAttributeCode("laptop_hdd_info");
		hddInfo.setValue(properties.get(11));
		customAttributes.add(hddInfo);
		
		KeyValueAttribute hddSize = new KeyValueAttribute();
		hddSize.setAttributeCode("laptop_hdd_size");
		hddSize.setValue(generateHddSize(properties.get(11)));
		customAttributes.add(hddSize);
		
		KeyValueAttribute optical = new KeyValueAttribute();
		optical.setAttributeCode("laptop_optical");
		optical.setValue(properties.remove(13));
		customAttributes.add(optical);
		
		KeyListAttribute osFilter = new KeyListAttribute();
		osFilter.setAttributeCode("laptop_os_filter");
		osFilter.setValue(new ArrayList<String>());
		osFilter.getValue().add(generateOsFilter(properties.remove(57)));
		properties.remove(3);
		customAttributes.add(osFilter);
		
		KeyValueAttribute processor = new KeyValueAttribute();
		processor.setAttributeCode("laptop_processor");
		processor.setValue(properties.remove(2));
		customAttributes.add(processor);
		
		KeyListAttribute ram = new KeyListAttribute();
		ram.setAttributeCode("laptop_ram");
		ram.setValue(new ArrayList<String>());
		ram.getValue().add(generateRamFilter(properties.remove(5)));
		customAttributes.add(ram);
		
		KeyValueAttribute ramInfo = new KeyValueAttribute();
		ramInfo.setAttributeCode("laptop_ram_info");
		ramInfo.setValue(properties.remove(6));
		customAttributes.add(ramInfo);
		
		KeyValueAttribute weight = new KeyValueAttribute();
		weight.setAttributeCode("laptop_weight");
		weight.setValue(properties.remove(48));
		customAttributes.add(weight);
		
		KeyValueAttribute wifi = new KeyValueAttribute();
		wifi.setAttributeCode("laptop_wifi");
		wifi.setValue(properties.remove(15));
		customAttributes.add(wifi);
		
		KeyValueAttribute warranty = new KeyValueAttribute();
		warranty.setAttributeCode("laptop_warranty");
		warranty.setValue(properties.remove(49));
		customAttributes.add(warranty);
		
		KeyListAttribute yesNo = new KeyListAttribute();
		yesNo.setAttributeCode("laptop_yes_no");
		yesNo.setValue(generateYesNo(properties.remove(17),properties.remove(51),properties.remove(11), properties.remove(74), properties.remove(38),properties.remove(40),
				properties.remove(59),properties.remove(28),properties.remove(62),properties.remove(52),properties.remove(22),properties.remove(9), properties.remove(69)));
		customAttributes.add(yesNo);
		
		
		KeyValueAttribute ports = new KeyValueAttribute();
		ports.setAttributeCode("laptop_ports");
		StringBuilder portsString = new StringBuilder();

		KeyValueAttribute otherInfo = new KeyValueAttribute();
		otherInfo.setAttributeCode("laptop_other_info");
		StringBuilder otherInfoString = new StringBuilder();
		List<Property> productProperties2 = new ArrayList<Property>();
		productProperties2.addAll(productProperties.values());
		for(Property property : productProperties.values()){
			if(property.getPropertyId() == 18){
				properties.remove(18);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(29);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 41){
				properties.remove(41);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 42){
				properties.remove(42);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 53){
				properties.remove(53);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 61){
				properties.remove(61);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 62){
				properties.remove(62);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 24){
				properties.remove(24);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 28){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
		}
		for(Property property : productProperties2){
			if(properties.containsKey(property.getPropertyId())){
				otherInfoString.append(property.getName() + ": " + property.getValue().get(0).getText());
				if(properties.size() != 1){
					otherInfoString.append("; ");
				}
			}
		}
		ports.setValue(portsString.toString());
		customAttributes.add(ports);
		otherInfo.setValue(otherInfoString.toString());
		customAttributes.add(otherInfo);
		
		KeyValueAttribute shortDescription = new KeyValueAttribute();
		shortDescription.setAttributeCode("short_description");
		shortDescription.setValue(generateShortDescription(magentoAttributes.get(displaySize.getValue().get(0)), magentoAttributes.get(cpuFilter.getValue().get(0)), 
				magentoAttributes.get(ram.getValue().get(0)), hddSize.getValue(), battery.getValue()));
		customAttributes.add(shortDescription);
		
		return customAttributes;
	}

	public String generateBrand(String brand){
		if(brand != null && !brand.equals("")){
			for(String string : brands.keySet()){
				if(this.contains(string, brand)){
					return brands.get(string);
				}
			}
		}
		return brands.get("Други");
	}
	
	private String generateHddSize(String string) {
		StringBuilder st = new StringBuilder();
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == ' ' || string.charAt(i) == 'T' || string.charAt(i) == 'G'){
				break;
			}
			st.append(string.charAt(i));
		}
		int gb = Integer.parseInt(st.toString());
		
		if(gb <= 5){
			return gb + " TB";
		}
		else{
			return gb + " GB";
		}
	}

	private String generateOsFilter(String string) {
		if(string != null && !string.equals("")){
			if(this.contains("Windows", string)){
				return magentoAttributesReversed.get("Windows");
			}
			if(this.contains("DOS", string) || this.contains("No OS", string)){
				return magentoAttributesReversed.get("MS-Dos");
			}
			if(this.contains("Mac", string)){
				return magentoAttributesReversed.get("Mac OS");
			}
			if(this.contains("Linux", string)){
				return magentoAttributesReversed.get("Linux");
			}
		}
		return magentoAttributesReversed.get("MS-Dos");
	}
	
	private String generateRamFilter(String memory){
		if(memory != null && !memory.equals("")){
			memory = memory.trim();
			StringBuilder st = new StringBuilder();
			for(int i = 0; i < memory.length(); i++) {
				if(memory.charAt(i) == ' ' || memory.charAt(i) == 'G') {
					break;
				}
				if(memory.charAt(i) == 'x'){
					String multiplyier = String.valueOf(memory.charAt(i-1));
					String ram = new String();
					for(int j = i + 1; j < memory.length(); j++) {
						if(memory.charAt(j) > 57 || memory.charAt(j) < 48){
							break;
						}
						ram = ram + memory.charAt(j);
					}
					return Integer.valueOf(multiplyier)*Integer.valueOf(ram) + " GB";
				}
				st.append(memory.charAt(i));
			}
			return magentoAttributesReversed.get(st.toString() + " GB");
		}
		return magentoAttributesReversed.get("4 GB");
	}

	private String generateColorFilter(String color) {
		if(color != null && !color.equals("")){
			for(String string : colors.keySet()){
				if(this.contains(string, color)){
					return colors.get(string);
				}
			}
		}
		return colors.get("Black");
	}
	
	private boolean contains(String string, String contains){
		return contains.toLowerCase().contains(string.toLowerCase());
//		return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(string).find();
		
	}

	private String generateCpuFilter(String cpuFilter, String cpuFilter2) {
		if(cpuFilter == null || cpuFilter.equals("")){
			cpuFilter = cpuFilter2;
		}
		if(this.contains("i7", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i7");
		}
		if(this.contains("i5", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i5");
		}
		if(this.contains("i3", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i3");
		}
		if(this.contains("entium", cpuFilter)){
			return magentoAttributesReversed.get("Intel Pentium");
		}
		if(this.contains("AMD", cpuFilter)){
			return magentoAttributesReversed.get("AMD");
		}
		if(this.contains("eleron", cpuFilter)){
			return magentoAttributesReversed.get("Intel Celeron");
		}
		if(this.contains("eon", cpuFilter)){
			return magentoAttributesReversed.get("Intel Xeon");
		}
		if(this.contains("tom", cpuFilter)){
			return magentoAttributesReversed.get("Intel Atom");
		}
		return magentoAttributesReversed.get("Intel Core i3");
	}

	private List<String> generateYesNo(String bluetooth, String camera, String ssd, String ssd2, String reader, String fingerprint, String hdmi, 
			String hdmi2, String onelink, String usb3, String rj45, String sensorscreen, String usbc){
		List<String> list = new ArrayList<String>();
		if(bluetooth != null && !bluetooth.contains("No")){
			list.add("Bluetooth");
		}
		if(camera != null && !camera.contains("No")){
			list.add("Камера");
		}
		if(ssd != null && (ssd.contains("SSD") || ssd.contains("ssd") || ssd.contains("Ssd"))){
			list.add("SSD");
		}
		else if(ssd2 != null && (ssd2.contains("SSD") || ssd2.contains("ssd") || ssd2.contains("Ssd"))){
			list.add("SSD");
		}
		if(reader != null && !reader.contains("No")){
			list.add("Четец за карти");
		}
		if(fingerprint != null && !fingerprint.contains("No")){
			list.add("Сензор за отпечатък");
		}
		if(hdmi != null && !hdmi.contains("No")){
			list.add("HDMI порт");
		}
		else if(hdmi2 != null && !hdmi2.contains("No")){
			list.add("HDMI порт");
		}
		if(onelink != null && !onelink.contains("No")){
			list.add("OneLink порт");
		}
		if(usb3 != null && !usb3.equals("")){
			list.add("USB 3.0");
		}
		if(rj45 != null && !rj45.contains("No")){
			list.add("RJ-45 порт");
		}
		if(sensorscreen != null && sensorscreen.contains("ouch")){
			list.add("Сензорен екран");
		}
		if(usbc != null){
			if(Integer.valueOf(usbc) > 0){
				list.add("USB Type C");
			}
		}
		ArrayList<String> newList = new ArrayList<String>();
		for(String name : list){
			newList.add(magentoAttributesReversed.get(name));
		}
		return newList;
	}
	private String generateDisplaySize(String string) {
		if(string != null && !string.equals("")){
			if(string.contains("15.6")){
				return magentoAttributesReversed.get("15.6 inch (39.62 cm)");
			}
			if(string.contains("14")){
				return magentoAttributesReversed.get("14.0 inch (35.56 cm)");
			}
			if(string.contains("17")){
				return magentoAttributesReversed.get("17.3 inch (43.94 cm)");
			}
			if(string.contains("12.0")){
				return magentoAttributesReversed.get("12.0 inch (30.48 cm)");
			}
			if(string.contains("13.3")){
				return magentoAttributesReversed.get("13.3 inch (33.78 cm)");
			}
			if(string.contains("13.0")){
				return magentoAttributesReversed.get("13.0 inch (33.02 cm)");
			}
			if(string.contains("11.6")){
				return magentoAttributesReversed.get("11.6 inch (29.46 cm)");
			}
			if(string.contains("15.4")){
				return magentoAttributesReversed.get("15.4 inch (39.12 cm)");
			}
			return magentoAttributesReversed.get("15.6 inch (39.62 cm)");
		}
		return null;
	}

	private String generateDisplayResolution(String string, String string1) {
		String string2 = string1 != null && !string1.equals("") ? string1 : string;
		if(string2 != null && !string2.equals("")){
			if(string2.contains("1366") && string2.contains("768")){
				return magentoAttributesReversed.get("1366x768");
			}
			if(string2.contains("1280") && string2.contains("720")){
				return magentoAttributesReversed.get("HD (1280x720)");
			}
			if(string2.contains("1600") && string2.contains("1080")){
				return magentoAttributesReversed.get("HD+ (1600x1080)");
			}
			if(string2.contains("1920") && string2.contains("1080")){
				return magentoAttributesReversed.get("Full HD (1920x1080)");
			}
			if(string2.contains("2560") && string2.contains("1440")){
				return magentoAttributesReversed.get("Quad HD (2560x1440)");
			}
			if(string2.contains("3200") && string2.contains("1800")){
				return magentoAttributesReversed.get("Quad HD+ (3200x1800)");
			}
			if(string2.contains("3640") && string2.contains("2160")){
				return magentoAttributesReversed.get("Ultra HD - 4K (3640x2160)");
			}
			if(string2.contains("2304") && string2.contains("1440")){
				return magentoAttributesReversed.get("Retina (2304x1440)");
			}
			if(string2.contains("2880") && string2.contains("1800")){
				return magentoAttributesReversed.get("Retina (2880x1800)");
			}
		}
		return magentoAttributesReversed.get("1366x768");
	}

	private String generateHddFilter(String string) {
		StringBuilder st = new StringBuilder();
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == ' ' || string.charAt(i) == 'T' || string.charAt(i) == 'G'){
				break;
			}
			st.append(string.charAt(i));
		}
		int gb = Integer.parseInt(st.toString());
		if(gb <= 200){
			if(gb <= 5){
				return magentoAttributesReversed.get("1001-1500 GB");
			}
			else{
				return magentoAttributesReversed.get("под 200 GB");
			}
		}
		if(gb > 200 && gb <= 400){
			return magentoAttributesReversed.get("200-400 GB");
		}
		if(gb > 400 && gb <= 600){
			return magentoAttributesReversed.get("401-600 GB");
			
		}
		if(gb > 600 && gb <= 800){
			return magentoAttributesReversed.get("601-800 GB");
		}
		if(gb > 800 && gb <= 1000){
			return magentoAttributesReversed.get("801-1000 GB");
		}
		if(gb > 1000 && gb <= 1500){
			return magentoAttributesReversed.get("1001-1500 GB");
		}
		if(gb > 1500){
			return magentoAttributesReversed.get("над 1500 GB");
		}
		return magentoAttributesReversed.get("401-600 GB");
	}

	public List<Attribute> generateSimpleAttributes(HashMap<Integer, Property> list) {
		KeyValueAttribute description = new KeyValueAttribute();
		description.setAttributeCode("description");
		StringBuilder st = new StringBuilder();
		st.append("<h4>");
		for(Property property : list.values()){
			st.append(property.getName() + ": " + property.getValue().get(0).getText() + "<br>");
		}
		st.append("</h4>");
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		description.setValue(st.toString());
		attributes.add(description);
		return attributes;
	}

	public List<Attribute> generateTabletAttributes(HashMap<Integer, Property> list) {
		HashMap<Integer, String> properties = new HashMap<Integer, String>();
//		for(Property property : list){
//			properties.put(property.getPropertyId(), property.getValue().get(0).getText());
//		}
		List<Attribute> customAttributes = new ArrayList<Attribute>();
		
		KeyListAttribute hddFilter = new KeyListAttribute();
		hddFilter.setAttributeCode("memory_tablet");
		hddFilter.setValue(new ArrayList<String>());
		hddFilter.getValue().add(generateHddFilter(properties.get(5)));
		customAttributes.add(hddFilter);
		
		KeyValueAttribute battery = new KeyValueAttribute();
		battery.setAttributeCode("tablet_battery");
		battery.setValue((properties.get(43) != null ? properties.remove(43) : "" + properties.get(44) != null ? " " + properties.remove(44) : ""));
		customAttributes.add(battery);
		
		KeyValueAttribute color = new KeyValueAttribute();
		color.setAttributeCode("color");
		color.setValue(generateColorFilter(properties.remove(35)));
		customAttributes.add(color);
		
		KeyListAttribute cpuFilter = new KeyListAttribute();
		cpuFilter.setAttributeCode("tablet_cpu");
		cpuFilter.setValue(new ArrayList<String>());
		cpuFilter.getValue().add(generateCpuFilter(properties.remove(55), properties.get(2)));
		customAttributes.add(cpuFilter);
		
		KeyValueAttribute dimensions = new KeyValueAttribute();
		dimensions.setAttributeCode("tablet_dimensions");
		dimensions.setValue(properties.remove(47));
		customAttributes.add(dimensions);
		
		KeyValueAttribute displayInfo = new KeyValueAttribute();
		displayInfo.setAttributeCode("tablet_display_type");
		displayInfo.setValue((properties.get(1) + " " + properties.get(9)).trim());
		customAttributes.add(displayInfo);
		
		KeyListAttribute displayResolution = new KeyListAttribute();
		displayResolution.setAttributeCode("tablet_display");
		displayResolution.setValue(new ArrayList<String>());
		displayResolution.getValue().add(generateDisplayResolution(properties.get(1), properties.remove(70)).trim());
		customAttributes.add(displayResolution);
		
		KeyListAttribute displaySize = new KeyListAttribute();
		displaySize.setAttributeCode("tablet_display_size_filt");
		displaySize.setValue(new ArrayList<String>());
		displaySize.getValue().add(generateDisplaySize(properties.remove(1)));
		customAttributes.add(displaySize);
		
		KeyValueAttribute gpu = new KeyValueAttribute();
		gpu.setAttributeCode("laptop_gpu");
		gpu.setValue(properties.remove(10));
		customAttributes.add(gpu);
		
		KeyValueAttribute hddInfo = new KeyValueAttribute();
		hddInfo.setAttributeCode("laptop_hdd_info");
		hddInfo.setValue(properties.get(11));
		customAttributes.add(hddInfo);
		
		KeyValueAttribute hddSize = new KeyValueAttribute();
		hddSize.setAttributeCode("laptop_hdd_size");
		hddSize.setValue(generateHddSize(properties.get(11)));
		customAttributes.add(hddSize);
		
		KeyValueAttribute optical = new KeyValueAttribute();
		optical.setAttributeCode("laptop_optical");
		optical.setValue(properties.remove(13));
		customAttributes.add(optical);
		
		KeyListAttribute osFilter = new KeyListAttribute();
		osFilter.setAttributeCode("laptop_os_filter");
		osFilter.setValue(new ArrayList<String>());
		osFilter.getValue().add(generateOsFilter(properties.remove(57)));
		properties.remove(3);
		customAttributes.add(osFilter);
		
		KeyValueAttribute processor = new KeyValueAttribute();
		processor.setAttributeCode("laptop_processor");
		processor.setValue(properties.remove(2));
		customAttributes.add(processor);
		
		KeyListAttribute ram = new KeyListAttribute();
		ram.setAttributeCode("laptop_ram");
		ram.setValue(new ArrayList<String>());
		ram.getValue().add(generateRamFilter(properties.remove(5)));
		customAttributes.add(ram);
		
		KeyValueAttribute ramInfo = new KeyValueAttribute();
		ramInfo.setAttributeCode("laptop_ram_info");
		ramInfo.setValue(properties.remove(6));
		customAttributes.add(ramInfo);
		
		KeyValueAttribute weight = new KeyValueAttribute();
		weight.setAttributeCode("laptop_weight");
		weight.setValue(properties.remove(48));
		customAttributes.add(weight);
		
		KeyValueAttribute wifi = new KeyValueAttribute();
		wifi.setAttributeCode("laptop_wifi");
		wifi.setValue(properties.remove(15));
		customAttributes.add(wifi);
		
		KeyValueAttribute warranty = new KeyValueAttribute();
		warranty.setAttributeCode("laptop_warranty");
		warranty.setValue(properties.remove(49));
		customAttributes.add(warranty);
		
		KeyListAttribute yesNo = new KeyListAttribute();
		yesNo.setAttributeCode("laptop_yes_no");
		yesNo.setValue(generateYesNo(properties.remove(17),properties.remove(51),properties.remove(11), properties.remove(74), properties.remove(38),properties.remove(40),
				properties.remove(59),properties.remove(28),properties.remove(62),properties.remove(52),properties.remove(22),properties.remove(9), properties.remove(69)));
		customAttributes.add(yesNo);
		
		
		KeyValueAttribute ports = new KeyValueAttribute();
		ports.setAttributeCode("laptop_ports");
		StringBuilder portsString = new StringBuilder();

		KeyValueAttribute otherInfo = new KeyValueAttribute();
		otherInfo.setAttributeCode("laptop_other_info");
		StringBuilder otherInfoString = new StringBuilder();
		List<Property> productProperties2 = new ArrayList<Property>();
		productProperties2.addAll(list.values());
		for(Property property : list.values()){
			if(property.getPropertyId() == 18){
				properties.remove(18);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(29);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 41){
				properties.remove(41);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 42){
				properties.remove(42);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 53){
				properties.remove(53);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 61){
				properties.remove(61);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 62){
				properties.remove(62);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 24){
				properties.remove(24);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 28){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
		}
		for(Property property : productProperties2){
			if(properties.containsKey(property.getPropertyId())){
				otherInfoString.append(property.getName() + ": " + property.getValue().get(0).getText());
				if(properties.size() != 1){
					otherInfoString.append("; ");
				}
			}
		}
		ports.setValue(portsString.toString());
		customAttributes.add(ports);
		otherInfo.setValue(otherInfoString.toString());
		customAttributes.add(otherInfo);
		
		KeyValueAttribute shortDescription = new KeyValueAttribute();
		shortDescription.setAttributeCode("short_description");
		shortDescription.setValue(generateShortDescription(magentoAttributes.get(displaySize.getValue().get(0)), magentoAttributes.get(cpuFilter.getValue().get(0)), 
				magentoAttributes.get(ram.getValue().get(0)), hddSize.getValue(), battery.getValue()));
		customAttributes.add(shortDescription);
		
		return customAttributes;
	}
}
