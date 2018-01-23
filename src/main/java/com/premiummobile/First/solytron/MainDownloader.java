package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.premiummobile.First.solytron.Model.ProductSet;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.util.PropertiesLoader;
import com.premiummobile.First.util.RequestsExecutor;

@Component
public class MainDownloader {
	
	
	@Autowired
	private RequestsExecutor downloader;
	
	@Autowired
	private PropertiesLoader propertiesLoader;
	
	@Autowired
	private MagentoMapper magentoMapper;
	
	public List<Object> downloadCategories() throws Exception{
		Properties properties = propertiesLoader.getSolytron();
		ProductSet productSet = downloader.getCategorySolytron(properties.getProperty("pc.laptop"));
		List<SolytronProduct> products = productSet.getProducts();
		int productCounter = 0;
		int productsSize = products.size();
		for(SolytronProduct product : products){
			productCounter++;
			product = downloader.getProductSolytron(product.getProductId());
			System.out.println(productCounter + " " + String.valueOf(((float) productCounter/productsSize)*100).substring(0, 1) + "%");
			System.out.print("\r");
			if(productCounter >= 10){
				break;
			}
		}
		List<Object> results = new ArrayList<Object>();
		results.add(products);
		return results;
	}
}
