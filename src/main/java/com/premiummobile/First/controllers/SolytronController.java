package com.premiummobile.First.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.premiummobile.First.solytron.MainDownloader;
import com.premiummobile.First.solytron.Model.SolytronProduct;

@Controller
public class SolytronController {
	
	@Autowired
	private MainDownloader downloader;
	
	@GetMapping("/readCategories")
	@ResponseBody
	public List<Object> readXML() throws Exception{
		List<Object> response = downloader.downloadCategories();
		
		return response;
	}
}
