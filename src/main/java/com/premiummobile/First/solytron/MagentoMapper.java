package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.premiummobile.First.magento.KeyListAttribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttribute;
import com.premiummobile.First.magento.MagentoProduct;
import com.premiummobile.First.magento.MagentoStockItem;
import com.premiummobile.First.solytron.Model.Property;
import com.premiummobile.First.solytron.Model.SolytronProduct;

@Component
public class MagentoMapper {
	
	public MagentoProduct mapProduct(SolytronProduct product){
		MagentoProduct magentoProduct = new MagentoProduct();
		HashMap<Integer, String> properties = new HashMap<Integer, String>();
		for(Property property : product.getProperties().get(1).getList()){
			properties.put(property.getPropertyId(), property.getValue().get(0).getText());
		}
		magentoProduct.setAttributeSetId(10);
		magentoProduct.setSku(product.getCodeId());
		magentoProduct.setName(trimName(product.getName()));
		if(product.getPriceEndUser().getCurrency().equals("BGN")){
			magentoProduct.setPrice(Double.valueOf(product.getPriceEndUser().getText()));
		}
		else if(product.getPriceEndUser().getCurrency().equals("EUR")){
			magentoProduct.setPrice(Integer.valueOf(product.getPriceEndUser().getText()) * 1.96);
		}
		magentoProduct.setVisibility(3);
		magentoProduct.setTypeId("simple");
		magentoProduct.setWeight(Double.valueOf(properties.get(48)));
		MagentoStockItem magentoStockItem = new MagentoStockItem();
		if(product.getStockInfoData().equals("OnHand")){
			magentoStockItem.setInStock(true);
			magentoStockItem.setQty(5);
		}
		else if(product.getStockInfoData().equals("Minimum")){
			magentoStockItem.setInStock(true);
			magentoStockItem.setQty(2);
		}
		else{
			magentoStockItem.setInStock(false);
			magentoStockItem.setQty(0);
		}
		magentoStockItem.setStockId(1);
		magentoProduct.setExtensionAttributes(new ArrayList<ExtensionAttribute>());
		magentoProduct.getExtensionAttributes().add(magentoStockItem);
		magentoProduct.setCustomAttributes(generateAttributes(properties));
		return magentoProduct;
	}

	private List<Attribute> generateAttributes(HashMap<Integer, String> properties) {
		List<Attribute> customAttributes = new ArrayList<Attribute>();
		
		KeyListAttribute hddFilter = new KeyListAttribute();
		hddFilter.setAttributeCode("hdd_razmer_filt_r_laptop");
		hddFilter.setValues(new ArrayList<String>());
		hddFilter.getValues().add(makeHddFilter(properties.remove(71)));
		customAttributes.add(hddFilter);
		
		KeyValueAttribute battery = new KeyValueAttribute();
		battery.setAttributeCode("laptop_battery");
		battery.setValue((properties.remove(43) + " " + properties.remove(44)).trim());
		customAttributes.add(battery);
		
		KeyListAttribute color = new KeyListAttribute();
		color.setAttributeCode("laptop_color");
		color.setValues(new ArrayList<String>());
		color.getValues().add(generateColorFilter(properties.remove(11)));
		customAttributes.add(color);
		
		KeyListAttribute cpuFilter = new KeyListAttribute();
		cpuFilter.setAttributeCode("laptop_cpu_filter");
		cpuFilter.setValues(new ArrayList<String>());
		cpuFilter.getValues().add(generateCpuFilter(properties.remove(55), properties.get(2)));
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
		displayResolution.setValues(new ArrayList<String>());
		displayResolution.getValues().add(generateDisplayResolution(properties.get(1), properties.remove(70)).trim());
		customAttributes.add(displayResolution);
		
		KeyListAttribute displaySize = new KeyListAttribute();
		displaySize.setAttributeCode("laptop_display_size");
		displaySize.setValues(new ArrayList<String>());
		displaySize.getValues().add(generateDisplaySize(properties.remove(1)));
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
		osFilter.setValues(new ArrayList<String>());
		osFilter.getValues().add(generateOsFilter(properties.remove(57)));
		customAttributes.add(osFilter);
		
		KeyValueAttribute otherInfo = new KeyValueAttribute();
		otherInfo.setAttributeCode("laptop_other_info");
		otherInfo.setValue(generateOtherInfo(properties.get(11)));
		customAttributes.add(otherInfo);
		
		KeyValueAttribute processor = new KeyValueAttribute();
		processor.setAttributeCode("laptop_processor");
		processor.setValue(properties.remove(2));
		customAttributes.add(processor);
		
		KeyListAttribute ram = new KeyListAttribute();
		ram.setAttributeCode("laptop_ram");
		ram.setValues(new ArrayList<String>());
		ram.getValues().add(generateRamFilter(properties.remove(5)));
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
		
		KeyValueAttribute ports = new KeyValueAttribute();
		ports.setAttributeCode("laptop_ports");
		ports.setValue(generatePorts(properties.get(11)));
		customAttributes.add(ports);
		
		KeyValueAttribute warranty = new KeyValueAttribute();
		warranty.setAttributeCode("laptop_warranty");
		warranty.setValue(properties.remove(49));
		customAttributes.add(warranty);
		
		KeyListAttribute yesNo = new KeyListAttribute();
		yesNo.setAttributeCode("laptop_yes_no");
		yesNo.setValues(generateYesNo(properties.remove(17),properties.remove(51),properties.remove(11), properties.remove(74), properties.remove(38),properties.remove(40),
				properties.remove(59),properties.remove(28),properties.remove(62),properties.remove(52),properties.remove(22),properties.remove(9), properties.remove(69)));
		customAttributes.add(yesNo);
		
		return customAttributes;
	}
	
