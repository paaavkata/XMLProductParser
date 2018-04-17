package com.premiummobile.First.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.premiummobile.First.domain.StockInfoProduct;
import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.KeyValueAttribute;
import com.premiummobile.First.magento.MagentoAttribute;
import com.premiummobile.First.magento.MagentoPriceUpdater;
import com.premiummobile.First.magento.MagentoProductResponse;
import com.premiummobile.First.magento.MagentoSiteMapUrlXML;
import com.premiummobile.First.magento.MagentoSiteMapXML;
import com.premiummobile.First.solytron.MainDownloader;
import com.premiummobile.First.util.PropertiesLoader;
import com.premiummobile.First.util.RequestsExecutor;

@Controller
public class MagentoController {
	
	@Autowired
	private MainDownloader downloader;
	
	@Autowired
	private MagentoPriceUpdater updater;
	
	@Autowired
	private RequestsExecutor executor;
	
	@Autowired
	private PropertiesLoader loader;
	
	@PostMapping("/magentoprices")
	@ResponseBody
	public List<String> uploadPrices(@RequestParam("file") MultipartFile file) throws Exception{
		return updater.mapFile(file);
	}
	
	@GetMapping("/cachewarmer")
	@ResponseBody
	public List<HashMap<String, String>> warmPremiumMobileCache() throws Exception{
		List<HashMap<String, String>> responses = new ArrayList<HashMap<String, String>>();

		MagentoSiteMapXML siteMap = executor.getMagentoSitemap();
		HashMap<String, String> error503 = new HashMap<String, String>();
		HashMap<String, String> error404 = new HashMap<String, String>();
		HashMap<String, String> error502 = new HashMap<String, String>();
		HashMap<String, String> status200 = new HashMap<String, String>();
		long totalTime = 0;
		int counter = 0;
		for(MagentoSiteMapUrlXML url : siteMap.getList()){
			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response = executor.getClient().execute(new HttpGet(url.getUrl()));
			long endTime = System.currentTimeMillis() - startTime;
			HttpEntity httpEntity = response.getEntity();
//			String responseString = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
			
			switch(response.getStatusLine().getStatusCode()){
				case 503:
					error503.put(url.getUrl(), response.getStatusLine().toString());
					break;
				case 404:
					error404.put(url.getUrl(), response.getStatusLine().toString());
					break;
				case 502:
					error502.put(url.getUrl(), response.getStatusLine().toString());
					break;
				case 200:
				default:
					status200.put(url.getUrl(), response.getStatusLine().toString());
					break;
			}
			
			System.out.println(url.getUrl() + ": " + response.getStatusLine().toString() + ", " + endTime);	
			counter++;
			totalTime += endTime;
		}
		System.out.println("Total Time: " + totalTime);
		System.out.println("Average Time: " + totalTime/counter);
		responses.add(error503);
		responses.add(error404);
		responses.add(error502);
		responses.add(status200);
		return responses;
	}
	
	
	@GetMapping("/readMagentoAttributes")
	@ResponseBody
	public List<MagentoAttribute> readAttributes() throws Exception{
		List<MagentoAttribute> response = downloader.downloadMagentoAttributes();
		
		return response;
	}
	
	@GetMapping("/readMagentoCategories")
	@ResponseBody
	public List<String> readCategories() throws Exception{
		List<String> response = downloader.downloadMagentoCategories();
		
		return response;
	}
	
	@GetMapping("/regenerateUrls")
	@ResponseBody
	public List<String> regenerateUrls() throws Exception{
		List<String> response = new ArrayList<>();
		HashMap<String, StockInfoProduct> products = loader.loadStockInfoProducts();
		for(Entry<String, StockInfoProduct> entry : products.entrySet()) {
			MagentoProductResponse magentoProduct = executor.getMagentoProduct(entry.getKey());
			Attribute keyValueAttr = null;
			for(Attribute attribute : magentoProduct.getCustomAttributes()) {
				if("url_key".equals(attribute.getAttributeCode())){
					keyValueAttr = attribute;
				}
			}
			if(entry.getValue().getPrice() > 150) {
//				keyValueAttr.setValue(magentoProduct.getName().replaceAll(" ", "-") + "-cena-na-izplashtane-" + magentoProduct.getSku());
			}
			else {
				keyValueAttr.setValue(magentoProduct.getName().replaceAll(" ", "-") + magentoProduct.getSku());
				magentoProduct.setExtensionAttributes(null);
				magentoProduct.setMediaGalleryEntries(null);
				List<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(keyValueAttr);
				magentoProduct.setCustomAttributes(attributes);
				response.add(executor.updateMagentoProduct(magentoProduct).toString());
			}
		}
		return response;
	}

}
