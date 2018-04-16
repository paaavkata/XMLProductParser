package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttributeRequest;
import com.premiummobile.First.magento.KeyListAttribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.magento.MagentoStockItemRequest;
import com.premiummobile.First.solytron.Model.Image;
import com.premiummobile.First.solytron.Model.Property;
import com.premiummobile.First.solytron.Model.PropertyGroup;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.stantek.Model.StantekProduct;
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
	
	public MagentoProductRequest mapSolytronProduct(SolytronProduct product, String[] params){
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		HashMap<Integer, Property> properties = new HashMap<Integer, Property>();
		KeyValueAttribute productGroup = new KeyValueAttribute();
		productGroup.setAttributeCode("product_group");
		if(product.getProperties().size() > 0){
			for(PropertyGroup g : product.getProperties()){
				productGroup.setValue(g.getProductGroupName() != null ? g.getProductGroupName() : "");
				if(g.getList() != null){
					for(Property property : g.getList()){
						properties.put(property.getPropertyId(), property);
					}
				}
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
		else if(product.getPrice() != null){
			if(product.getPrice().getCurrency().equals("BGN")){
				magentoProduct.setPrice(Double.valueOf(product.getPrice().getText()) * 1.2 * 1.3);
			}
			else if(product.getPrice().getCurrency().equals("EUR")){
				magentoProduct.setPrice(Integer.valueOf(product.getPrice().getText()) * 1.96 * 1.3);
			}
		}
		if(params[1] == "3" || params[1] == "4" || params[1] == "5"){
			magentoProduct.setName(trimName(product.getName(), 4));
		}
		else{
			magentoProduct.setName(trimName(product.getName(), 6));
		}
		if(!magentoProduct.getName().toLowerCase().contains(product.getVendor().toLowerCase())){
			magentoProduct.setName(product.getVendor() + " " + magentoProduct.getName());
		}
		String productName = magentoProduct.getName();
		magentoProduct.setTypeId("simple");
		magentoProduct.setWeight(Double.valueOf(properties.get(48) != null ? properties.get(48).getValue().get(0).getText() : "1"));
		
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
		else{
			magentoStockItem.setStock(false);
			magentoStockItem.setQty(0);
		}
		magentoProduct.setExtensionAttributes(new ExtensionAttributeRequest());
		magentoProduct.getExtensionAttributes().setItem(magentoStockItem);
		switch(params[1]){
			case "4":
				if(properties.size() > 0){
					magentoProduct.setCustomAttributes(helper.generateLaptopAttributes(properties));
				}
				magentoProduct.setAttributeSetId(10);
				break;
			
			case "5":
				if(properties.size() > 0){
					magentoProduct.setCustomAttributes(helper.generateTabletAttributes(properties));
				}
				magentoProduct.setAttributeSetId(11);
				break;
			
			case "6":
				if(properties.size() > 0){
					magentoProduct.setCustomAttributes(helper.generateTabletAttributes(properties));
				}
				magentoProduct.setAttributeSetId(12);
				break;
			
			default:
				if(properties.size() > 0){
					magentoProduct.setCustomAttributes(helper.generateSimpleAttributes(properties));
				}
				magentoProduct.setAttributeSetId(4);
				break;
		}
		if(magentoProduct.getCustomAttributes() == null){
			magentoProduct.setCustomAttributes(new ArrayList<Attribute>());
		}		
		
		magentoProduct.getCustomAttributes().add(productGroup);
		
		KeyValueAttribute brand = new KeyValueAttribute();
		brand.setAttributeCode("manufacturer");
		if(product.getVendor() == null){
			brand.setValue(helper.generateBrand(productName));
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
		String productName2 = productName.replace((char) 34, (char) 0);
		if(urls.containsKey(productName2)){
			int counter = urls.get(productName);
			uniqueName = productName + counter;
			urls.remove(productName);
			urls.put(productName, ++counter);
		}
		else{
			urls.put(productName, 1);
			uniqueName = productName;
		}
//		uniqueName.replace((char) 34, (char) 0);
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
		
		KeyValueAttribute ean = new KeyValueAttribute();
		ean.setAttributeCode("ean");
		ean.setValue(product.getEan());
		magentoProduct.getCustomAttributes().add(ean);
		
		KeyListAttribute categories = new KeyListAttribute();
		categories.setAttributeCode("category_ids");
		categories.setValue(generateCategories(params, brand.getValue()));
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
			else if(params[1].equals("5")){
				categories.add(magentoCategories.get("tablets" + brand.toLowerCase()));
			}
			else if(params[1].equals("6")){
				categories.add(magentoCategories.get("accessory"));
				if(params.length >= 3){
					categories.add(params[2]);
				}
			}
			if(params.length >= 4){
				categories.add(params[3]);
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
		name = name.replace("&quot", "");
		name = name.replace("\"", "");
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
	
	public MagentoProductRequest mapStantekProduct(StantekProduct product){
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		
		
		return magentoProduct;
	}
}
