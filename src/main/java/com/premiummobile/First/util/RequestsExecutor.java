package com.premiummobile.First.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.imgscalr.Scalr;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.premiummobile.First.domain.StockInfoProduct;
import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttributeRequest;
import com.premiummobile.First.magento.ItemResponse;
import com.premiummobile.First.magento.MagentoAttribute;
import com.premiummobile.First.magento.MagentoPostProduct;
import com.premiummobile.First.magento.MagentoProduct;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.magento.MagentoProductResponse;
import com.premiummobile.First.magento.MagentoSiteMapXML;
import com.premiummobile.First.magento.MediaGalleryContent;
import com.premiummobile.First.magento.MediaGalleryEntry;
import com.premiummobile.First.magento.MediaGalleryEntryWrapper;
import com.premiummobile.First.magento.Option;
import com.premiummobile.First.magento.ProductLink;
import com.premiummobile.First.magento.TierPrice;
import com.premiummobile.First.solytron.Model.Category;
import com.premiummobile.First.solytron.Model.ProductSet;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.stantek.Model.StantekPriceList;
	
@Component
public class RequestsExecutor {
	
	private PropertiesLoader loader;
	
	private ObjectMapper om;
	
	private String magentoToken;
	
	private Serializer serializer;
	
	private HashMap<String, String> solytronProperties;
	
	private HashMap<String, String> stantekProperties;
	
	private HashMap<String, String> magentoProperties;
	
	@Autowired
	public RequestsExecutor(PropertiesLoader loader, ObjectMapper om) throws Exception {
		this.om = om;
		this.loader = loader;
		this.solytronProperties = loader.getSolytron();
		this.stantekProperties = loader.getStantek();
		this.magentoProperties = loader.getMagento();
//		this.magentoAuthenticate();
	}
	
	@Bean
	public CloseableHttpClient getClient(){
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(30);
		manager.setDefaultMaxPerRoute(20);
		CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).build();
		
		return client;
	}
	
