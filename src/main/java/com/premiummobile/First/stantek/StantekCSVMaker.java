package com.premiummobile.First.stantek;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class StantekCSVMaker {
	
	public String makeLaptopCSV(Set<Laptop> laptops){
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
				+ "meta_keywords" + c
				+ "meta_description" + c
				+ "weight" + c
				
				+ "base_image" + c
				+ "base_image_label" + c
				+ "small_image" + c
				+ "small_image_label" + c
				+ "thumbnail_image" + c
				+ "thumbnail_image_label" + c;
				
		st.append(firstRow + "\n");
		
		for(Laptop laptop : laptops){
			st.append(laptop.getSku().trim() + c);
			st.append(laptop.getName() + c);
			st.append(String.valueOf(laptop.getPrice()) + c);
//			st.append(c);
			st.append("simple" + c);
			st.append("Лаптопи" + c);
			st.append("base" + c);
			st.append("1" + c);
			st.append("probni atributi" + c);
			//st.append(generateAttributes(laptop) + c);
			st.append("1" + c);
			st.append("Taxable goods" + c);
			st.append("Catalog, Search" + c);
			st.append("1" + c);
			
			st.append("Главна директория,Главна директория/Лаптопи,Главна директория/Лаптопи/" + laptop.getBrand() + c);
			//TO-DO short description generator
			st.append(generateShortDescription(laptop) + c);
			st.append(laptop.getName() + "-top-cena-na-izplashtane" + c);
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
			st.append( (char) 012 );
			
		}
		pw.write(st.toString());
		pw.close();
		return "response";
	}
	
	private String generateShortDescription(Laptop laptop){
		StringBuilder st = new StringBuilder();
		st.append("<tags>");
		st.append(laptop.getCpu());
		st.append("<tags>");
		st.append(laptop.getMemoryRam());
		st.append("<tags>");
		st.append(laptop.getHdd());
		st.append("<tags>");
		st.append(laptop.getGpuMemory());
		st.append("<tags>");
		st.append(laptop.getBattery());
		st.append("<tags>");
		return st.toString();
	}
	
	private String generateAttributes(Laptop laptop){
		StringBuilder st = new StringBuilder();
		char c = ',';
		st.append("hdd_razmer_filt_r_laptop=" + c);
		st.append("laptop_battery=" + "Laptop Battery info" + c);
		st.append("laptop_battery_filter=" + "15-клетъчна" + c);
		st.append("laptop_cpu_filter=" + "Intel i7 (4-ядрен)"+ c);
		st.append("laptop_dimensions=" + "133x3413x3111"+ c);
		st.append("laptop_display_info=" + "Additional info for the display of the laptop"+ c);
		st.append("laptop_display_resolution=" + "Full HD (1920x1080)"+ c);
		st.append("laptop_display_size=" + "15.6" + c);
		st.append("laptop_gpu=" + " Video graphics card for the laptop"+ c);
		st.append("laptop_gpu_memory=" + "3 GB"+ c);
		st.append("laptop_hdd_info=" + "Info for the hdd drive"+ c);
		st.append("laptop_hdd_size=" + "1000 GB" + c);
		st.append("laptop_optical=" + "Optical device" + c);
		st.append("laptop_os_filter=" + "Windows 10" + c);
		st.append("laptop_other_info=" + "Dopylnitelna informaciq za kompa" + c);
		st.append("laptop_ports=" + "Ports that the machina has" + c);
		st.append("laptop_processor=" + "CPU info for the gdagadgada" + c);
		st.append("laptop_ram=" + "8 GB" + c);
		st.append("laptop_ram_info=" + "Info for the RAM Memory" + c);
		st.append("laptop_sound=" + "Sound Card of the Laptop" + c);
		st.append("laptop_warranty=" + "24 месеца" + c);
		st.append("laptop_weight=" + "2.222" + c);
		st.append("laptop_weight_filter=" + "от 2.1 до 2.5 kg" + c);
		st.append("laptop_wifi=" + "Wifi info for the laptop machine" + c);
		st.append("laptop_yes_no=Bluetooth|Камера|SSD|Четец за карти|Сензор за отпечатък|HDMI порт|OneLink порт|USB 3.0|RJ-45 порт|Сензорен екран|Подсветка на клавиатурата");
		return st.toString();
	}
//	public String makeTabletCSV(List<Tablet> tablets){
//		
//		return "";
//	}
}
