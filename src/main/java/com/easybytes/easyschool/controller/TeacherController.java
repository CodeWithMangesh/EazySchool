package com.easybytes.easyschool.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.easybytes.easyschool.model.EazyClass;
import com.easybytes.easyschool.model.LeaveRequests;
import com.easybytes.easyschool.model.Marks;
import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Question;
import com.easybytes.easyschool.model.QuestionTO;
import com.easybytes.easyschool.model.Subjects;
import com.easybytes.easyschool.model.Teacher;
import com.easybytes.easyschool.repository.EazyClassRepository;
import com.easybytes.easyschool.repository.LeaveRequestsRepository;
import com.easybytes.easyschool.repository.MarksRepository;
import com.easybytes.easyschool.repository.PersonRepository;
import com.easybytes.easyschool.repository.QuestionRepository;
import com.easybytes.easyschool.repository.SubjectsRepository;
import com.easybytes.easyschool.repository.TeacherRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("teacher")  
public class TeacherController {
	
	@Autowired
	TeacherRepository teacherRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	EazyClassRepository eazyClassRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	MarksRepository marksRepository;
	
	@Autowired
	SubjectsRepository subjectsRepository;
	
	@Autowired
	LeaveRequestsRepository leaveRequestsRepository;
	
	@GetMapping("/displaySubjects")
	public ModelAndView displaySubjects(Model model, HttpSession session) {
		Person person = (Person) session.getAttribute("loggedInPerson");
		Teacher teacher = person.getTeacher();
		ModelAndView modelAndView = new ModelAndView("subject_secure");
		modelAndView.addObject("teacher", teacher);
		
		List<Subjects> subjects = subjectsRepository.findByStatus(EazySchoolConstants.NOT_ASSIGNED);
		if(teacher.getSubject() != null) {
			subjects.clear();
		}
		modelAndView.addObject("allSubjects", subjects);
		return modelAndView;
	}
	
	@GetMapping("/addSubjectToTeacher")
	public ModelAndView addSubjectToTeacher(Model model, @RequestParam int id, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Person person = (Person) session.getAttribute("loggedInPerson");
		Teacher teacher2 = person.getTeacher();
		Subjects subject = subjectsRepository.findById(id).get();
		System.out.println(subject.getName());
		//teacher2.setSubject(subject);
		//subject.setTeacher(teacher2);
		subject.setStatus(EazySchoolConstants.ASSIGNED);
		System.out.println(teacher2.getTeacherId());
		//System.out.println(teacher2.getSubjectSpecialization());
		System.out.println(teacher2.getEmail());
		//teacherRepository.save(teacher2);
		Subjects savedSub = subjectsRepository.save(subject);
		Set<EazyClass> classes = savedSub.getClasses();
		teacher2.getClasses().addAll(classes);
		teacher2.setSubject(savedSub);
		teacherRepository.save(teacher2);
		modelAndView.setViewName("redirect:/teacher/displaySubjects");
		return modelAndView;
		
	}
	
	@GetMapping("/deleteSubject")
	public ModelAndView deleteSubjectFromTeacher(Model model, @RequestParam Long teacherId, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Teacher teacher = teacherRepository.findById(teacherId).get();
		Subjects subject = teacher.getSubject();
		teacher.setSubject(null);
		subject.setStatus(EazySchoolConstants.NOT_ASSIGNED);
		teacherRepository.save(teacher);
		subjectsRepository.save(subject);
		Person person = personRepository.readByEmail(teacher.getEmail());
		session.setAttribute("loggedInPerson", person);
		modelAndView.setViewName("redirect:/teacher/displaySubjects");
		return modelAndView;
	}
	
	@GetMapping("/displayTeacherClasses")
	public ModelAndView displayTeacherClasses(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("classes.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		Teacher teacher = teacherRepository.readByEmail(person.getEmail());
		Set<EazyClass> classes = teacher.getClasses();
		modelAndView.addObject("eazyClasses",classes);
		modelAndView.addObject("eazyClass", new EazyClass());
		return modelAndView;
	}
	
	@GetMapping("/displayStudents")
	public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
						@RequestParam(value = "error", required = false) String error) {
		//String errorMessage = null;
		ModelAndView modelAndView = new ModelAndView("students.html");
		Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
		modelAndView.addObject("eazyClass",eazyClass.get());
		modelAndView.addObject("person", new Person());
		modelAndView.addObject("teacher", new Teacher());
		modelAndView.addObject("marks", new Marks());
		session.setAttribute("eazyClass", eazyClass.get());
		if(error != null) {
			if(error.equals("false")) {
				System.out.println("inside teacher added");
        		String errorMessageForTeacher = "Teacher Added Successfully.";
        		String status = "alert alert-success";
        		modelAndView.addObject("errorMessageForTeacher", errorMessageForTeacher);
        		modelAndView.addObject("status",status);
        	}
        	else {
            String errorMessageForStudent = "Invalid Email entered!!";
            String status = "alert alert-danger";
            modelAndView.addObject("errorMessageForStudent", errorMessageForStudent);
            modelAndView.addObject("status",status);
        	}
			
		}
		return modelAndView;
	}
	
