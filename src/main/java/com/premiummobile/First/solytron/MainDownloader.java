package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	private HashMap<String, String> solytronCategories;
	
	@Autowired
	public MainDownloader(RequestsExecutor requestExecutor, PropertiesLoader loader, MagentoMapper magentoMapper,ObjectMapper om){
		this.requestExecutor = requestExecutor;
		this.loader = loader;
		this.magentoMapper = magentoMapper;
		solytronCategories = loader.getSolytronCategories();
	}
	public int downloadCategory(String category){
		//0.url, 1.category , 2.subcategory
		String[] params = solytronCategories.get(category).split(",");
		List<SolytronProduct> categoryProducts;
		try{
			categoryProducts = requestExecutor.getCategorySolytron(params[0]);
		}
		catch(Exception e){
			System.out.println("Error while downloading category " + params[0]);
			categoryProducts = null;
		}
		int productCounter = 0;
//		int productsSize = products.size();
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
//		List<MagentoProductRequest> magentoProducts = new ArrayList<MagentoProductRequest>();
		System.out.println(categoryProducts.size());
		for(SolytronProduct productSimple : categoryProducts){
			productCounter++;
			if(productCounter < 232){
				continue;
			}
			long time = System.currentTimeMillis();
			System.out.println(productCounter);
			SolytronProduct product;
			try{
			product = requestExecutor.getProductSolytron(productSimple);
			}
			catch(Exception e){
				System.out.println("Error while downloading product " + productSimple.getCodeId());
				product = null;
			}
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
			MagentoProductRequest magentoProduct = magentoMapper.mapProduct(product, params);
//			magentoProducts.add(magentoProduct);
			try{
				requestExecutor.newMagentoProduct(magentoProduct);
			}
			catch(Exception e){
				System.out.println("Error while uploading product " + product.getCodeId() + " to Magento2");
			}
			try{
				magentoMapper.downloadImages(product, magentoProduct);
			}
			catch(Exception e){
				System.out.println("Error while downloading images for product " + product.getCodeId());
			}
			if(productCounter >= 250){
				break;
			}
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
		return attributes;
	}
	public List<String> downloadMagentoCategories() throws Exception {
		List<String> categories = requestExecutor.downloadMagentoCategories();
		return categories;
	}
}