	private String generatePorts(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateOtherInfo(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateBrand(String string){
		// TODO Auto-generated method stub
		return null;
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
			if(string.contains("indows")){
				return "Windows";
			}
			if(string.contains("DOS") || string.contains("No OS")){
				return "DOS";
			}
			if(string.contains("Mac")){
				return "Mac OS";
			}
			if(string.contains("inux")){
				return "Linux";
			}
		}
		return "DOS";
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
					int multiplyier = (int) memory.charAt(i-1);
					String ram = new String();
					for(int j = i + 1; j < memory.length(); j++) {
						ram = ram + memory.charAt(j);
						if(memory.charAt(j) > 57 || memory.charAt(j) < 48){
							break;
						}
					}
					return multiplyier*Integer.valueOf(ram) + " GB";
				}
				st.append(memory.charAt(i));
			}
			return st.toString() + " GB";
		}
		return "4 GB";
	}

	private String generateColorFilter(String color) {
		if(color != null && !color.equals("")){
			if(Pattern.compile(Pattern.quote("black"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Черен";
			}
			if(Pattern.compile(Pattern.quote("черен"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Черен";
			}
			if(Pattern.compile(Pattern.quote("blue"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Син";
			}
			if(Pattern.compile(Pattern.quote("red"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Червен";
			}
			if(Pattern.compile(Pattern.quote("white"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Бял";
			}
			if(Pattern.compile(Pattern.quote("gray"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Сив";
			}
			if(Pattern.compile(Pattern.quote("grey"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Сив";
			}
			if(Pattern.compile(Pattern.quote("silver"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Сребрист";
			}
			if(Pattern.compile(Pattern.quote("gold"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Златист";
			}
			if(Pattern.compile(Pattern.quote("purple"), Pattern.CASE_INSENSITIVE).matcher(color).find()){
				return "Лилав";
			}
		}
		return "Черен";
	}

	private String generateCpuFilter(String cpuFilter, String cpuFilter2) {
		if(cpuFilter == null || cpuFilter.equals("")){
			cpuFilter = cpuFilter2;
		}
		if(cpuFilter.contains("i7")){
			return "Intel Core i7";
		}
		if(cpuFilter.contains("i5")){
			return "Intel Core i5";
		}
		if(cpuFilter.contains("i3")){
			return "Intel Core i3";
		}
		if(cpuFilter.contains("entium")){
			return "Intel Pentium";
		}
		if(cpuFilter.contains("AMD")){
			return "AMD";
		}
		if(cpuFilter.contains("eleron")){
			return "Intel Celeron";
		}
		if(cpuFilter.contains("eon")){
			return "Intel Xeon";
		}
		if(cpuFilter.contains("tom")){
			return "Intel Atom";
		}
		return "Intel Core i3";
	}

	private List<String> generateYesNo(String bluetooth, String camera, String ssd, String ssd2, String reader, String fingerprint, String hdmi, 
			String hdmi2, String onelink, String usb3, String rj45, String sensorscreen, String usbc){
		List<String> list = new ArrayList<String>();
		if(bluetooth.contains("Yes")){
			list.add("Bluetooth");
		}
		if(!camera.contains("No")){
			list.add("Камера");
		}
		if(ssd.contains("SSD") || ssd.contains("ssd") || ssd.contains("Ssd")){
			list.add("SSD");
		}
		else if(ssd2.contains("SSD") || ssd2.contains("ssd") || ssd2.contains("Ssd")){
			list.add("SSD");
		}
		if(reader.contains("No")){
			list.add("Четец за карти");
		}
		if(fingerprint.contains("No")){
			list.add("Сензор за отпечатък");
		}
		if(!hdmi.contains("No")){
			list.add("HDMI порт");
		}
		else if(!hdmi2.contains("No")){
			list.add("HDMI порт");
		}
		if(!onelink.contains("No")){
			list.add("OneLink порт");
		}
		if(usb3 != null && !usb3.equals("")){
			list.add("USB 3.0");
		}
		if(!rj45.contains("No")){
			list.add("RJ-45 порт");
		}
		if(!sensorscreen.contains("ouch")){
			list.add("Сензорен екран");
		}
		if(usbc != null && !usbc.equals("")){
			list.add("Сензорен екран");
		}
		
		return list;
	}
	private String generateDisplaySize(String string) {
		if(string != null && !string.equals("")){
			if(string.contains("15.6")){
				return "15.6 inch (39.62 cm)";
			}
			if(string.contains("14")){
				return "14.0 inch (35.56 cm)";
			}
			if(string.contains("17")){
				return "17.3 inch (43.94 cm)";
			}
			if(string.contains("12.0")){
				return "12.0 inch (30.48 cm)";
			}
			if(string.contains("13.3")){
				return "13.3 inch (33.78 cm)";
			}
			if(string.contains("13.0")){
				return "13.0 inch (33.02 cm)";
			}
			if(string.contains("11.6")){
				return "11.6 inch (29.46 cm)";
			}
			if(string.contains("15.4")){
				return "15.4 inch (39.12 cm)";
			}
			return "15.6 inch (39.62 cm)";
		}
		return null;
	}

	private String generateDisplayResolution(String string, String string1) {
		String string2 = string1 != null && !string1.equals("") ? string1 : string;
		if(string2 != null && !string2.equals("")){
			if(string2.contains("1366 x 768")){
				return "1366x768";
			}
			if(string2.contains("1280 × 720")){
				return "HD (1280x720)";
			}
			if(string2.contains("1600 x 1080")){
				return "HD+ (1600x1080)";
			}
			if(string2.contains("1920 x 1080")){
				return "Full HD (1920x1080)";
			}
			if(string2.contains("2560 x 1440")){
				return "Quad HD (2560x1440)";
			}
			if(string2.contains("3200 x 1800")){
				return "Quad HD+ (3200x1800)";
			}
			if(string2.contains("3640 x 2160")){
				return "Ultra HD - 4K (3640x2160)";
			}
			if(string2.contains("2304 x 1440")){
				return "Retina (2304x1440)";
			}
			if(string2.contains("2880 x 1800")){
				return "Retina (2880x1800)";
			}
		}
		return "1366x768";
	}

	private String makeHddFilter(String string) {
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
				return "1001-1500 GB";
			}
			else{
				return "под 200 GB";
			}
		}
		if(gb > 200 && gb <= 400){
			return "200-400 GB";
		}
		if(gb > 400 && gb <= 600){
			return "401-600 GB";
			
		}
		if(gb > 600 && gb <= 800){
			return "601-800 GB";
		}
		if(gb > 800 && gb <= 1000){
			return "801-1000 GB";
		}
		if(gb > 1000 && gb <= 1500){
			return "1001-1500 GB";
		}
		if(gb > 1500){
			return "над 1500 GB";
		}
		return "401-600 GB";
	}

	private String trimName(String name) {
		StringBuilder st = new StringBuilder();
		name.replace("NB", "");
		name.replace("Преносим компютър", "");
		name.replace("Tablet","");
		name.replace("Notebook","");
		name.replace("Mobile workstation","");
		name.replace("Ultrabook", "");
		name.trim();
		int spaces = 0;
		for(int i = 0; i < name.length(); i++){
			if(name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == (char) 047 ){
				break;
			}
			if(name.charAt(i) == ' '){
				spaces++;
			}
			if(spaces > 3){
				break;
			}
			st.append(name.charAt(i));
		}
		return st.toString().trim();
	}
}
