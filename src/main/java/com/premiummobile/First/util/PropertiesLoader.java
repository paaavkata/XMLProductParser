package com.premiummobile.First.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.domain.StockInfoProduct;

@Component
public class PropertiesLoader {
	
	private final String solytron = "solytron.properties";
	private final String stantek = "stantek.properties";
	private final String magentoAttributes = "magento2attr.properties";
	private final String magentoCategories = "magento2categories.properties";
	private final String magentoAttributesValues = "magento2laptop.properties";
	private final String solytronLaptop = "solytronLaptop.properties";
	private final String solytronTablet = "solytronTablet.properties";
	private final String solytronCategories = "solytronCategories.properties";
	private final String filePath = "StockInfo.json";
	private final String brands = "brands.properties";
	private final String colors = "colors.properties";
	
	@Autowired
	ObjectMapper om;
	
	public HashMap<String, String> getSolytronLaptop(){
		return load(solytronLaptop);
	}
	
	public HashMap<String, String> getBrands(){
		return load(brands);
	}
	
	public HashMap<String, String> getColors(){
		return load(colors);
	}
	
	public HashMap<String, String> getSolytronCategories(){
		return load(solytronCategories);
	}
	
	public HashMap<String, String> getSolytronTablet(){
		return load(solytronTablet);
	}
	
	public HashMap<String, String> getSolytron(){
		return load(solytron);
	}
	
	public HashMap<String, String> getStantek(){
		return load(stantek);
	}
	
	public HashMap<String, String> getMagento(){
		return load(magentoAttributes);
	}
	
	public HashMap<String, String> getMagentoAttributes(){
		return load(magentoAttributesValues);
	}
	
	public HashMap<String, String> getMagentoCategories(){
		return load(magentoCategories);
	}
	
	private HashMap<String, String> load(String fileName){
		Properties properties = new Properties();
		
		try {
			InputStream fistream = getClass().getClassLoader().getResourceAsStream(fileName);
			DataInputStream in = new DataInputStream(fistream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			properties.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashMap<String, String> propertiesMap = new HashMap<String, String>();
		for(Entry<Object, Object> property : properties.entrySet()){
			propertiesMap.put(String.valueOf(property.getKey()), String.valueOf(property.getValue()));
		}
		return propertiesMap;
	}

	public HashMap<String, String> getMagentoAttributesReversed() {
		HashMap<String, String> propertiesMap = new HashMap<String, String>();
		for(Entry<String, String> e : load(magentoAttributesValues).entrySet()){
			propertiesMap.put(String.valueOf(e.getValue()), String.valueOf(e.getKey()));
		}
		return propertiesMap;
	}

	public HashMap<String, StockInfoProduct> loadStockInfoProducts() {
		File file = new File(filePath);
		List<StockInfoProduct> productList = null;
		if(file.exists() && !file.isDirectory() && file.length() != 0){
			try {
				productList = om.readValue(file, new TypeReference<List<StockInfoProduct>>(){});
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		HashMap<String, StockInfoProduct> products = new HashMap<String, StockInfoProduct>();
		if(productList != null){
			for(StockInfoProduct data : productList){
				products.put(data.getSku(), data);
			}
		}
		else{
			return new HashMap<String, StockInfoProduct>();
		}
		
		return products;
	}
	
	public String saveStockInfoProducts(HashMap<String, StockInfoProduct> products){
		List<StockInfoProduct> productList = new ArrayList<StockInfoProduct>();
		for(Entry<String, StockInfoProduct> product : products.entrySet()){
			productList.add(product.getValue());
		}
		File file = new File(filePath);
		String json = "";
		if(!file.exists() && !file.isDirectory()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			json = om.writeValueAsString(productList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		try (PrintWriter printer = new PrintWriter(file)){
			printer.println(json);
			if(json != null && printer != null){
				return "Success";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "Not saved!";
	}
}
