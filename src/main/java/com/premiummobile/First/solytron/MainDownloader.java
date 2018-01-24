package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.magento.MagentoProduct;
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
	
	@Autowired
	private ObjectMapper om;
	
	public List<Object> downloadCategories() throws Exception{
		Properties properties = propertiesLoader.getSolytron();
		ProductSet productSet = downloader.getCategorySolytron(properties.getProperty("pc.laptop"));
		List<SolytronProduct> products = productSet.getProducts();
		int productCounter = 0;
		int productsSize = products.size();
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
		for(SolytronProduct productSimple : products){
			productCounter++;
			SolytronProduct product = downloader.getProductSolytron(productSimple);
			product.setPrice(productSimple.getPrice());
			product.setGroupId(productSimple.getProductId());
			product.setCodeId(productSimple.getCodeId());
			product.setEan(productSimple.getEan());
			product.setVendor(productSimple.getVendor());
			product.setWarrantyQty(productSimple.getWarrantyQty());
			product.setWarrantyUnit(productSimple.getWarrantyUnit());
			product.setPriceEndUser(productSimple.getPriceEndUser());
			product.setStockInfoData(productSimple.getStockInfoData());
			product.setStockInfoValue(productSimple.getStockInfoValue());
//			System.out.println(productCounter + " " + String.valueOf(((float) productCounter/productsSize)*100).substring(0, 1) + "%");
//			System.out.print("\r");
			MagentoProduct magentoProduct = magentoMapper.mapProduct(product);
			String json = om.writeValueAsString(magentoProduct);
			System.out.println(json);
//			if(productCounter >= 30){
//				break;
//			}
			productsNew.add(product);
		}
		List<Object> results = new ArrayList<Object>();
		results.add(productsNew);
		return results;
	}
}
