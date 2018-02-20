package com.premiummobile.First.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.domain.StockInfoProduct;
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
	
	private HashMap<String, StockInfoProduct> stockInfoProducts;
	
	@Autowired
	public MainDownloader(RequestsExecutor requestExecutor, PropertiesLoader loader, MagentoMapper magentoMapper,ObjectMapper om){
		this.requestExecutor = requestExecutor;
		this.loader = loader;
		this.magentoMapper = magentoMapper;
		this.solytronCategories = loader.getSolytronCategories();
		this.stockInfoProducts = loader.loadStockInfoProducts();
		
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
			categoryProducts = new ArrayList<SolytronProduct>();
		}
		int productCounter = 0;
//		int productsSize = products.size();
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
//		List<MagentoProductRequest> magentoProducts = new ArrayList<MagentoProductRequest>();
		System.out.println(categoryProducts.size());
		long start = System.currentTimeMillis();
		long total = 0;
		for(SolytronProduct productSimple : categoryProducts){
			productCounter++;
//			if(productCounter < 18){
//				continue;
//			}
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
			MagentoProductRequest magentoProduct = magentoMapper.mapSolytronProduct(product, params);
			if(stockInfoProducts.containsKey(product.getCodeId())){
				boolean newValues = false;
				if(product.getStockInfoValue() != null){
					if(product.getStockInfoValue().contains("OnHand")){
						stockInfoProducts.get(product.getCodeId()).setInStock(true);
						stockInfoProducts.get(product.getCodeId()).setQty(5);
						if(stockInfoProducts.get(product.getCodeId()).getInStock() != magentoProduct.getExtensionAttributes().getItem().isStock()){
							newValues = true;
						}
						
					}
					else if(product.getStockInfoValue().contains("Minimum")){
						stockInfoProducts.get(product.getCodeId()).setInStock(true);
						stockInfoProducts.get(product.getCodeId()).setQty(2);
						if(stockInfoProducts.get(product.getCodeId()).getInStock() != magentoProduct.getExtensionAttributes().getItem().isStock()){
							newValues = true;
						}
					}
					else{
						stockInfoProducts.get(product.getCodeId()).setInStock(false);
						stockInfoProducts.get(product.getCodeId()).setQty(0);
						if(stockInfoProducts.get(product.getCodeId()).getInStock() != magentoProduct.getExtensionAttributes().getItem().isStock()){
							newValues = true;
						}
					}
				}
				if(newValues){
					try {
						requestExecutor.updateMagentoStockInfo(stockInfoProducts.get(product.getCodeId()));
						System.out.println("Product " + product.getCodeId() + " updated.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					System.out.println("Product " + product.getCodeId() + " has no changes.");
				}
			}
			else{
				StockInfoProduct infoProduct = new StockInfoProduct(magentoProduct.getSku(),magentoProduct.getPrice(), magentoProduct.getStatus(), 
						magentoProduct.getVisibility(), magentoProduct.getExtensionAttributes().getItem().getQty(), magentoProduct.getExtensionAttributes().getItem().isStock());
				stockInfoProducts.put(magentoProduct.getSku(), infoProduct);
				System.out.println("Product " + product.getCodeId() + " is new and is persisted in Magento2.");
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
			}
//			if(productCounter >= 8){
//				break;
//			}
			productsNew.add(product);
			long time2 = System.currentTimeMillis();
			System.out.println("Product upload duration: " + (time2-time));
			System.out.println("==========================");
			total += time2-time;
		}
		long end = System.currentTimeMillis();
		loader.saveStockInfoProducts(stockInfoProducts);
		System.out.println("Total time: " + (double)(end-start)/1000 + "s, average time for upload: " + total/productsNew.size() + "ms");
		System.out.println("loader: " + stockInfoProducts.size());
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
