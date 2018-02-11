package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.util.PropertiesLoader;
import com.premiummobile.First.util.RequestsExecutor;

@Component
public class MainDownloader {
	
	
	private RequestsExecutor requestExecutor;
	
	private PropertiesLoader loader;
	
	private MagentoMapper magentoMapper;
	
	private ObjectMapper om;
	
	private HashMap<String, String> solytronCategories;
	
	@Autowired
	public MainDownloader(RequestsExecutor requestExecutor, PropertiesLoader loader, MagentoMapper magentoMapper,ObjectMapper om){
		this.requestExecutor = requestExecutor;
		this.loader = loader;
		this.magentoMapper = magentoMapper;
		this.om = om;
		solytronCategories = loader.getSolytronCategories();
	}
	public int downloadCategory(String category) throws Exception{
		List<SolytronProduct> categoryProducts = requestExecutor.getCategorySolytron(solytronCategories.get(category));
//		int productCounter = 0;
//		int productsSize = products.size();
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
		List<MagentoProductRequest> magentoProducts = new ArrayList<MagentoProductRequest>();
		for(SolytronProduct productSimple : categoryProducts){
			long time = System.currentTimeMillis();
//			productCounter++;
			SolytronProduct product = requestExecutor.getProductSolytron(productSimple);
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
			MagentoProductRequest magentoProduct = magentoMapper.mapProduct(product, category);
//			magentoProducts.add(magentoProduct);
			requestExecutor.newMagentoProduct(magentoProduct);
			magentoMapper.downloadImages(product, magentoProduct);
//			if(productCounter >= 10){
//				break;
//			}
			productsNew.add(product);
			long time2 = System.currentTimeMillis();
			System.out.println("Total time: " + (time2-time));
			System.out.println("==========================");
		}
		if(productsNew.size() != 0){
			return productsNew.size();
		}
		return -1;
	}

	public List<String> downloadMagentoAttributes() throws Exception {
		List<String> attributes = requestExecutor.downloadMagentoAttributes();
		return null;
	}
}
