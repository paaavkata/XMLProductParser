package com.premiummobile.First.magento;

//import java.util.List;
//
//import com.github.chen0040.magento.MagentoClient;
//import com.github.chen0040.magento.models.Account;
//import com.github.chen0040.magento.models.Product;
//import com.github.chen0040.magento.models.ProductPage;
//
//public class APIConnector {
//
//	String magento_site_url = "http://magento.ll";
//	String username = "Pavel";
//	String password = "zwEriP8mOGXs";
//	MagentoClient client = new MagentoClient(magento_site_url);
//	String token = client.loginAsAdmin(username, password);
//	Account account = client.getMyAccount();
//
//
//	String sku = "";
//	int pageIndex = 0;
//	int pageSize = 10;
//	ProductPage page = client.products().page(pageIndex, pageSize);
//	List<Product> products = page.getItems();
//
//	boolean exists = client.products().hasProduct(sku);
////	System.out.println(exists);
//	Product product = client.products().getProductBySku(sku);
//	
////	// create or update a product 
////	product.setSku("B203-SKU");
////	product.setName("B203");
////	product.setPrice(30.00);
////	product.setStatus(1);
////	product.setType_id("simple");
////	product.setAttribute_set_id(4);
////	product.setWeight(1);
////	product.setVisibility(Product.VISIBILITY_BOTH);
////	product.setStatus(Product.STATUS_ENABLED);
////	      
////	Product saveProduct = client.products().saveProduct(product);
////
////	// delete a product
////	client.products().deleteProduct(sku);
//}
