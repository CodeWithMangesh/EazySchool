package com.easybytes.easyschool.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
	
	@Value("${eazyschool.pagesize}")
	private int defaultPageSize;
	
	@Value("${eazyschool.contact.successMsg}")
	private String message;
	
	@Autowired
	Environment environment;

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
        logMessages();
        return "dashboard.html";
    }
    
    private void logMessages() {
    	log.error("Error message from the Dashboard page");
    	log.warn("Warning message from the Dashboard page");
    	log.info("Info message from the Dashboard page");
    	log.debug("Debug message from the Dashboard page");
    	log.trace("Trace message from the Dashboard page");
    	
    	log.error("default pagesize value with @value annotation is : "+defaultPageSize);
    	log.error("successMsg value with @value annotation is : "+message);
    	
    	log.error("default pagesize value with env is : "+environment.getProperty("eazyschool.pagesize"));
    	log.error("successMsg value with env is : "+environment.getProperty("eazyschool.contact.successMsg"));
    	log.error("Java home env variable using env is : "+environment.getProperty("JAVA_HOME"));
    }

}