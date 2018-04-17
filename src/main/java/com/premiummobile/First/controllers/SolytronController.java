package com.premiummobile.First.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.premiummobile.First.solytron.MainDownloader;

@Controller
public class SolytronController {
	
	@Autowired
	private MainDownloader downloader;
	
	@ResponseBody
	@GetMapping("/readCategory/{categoryId}/")
	public String readXML(@PathVariable("categoryId") String category){
		int response = -1;
		try{
			response = downloader.downloadCategory(category);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error while downloading " + category);
		}
		if(response == -1){
			return "Downloading categories failed. Check configurations.";
		}
		else{
			return "Success! There are " + response + " products in selected category.";
		}
	}
	
//	@RequestMapping(value = "/welcome/{userId}/{userName}/", method = RequestMethod.GET)
//	public String printWelcome(@PathVariable("userId") String userId,
//			@PathVariable("userName") String userName, ModelMap model,
//			HttpServletRequest request) {
//		System.out.println("User Id : " + userId);
//		System.out.println("User Name : " + userName);
//		model.addAttribute("msg", userId);
//		return "hello";
//	}
}
