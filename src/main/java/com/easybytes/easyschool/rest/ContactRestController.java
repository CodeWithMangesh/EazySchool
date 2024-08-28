package com.easybytes.easyschool.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.easybytes.easyschool.model.Contact;
import com.easybytes.easyschool.model.Response;
import com.easybytes.easyschool.repository.ContactRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/contact")
public class ContactRestController {
	
	@Autowired
	ContactRepository contactRepository;
	
	@GetMapping("/getMessagesByStatus")
	//@ResponseBody
	public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status){
		return contactRepository.readByStatus(status);
	}
	
	@GetMapping("/getAllMsgsByStatus")
	//@ResponseBody
	public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact){
		if(null != contact && null != contact.getStatus()) {
			return contactRepository.readByStatus(contact.getStatus());
		}
		else {
			return List.of();
		}
	}
	
	@PostMapping("/saveMsg")
	public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom") String invocationFrom, 
											@Valid @RequestBody Contact contact){
		log.info(String.format("Header invocation = %s", invocationFrom));
		contactRepository.save(contact);
		Response response = new Response();
		response.setStatusCode("200");
		response.setStatysMsg("Message saved");
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.header("isMsgSaved", "true")
				.body(response);
		
	}
}
