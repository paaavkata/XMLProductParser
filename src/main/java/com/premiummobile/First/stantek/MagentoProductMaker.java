package com.premiummobile.First.stantek;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import com.github.chen0040.magento.MagentoClient;
//import com.github.chen0040.magento.models.MagentoAttribute;
//import com.github.chen0040.magento.models.Product;
//import com.github.chen0040.magento.services.MagentoProductManager;

@Component
public class MagentoProductMaker {
	
	@Autowired
	private StantekHelper helper;
	
	@Value("${premiummobileurl}")
	private String magento_site_url;
	@Value("${premiummobileuser}")
	private String username;
	@Value("${premiummobilepass}")
	private String password;
	
//	public String uploadProductsToProduction(Set<Laptop> laptops){
//		MagentoClient client = new MagentoClient(magento_site_url);
//		client.loginAsAdmin(username, password);
//		System.out.println(client.getToken());
//		MagentoProductManager pm = new MagentoProductManager(client);
//		
//		List<Product> products = new ArrayList<Product>();
//		laptops = helper.checkUniqueUrls(laptops);
//		laptops = helper.downloadImages(laptops);
//		int i = 0;
//		for(Laptop laptop : laptops){
//			i++;
//			if(i >= 3){
//				break;
//			}
//			Product product = new Product();
//			product.setSku(laptop.getSku().trim());
//			product.setName(laptop.getName());
//			product.setPrice(laptop.getPrice());
//			product.setType_id("simple");
//			product.setAttribute_set_id(10);
//			product.setStatus(1);
//			product.setVisibility(3);
//			product.setWeight(Double.valueOf(laptop.getWeight()));
//			List<MagentoAttribute> extensionAttributes = new ArrayList<MagentoAttribute>();
//			
//			List<MagentoAttribute> customAttributes = new ArrayList<MagentoAttribute>();
//			customAttributes.add(new MagentoAttribute("categories", "Главна директория,Главна директория/Лаптопи,Главна директория/Лаптопи/" + laptop.getBrand()));
//			customAttributes.add(new MagentoAttribute("short_description", helper.checkForCommas(helper.generateShortDescription(laptop))));
//			customAttributes.add(new MagentoAttribute("url_key", laptop.getUrl()));
//			customAttributes.add(new MagentoAttribute("meta_title", laptop.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД :: ревюта, характеристики, снимки"));
//			customAttributes.add(new MagentoAttribute("meta_description", "Можете да вземете " + laptop.getName() + " още днес на топ цена и на изплащане "
//					+ "с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. "
//					+ "Бърза доставка на следващия ден и любезно обслужване."));
//			customAttributes.add(new MagentoAttribute("weight", laptop.getWeight()));
//			customAttributes.add(new MagentoAttribute("base_image", laptop.getImages().get(0)));
//			customAttributes.add(new MagentoAttribute("base_image_label", laptop.getName() + " топ цена на изплащане"));
//			customAttributes.add(new MagentoAttribute("small_image", laptop.getImages().get(0)));
//			customAttributes.add(new MagentoAttribute("small_image_label", laptop.getName() + " топ цена на изплащане"));
//			customAttributes.add(new MagentoAttribute("thumbnail_image", laptop.getImages().get(0)));
//			customAttributes.add(new MagentoAttribute("thumbnail_image_label", laptop.getName() + " топ цена на изплащане"));
//			customAttributes.add(new MagentoAttribute("hdd_razmer_filt_r_laptop", laptop.getHddFilter()));
//			customAttributes.add(new MagentoAttribute("laptop_battery", laptop.getBattery()));
//			customAttributes.add(new MagentoAttribute("laptop_cpu_filter", laptop.getCpuFilter()));
//			customAttributes.add(new MagentoAttribute("laptop_color", laptop.getColor()));
//			customAttributes.add(new MagentoAttribute("laptop_dimensions", laptop.getDimensions()));
//			customAttributes.add(new MagentoAttribute("laptop_display_info", laptop.getDisplayInfo()));
//			customAttributes.add(new MagentoAttribute("laptop_display_resolution", laptop.getDisplayResolution()));
//			customAttributes.add(new MagentoAttribute("laptop_display_size", laptop.getDisplaySize()));
//			customAttributes.add(new MagentoAttribute("laptop_gpu", laptop.getGpu()));
//			customAttributes.add(new MagentoAttribute("laptop_gpu_memory", laptop.getGpuMemory()));
//			customAttributes.add(new MagentoAttribute("laptop_hdd_size", laptop.getHddSize()));
//			customAttributes.add(new MagentoAttribute("laptop_optical", laptop.getOptical()));
//			customAttributes.add(new MagentoAttribute("laptop_os_filter", laptop.getOsFilter()));
//			customAttributes.add(new MagentoAttribute("laptop_other_info", laptop.getOtherInfo()));
//			customAttributes.add(new MagentoAttribute("laptop_ports", laptop.getPorts()));
//			customAttributes.add(new MagentoAttribute("laptop_processor", laptop.getCpu()));
//			customAttributes.add(new MagentoAttribute("laptop_ram", laptop.getMemoryRam()));
//			customAttributes.add(new MagentoAttribute("laptop_ram_info", laptop.getMemoryInfo()));
//			customAttributes.add(new MagentoAttribute("laptop_sound", laptop.getAudio()));
//			customAttributes.add(new MagentoAttribute("laptop_warranty", laptop.getWarranty()));
//			customAttributes.add(new MagentoAttribute("laptop_weight", laptop.getWeight()));
//			customAttributes.add(new MagentoAttribute("laptop_wifi", laptop.getWifi()));
//			customAttributes.add(new MagentoAttribute("gsm_manufacturer", laptop.getBrand()));
//			customAttributes.add(new MagentoAttribute("laptop_yes_no", helper.generateYesNoFilter(laptop)));
//			product.setCustom_attributes(customAttributes);
//			pm.saveProduct(product);
//		}
		
//		return "";
//	}
	
	
}
