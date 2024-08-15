package com.easybytes.easyschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.repository.CoursesRepository;
import com.easybytes.easyschool.repository.EazyClassRepository;
import com.easybytes.easyschool.repository.PersonRepository;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("student")  
public class StudentController {
	
	@Autowired
	EazyClassRepository eazyClassRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	CoursesRepository coursesRepository;
	
	@GetMapping("/displayCourses")
	public ModelAndView displayCourses(Model model, HttpSession session) {
		Person person = (Person) session.getAttribute("loggedInPerson");
		ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
		modelAndView.addObject("person", person);
		return modelAndView;
	}

}
