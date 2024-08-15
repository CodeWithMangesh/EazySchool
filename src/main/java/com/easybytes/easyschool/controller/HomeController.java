package com.easybytes.easyschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping(value = {"", "/", "home"})
	public String displayHomePage(Model model) {
		System.out.println("Calling home controller");
		model.addAttribute("username", "Mangesh");
		return "home.html";
	}

}
