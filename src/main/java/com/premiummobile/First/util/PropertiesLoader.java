package com.premiummobile.First.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PropertiesLoader {
	
	private final String solytron = "solytron.properties";
	private final String stantek = "stantek.properties";
	private final String magentoAttributes = "magento2attr.properties";
	private final String magentoAttributesValues = "magento2laptopattributes.properties";
	private final String solytronLaptop = "solytronLaptop.properties";
	private final String solytronTablet = "solytronTablet.properties";
	private final String solytronCategories = "solytronCategories.properties";
	
	@Bean
	public HashMap<String, String> getSolytronLaptop(){
		return load(solytronLaptop);
	}
	
	@Bean
	public HashMap<String, String> getSolytronCategories(){
		return load(solytronCategories);
	}
	
	@Bean
	public HashMap<String, String> getSolytronTablet(){
		return load(solytronTablet);
	}
	
	@Bean
	public HashMap<String, String> getSolytron(){
		return load(solytron);
	}
	
	@Bean
	public HashMap<String, String> getStantek(){
		return load(stantek);
	}
	
	@Bean
	public HashMap<String, String> getMagento(){
		return load(magentoAttributes);
	}
	
	@Bean
	public HashMap<String, String> getMagentoAttributes(){
		return load(magentoAttributesValues);
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
}
