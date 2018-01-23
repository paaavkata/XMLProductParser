package com.premiummobile.First.stantek;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsvMaker {
	
	@Autowired
	private StantekHelper helper;
	
	public String makeLaptopCSV(Set<Laptop> laptops){
		laptops = helper.downloadImages(laptops);
		laptops = helper.checkUniqueUrls(laptops);
		PrintWriter pw = null;
		try {
		    pw = new PrintWriter(new File("Laptops.csv"));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		char c = ';';
		StringBuilder st = new StringBuilder();
		String firstRow = "sku" + c
				+ "store_view_code" + c
				+ "name" + c
				+ "price" + c
				+ "product_type" + c
				+ "attribute_set_code" + c
				+ "product_websites" + c
				+ "qty" + c
//				+ "additional_attributes" + c
				
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
				+ "thumbnail_image_label" + c;
		st.append(firstRow);		
//		st.append(firstRow + "\n");
		
		
		st.append("hdd_razmer_filt_r_laptop" + c);
		st.append("laptop_battery" + c);
		st.append("laptop_cpu_filter" + c);
		st.append("laptop_color" + c);
		st.append("laptop_dimensions" + c);
		st.append("laptop_display_info" + c);
		st.append("laptop_display_resolution" + c);
		st.append("laptop_display_size" + c);
		st.append("laptop_gpu" + c);
		st.append("laptop_gpu_memory" + c);
		st.append("laptop_hdd_size" + c);
		st.append("laptop_optical" + c);
		st.append("laptop_os_filter" + c);
		st.append("laptop_other_info" + c);
		st.append("laptop_ports" + c);
		st.append("laptop_processor" + c);
		st.append("laptop_ram" + c);
		st.append("laptop_ram_info" + c);
		st.append("laptop_sound" + c);
		st.append("laptop_warranty" + c);
		st.append("laptop_weight" + c);
		st.append("laptop_wifi" + c);
		st.append("gsm_manufacturer" + c);
		st.append("laptop_yes_no");
		st.append("\n");
		
		
		for(Laptop laptop : laptops){
			st.append(laptop.getSku().trim() + c + c);
			st.append(laptop.getName() + c);
			st.append(String.valueOf(laptop.getPrice()) + c);
			st.append("simple" + c);
			st.append("Лаптопи" + c);
			st.append("base" + c);
			st.append("1" + c);	
//			st.append(generateAttributes(laptop) + c);
			st.append("1" + c);
			st.append("Taxable Goods" + c);
			st.append("Catalog, Search" + c);
			st.append("1" + c);
			st.append("Главна директория,Главна директория/Лаптопи,Главна директория/Лаптопи/" + laptop.getBrand() + c);
			st.append(helper.checkForCommas(helper.generateShortDescription(laptop)) + c);
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
			st.append(laptop.getName() + " топ цена на изплащане" + c);
			//23
			st.append(helper.checkForCommas(laptop.getHddFilter()) + c);
			st.append(helper.checkForCommas(laptop.getBattery()) + c);
			st.append(helper.checkForCommas(laptop.getCpuFilter())+ c);
			st.append(helper.checkForCommas(laptop.getColor()) + c);
			st.append(helper.checkForCommas(laptop.getDimensions())+ c);
			st.append(helper.checkForCommas(laptop.getDisplayInfo())+ c);
			st.append(helper.checkForCommas(laptop.getDisplayResolution())+ c);
			st.append(helper.checkForCommas(laptop.getDisplaySize()) + c);
			st.append(helper.checkForCommas(laptop.getGpu())+ c);
			st.append(helper.checkForCommas(laptop.getGpuMemory())+ c);
			st.append(helper.checkForCommas(laptop.getHddSize()) + c);
			st.append(helper.checkForCommas(laptop.getOptical()) + c);
			st.append(helper.checkForCommas(laptop.getOsFilter()) + c);
			st.append(helper.checkForCommas(laptop.getOtherInfo()) + c);
			st.append(helper.checkForCommas(laptop.getPorts()) + c);
			st.append(helper.checkForCommas(laptop.getCpu()) + c);
			st.append(helper.checkForCommas(laptop.getMemoryRam()) + c);
			st.append(helper.checkForCommas(laptop.getMemoryInfo()) + c);
			st.append(helper.checkForCommas(laptop.getAudio()) + c);
			st.append(helper.checkForCommas(laptop.getWarranty()) + c);
			st.append(helper.checkForCommas(laptop.getWeight()) + c);
			st.append(helper.checkForCommas(laptop.getWifi()) + c);
			st.append(laptop.getBrand() + c);
			st.append(helper.generateYesNoFilter(laptop));
			
			st.append((char) 012);
		}
		pw.write(st.toString());
		pw.close();
		return "response";
	}
}
