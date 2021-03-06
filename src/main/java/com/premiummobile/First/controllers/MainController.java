package com.premiummobile.First.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.premiummobile.First.emails.EmailsConverter;
import com.premiummobile.First.emails.Order;

@Controller
public class MainController {
	
	@Autowired
	private EmailsConverter converter;
	
	@GetMapping("/readmails")
	@ResponseBody
	public List<Order> readXML() throws IOException{
		List<Order> response = converter.convertEmail();
		return response;
	}
}
