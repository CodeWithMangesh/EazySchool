package com.easybytes.easyschool.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.easybytes.easyschool.model.Courses;
import com.easybytes.easyschool.model.EazyClass;
import com.easybytes.easyschool.model.LeaveRequests;
import com.easybytes.easyschool.model.Marks;
import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Question;
import com.easybytes.easyschool.model.QuestionTO;
import com.easybytes.easyschool.model.StudentsRequestedCourses;
import com.easybytes.easyschool.model.Teacher;
import com.easybytes.easyschool.repository.CoursesRepository;
import com.easybytes.easyschool.repository.EazyClassRepository;
import com.easybytes.easyschool.repository.LeaveRequestsRepository;
import com.easybytes.easyschool.repository.MarksRepository;
import com.easybytes.easyschool.repository.PersonRepository;
import com.easybytes.easyschool.repository.QuestionRepository;
import com.easybytes.easyschool.repository.StudentsRequestedRepository;
import com.easybytes.easyschool.repository.TeacherRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

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

	@Autowired
	StudentsRequestedRepository requestedRepository;

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	MarksRepository marksRepository;
	
	@Autowired
	LeaveRequestsRepository leaveRequestsRepository;

	@GetMapping("/displayCourses")
	public ModelAndView displayCourses(Model model, HttpSession session) {
		Person person = (Person) session.getAttribute("loggedInPerson");
		Set<Courses> courses = person.getCourses();
		ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
		List<Courses> allCourses = coursesRepository.findAll();
		List<Courses> reqs = new ArrayList<Courses>();
		List<StudentsRequestedCourses> requests = requestedRepository.findByPersonIdAndStatus(person.getPersonId(),
				EazySchoolConstants.REQUESTED);
		for (Courses c : courses) {
			if (allCourses.contains(c)) {
				allCourses.remove(c);
			}
		}
		for (StudentsRequestedCourses s : requests) {
			Courses courses2 = coursesRepository.findById(s.getCourseId()).get();
			reqs.add(courses2);
			if (allCourses.contains(courses2)) {
				allCourses.remove(courses2);
			}
		}
		modelAndView.addObject("allCourses", allCourses);
		modelAndView.addObject("person", person);
		modelAndView.addObject("requests", reqs);
		return modelAndView;
	}

	@GetMapping("/enrollCourses/{id}")
	public ModelAndView enrollInCourse(Model model, @PathVariable int id, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Person person = (Person) session.getAttribute("loggedInPerson");
		StudentsRequestedCourses requested = new StudentsRequestedCourses();
		requested.setPersonId(person.getPersonId());
		requested.setCourseId(id);
		requested.setStatus(EazySchoolConstants.REQUESTED);
		requestedRepository.save(requested);
		modelAndView.setViewName("redirect:/student/displayCourses");
		return modelAndView;
	}

	@GetMapping("/displayTeachers")
	public ModelAndView displayTeachers(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("all_teachers.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
		List<Teacher> teachers = teacherRepository.findByClassNativeSQL(person.getEazyClass().getClassId());
		modelAndView.addObject("teachers", teachers);
		modelAndView.addObject("question", new Question());
		return modelAndView;
	}

	@PostMapping("/askQuestion")
	public ModelAndView askQuestion(Model model, @ModelAttribute("question") Question question, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Person person = (Person) session.getAttribute("loggedInPerson");
		System.out.println(question.getQue());
		System.out.println(question.getTeacherId());
		question.setPersonId(person.getPersonId());
		question.setStatus(EazySchoolConstants.OPEN);
		questionRepository.save(question);
		modelAndView.setViewName("redirect:/student/displayTeachers");
		return modelAndView;
	}

	@GetMapping("/displayQuestions")
	public ModelAndView displayQuestions(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("queries.html");
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		Person person = (Person) session.getAttribute("loggedInPerson");
		List<Question> questions = questionRepository.findByPersonId(person.getPersonId());
		for (Question que : questions) {
			Teacher teacher = teacherRepository.findById(que.getTeacherId()).get();
			QuestionTO q = new QuestionTO();
			q.setQuestionId(que.getQuestionId());
			q.setPersonId(que.getPersonId());
			q.setTeacherId(que.getTeacherId());
			q.setQue(que.getQue());
			q.setAns(que.getAns());
			q.setStatus(que.getStatus());
			q.setTeacherName(teacher.getName());
			q.setStudentName(person.getName());
			questionToList.add(q);
		}
		modelAndView.addObject("questionToList", questionToList);
		return modelAndView;
	}

	@GetMapping("/displayMarks")
	public ModelAndView displayMarks(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("marks.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		List<Marks> marks = marksRepository.findByPerson(person);
		modelAndView.addObject("marks", marks);
		return modelAndView;
		
	}

	@GetMapping("/displayLeaves")
	public ModelAndView displayLeaves(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("leave.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		List<LeaveRequests> leaves = leaveRequestsRepository.findByStudent(person);
		modelAndView.addObject("leaves", leaves);
		modelAndView.addObject("leaveRequest", new LeaveRequests());
		return modelAndView;
	}
	
	@PostMapping("/addNewLeave")
	public ModelAndView addNewLeaveForStudent(Model model, @ModelAttribute("leaveRequest") LeaveRequests leaveRequest, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Person person = (Person) session.getAttribute("loggedInPerson");
		leaveRequest.setStudent(person);
		leaveRequest.setStatus(EazySchoolConstants.PENDING);
		leaveRequestsRepository.save(leaveRequest);
		modelAndView.setViewName("redirect:/student/displayLeaves");
		return modelAndView;
	}
}
