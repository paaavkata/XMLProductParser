package com.premiummobile.First;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import solytron.LaptopCategory;


@SpringBootApplication
public class XmlSaxParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlSaxParserApplication.class, args);
		
		LaptopCategory.parseXML();
	}
}

