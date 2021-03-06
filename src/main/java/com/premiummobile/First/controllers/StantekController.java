package com.premiummobile.First.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.premiummobile.First.stantek.StantekMainDownloader;
import com.premiummobile.First.stantek.StantekXMLReader;

@Controller
public class StantekController {
	
	@Autowired
	private StantekXMLReader reader;
	
	@Autowired
	private StantekMainDownloader executor;
	
	@GetMapping("/stantek/read")
	@ResponseBody
	public String readXML(){
		String response = reader.readXML();
		return response;
	}
	
	@GetMapping("/stantek/readNew")
	@ResponseBody
	public String readNew(){
		return executor.downloadFile();
	}
}