	@GetMapping("/displayQuestions")
	public ModelAndView displayQuestions(Model model, HttpSession session) {
		Person person = (Person) session.getAttribute("loggedInPerson");
		ModelAndView modelAndView = new ModelAndView("queries.html");
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		List<Question> questions = questionRepository.findByTeacherIdAndStatus(person.getTeacher().getTeacherId(), EazySchoolConstants.OPEN);
		for(Question que : questions) {
			Person per = personRepository.findById(que.getPersonId()).get();
			QuestionTO q = new QuestionTO();
			q.setQuestionId(que.getQuestionId());
			q.setPersonId(que.getPersonId());
			q.setTeacherId(que.getTeacherId());
			q.setQue(que.getQue());
			q.setAns(que.getAns());
			q.setStatus(que.getStatus());
			q.setTeacherName(person.getName());
			q.setStudentName(per.getName());
			questionToList.add(q);
		}
		modelAndView.addObject("questionToList", questionToList);
		modelAndView.addObject("question", new Question());
		return modelAndView;
	}
	
	@PostMapping("/postAnswer")
	public ModelAndView postAnswer(Model model, @ModelAttribute("question") Question question) {
		ModelAndView modelAndView = new ModelAndView();
		Question question2 = questionRepository.findById(question.getQuestionId()).get();
		question2.setStatus(EazySchoolConstants.CLOSE);
		question2.setAns(question.getAns());
		questionRepository.save(question2);
		modelAndView.setViewName("redirect:/teacher/displayQuestions");
		return modelAndView;
	}
	
	@PostMapping("/saveMarks")
	public ModelAndView saveMarks(Model model, @ModelAttribute("marks") Marks marks, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Person person = (Person) session.getAttribute("loggedInPerson");
		System.out.println(person.getTeacher().getTeacherId());
		Teacher teacher = teacherRepository.findById(person.getTeacher().getTeacherId()).get();
		marks.setTeacher(teacher);
		System.out.println(marks.getPerson().getPersonId());
		Person person2 = personRepository.findById(marks.getPerson().getPersonId()).get();
		marks.setPerson(person2);
		marks.setSubject(teacher.getSubject().getName());
		marks.setEazyClass(person2.getEazyClass());
		marksRepository.save(marks);
		modelAndView.setViewName("redirect:/teacher/displayStudents?classId="+person2.getEazyClass().getClassId());
		return modelAndView;
	}
	
	@GetMapping("/displayMarks")
	public ModelAndView displayMarks(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("marks.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		List<Marks> marks = marksRepository.findByTeacher(person.getTeacher());
		modelAndView.addObject("marks", marks);
		modelAndView.addObject("mark", new Marks());
		return modelAndView;
	}
	
	@PostMapping("/editMarks")
	public ModelAndView editMarks(Model model, @ModelAttribute("mark") Marks mark) {
		ModelAndView modelAndView = new ModelAndView();
		Marks marks = marksRepository.findById(mark.getMarksId()).get();
		marks.setMarksObtained(mark.getMarksObtained());
		marksRepository.save(marks);
		modelAndView.setViewName("redirect:/teacher/displayMarks");
		return modelAndView;
	}
	
	@GetMapping("/displayLeaves")
	public ModelAndView displayLeaves(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("leave.html");
		Person person = (Person) session.getAttribute("loggedInPerson");
		EazyClass classW = eazyClassRepository.findByClassTeacher(person.getTeacher());
		List<LeaveRequests> leaves = leaveRequestsRepository.findByClassAndStatusNativeSQL(classW.getClassId(), EazySchoolConstants.PENDING);
		modelAndView.addObject("leaves",leaves);
		modelAndView.addObject("leaveRequest", new LeaveRequests());
		return modelAndView;
	}
	
	@GetMapping("/updateLeave")
	public ModelAndView updateLeave(Model model, @RequestParam int leaveId, boolean status) {
		ModelAndView modelAndView = new ModelAndView();
		LeaveRequests leave = leaveRequestsRepository.findById(leaveId).get();
		if(status) {
			leave.setStatus(EazySchoolConstants.APPROVED);
		}
		else {
			leave.setStatus(EazySchoolConstants.REJECTED);
		}
		leaveRequestsRepository.save(leave);
		
		modelAndView.setViewName("redirect:/teacher/displayLeaves");
		return modelAndView;
	}

}
