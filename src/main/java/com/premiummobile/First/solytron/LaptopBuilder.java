package com.premiummobile.First.solytron;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.premiummobile.First.util.SolytronUrlGenerator;

public class LaptopBuilder {
	
	@Autowired
	SolytronUrlGenerator urlGenerator;
	
	public Laptop parseXML(Laptop laptop){
		try {
			URL url = urlGenerator.generateSolytronProduct(laptop.getProductId());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(url.openStream());
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("productGroup");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					NodeList list2 = element.getElementsByTagName("property");
					
					for(int temp2 = 0; temp < list2.getLength(); temp2++){
						Node node2 = list2.item(temp2);
						if(node2.getNodeType() == Node.ELEMENT_NODE){
							Element element2 = (Element) node2;
							System.out.println(element2.getAttribute(""));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return laptop;
	}
}
