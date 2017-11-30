package solytron;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import util.PropertiesLoad;
//import util.UrlGenerator;
//
//public class LaptopBuilder {
//	
//	public Laptop parseXML(Laptop laptop){
//		try {
//			URL url = UrlGenerator.generateSolytronProduct(laptop.getProductId());
//			
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(url.openStream());
//			doc.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			NodeList nList = doc.getElementsByTagName("product");
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				Node nNode = nList.item(temp);
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
////				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
////					for(int temp2 = 0; temp2 <)
////					Element element = (Element) nNode;
////					System.out.println(element.getElementsByTagName("property").item(0).getTextContent());
////					String smthn;
////					String codeId = element.getAttribute("codeId");
////					String name = element.getElementsByTagName("name").item(0).getTextContent();
////					String vendor = element.getElementsByTagName("vendor").item(0).getTextContent();
////					double price = element.getElementsByTagName("price").toString() != ""
////							? Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent()) : 0;
////					double userPrice = element.getElementsByTagName("priceEndUser").toString() != ""
////							? Double.parseDouble(element.getElementsByTagName("priceEndUser").item(0).getTextContent())
////							: 0;
////					String stockInfoValue = (element.getAttribute("stockInfoValue") != ""
////							? element.getAttribute("stockInfoValue") : null);
////					int stockInfoData = element.getAttribute("stockInfoData").toString() != ""
////							? Integer.parseInt(element.getAttribute("stockInfoData").toString()) : 0;
////				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return laptop;
//	}
//}
