package com.premiummobile.First.solytron;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.premiummobile.First.util.SolytronUrlGenerator;


public class LaptopCategory {
	
	@Autowired
	SolytronUrlGenerator urlGenerator;
	
	@Autowired
	DocumentBuilderFactory dbFactory;
	
	public void parseXML() {
				
		try {
			URL url = urlGenerator.generateSolytronCategory("pc.laptop");

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(url.openStream());
			Document doc = dBuilder.parse(new File("C:\\Users\\Pavel Damyanov\\Desktop\\First\\src\\main\\java\\solytron\\list.xml"));
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("product");

			HashMap<String, Laptop> laptops = new HashMap<String, Laptop>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String codeId = eElement.getAttribute("codeId");
					System.out.println(codeId);
					String productId = buildProductId(eElement.getAttribute("productId"));
					System.out.println("productId : " + productId);
					String name = eElement.getElementsByTagName("name").item(0).getTextContent();
					System.out.println(name);
					String vendor = eElement.getElementsByTagName("vendor").item(0).getTextContent();
					System.out.println(vendor);
					double price = eElement.getElementsByTagName("price").getLength() != 0
							? Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent()) : 0;
							System.out.println(price);
					double userPrice = eElement.getElementsByTagName("priceEndUser").getLength() != 0
							? Double.parseDouble(eElement.getElementsByTagName("priceEndUser").item(0).getTextContent())
							: 0;
							System.out.println(userPrice);
					String stockInfoValue = (eElement.getElementsByTagName("stockInfoValue").getLength() != 0
							? eElement.getElementsByTagName("stockInfoValue").item(0).getTextContent() : "Ask");
					System.out.println(stockInfoValue);
					int stockInfoData = eElement.getElementsByTagName("stockInfoData").getLength() != 0
							? Integer.parseInt(eElement.getElementsByTagName("stockInfoData").item(0).getTextContent()) : 0;
							System.out.println(stockInfoData);
					laptops.put(codeId, new Laptop(codeId, productId, name, vendor, price, userPrice, stockInfoValue,
							stockInfoData));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String buildProductId(String id) {
		StringBuilder st = new StringBuilder();
		st.append("{");
		st.append(id.substring(0, 8));
		st.append("-");
		st.append(id.substring(8, 12));
		st.append("-");
		st.append(id.substring(12, 16));
		st.append("-");
		st.append(id.substring(16, 20));
		st.append("-");
		st.append(id.substring(20));
		st.append("}");
		return st.toString();

	}
}
