package com.premiummobile.First.stantek;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.premiummobile.First.util.PropertiesLoader;

@Component
public class StantekXMLReader {
	
	@Autowired
	private PropertiesLoader propertiesLoad;

	@Autowired
	private StantekLaptopsBuilder builder;

	public String readXML(){
		
		URL url = null;
		Properties stantek = propertiesLoad.getStantek();
		try {
			url = new URL(stantek.getProperty("url"));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(url.openStream());
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("Product");
		String laptop = stantek.getProperty("laptopCategory");
		String tablet = stantek.getProperty("tabletCategory");
		String gsmAcc = stantek.getProperty("gsmAccessoriesCategory");
		String gsmDis = stantek.getProperty("gsmDisplayCategory");
		String gsmBat = stantek.getProperty("gsmBatteryCategory");
		String gsmPar = stantek.getProperty("gsmPartsCategory");
		ArrayList<Element> laptops = new ArrayList<Element>();
		ArrayList<Element> tablets = new ArrayList<Element>();
		ArrayList<Element> gsmAccessories = new ArrayList<Element>();
		ArrayList<Element> gsmDisplay = new ArrayList<Element>();
		ArrayList<Element> gsmBattery = new ArrayList<Element>();
		ArrayList<Element> gsmParts = new ArrayList<Element>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(laptop)){
				laptops.add(eElement);
				continue;
			}
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(tablet)){
				tablets.add(eElement);
				continue;
			}
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(gsmAcc)){
				gsmAccessories.add(eElement);
				continue;
			}
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(gsmDis)){
				gsmDisplay.add(eElement);
				continue;
			}
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(gsmBat)){
				gsmBattery.add(eElement);
				continue;
			}
			if(eElement.getElementsByTagName("Group").item(0).getTextContent().equals(gsmPar)){
				gsmParts.add(eElement);
			}
		}
		String response = "All Products: ";
		response = response + nList.getLength() + System.lineSeparator();
		response = response + "<br> Laptops: " + laptops.size();
		response = response + "<br> Tablets: " + tablets.size();
		response = response + "<br> GSM Acc: " + gsmAccessories.size();
		response = response + "<br> GSM Display: " + gsmDisplay.size();
		response = response + "<br> GSM Battery: " + gsmBattery.size();
		response = response + "<br> GSM Parts: " + gsmParts.size();
		
		String response1 = builder.parseXML(laptops);
		
		return response + "%n" + response1;
	}
}
