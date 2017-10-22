package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class UrlGenerator {
	static Properties solytronProperties = PropertiesLoad.load(
			new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\util\\solytron.properties"));
	static Properties polycompProperties = PropertiesLoad.load(
			new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\util\\solytron.properties"));
	static Properties stantekProperties = PropertiesLoad.load(
			new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\util\\solytron.properties"));
	static Properties jarcomputersProperties = PropertiesLoad.load(
			new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\util\\solytron.properties"));
	static Properties mostProperties = PropertiesLoad.load(
			new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\util\\solytron.properties"));
	
	public static URL generateSolytronProduct(String productId) throws MalformedURLException{
		StringBuilder st = new StringBuilder();
		st.append(solytronProperties.getProperty("domainname"));
		st.append(solytronProperties.getProperty("options.product"));
		st.append(productId);
		st.append("&j_u=");
		st.append(solytronProperties.getProperty("username"));
		st.append("&j_p=");
		st.append(solytronProperties.getProperty("password"));
		URL url = new URL(st.toString());
		return url;
	}
	
	public static URL generateSolytronCategory(String categoryId) throws MalformedURLException{
		StringBuilder st = new StringBuilder();
		st.append(solytronProperties.getProperty("domainname"));
		st.append(solytronProperties.getProperty("options.category"));
		st.append(solytronProperties.getProperty(categoryId));
		st.append("&j_u=");
		st.append(solytronProperties.getProperty("username"));
		st.append("&j_p=");
		st.append(solytronProperties.getProperty("password"));
		URL url = new URL(st.toString());
		return url;
	}
}
