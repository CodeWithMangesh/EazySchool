package com.easybytes.easyschool.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.Errors;

import com.easybytes.easyschool.model.Contact;
import com.easybytes.easyschool.service.ContactService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j //this will create the logger object with variable log
@Controller
public class ContactController {
	
	//private static Logger log = LoggerFactory.getLogger(ContactController.class);
	
	private final ContactService contactService;
	
	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}


	@RequestMapping("/contact")
	public String displayContactPage(Model model) {
		model.addAttribute("contact", new Contact());
		System.out.println("Calling Contact Controller");
		return "contact.html";
	}
	
	
//	@RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
//	public ModelAndView saveMessage(@RequestParam String name, @RequestParam String mobileNum,
//									@RequestParam String email, @RequestParam String subject, @RequestParam String message) {
//		log.info("name : "+name);
//		log.info("name : "+mobileNum);
//		log.info("name : "+email);
//		log.info("name : "+subject);
//		log.info("name : "+message);
//		
//		return new ModelAndView("redirect:/contact");
//	}
	
	@RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
	public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {
		if(errors.hasErrors()) {
			log.error("Contact form validation falied due to : " + errors.toString());
			return "contact.html";
		}
		contactService.saveMessageDetails(contact);
		return "redirect:/contact";
	}
	
	@RequestMapping("/displayMessages/page/{pageNum}")
    public ModelAndView displayMessages(Model model,
    		@PathVariable(name = "pageNum") int pageNum, @RequestParam("sortField") String sortField,
    		@RequestParam("sortDir") String sortDir) {
        //List<Contact> contactMsgs = contactService.findMsgsWithOpenStatus();
		Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum, sortField, sortDir);
		List<Contact> contactMsgs = msgPage.getContent();
        ModelAndView modelAndView = new ModelAndView("messages.html");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", msgPage.getTotalPages());
        model.addAttribute("totalMsgs", msgPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("contactMsgs",contactMsgs);
        return modelAndView;
    }
	
	@RequestMapping(value = "/closeMsg", method = RequestMethod.GET)
	public String closeMsg(@RequestParam int id/* , Authentication authentication */) {
		contactService.updateMsgStatus(id/* , authentication.getName() */);
		return "redirect:/displayMessages/page/1?sortField=name&sortDir=desc";
	}
}
