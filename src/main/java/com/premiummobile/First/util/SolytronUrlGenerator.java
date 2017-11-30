package com.premiummobile.First.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolytronUrlGenerator {
	
	@Autowired
	PropertiesLoader propLoader;
	
	
	public URL generateSolytronProduct(String productId) throws MalformedURLException{
		Properties solytronProperties = propLoader.getSolytron();
		StringBuilder st = new StringBuilder();
		st.append(solytronProperties.getProperty("domainname"));
		st.append(solytronProperties.getProperty("options.product"));
		st.append(productId);
		st.append("&j_u=");
		st.append(solytronProperties.getProperty("username"));
		st.append("&j_p=");
		st.append(solytronProperties.getProperty("password"));
		URL url = new URL(st.toString());
		return url;
	}
	
	public URL generateSolytronCategory(String categoryId) throws MalformedURLException{
		Properties solytronProperties = propLoader.getSolytron();
		StringBuilder st = new StringBuilder();
		st.append(solytronProperties.getProperty("domainname"));
		st.append(solytronProperties.getProperty("options.category"));
		st.append(solytronProperties.getProperty(categoryId));
		st.append("&j_u=");
		st.append(solytronProperties.getProperty("username"));
		st.append("&j_p=");
		st.append(solytronProperties.getProperty("password"));
		URL url = new URL(st.toString());
		return url;
	}
}
