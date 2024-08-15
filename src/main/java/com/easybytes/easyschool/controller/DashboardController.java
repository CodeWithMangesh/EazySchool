package com.easybytes.easyschool.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.repository.PersonRepository;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {
	
	@Autowired
	PersonRepository personRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session) {
    	Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if(null != person.getEazyClass() && null != person.getEazyClass().getName()) {
        	model.addAttribute("enrolledClass",person.getEazyClass().getName());
        }
        //throw new RuntimeException("Its been a bad day");
        session.setAttribute("loggedInPerson", person);
        return "dashboard.html";
    }

}