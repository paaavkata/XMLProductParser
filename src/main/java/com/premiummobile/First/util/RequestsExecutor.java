package com.premiummobile.First.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premiummobile.First.magento.MagentoPostProduct;
import com.premiummobile.First.magento.MagentoProduct;
import com.premiummobile.First.solytron.Model.SolytronProduct;
import com.premiummobile.First.solytron.Model.ProductSet;
	
@Component
public class RequestsExecutor {
	
	private PropertiesLoader loader;
	
	@Autowired
	private ObjectMapper om;
	
	private String magentoToken;
	
	private Serializer serializer;
	
	private Properties solytronProperties;
	
	private Properties magentoProperties;
	
	@Autowired
	public RequestsExecutor(PropertiesLoader loader) throws Exception {
		this.loader = loader;
		this.solytronProperties = loader.getSolytron();
		this.magentoProperties = loader.getMagento();
		this.magentoAuthenticate();
	}
	
	private void magentoAuthenticate() throws Exception{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.getProperty("host"));
		builder.setPath(magentoProperties.getProperty("authorization"));
		HttpPost httpPost = new HttpPost(builder.build());
		httpPost.addHeader("Content-Type", "application/json");
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", magentoProperties.getProperty("username")));
		params.add(new BasicNameValuePair("password", magentoProperties.getProperty("password")));
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		CloseableHttpResponse response = this.getClient().execute(httpPost);
		this.magentoToken = response.getEntity().toString();
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

	public MagentoProduct postMagentoProduct(MagentoProduct product) throws Exception{
		if(magentoToken != null){
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost(magentoProperties.getProperty("host"));
			builder.setPath(magentoProperties.getProperty("product"));
			HttpPost httpPost = new HttpPost(builder.build());
			httpPost.addHeader("Authorization", "Bearer " + magentoToken);
			httpPost.addHeader("Content-Type", "application/json");
			MagentoPostProduct postProduct = new MagentoPostProduct();
			postProduct.setMagentoProduct(product);
			StringEntity params = new StringEntity(om.writeValueAsString(postProduct));
			httpPost.setEntity(params);
			CloseableHttpResponse response = getClient().execute(httpPost);
			MagentoProduct productResponse = null;
			productResponse = om.readValue(response.getEntity().toString(), MagentoProduct.class);
			return productResponse;
		}
		else{
			magentoAuthenticate();
			return postMagentoProduct(product);
		}
		
	}
	
	public SolytronProduct getProductSolytron(String code) throws Exception{
		CloseableHttpResponse response;
		HttpGet httpGet = generateGetRequest(code, "product", "solytron");
		response = getClient().execute(httpGet);
		SolytronProduct product = getSerializer().read(SolytronProduct.class, response.getEntity().getContent());
		return product;
	}
	
	public ProductSet getCategorySolytron(String code) throws Exception{
		CloseableHttpResponse response;
		HttpGet httpGet = generateGetRequest(code, "category", "solytron");
		response = getClient().execute(httpGet);
		ProductSet productSet = getSerializer().read(ProductSet.class, response.getEntity().getContent());
		return productSet;
	}
	
	
	private HttpGet generateGetRequest(String code, String type, String provider) throws Exception{
		URIBuilder builder = new URIBuilder();
		String path = "";
		builder.setScheme("http").setHost(solytronProperties.getProperty("domainname"));
		if(type.equals("product")){
			path = solytronProperties.getProperty("options.product");
			builder.setPath(path).setParameter("productId", code);
		}
		else if(type.equals("category")){
			path = solytronProperties.getProperty("options.category");
			builder.setPath(path).setParameter("propertyId", code);
		}
		HttpGet httpGet;
		builder.setParameter("j_u", solytronProperties.getProperty("username"))
				.setParameter("j_p", solytronProperties.getProperty("password"));
		httpGet = new HttpGet(builder.build());
		return httpGet;
		
	}
	
	@Bean
	public CloseableHttpClient getClient(){
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(30);
		manager.setDefaultMaxPerRoute(10);
		CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).build();
		
		return client;
	}
}
