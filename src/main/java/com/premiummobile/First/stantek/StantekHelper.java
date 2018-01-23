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
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class StantekHelper {
	
	public Set checkUniqueUrls(Set<Laptop> products) {
		HashMap<String, LocalProduct> mappedProducts = new HashMap<String, LocalProduct>();
		int count = 1;
		for(LocalProduct product : products) {
			if(!mappedProducts.containsKey(product.getUrl())) {
				mappedProducts.put(product.getUrl(), product);
			}
			else{
				product.setUrl(product.getUrl() + count);
				mappedProducts.put(product.getUrl(), product);
				count++;
			}
		}
		return products;
	}

	public Set downloadImages(Set<Laptop> products) {
		int imageCounter = 1;
		for (LocalProduct product : products){
			if(product.getImages().size() > 1){
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
				ArrayList<String> images = new ArrayList<String>();
				String imageName = "image" + imageCounter + ".jpg";
				try {
					URL url = new URL(product.getImages().get(0));
			        BufferedImage img = ImageIO.read(url);
			        File file = new File("c:\\images\\" + imageName);
			        if(img == null || file == null) {
			        	continue;
			        }
			        ImageIO.write(img, "jpg", file);
			        
				    images.add(imageName);
				    product.setImages(images);
				}
				catch(IOException e){
					System.out.println(e.getMessage());
					images.add(imageName);
					product.setImages(images);
				}
			}
			imageCounter++;
		}
		return products;
	}

	public String generateShortDescription(Laptop laptop){
		StringBuilder st = new StringBuilder();
		st.append("<ul class=\"short-description-list smartphone\"><div class=\"row\"><div class=\"col-md-2 col-md-offset-1\"><li class=\"display-size\">");
		st.append(laptop.getDisplaySize().replaceAll("$quot", "").replaceAll("$apos", ""));
		st.append("</li></div><div class=\"col-md-2\"><li class=\"processor\">");
		st.append(laptop.getCpuFilter());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"memory\">");
		st.append(laptop.getMemoryRam());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"hdd\">");
		st.append(laptop.getHddSize());
		st.append("</li></div><div class=\"col-md-2\"><li class=\"battery\">");
		StringBuilder st2 = new StringBuilder();
		String battery = laptop.getBattery();
		int spaces = 0;
		for(int i = 0; i < battery.length(); i++){
			if(battery.charAt(i) == ' '){
				spaces++;
			}
			if(spaces == 3){
				break;
			}
			st2.append(battery.charAt(i));
		}
		st.append(st2.toString());
		st.append("</li></div></div></ul>");
		return st.toString();
	}
	
	private String generateAttributes(Laptop laptop){
		StringBuilder st = new StringBuilder();
		char c = ';';
		st.append("hdd_razmer_filt_r_laptop=" + checkForCommas(laptop.getHddFilter()) + c);
		st.append("laptop_battery=" + checkForCommas(laptop.getBattery()) + c);
		st.append("laptop_cpu_filter=" + checkForCommas(laptop.getCpuFilter())+ c);
		st.append("laptop_color=" + checkForCommas(laptop.getColor()) + c);
		st.append("laptop_dimensions=" + checkForCommas(laptop.getDimensions())+ c);
		st.append("laptop_display_info=" + checkForCommas(laptop.getDisplayInfo())+ c);
		st.append("laptop_display_resolution=" + checkForCommas(laptop.getDisplayResolution())+ c);
		st.append("laptop_display_size=" + checkForCommas(laptop.getDisplaySize()) + c);
		st.append("laptop_gpu=" + checkForCommas(laptop.getGpu())+ c);
		st.append("laptop_gpu_memory=" + checkForCommas(laptop.getGpuMemory())+ c);
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
		st.append("laptop_wifi=" + checkForCommas(laptop.getWifi()) + c);
		st.append("gsm_manufacturer=" + laptop.getBrand() + c);
		st.append("laptop_yes_no=" + generateYesNoFilter(laptop));
		return st.toString();
	}
	
	public String checkForCommas(String text) {
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

	public String generateYesNoFilter(Laptop laptop){
		
		StringBuilder st = new StringBuilder();
		if(laptop.getBluetooth()){
			st.append("Bluetooth");
		}
		if(laptop.isWebcam()){
			if(st.length() < 2){
				st.append("Камера");
			}
			else{
				st.append("|Камера");
			}
		}
		if(laptop.isSsd()){
			if(st.length() < 2){
				st.append("SSD");
			}
			else{
				st.append("|SSD");
			}
		}
		if(laptop.isCardReader()){
			if(st.length() < 2){
				st.append("Четец за карти");
			}
			else{
				st.append("|Четец за карти");
			}
		}
		if(laptop.isFingerPrint()){
			if(st.length() < 2){
				st.append("Сензор за отпечатък");
			}
			else{
				st.append("|Сензор за отпечатък");
			}
		}
		if(laptop.isHdmi()){
			if(st.length() < 2){
				st.append("HDMI порт");
			}
			else{
				st.append("|HDMI порт");
			}
		}
		if(laptop.isOneLink()){
			if(st.length() < 2){
				st.append("OneLink порт");
			}
			else{
				st.append("|OneLink порт");
			}
		}
		if(laptop.isUsb3()){
			if(st.length() < 2){
				st.append("USB 3.0");
			}
			else{
				st.append("|USB 3.0");
			}
		}
		if(laptop.isRj45()){
			if(st.length() < 2){
				st.append("RJ-45 порт");
			}
			else{
				st.append("|RJ-45 порт");
			}
		}
		if(laptop.isSensorScreen()){
			if(st.length() < 2){
				st.append("Сензорен екран");
			}
			else{
				st.append("|Сензорен екран");
			}
		}
		if(laptop.isKeyboardBacklit()){
			if(st.length() < 2){
				st.append("Подсветка на клавиатурата");
			}
			else{
				st.append("|Подсветка на клавиатурата");
			}
		}
		
		
		return st.toString();
	}
}