//	@Bean
//	public ChannelSftp getFtpClient() throws JSchException{
//		JSch jsch = new JSch();
//		Session session;
//		session = jsch.getSession("premiummobile", "www.premiummobile.bg", 22);
//        session.setConfig("StrictHostKeyChecking", "no");
//        session.setPassword("FhIV4jvf7Hkli1mi");
//		session.connect();
//
//		Channel channel = session.openChannel( "sftp" );
//		channel.connect();
//
//		ChannelSftp sftpChannel = (ChannelSftp) channel;
//		return sftpChannel;
//
//	}
	private void magentoAuthenticate() throws Exception{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("authorization"));
		HttpPost httpPost = new HttpPost(builder.build());
		Map<String, String> data = new HashMap<>();
		data.put("username", magentoProperties.get("username"));
		data.put("password", magentoProperties.get("password"));
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(om.writeValueAsString(data)));
		CloseableHttpResponse response = this.getClient().execute(httpPost);
		if (response.getEntity() != null) {
			this.magentoToken = EntityUtils.toString(response.getEntity(), "UTF-8").replaceAll("\"", "");
			if(this.magentoToken.length() != 32){
				this.magentoToken = null;
				System.out.println("There was an error while authenticating to Magento2.");
			}
			else{
				System.out.println(this.magentoToken + " <======== MAGENTO TOKEN");
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						magentoToken = null;
					}
				}, 4 * 60 * 60 * 1000);
			}
        }
	}
	
	private String magentoToken() throws Exception{
		if(this.magentoToken != null){
			return this.magentoToken;
		}
		else{
			magentoAuthenticate();
			if(this.magentoToken != null){
				return this.magentoToken;
			}
			else{
				throw new Exception("There was an error while trying to authenticate to magento!");
			}
		}
	}
	
	private Serializer getSerializer(){
		if(serializer != null){
			return serializer;
		}
		else{
			serializer = new Persister();
			return serializer;
		}
	}

	public StatusLine newMagentoProduct(MagentoProductRequest product) throws Exception{
		CloseableHttpClient client = this.getClient();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("product") + "/" + product.getSku());
		HttpPut httpPut = new HttpPut(builder.build());
		httpPut.addHeader("Authorization", "Bearer " + magentoToken());
		httpPut.addHeader("Content-Type", "application/json");
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(generateInitialProduct(product));
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
		httpPut.setEntity(params);
		CloseableHttpResponse response = client.execute(httpPut);
		StatusLine statusLine;
		if(response.getStatusLine().getStatusCode() == 200){
			statusLine = updateMagentoProduct(product);
			return statusLine;
		}
		else{
			System.out.println(om.writeValueAsString(postProduct));
			System.out.println("Product upload error: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
		response.close();
		client.close();
		return response.getStatusLine();
	}
	
	public StatusLine updateMagentoProduct(MagentoProduct product) throws Exception{
		CloseableHttpClient client = this.getClient();
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(product);
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
		System.out.println("Product Json: ");
		System.out.println(om.writeValueAsString(postProduct));
//		System.out.println("Product: " + product.getName() + " " + product.getSku());
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("product"));
		HttpPost httpPost = new HttpPost(builder.build());
		httpPost.addHeader("Authorization", "Bearer " + magentoToken());
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println("Product response: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		System.err.println("Product status: " + response.getStatusLine().getStatusCode());
		response.close();
		client.close();
		return response.getStatusLine();
	}
	
	private MagentoProductRequest generateInitialProduct(MagentoProductRequest product) {
		MagentoProductRequest initial = new MagentoProductRequest();
		initial.setName(product.getName());
		initial.setSku(product.getSku());
		initial.setPrice(product.getPrice());
		initial.setAttributeSetId(product.getAttributeSetId());
		initial.setStatus(1);
		initial.setVisibility(4);
		initial.setOptions(new ArrayList<Option>());
		initial.setTypeId("simple");
		initial.setExtensionAttributes(new ExtensionAttributeRequest());
		initial.setProductLinks(new ArrayList<ProductLink>());
//		initial.setMediaGalleryEntries(new ArrayList<MediaGalleryEntry>());
		initial.setTierPrices(new ArrayList<TierPrice>());
		initial.setCustomAttributes(new ArrayList<Attribute>());
		return initial;
	}

	public SolytronProduct getProductSolytron(SolytronProduct product) throws Exception{
		CloseableHttpResponse response;
		HttpGet httpGet = generateGetRequest(product.getProductId(), "product", "solytron");
		response = getClient().execute(httpGet);
		product = getSerializer().read(SolytronProduct.class, response.getEntity().getContent());
		return product;
	}
	
	public List<SolytronProduct> getCategorySolytron(String code) throws Exception{
		CloseableHttpResponse response;
		HttpGet httpGet = generateGetRequest(code, "category", "solytron");
		response = getClient().execute(httpGet);
		ProductSet productSet = getSerializer().read(ProductSet.class, response.getEntity().getContent());
		return productSet.getProducts();
	}
	
	
	private HttpGet generateGetRequest(String code, String type, String provider) throws Exception{
		URIBuilder builder = new URIBuilder();
		String path = "";
		builder.setScheme("http").setHost(solytronProperties.get("domainname"));
		if(type.equals("product")){
			path = solytronProperties.get("options.product");
			builder.setPath(path).setParameter("productId", code);
		}
		else if(type.equals("category")){
			path = solytronProperties.get("options.category");
			builder.setPath(path).setParameter("propertyId", code);
		}
		HttpGet httpGet;
		builder.setParameter("j_u", solytronProperties.get("username"))
				.setParameter("j_p", solytronProperties.get("password"));
		httpGet = new HttpGet(builder.build());
		return httpGet;
		
	}
	

	public List<MagentoAttribute> downloadMagentoAttributes() throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("attributes"));
		builder.setParameter("searchCriteria", "0");
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		httpGet.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = this.getClient().execute(httpGet);
		ItemResponse itemResponse = new ItemResponse();
		if (response.getEntity() != null) {
			itemResponse = om.readValue(EntityUtils.toString(response.getEntity()), ItemResponse.class);
//			System.out.println(EntityUtils.toString(response.getEntity()));
			HashMap<Integer, String> attributes = new HashMap<Integer, String>();
			TreeMap<String, String> options = new TreeMap<String, String>(); 
			for(MagentoAttribute attribute : itemResponse.getAttributes()){
				attributes.put(attribute.getId(), attribute.getName());
				if(attribute.getOptions().size() > 0){
//					System.out.println(attribute.getName());
					for(Option option : attribute.getOptions()){
						options.put(option.getValue(), option.getLabel());
//						System.out.println(option.getValue() + "=" + option.getLabel());
					}
				}
			}
//			for(Entry<Integer,String> entry : attributes.entrySet()){
//				System.out.println(entry.getKey() + "=" + entry.getValue());
//			}
//			for(Entry<String, String> entry : options.entrySet()){
//				System.out.println(entry.getKey() + "=" + entry.getValue());
//			}
        }
		return itemResponse.getAttributes();
	}
	
	public String uploadMagentoImage(MagentoProductRequest product, String imageUrl, int counter) throws Exception{
		CloseableHttpClient client = this.getClient();
//		System.out.println(imageUrl);
		URL url = new URL(imageUrl);
		BufferedImage img = ImageIO.read(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extension;
		if(imageUrl.contains("png")){
			extension = "png";
		}
		else{
			extension = "jpeg";
		}
		String imageName = product.getSku() + counter + "." + extension;
//		System.out.println(imageName);
		MediaGalleryEntry entry = new MediaGalleryEntry();
        entry.setMediaType("image");
        entry.setDisabled(false);
        entry.setLabel(product.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД");
        entry.setPosition(counter);
        entry.setFileName("solytron/" + imageName);

        ArrayList<String> types = new ArrayList<String>();
        if( counter == 1 ){
        	types.add("base");
        	types.add("small_image");
        	types.add("thumbnail");
        }
        entry.setTypes(types);
        
        MediaGalleryContent content = new MediaGalleryContent();
		content.setName(imageName);
		content.setType("image/" + extension);
		entry.setContent(content);
		if(img.getHeight() > 1200 || img.getWidth() > 1200){
			BufferedImage scaledImage = Scalr.resize(img, 1200, 1200);
			ImageIO.write(scaledImage, extension, baos);
		}
		else{
			ImageIO.write(img, extension, baos);
		}
		String base64EncodedData = new String(Base64.encodeBase64(baos.toByteArray()));
		entry.getContent().setData(base64EncodedData);
		MediaGalleryEntryWrapper wrapper = new MediaGalleryEntryWrapper();
		wrapper.setEntry(entry);
//		System.out.println("Entry Json:");
//		System.out.println(om.writeValueAsString(wrapper));
		StringEntity params = new StringEntity(om.writeValueAsString(wrapper), "UTF-8");
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("product") + "/" + product.getSku() + "/media");
		
		HttpPost httpPost = new HttpPost(builder.build());
		httpPost.addHeader("Authorization", "Bearer " + magentoToken());
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		
		CloseableHttpResponse response = client.execute(httpPost);
		String statusResponse = response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
		System.out.println("---->Image Upload Status: " + statusResponse);
		baos.close();
		response.close();
		client.close();
		return statusResponse;
	}

	public List<String> downloadMagentoCategories() throws Exception{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("categories"));
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		httpGet.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = this.getClient().execute(httpGet);
		Category category = new Category();
		if (response.getEntity() != null) {
			category = om.readValue(EntityUtils.toString(response.getEntity()), Category.class);
        }
		
		return walkCategories(category);
	}

	private List<String> walkCategories(Category category) {
		List<String> result = new ArrayList<String>();
		System.out.println(category.getName() + "=" + category.getId());
		result.add(category.getName() + "=" + category.getId());
		String categoryName = "";
		for(Category categoryInner : category.getChildrenData()){
			if(categoryInner.getName().equals("Смартфони")){
				categoryName = "Smartphone";
			}
			if(categoryInner.getName().equals("Лаптопи")){
				categoryName = "Laptops";
			}
			if(categoryInner.getName().equals("Таблети")){
				categoryName = "Tablets";
			}
			if(categoryInner.getName().equals("Аксесоари")){
				categoryName = "Accessory";
			}
			if(categoryInner.getName().equals("Дронове")){
				categoryName = "Drones";
			}
			System.out.println(categoryName + "=" + categoryInner.getId());
			result.add(categoryName + "=" + categoryInner.getId());
			for(Category categoryInnerInner : categoryInner.getChildrenData()){
				System.out.println(categoryName + categoryInnerInner.getName() + "=" + categoryInnerInner.getId());
				result.add(categoryName + categoryInnerInner.getName() + "=" + categoryInnerInner.getId());
			}
		}
		return result;
	}

	public void updateMagentoStockInfo(StockInfoProduct stockInfoProduct) throws Exception {
		this.updateMagentoProduct(stockInfoProduct);
		
	}
	
	public StantekPriceList getStantekFile(){
		HttpGet httpGet = new HttpGet(stantekProperties.get("url"));
		CloseableHttpResponse response;
		try {
			response = this.getClient().execute(httpGet);
		} catch (IOException e) {
			response = null;
			e.printStackTrace();
		}
		StantekPriceList priceList = null;
		if(response.getEntity() != null){
			try {
				priceList = getSerializer().read(StantekPriceList.class, response.getEntity().getContent());
			} catch (Exception e) {
				priceList = new StantekPriceList();
				e.printStackTrace();
			}
		}
		return priceList;
	}

	public MagentoProductResponse getMagentoProduct(String sku) throws Exception {
		CloseableHttpClient client = this.getClient();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("product") + "/" + sku);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		CloseableHttpResponse response = client.execute(httpGet);
		MagentoProductResponse product;
		if(response.getStatusLine().getStatusCode() == 200){
			product = om.readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), MagentoProductResponse.class);
			return product;
		}
		response.close();
		client.close();
		return null;
	}
	
	public MagentoSiteMapXML getMagentoSitemap() throws Exception{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(magentoProperties.get("host") + "/" + magentoProperties.get("siteMapUri"));
		HttpGet httpGet = new HttpGet(builder.build());
		CloseableHttpResponse response = getClient().execute(httpGet);
		MagentoSiteMapXML siteMap = getSerializer().read(MagentoSiteMapXML.class, response.getEntity().getContent());
		return siteMap;
	}
}
