package com.premiummobile.First.solytron;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttribute;
import com.premiummobile.First.magento.KeyListAttribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.magento.MagentoStockItemFull;
import com.premiummobile.First.magento.MagentoStockItemRequest;
import com.premiummobile.First.magento.MediaGalleryContent;
import com.premiummobile.First.magento.MediaGalleryEntry;
import com.premiummobile.First.solytron.Model.Image;
import com.premiummobile.First.solytron.Model.Property;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.util.PropertiesLoader;
import com.premiummobile.First.util.RequestsExecutor;
	
@Component
public class MagentoMapper {
	
	PropertiesLoader loader;
	
	HashMap<String, String> solytronLaptop;
	
	HashMap<String, String> magentoAttributes;
	
	HashMap<String, String> magentoAttributesReversed;
	
	RequestsExecutor requestsExecutor;
	
	MagentoLaptopHelper helper;
	
	@Autowired
	public MagentoMapper(PropertiesLoader loader, RequestsExecutor requestsExecutor, MagentoLaptopHelper helper){
		this.requestsExecutor = requestsExecutor;
		this.loader = loader;
		this.helper = helper;
		this.solytronLaptop = loader.getSolytronLaptop();
		this.magentoAttributes = loader.getMagentoAttributes();
		this.magentoAttributesReversed = loader.getMagentoAttributesReversed();
	}
	
	public MagentoProductRequest mapProduct(SolytronProduct product, String category){
		
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		HashMap<Integer, String> properties = new HashMap<Integer, String>();
		for(Property property : product.getProperties().get(1).getList()){
			properties.put(property.getPropertyId(), property.getValue().get(0).getText());
		}
		magentoProduct.setStatus(1);
		magentoProduct.setVisibility(4);
		magentoProduct.setAttributeSetId(10);
		magentoProduct.setSku(product.getCodeId());
		if(product.getPriceEndUser() != null){
			if(product.getPriceEndUser().getCurrency().equals("BGN")){
				magentoProduct.setPrice(Double.valueOf(product.getPriceEndUser().getText()));
			}
			else if(product.getPriceEndUser().getCurrency().equals("EUR")){
				magentoProduct.setPrice(Integer.valueOf(product.getPriceEndUser().getText()) * 1.96);
			}
			if(product.getName().contains("romo") || product.getName().contains("ромо")){
				magentoProduct.setSpecialPrice(magentoProduct.getPrice());
				magentoProduct.setPrice(magentoProduct.getSpecialPrice() * 1.1);
			}
		}
		magentoProduct.setName(trimName(product.getName()));
		if(!magentoProduct.getName().toLowerCase().contains(product.getVendor().toLowerCase())){
			magentoProduct.setName(product.getVendor() + " " + magentoProduct.getName());
		}
		magentoProduct.setTypeId("simple");
		magentoProduct.setWeight(Double.valueOf(properties.get(48)));
		
		MagentoStockItemRequest magentoStockItem = new MagentoStockItemRequest();
		if(product.getStockInfoValue() != null){
			if(product.getStockInfoValue().contains("OnHand")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(5);
			}
			else if(product.getStockInfoValue().contains("Minimum")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(2);
			}
			else{
				magentoStockItem.setStock(false);
				magentoStockItem.setQty(0);
			}
		}
		magentoProduct.setExtensionAttributes(new ExtensionAttribute());
		magentoProduct.getExtensionAttributes().setItem(magentoStockItem);
		
		magentoProduct.setCustomAttributes(helper.generateAttributes(product.getProperties().get(1).getList()));
		
		KeyValueAttribute brand = new KeyValueAttribute();
		brand.setAttributeCode("gsm_manufacturer");
		brand.setValue(helper.generateBrand(product.getVendor()));
		magentoProduct.getCustomAttributes().add(brand);
		
		KeyValueAttribute metaTitle = new KeyValueAttribute();
		metaTitle.setAttributeCode("meta_title");
		
		KeyValueAttribute metaDescription = new KeyValueAttribute();
		metaDescription.setAttributeCode("meta_description");
		
		KeyValueAttribute url = new KeyValueAttribute();
		url.setAttributeCode("url_key");
		
		if(magentoProduct.getPrice() > 150){
			metaTitle.setValue(magentoProduct.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
			metaDescription.setValue("Купете днес " + magentoProduct.getName() + " на изплащане и на супер цена с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
			url.setValue(magentoProduct.getName() + "-cena-na-izplashtane");
		}
		else{
			metaTitle.setValue(magentoProduct.getName() + " цена без конкуренция от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
			metaDescription.setValue("Купете днес " + magentoProduct.getName() + " на страхотна цена с безплатна доставка от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
			url.setValue(magentoProduct.getName() + "-cena-za-balgaria");
		}
		magentoProduct.getCustomAttributes().add(url);
		magentoProduct.getCustomAttributes().add(metaDescription);
		magentoProduct.getCustomAttributes().add(metaTitle);
		
		KeyListAttribute categories = new KeyListAttribute();
		categories.setAttributeCode("category_ids");
		categories.setValues(generateCategories(category));
		magentoProduct.getCustomAttributes().add(categories);

		return magentoProduct;
	}

	private ArrayList<String> generateCategories(String category) {
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("2");
		categories.add("4");
		categories.add("");
		// TODO Auto-generated method stub
		return categories;
	}

	public List<String> downloadImages(SolytronProduct solytronProduct, MagentoProductRequest magentoProduct) throws Exception{
		int counter = 1;
		List<String> statuses = new ArrayList<String>();
		for(Image image : solytronProduct.getImages()){
			String status = requestsExecutor.uploadMagentoImage(magentoProduct, image.getText(), counter);
			statuses.add(status);
		    counter++;
		}
		return statuses;
	}

	private String trimName(String name) {
		StringBuilder st = new StringBuilder();
		name = name.replace("NB", "");
		name = name.replace("Преносим компютър", "");
		name = name.replace("Tablet","");
		name = name.replace("Notebook","");
		name = name.replace("Mobile workstation","");
		name = name.replace("Ultrabook", "");
		name = name.replace("РАЗПРОДАЖБА!", "");
		name = name.replace("PROMO!  ", "");
		name = name.trim();
		int spaces = 0;
		for(int i = 0; i < name.length(); i++){
			if(name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == (char) 047 || name.charAt(i) == '/'){
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
