package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttribute;
import com.premiummobile.First.magento.KeyListAttribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.magento.MagentoStockItemRequest;
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
	
	HashMap<String, String> magentoCategories;
	
	RequestsExecutor requestsExecutor;
	
	MagentoMappingHelper helper;
	
	HashMap<String, Integer> urls;
	
	@Autowired
	public MagentoMapper(PropertiesLoader loader, RequestsExecutor requestsExecutor, MagentoMappingHelper helper){
		this.urls = new HashMap<String, Integer>();
		this.requestsExecutor = requestsExecutor;
		this.loader = loader;
		this.helper = helper;
		this.solytronLaptop = loader.getSolytronLaptop();
		this.magentoAttributes = loader.getMagentoAttributes();
		this.magentoAttributesReversed = loader.getMagentoAttributesReversed();
		this.magentoCategories = loader.getMagentoCategories();
	}
	
	public MagentoProductRequest mapProduct(SolytronProduct product, String[] params){
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		HashMap<Integer, String> properties = new HashMap<Integer, String>();
		if(product.getProperties().get(1) != null && product.getProperties().get(1).getList() != null){
			for(Property property : product.getProperties().get(1).getList()){
				properties.put(property.getPropertyId(), property.getValue().get(0).getText());
			}
		}
		magentoProduct.setStatus(1);
		magentoProduct.setVisibility(4);
		String sku = product.getCodeId();
		sku = sku.replace("/", "");
		sku = sku.replace((char) 92, (char) 0);
		sku = sku.replace((char) 34, (char) 0);
		magentoProduct.setSku(sku);
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
		if(params[1] == "6"){
			magentoProduct.setName(trimName(product.getName(), 6));
		}
		else{
			magentoProduct.setName(trimName(product.getName(), 4));
		}
		if(!magentoProduct.getName().toLowerCase().contains(product.getVendor().toLowerCase())){
			magentoProduct.setName(product.getVendor() + " " + magentoProduct.getName());
		}
		String productName = magentoProduct.getName();
		magentoProduct.setTypeId("simple");
		magentoProduct.setWeight(Double.valueOf(properties.get(48) != null ? properties.get(48) : "1"));
		
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
		if(product.getProperties().get(1).getList() != null){
			switch(params[1]){
				case "4":
					magentoProduct.setCustomAttributes(helper.generateLaptopAttributes(product.getProperties().get(1).getList()));
					magentoProduct.setAttributeSetId(10);
					break;
				
				case "5":
					magentoProduct.setCustomAttributes(helper.generateTabletAttributes(product.getProperties().get(1).getList()));
					magentoProduct.setAttributeSetId(11);
					break;
				
				default:
					magentoProduct.setCustomAttributes(helper.generateSimpleAttributes(product.getProperties().get(1).getList()));
					magentoProduct.setAttributeSetId(12);
					break;
			}
		}
		else{
			magentoProduct.setCustomAttributes(new ArrayList<Attribute>());
		}
		KeyValueAttribute brand = new KeyValueAttribute();
		brand.setAttributeCode("gsm_manufacturer");
		if(product.getVendor() == null){
			if(productName.contains("Lenovo") || productName.contains("LENOVO")){
				brand.setValue(magentoAttributesReversed.get("Lenovo"));
			}
			else if(productName.contains("Acer") || productName.contains("ACER")){
				brand.setValue(magentoAttributesReversed.get("Acer"));
			}
			else if(productName.contains("HP") || productName.contains("Hewl")){
				brand.setValue(magentoAttributesReversed.get("HP"));
			}
			else if(productName.contains("Dell") || productName.contains("DELL")){
				brand.setValue(magentoAttributesReversed.get("DELL"));
			}
			else if(productName.contains("Asus") || productName.contains("ASUS")){
				brand.setValue(magentoAttributesReversed.get("ASUS"));
			}
			else if(productName.contains("Apple") || productName.contains("APPLE")){
				brand.setValue(magentoAttributesReversed.get("Apple"));
			}
			else if(productName.contains("Toshiba") || productName.contains("TOSHIBA")){
				brand.setValue(magentoAttributesReversed.get("Toshiba"));
			}
			else{
				brand.setValue(magentoAttributesReversed.get("Други"));
			}
		}
		else{
			brand.setValue(helper.generateBrand(product.getVendor()));
		}
		magentoProduct.getCustomAttributes().add(brand);
		
		KeyValueAttribute metaTitle = new KeyValueAttribute();
		metaTitle.setAttributeCode("meta_title");
		
		KeyValueAttribute metaDescription = new KeyValueAttribute();
		metaDescription.setAttributeCode("meta_description");
		
		KeyValueAttribute url = new KeyValueAttribute();
		url.setAttributeCode("url_key");
		
		String uniqueName;
		if(urls.containsKey(productName)){
			int counter = urls.get(productName);
			uniqueName = productName + counter;
			urls.remove(productName);
			urls.put(productName, ++counter);
		}
		else{
			urls.put(productName, 1);
			uniqueName = productName;
		}
		uniqueName.replace((char) 34, (char) 0);
		if(magentoProduct.getPrice() > 150){
			metaTitle.setValue(uniqueName + " на топ цена и на изплащане от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
			metaDescription.setValue("Купете днес " + uniqueName + " на изплащане и на супер цена с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
			url.setValue(uniqueName + "-cena-na-izplashtane");
		}
		else{
			metaTitle.setValue(uniqueName + " цена без конкуренция от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
			metaDescription.setValue("Купете днес " + uniqueName + " на страхотна цена с безплатна доставка от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
			url.setValue(uniqueName + "-cena-za-balgaria");
		}
		System.out.println(url.getValue());
		magentoProduct.getCustomAttributes().add(url);
		magentoProduct.getCustomAttributes().add(metaDescription);
		magentoProduct.getCustomAttributes().add(metaTitle);
		
		KeyListAttribute categories = new KeyListAttribute();
		categories.setAttributeCode("category_ids");
		categories.setValues(generateCategories(params, brand.getValue()));
		magentoProduct.getCustomAttributes().add(categories);

		return magentoProduct;
	}

	private ArrayList<String> generateCategories(String[] params, String brand) {
		ArrayList<String> categories = new ArrayList<String>();
		int size = params.length;
		categories.add(magentoCategories.get("main"));
		categories.add(params[1]);
		if(params.length >= 2){
			if(params[1].equals("4")){
				categories.add(magentoCategories.get("laptops" + brand.toLowerCase()));
			}
			if(params[1].equals("5")){
				categories.add(magentoCategories.get("tablets" + brand.toLowerCase()));
			}
			if(params[1].equals("6")){
				categories.add(magentoCategories.get("accessory"));
				if(params.length >= 3){
					categories.add(params[2]);
				}
			}
		}
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

	private String trimName(String name, int spaces) {
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
		int counter = 0;
		for(int i = 0; i < name.length(); i++){
			if(name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == (char) 047 || name.charAt(i) == '/'){
				break;
			}
			if(name.charAt(i) == ' '){
				counter++;
			}
			if(counter > spaces){
				break;
			}
			st.append(name.charAt(i));
		}
		return st.toString().trim();
	}
}
