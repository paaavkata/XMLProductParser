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
		StringBuilder st = new StringBuilder();
		String firstRow = "sku,"
				+ "attribute_set_code,"
				+ "product_type,"
				+ "categories,"
				+ "product_websites,"
				+ "name,"
				+ "short_description,"
				+ "weight,"
				+ "product_online,"
				+ "tax_class_name,"
				+ "visibility,"
				+ "price,"
				+ "special_price,"
				+ "special_price_from_date,"
				+ "special_price_to_date,"
				+ "url_key,"
				+ "meta_title,"
				+ "meta_keywords,"
				+ "meta_description,"
				+ "base_image,"
				+ "base_image_label,"
				+ "small_image,"
				+ "small_image_label,"
				+ "thumbnail_image,"
				+ "thumbnail_image_label,"
				+ "swatch_image,"
				+ "swatch_image_label,"
				+ "display_product_options_in,"
				+ "gift_message_available,"
				+ "additional_attributes,"
				+ "qty,"
				+ "out_of_stock_qty,"
				+ "use_config_min_qty,"
				+ "is_qty_decimal,"
				+ "allow_backorders,"
				+ "use_config_backorders,"
				+ "min_cart_qty,"
				+ "use_config_min_sale_qty,"
				+ "max_cart_qty,"
				+ "use_config_max_sale_qty,"
				+ "is_in_stock,"
				+ "notify_on_stock_below,"
				+ "use_config_notify_stock_qty,"
				+ "manage_stock,"
				+ "use_config_manage_stock,"
				+ "use_config_qty_increments,"
				+ "qty_increments,"
				+ "use_config_enable_qty_inc,"
				+ "enable_qty_increments,"
				+ "is_decimal_divided,"
				+ "website_id,"
				+ "additional_images,"
				+ "additional_image_labels";
		st.append(firstRow + "\n");
		char c = ',';
		for(Laptop laptop : laptops){
			st.append(laptop.getSku() + c);
			st.append("Лаптопи" + c);
			st.append("simple" + c);
			st.append("Главна директория,Главна директория/Лаптопи,Главна директория/Лаптопи/" + laptop.getBrand() + c);
			st.append("base" + laptop.getBrand() + c);
			st.append(laptop.getName() + c);
			st.append("simple" + c);
			st.append(c);
			st.append(laptop.getWeight() + c);
			st.append("1" + c);
			st.append("Taxable Goods" + c);
			st.append("Catalog, Search" + c);
			st.append(laptop.getPrice() + c);
			st.append(c);
			st.append(c);
			st.append(c);
			st.append(laptop.getName() + "-top-cena-na-izplashtane" + c);
			st.append(laptop.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД :: ревюта, характеристики, снимки" + c);
			st.append(c);
			st.append("Можете да вземете " + laptop.getName() + " още днес на топ цена и на изплащане "
					+ "с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. "
					+ "Бърза доставка на следващия ден и любезно обслужване.");
			st.append(laptop.getImages().get(0));
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append(laptop.getImages().get(0));
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append(laptop.getImages().get(0));
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append(laptop.getImages().get(0));
			st.append(laptop.getName() + " топ цена на изплащане"+ c);
			st.append("Block after Info Column" + c);
			st.append("Use config" + c);
			st.append("hdd_razmer_filt_r_laptop= + c +
					laptop_battery=""Laptop Battery info"" + c
					laptop_battery_filter=""15-клетъчна"",
					laptop_cpu_filter=""Intel i7 (4-ядрен)"",
					laptop_dimensions=""133x3413x3111"",
					laptop_display_info=""Additional info for the display of the laptop"",
					laptop_display_resolution=""Full HD (1920x1080)"",
					laptop_display_size=""15.6"""""",
					laptop_gpu=""Video graphics card for the laptop"",
					laptop_gpu_memory=""3 GB"",
					laptop_hdd_info=""Info for the hdd drive"",
					laptop_hdd_size=""1000 GB"",
					laptop_optical=""Optical device"",
					laptop_os_filter=""Windows 10"",
					laptop_other_info=""Dopylnitelna informaciq za kompa"",
					laptop_ports=""Ports that the machina has"",
					laptop_processor=""CPU info for the gdagadgada"",
					laptop_ram=""8 GB"",
					laptop_ram_info=""Info for the RAM Memory"",
					laptop_sound=""Sound Card of the Laptop"",
					laptop_warranty=""24 месеца"",
					laptop_weight=""2.222"",
					laptop_weight_filter=""от 2.1 до 2.5 kg"",
					laptop_wifi=""Wifi info for the laptop machine"",
					laptop_yes_no=Bluetooth|
								Камера|
								SSD|
								Четец за карти|
								Сензор за отпечатък|
								HDMI порт|
								OneLink порт|
								USB 3.0|
								RJ-45 порт|
								Сензорен екран|
								Подсветка на клавиатурата");
			st.append("5.0000" + c);
			st.append("0.0000" + c);
			st.append("1" + c);
			st.append("0" + c);
			st.append("0" + c);
			st.append("1" + c);
			st.append("1.0000" + c);
			st.append("1" + c);
			st.append("10000.0000" + c);
			st.append("1" + c);
			st.append("1" + c);
			st.append("1.0000" + c);
			st.append("1" + c);
			st.append("1" + c);
			st.append("1" + c);
			st.append("1" + c);
			st.append("1.0000" + c);
			st.append("1" + c);
			st.append("0" + c);
			st.append("0" + c);
			st.append("0" + c);
			st.append(laptop.getImages().get(0) + c);
			st.append(laptop.getName() + " топ цена на изплащане");
			
			
		}
		return "";
	}
	
//	public String makeTabletCSV(List<Tablet> tablets){
//		
//		return "";
//	}
}
