package com.premiummobile.First.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PropertiesLoader {
	
	private final String solytronFile = "solytron.properties";
	private final String stantekFile = "stantek.properties";
	private final String magentoAttributesFile = "magento2attr.properties";
	
	@Bean
	public Properties getSolytron(){
		
		return load(solytronFile);
	}
	@Bean
	public Properties getStantek(){
		return load(stantekFile);
	}
	@Bean
	public Properties getMagento(){
		return load(magentoAttributesFile);
	}
	
	private Properties load(String fileName){
		Properties properties = new Properties();
		
		try {
			InputStream fistream = getClass().getClassLoader().getResourceAsStream(fileName);
			DataInputStream in = new DataInputStream(fistream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			properties.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
