package com.premiummobile.First.stantek;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class StantekCSVMaker {
	
	public String makeLaptopCSV(Set<Laptop> laptops){
		//laptops = downloadImages(laptops);
		laptops = checkUniqueUrls(laptops);
		PrintWriter pw = null;
		try {
		    pw = new PrintWriter(new File("Laptops.csv"));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		char c = ';';
		StringBuilder st = new StringBuilder();
		String firstRow = "sku" + c
				+ "name" + c
				+ "price" + c
				+ "product_type" + c
				+ "attribute_set_code" + c
				+ "product_websites" + c
				+ "qty" + c
				+ "additional_attributes" + c
				
				+ "product_online" + c
				+ "tax_class_name" + c
				+ "visibility" + c
				+ "is_in_stock" + c
				
				+ "categories" + c
				+ "short_description" + c
				+ "url_key" + c
				+ "meta_title" + c
				+ "meta_description" + c
				+ "weight" + c
				
				+ "base_image" + c
				+ "base_image_label" + c
				+ "small_image" + c
				+ "small_image_label" + c
				+ "thumbnail_image" + c
				+ "thumbnail_image_label";
				
		st.append(firstRow + "\n");
		
		for(Laptop laptop : laptops){
			st.append(laptop.getSku().trim() + c);
			st.append(laptop.getName() + c);
			st.append(String.valueOf(laptop.getPrice()) + c);
			st.append("simple" + c);
			st.append("Лаптопи" + c);
			st.append("base" + c);
			st.append("1" + c);
			st.append(generateAttributes(laptop) + c);
			st.append("1" + c);
			st.append("Taxable goods" + c);
			st.append("Catalog, Search" + c);
			st.append("1" + c);
			st.append("Главна директория,Главна директория/Лаптопи,Главна директория/Лаптопи/" + laptop.getBrand() + c);
			st.append(checkForCommas(generateShortDescription(laptop)) + c);
			st.append(laptop.getUrl() + c);
			st.append(laptop.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД :: ревюта, характеристики, снимки" + c);
			st.append("Можете да вземете " + laptop.getName() + " още днес на топ цена и на изплащане "
					+ "с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. "
					+ "Бърза доставка на следващия ден и любезно обслужване." + c);
			st.append(laptop.getWeight() + c);
			st.append(laptop.getImages().get(0) + c);
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append(laptop.getImages().get(0) + c);
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append(laptop.getImages().get(0) + c);
			st.append(laptop.getName() + " топ цена на изплащане");
			st.append((char) 012);
		}
		pw.write(st.toString());
		pw.close();
		return "response";
	}
	
	private Set<Laptop> checkUniqueUrls(Set<Laptop> laptops) {
		ArrayList<Laptop> laptopsList = new ArrayList<Laptop>();
		for(Laptop laptop : laptops){
			laptopsList.add(laptop);
		}
		for(int i = 0; i < laptopsList.size(); i++){
			if(laptopsList.size()-1 >= i){
				break;
			}
			int counter = 1;
			for(int j = i+1; i < laptopsList.size()-1; j++){
				if(laptopsList.get(i).getUrl().equals(laptopsList.get(j).getUrl())){
					laptopsList.get(j).setUrl(laptopsList.get(j).getUrl() + counter);
					counter++;
				}
			}
		}
		laptops.clear();
		laptops.addAll(laptopsList);
		return laptops;
	}

	private Set<Laptop> downloadImages(Set<Laptop> laptops) {
		int imageCounter = 1;
		for (Laptop laptop : laptops){
			if(laptop.getImages().size() > 1){
//				int count = 1;
//				ArrayList<String> images = new ArrayList<String>();
//				for(String imageString : laptop.getImages()){
//					String path = "c:\\images\\" + laptop.getSku() + String.valueOf(count) + ".jpg";
//					try{
//						URL url = new URL(imageString);
//				        BufferedImage img = ImageIO.read(url);
//				        File file = new File("c:\\images\\" + laptop.getSku() + ".jpg");
//				        ImageIO.write(img, "jpg", file);
//					    images.add(path);
//					}
//					catch(IOException e){
//						e.printStackTrace();
//					}
//					count++;
//				}
//				count = 1;
//				laptop.setImages(images);
			}
			else{
				ArrayList<String> images= new ArrayList<String>();
				String imageName = "image" + imageCounter + ".jpg";
				try {
				URL url = new URL(laptop.getImages().get(0));
		        BufferedImage img = ImageIO.read(url);
		        
		        File file = new File("c:\\images\\" + imageName);
		        if(img == null || file == null) {
		        	continue;
		        }
		        ImageIO.write(img, "jpg", file);
		        
			    images.add(imageName);
			    laptop.setImages(images);
				}
				catch(IOException e){
					System.out.println(e.getMessage());
					images.add(imageName);
				    laptop.setImages(images);
				}
			}
			imageCounter++;
		}
		return laptops;
	}

	private String generateShortDescription(Laptop laptop){
//		<ul class="short-description-list smartphone">
//		<div class="row">
//		<div class="col-md-2 col-md-offset-1"><li class="display-size">4.7'' </li></div>
//		<div class="col-md-2"><li class="processor">8-ядрен</li></div>
//		<div class="col-md-2"><li class="memory">2 GB</li></div>
//		<div class="col-md-2"><li class="hdd">16 GB</li></div>
//		<div class="col-md-2"><li class="battery">2000 mAh</li></div></div>
//		</ul>
		StringBuilder st = new StringBuilder();
		st.append("<ul class=\"short-description-list smartphone\"><div class=\"row\"><div class=\"col-md-2 col-md-offset-1\"><li class=\"display-size\">");
		st.append(laptop.getDisplaySize());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"processor\">");
		st.append(laptop.getCpuFilter());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"memory\">");
		st.append(laptop.getMemoryRam());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"hdd\">");
		st.append(laptop.getHddSize());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"battery\">");
		st.append(laptop.getBattery());
		st.append("</li></div></div></ul>");
		return st.toString();
	}
	
	private String generateAttributes(Laptop laptop){
		StringBuilder st = new StringBuilder();
		char c = ',';
		st.append("hdd_razmer_filt_r_laptop=" + checkForCommas(laptop.getHddFilter()) + c);
		st.append("laptop_battery=" + checkForCommas(laptop.getBattery()) + c);
		st.append("laptop_battery_filter=" + checkForCommas(laptop.getBatteryFilter()) + c);
		st.append("laptop_cpu_filter=" + checkForCommas(laptop.getCpuFilter())+ c);
		st.append("laptop_dimensions=" + checkForCommas(laptop.getDimensions())+ c);
		st.append("laptop_display_info=" + checkForCommas(laptop.getDisplayInfo())+ c);
		st.append("laptop_display_resolution=" + checkForCommas(laptop.getDisplayResolution())+ c);
		st.append("laptop_display_size=" + checkForCommas(laptop.getDisplaySize().replaceAll("&quot", "").replaceAll("&apos", "")) + c);
		st.append("laptop_gpu=" + checkForCommas(laptop.getGpu())+ c);
		st.append("laptop_gpu_memory=" + checkForCommas(laptop.getGpuMemory())+ c);
		st.append("laptop_hdd_info=" + checkForCommas(laptop.getHdd())+ c);
		st.append("laptop_hdd_size=" + checkForCommas(laptop.getHddSize()) + c);
		st.append("laptop_optical=" + checkForCommas(laptop.getOptical()) + c);
		st.append("laptop_os_filter=" + checkForCommas(laptop.getOsFilter()) + c);
		st.append("laptop_other_info=" + checkForCommas(laptop.getOtherInfo()) + c);
		st.append("laptop_ports=" + checkForCommas(laptop.getPorts()) + c);
		st.append("laptop_processor=" + checkForCommas(laptop.getCpu()) + c);
		st.append("laptop_ram=" + checkForCommas(laptop.getMemoryRam()) + c);
		st.append("laptop_ram_info=" + checkForCommas(laptop.getMemoryInfo()) + c);
		st.append("laptop_sound=" + checkForCommas(laptop.getAudio()) + c);
		st.append("laptop_warranty=" + checkForCommas(laptop.getWarranty()) + c);
		st.append("laptop_weight=" + checkForCommas(laptop.getWeight()) + c);
		st.append("laptop_weight_filter=" + checkForCommas(laptop.getWeightFilter()) + c);
		st.append("laptop_wifi=" + checkForCommas(laptop.getWifi()) + c);
		st.append("laptop_yes_no=" + generateYesNoFilter(laptop));
		return st.toString();
	}
	
	private String checkForCommas(String text) {
		StringBuilder st = new StringBuilder();
		if(text == null){
			return "";
		}
		for(int i = 0; i < text.length(); i++){
			if(text.charAt(i) == ',' || text.charAt(i) == ';'){
				st.append(' ');
				continue;
			}
			st.append(text.charAt(i));
		}
		return st.toString().trim();
	}

	private String generateYesNoFilter(Laptop laptop){
		
		StringBuilder st = new StringBuilder();
		if(laptop.getBluetooth()){
			st.append("Bluetooth");
		}
		if(laptop.isWebcam()){
			if(st.equals("")){
				st.append("Камера");
			}
			else{
				st.append("|Камера");
			}
		}
		if(laptop.isSsd()){
			if(st.equals("")){
				st.append("SSD");
			}
			else{
				st.append("|SSD");
			}
		}
		if(laptop.isCardReader()){
			if(st.equals("")){
				st.append("Четец за карти");
			}
			else{
				st.append("|Четец за карти");
			}
		}
		if(laptop.isFingerPrint()){
			if(st.equals("")){
				st.append("Сензор за отпечатък");
			}
			else{
				st.append("|Сензор за отпечатък");
			}
		}
		if(laptop.isHdmi()){
			if(st.equals("")){
				st.append("HDMI порт");
			}
			else{
				st.append("|HDMI порт");
			}
		}
		if(laptop.isOneLink()){
			if(st.equals("")){
				st.append("OneLink порт");
			}
			else{
				st.append("|OneLink порт");
			}
		}
		if(laptop.isUsb3()){
			if(st.equals("")){
				st.append("USB 3.0");
			}
			else{
				st.append("|USB 3.0");
			}
		}
		if(laptop.isRj45()){
			if(st.equals("")){
				st.append("RJ-45 порт");
			}
			else{
				st.append("|RJ-45 порт");
			}
		}
		if(laptop.isSensorScreen()){
			if(st.equals("")){
				st.append("Сензорен екран");
			}
			else{
				st.append("|Сензорен екран");
			}
		}
		if(laptop.isKeyboardBacklit()){
			if(st.equals("")){
				st.append("Подсветка на клавиатурата");
			}
			else{
				st.append("|Подсветка на клавиатурата");
			}
		}
		
		
		return st.toString();
	}
}
