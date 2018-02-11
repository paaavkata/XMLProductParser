package com.premiummobile.First.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
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
import com.premiummobile.First.magento.Attribute;
import com.premiummobile.First.magento.ExtensionAttribute;
import com.premiummobile.First.magento.ItemResponse;
import com.premiummobile.First.magento.MagentoAttribute;
import com.premiummobile.First.magento.MagentoPostProduct;
import com.premiummobile.First.magento.MagentoProductRequest;
import com.premiummobile.First.magento.MediaGalleryContent;
import com.premiummobile.First.magento.MediaGalleryEntry;
import com.premiummobile.First.magento.MediaGalleryEntryWrapper;
import com.premiummobile.First.magento.Option;
import com.premiummobile.First.magento.ProductLink;
import com.premiummobile.First.magento.TierPrice;
import com.premiummobile.First.solytron.Model.ProductSet;
import com.premiummobile.First.solytron.Model.SolytronProduct;
	
@Component
public class RequestsExecutor {
	
	private PropertiesLoader loader;
	
	private ObjectMapper om;
	
	private String magentoToken;
	
	private Serializer serializer;
	
	private HashMap<String, String> solytronProperties;
	
	private HashMap<String, String> magentoProperties;
	
	@Autowired
	public RequestsExecutor(PropertiesLoader loader, ObjectMapper om) throws Exception {
		this.om = om;
		this.loader = loader;
		this.solytronProperties = loader.getSolytron();
		this.magentoProperties = loader.getMagento();
		this.magentoAuthenticate();
	}
	
	@Bean
	public CloseableHttpClient getClient(){
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(30);
		manager.setDefaultMaxPerRoute(10);
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
			System.out.println(this.magentoToken);
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

	public String newMagentoProduct(MagentoProductRequest product) throws Exception{
		if(magentoToken != null){
			CloseableHttpClient client = this.getClient();
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost(magentoProperties.get("host"));
			builder.setPath(magentoProperties.get("product") + "/" + product.getSku());
			HttpPut httpPut = new HttpPut(builder.build());
			httpPut.addHeader("Authorization", "Bearer " + magentoToken);
			httpPut.addHeader("Content-Type", "application/json");
			MagentoPostProduct postProduct = new MagentoPostProduct();
			postProduct.setMagentoProduct(generateInitialProduct(product));
			StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
			httpPut.setEntity(params);
			CloseableHttpResponse response = client.execute(httpPut);
			updateMagentoProduct(product);
			response.close();
			client.close();
			return response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
		}
		else{
			magentoAuthenticate();
			return newMagentoProduct(product);
		}
		
		
		
	}
	
	public String updateMagentoProduct(MagentoProductRequest product) throws Exception{
		CloseableHttpClient client = this.getClient();
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(product);
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
//		System.out.println("Product Json: ");
//		System.out.println(om.writeValueAsString(postProduct));
		System.out.println("Product: " + product.getName() + " " + product.getSku());
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("product"));
		HttpPost httpPost = new HttpPost(builder.build());
		httpPost.addHeader("Authorization", "Bearer " + magentoToken);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		CloseableHttpResponse response = client.execute(httpPost);
//		System.out.println("Product response: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		System.err.println("Product status: " + response.getStatusLine().getStatusCode());
		response.close();
		client.close();
		return response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
	}
	
	private MagentoProductRequest generateInitialProduct(MagentoProductRequest product) {
		MagentoProductRequest initial = new MagentoProductRequest();
		initial.setName(product.getName());
		initial.setSku(product.getSku());
		initial.setPrice(product.getPrice());
		initial.setStatus(1);
		initial.setVisibility(4);
		initial.setAttributeSetId(10);
		initial.setOptions(new ArrayList<Option>());
		initial.setTypeId("simple");
		initial.setExtensionAttributes(new ExtensionAttribute());
		initial.setProductLinks(new ArrayList<ProductLink>());
		initial.setMediaGalleryEntries(new ArrayList<MediaGalleryEntry>());
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
	

	public List<String> downloadMagentoAttributes() throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("attributes"));
		builder.setParameter("searchCriteria", "0");
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.addHeader("Authorization", "Bearer " + magentoToken);
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
					for(Option option : attribute.getOptions()){
						options.put(option.getValue(), option.getLabel());
					}
				}
			}
			for(Entry<Integer,String> entry : attributes.entrySet()){
				System.out.println(entry.getKey() + "=" + entry.getValue());
			}
			for(Entry<String, String> entry : options.entrySet()){
				System.out.println(entry.getKey() + "=" + entry.getValue());
			}
        }
		
		return null;
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
		httpPost.addHeader("Authorization", "Bearer " + magentoToken);
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
}
