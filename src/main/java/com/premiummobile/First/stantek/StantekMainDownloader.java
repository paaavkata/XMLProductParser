package com.premiummobile.First.stantek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.domain.StockInfoProduct;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.solytron.MagentoMapper;
import com.premiummobile.First.stantek.Model.StantekPriceList;
import com.premiummobile.First.stantek.Model.StantekProduct;
import com.premiummobile.First.util.PropertiesLoader;
import com.premiummobile.First.util.RequestsExecutor;

@Component
public class StantekMainDownloader {

	private RequestsExecutor requestExecutor;

	private PropertiesLoader loader;

	private MagentoMapper magentoMapper;

	private HashMap<String, String> stantekProperties;

	private HashMap<String, StockInfoProduct> stockInfoProducts;

	@Autowired
	public StantekMainDownloader(RequestsExecutor requestExecutor, PropertiesLoader loader, MagentoMapper magentoMapper,
			ObjectMapper om) {
		this.requestExecutor = requestExecutor;
		this.loader = loader;
		this.magentoMapper = magentoMapper;
		this.stantekProperties = loader.getStantek();
		this.stockInfoProducts = loader.loadStockInfoProducts();

	}

	public String downloadFile() {
		StantekPriceList priceList = requestExecutor.getStantekFile();
		List<StantekProduct> products = priceList.getProducts();
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		ArrayList<String> names = new ArrayList<String>();

		for (StantekProduct product : products) {
			if(product.getGroup().equals("Notebook")){
				MagentoProductRequest magentoProduct = new MagentoProductRequest();
				HashMap<String, String> values;
				if (product.getDescription() != null) {
					values = getValues(product.getDescription());
				} else {
					values = new HashMap<String, String>();
				}
				magentoProduct.setSku(generateSku(product, values));
				magentoProduct.setName(generateName(product));
				System.out.println(magentoProduct.getName());
			}
		}
		return "";
	}

	private String generateName(StantekProduct product) {
		String name = product.getName();
		name = name.replaceAll("РАЗПРОДАЖБА! ", "");
		name = name.replaceAll("Hewlett Packard ", "");
		name = name.replaceAll("NB ", "");
		name = name.replaceAll("NEW! ", "");
		name = name.replaceAll("PROMO! ", "");
		name = name.replaceAll("Ultrabook ", "");
		name = name.replaceAll("Tablet", "");
		name = name.replaceAll("&quot", "");
		StringBuilder st = new StringBuilder();
		int spaces = 0;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == '('
					|| name.charAt(i) == (char) 047) {
				break;
			}
			if(name.charAt(i) == ' '){
				spaces++;
				if(spaces >= 6){
					break;
				}
			}
			st.append(name.charAt(i));
		}
		return st.toString();
	}

	private String generateSku(StantekProduct product, HashMap<String, String> values) {
		StringBuilder st = new StringBuilder();
		String name = product.getName();
		String sku = "";
		if (product.getDescription() != null) {
			if (values.containsKey("Партиден номер")) {
				sku = values.get("Партиден номер");
			} else {
				if (!name.contains(",")) {
					for (int i = name.length() - 1; i >= name.length() - 40; i--) {
						if (name.charAt(i) == ' ') {
							break;
						}
						st.append(name.charAt(i));
					}
					sku = st.reverse().toString();
				} else {
					boolean shouldBrake = false;
					for (int i = 0; i < name.length(); i++) {
						if (name.charAt(i) == ',') {
							for (int j = i + 2; i < name.length(); j++) {
								if (j >= name.length() - 1) {
									shouldBrake = true;
									break;
								}
								if (name.charAt(j) == ',' || name.charAt(j) == ' ') {
									shouldBrake = true;
									break;
								}
								st.append(name.charAt(j));
							}
							if (shouldBrake) {
								break;
							}
						}
					}
				}
			}
		}
		sku = st.toString();
		return sku;
	}

	public HashMap<String, String> getValues(String desc) {
		HashMap<String, String> values = new HashMap<String, String>();
		for (int i = 0; i < desc.length(); i++) {
			if (desc.charAt(i) == '[') {
				i++;
				StringBuilder st1 = new StringBuilder();
				while (i < desc.length()) {
					if (desc.charAt(i) == ']' || desc.charAt(i) == ':') {
						break;
					}
					st1.append(desc.charAt(i));
					i++;
				}
				i += 2;
				StringBuilder st2 = new StringBuilder();
				while (i < desc.length()) {
					if (i > desc.length() - 1 || desc.charAt(i) == '\n') {
						break;
					}
					st2.append(desc.charAt(i));
					i++;
				}
				values.put(st1.toString().trim(), st2.toString().trim());
				// System.out.println(st1.toString().trim() + " " +
				// st2.toString().trim());
				// System.out.println("=======================================");
			}
		}
		return values;
	}
}
