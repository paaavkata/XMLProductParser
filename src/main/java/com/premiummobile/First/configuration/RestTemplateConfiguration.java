package com.premiummobile.First.configuration;

//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;

//@Configuration
//public class RestTemplateConfiguration {
//
//	@Autowired
//	private ObjectMapper om;
//	
//	@Autowired
//	private HttpClientFactory factory;
//	
//	@Bean
//	public ClientHttpRequestFactory httpRequestFactory(){
//		return new HttpComponentsClientHttpRequestFactory(factory.getClient());
//	}
//	@Bean
//	public RestTemplate restTempalte(){
//		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
//		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
//		
//		for(HttpMessageConverter<?> converter : converters){
//			if(converter instanceof MappingJackson2HttpMessageConverter){
//				MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
//				jsonConverter.setObjectMapper(om);
//			}
//		}
//		return restTemplate;
//	}
//}
