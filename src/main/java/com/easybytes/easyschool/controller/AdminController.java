package com.easybytes.easyschool.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.easybytes.easyschool.model.Courses;
import com.easybytes.easyschool.model.EazyClass;
import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.RequestTO;
import com.easybytes.easyschool.model.Roles;
import com.easybytes.easyschool.model.StudentsRequestedCourses;
import com.easybytes.easyschool.model.Subjects;
import com.easybytes.easyschool.model.SubjectsTO;
import com.easybytes.easyschool.model.Teacher;
import com.easybytes.easyschool.repository.CoursesRepository;
import com.easybytes.easyschool.repository.EazyClassRepository;
import com.easybytes.easyschool.repository.PersonRepository;
import com.easybytes.easyschool.repository.StudentsRequestedRepository;
import com.easybytes.easyschool.repository.SubjectsRepository;
import com.easybytes.easyschool.repository.TeacherRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {
	
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
	SubjectsRepository subjectsRepository;
	
	@RequestMapping("/displayClasses")
	public ModelAndView displayClasses(Model model) {
		List<EazyClass> eazyClasses = eazyClassRepository.findAll();
		ModelAndView modelAndView = new ModelAndView("classes.html");
		modelAndView.addObject("eazyClasses", eazyClasses);
		modelAndView.addObject("eazyClass", new EazyClass());
		return modelAndView;
	}
	@PostMapping("/addNewClass")
	public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass) {
		eazyClassRepository.save(eazyClass);
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
		return modelAndView;
	}
	
	@RequestMapping("/deleteClass")
	public ModelAndView deleteClass(Model model, @RequestParam int id) {
		Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
		for(Person person : eazyClass.get().getPersons()) {
			person.setEazyClass(null);
			personRepository.save(person);
		}
		eazyClassRepository.deleteById(id);
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
		return modelAndView;
	}
	
	@GetMapping("/displayStudents")
	public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
						@RequestParam(value = "error", required = false) String error) {
		//String errorMessage = null;
		ModelAndView modelAndView = new ModelAndView("students.html");
		Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
		List<Teacher> teachers = new ArrayList<Teacher>();
		if(eazyClass.get().getClassTeacher() == null) {
			teachers = teacherRepository.findByClassNativeSQL(eazyClass.get().getClassId());
		}
		modelAndView.addObject("classTeacherEligible",teachers);
		modelAndView.addObject("eazyClass",eazyClass.get());
		modelAndView.addObject("person", new Person());
		modelAndView.addObject("teacher", new Teacher());
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
	
	@PostMapping("/addStudent")
	public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
		Person personEntity = personRepository.readByEmail(person.getEmail());
		if(personEntity == null || !(personEntity.getPersonId()>0)) {
			modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=true");
			return modelAndView;
		}
		personEntity.setEazyClass(eazyClass);
		personRepository.save(personEntity);
		eazyClass.getPersons().add(personEntity);
		eazyClassRepository.save(eazyClass);
		modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
		return modelAndView;
		
	}
	
	@GetMapping("/deleteStudent")
	public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
		EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
		Optional<Person> person = personRepository.findById(personId);
		person.get().setEazyClass(null);
		eazyClass.getPersons().remove(person.get());
		EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
		session.setAttribute("eazyClass", eazyClassSaved);
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
		return modelAndView;
	}
	
	@GetMapping("/displayCourses")
	public ModelAndView displayCourses(Model model) {
		//List<Courses> courses = coursesRepository.findByOrderByName();
		List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());
		ModelAndView modelAndView = new ModelAndView("courses_secure.html");
		modelAndView.addObject("courses", courses);
		modelAndView.addObject("course", new Courses());
		return modelAndView;
	}
	
	@PostMapping("/addNewCourse")
	public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course) {
		ModelAndView modelAndView = new ModelAndView();
		coursesRepository.save(course);
		modelAndView.setViewName("redirect:/admin/displayCourses");
		return modelAndView;
	}
	
	@GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id
                 ,HttpSession session,@RequestParam(required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        modelAndView.addObject("courses",courses.get());
        modelAndView.addObject("person",new Person());
        session.setAttribute("courses",courses.get());
        if(error != null) {
        	errorMessage = "Invalid Email Address!";
			modelAndView.addObject("errorMessage", errorMessage);
            
        }
        return modelAndView;
    }
	
	
	@PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person,
                                           HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        session.setAttribute("courses",courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }
	
	@GetMapping("/deleteStudentFromCourse")
	public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId, HttpSession session) {
		Courses courses = (Courses) session.getAttribute("courses");
		Optional<Person> person = personRepository.findById(personId);
		person.get().getCourses().remove(courses);
		courses.getPersons().remove(person.get());
		personRepository.save(person.get());
		session.setAttribute("courses", courses);
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
		return modelAndView;
	}
	
	//below block is added by mangesh
	
	@GetMapping("/allStudents")
	public ModelAndView allStudents(Model model, @RequestParam(required = false) String name) {
		System.out.println("calling all students");
		Roles roles = new Roles();
		roles.setRoleId(2);
		String errorMessage = null;
		if (StringUtils.hasText(name)) {
			List<Person> students = personRepository.findByNameStartingWithAndRoles(name, roles);
			ModelAndView modelAndView = new ModelAndView("all_students.html");
			modelAndView.addObject("students", students);
			if(students == null || students.isEmpty()) {
			errorMessage = "No student found!";
			}
			modelAndView.addObject("errorMessage", errorMessage);
			return modelAndView;
		}
		List<Person> students = personRepository.findAllByRoles(roles);
		System.out.println(students);
		for(Person per : students) {
			if(per.getEazyClass() != null)
			System.out.println(per.getEazyClass().getName());
		}
		ModelAndView modelAndView = new ModelAndView("all_students.html");
		modelAndView.addObject("students", students);
		return modelAndView;
	}
	@GetMapping("/viewStudent/{id}/courses")
	public String viewStudent(@PathVariable int id, Model model) {
		System.out.println("calling student");
		System.out.println(id);
		Optional<Person> person = personRepository.findById(id);
		System.out.println(person.get());
		if(person.isPresent()) {
			System.out.println(person.get().getName());
			model.addAttribute("student1", person.get());
            model.addAttribute("courses", person.get().getCourses());
		}
		return "all_students";
	}
	
	@GetMapping("/allRequests")
	public ModelAndView viewAllRequests(Model model) {
		ModelAndView modelAndView = new ModelAndView("all_requests.html");
		List<RequestTO> requestTo = new ArrayList<RequestTO>();
		List<StudentsRequestedCourses> requests = requestedRepository.findByStatus(EazySchoolConstants.REQUESTED);
		for(StudentsRequestedCourses req : requests) {
			RequestTO request = new RequestTO();
			Optional<Person> person = personRepository.findById(req.getPersonId());
			Optional<Courses> course = coursesRepository.findById(req.getCourseId());
			request.setRequestId(req.getRequestedId());
			request.setPerson(person.get());
			request.setCourses(course.get());
			request.setStatus(req.getStatus());
			requestTo.add(request);
		}
		modelAndView.addObject("requests", requestTo);
		return modelAndView;
	}
	
	@GetMapping("/enrollStudentToCourse/{requestid}")
	public ModelAndView enrollStudentToCourse(Model model, @PathVariable int requestid) {
		ModelAndView modelANdView = new ModelAndView();
		StudentsRequestedCourses request = requestedRepository.findById(requestid).get();
		Courses course = coursesRepository.findById(request.getCourseId()).get();
		Person person = personRepository.findById(request.getPersonId()).get();
		person.getCourses().add(course);
		personRepository.save(person);
		request.setStatus(EazySchoolConstants.APPROVED);
		requestedRepository.save(request);
		modelANdView.setViewName("redirect:/admin/allRequests");
		return modelANdView;
	}
	
	@GetMapping("/rejectStudentFromCourse/{id}")
	public ModelAndView rejectStudentFromCourse(Model model, @PathVariable int id) {
		StudentsRequestedCourses req = requestedRepository.findById(id).get();
		req.setStatus(EazySchoolConstants.REJECTED);
		requestedRepository.save(req);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/admin/allRequests");
		return modelAndView;
	}
	
	@GetMapping("/displayTeachers")
	public ModelAndView displayTeachers(Model model, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("all_teachers.html");
		List<Teacher> teachers = teacherRepository.findAll();
		System.out.println(teachers);
		modelAndView.addObject("teachers",teachers);
		return modelAndView;
	}
	@GetMapping("/deleteTeacher")
	public ModelAndView deleteTeacher(Model model, @RequestParam Long teacherId) {
		ModelAndView modelAndView = new ModelAndView();
		Teacher teacher = teacherRepository.findById(teacherId).get();
		Person person = personRepository.findByTeacher(teacher);
		personRepository.deleteById(person.getPersonId());
		teacherRepository.deleteById(teacherId);
		modelAndView.setViewName("redirect:/admin/displayTeachers");
		return modelAndView;
	}
	
	@PostMapping("/addTeacherToClass")
	public ModelAndView addTeacherToClass(Model model, @ModelAttribute("teacher") Teacher teacher, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
		Teacher teacherEntity = teacherRepository.readByEmail(teacher.getEmail());
		//System.out.println(teacherEntity.getEmail());
        if(teacherEntity==null || !(teacherEntity.getTeacherId()>0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=true");
            return modelAndView;
        }
        teacherEntity.getClasses().add(eazyClass);
        teacherRepository.save(teacherEntity);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=false");
        return modelAndView;
	}
	
	@GetMapping("/deleteClassFromTeacher")
	public ModelAndView deleteClassFromTeacher(Model model, @RequestParam Long teacherId, @RequestParam int classId) {
		ModelAndView modelAndView = new ModelAndView();
		Teacher teacher = teacherRepository.findById(teacherId).get();
		EazyClass classs = eazyClassRepository.findById(classId).get();
		teacher.getClasses().remove(classs);
		teacherRepository.save(teacher);
		modelAndView.setViewName("redirect:/admin/displayTeachers");
		return modelAndView;
	}
	
	@GetMapping("/displaySubjects")
	public ModelAndView displaySubjects(Model model) {
		ModelAndView modelAndView = new ModelAndView("subject_secure.html");
		List<Subjects> subjects = subjectsRepository.findAll();
		List<SubjectsTO> allSubjects = new ArrayList<SubjectsTO>();
		for(Subjects s : subjects) {
			Teacher t = teacherRepository.findBySubject(s);
			SubjectsTO sub = new SubjectsTO();
			sub.setSubject(s);
			System.out.println(s.getClasses());
			sub.setEazyClass(s.getClasses());
			sub.setTeacher(t);
			allSubjects.add(sub);
		}
		
		modelAndView.addObject("allSubjects", allSubjects);
		modelAndView.addObject("subject", new SubjectsTO());
		return modelAndView;
		
	}
	@PostMapping("/addSubject")
	public ModelAndView addSubject(Model model, @ModelAttribute("subject") SubjectsTO subject) {
		ModelAndView modelAndView = new ModelAndView();
		Subjects subject2 = subject.getSubject();
		subject2.setStatus(EazySchoolConstants.NOT_ASSIGNED);
		EazyClass eazyClass = eazyClassRepository.findByName(subject.getClassName());
		subject2.getClasses().add(eazyClass);
		subjectsRepository.save(subject2);
		modelAndView.setViewName("redirect:/admin/displaySubjects");
		return modelAndView;
	}
	
	@GetMapping("/displayClassSubjects")
	public ModelAndView displayClassSubjects(Model model, @RequestParam int classId) {
		ModelAndView modelAndView = new ModelAndView("class_subjects.html");
		EazyClass eazyClass = eazyClassRepository.findById(classId).get();
		Set<Subjects> subjects = eazyClass.getSubjects();
		//List<Subjects> subjects = subjectsRepository.findByClasses(eazyClass);
		List<Subjects> allSubjects = subjectsRepository.findAll();
		for(Subjects sub : subjects) {
			allSubjects.remove(sub);
		}
		modelAndView.addObject("subjects",subjects);
		modelAndView.addObject("currentClass", eazyClass);
		modelAndView.addObject("allSubjects",allSubjects);
		return modelAndView;
	}
	
	@GetMapping("/addSubjectToClass")
	public ModelAndView addSubjectToClass(Model model, @RequestParam int subjectId, @RequestParam int classId) {
		ModelAndView modelAndView = new ModelAndView();
		Subjects subject = subjectsRepository.findById(subjectId).get();
		EazyClass eazyClass = eazyClassRepository.findById(classId).get();
		subject.getClasses().add(eazyClass);
		eazyClass.getSubjects().add(subject);
		subjectsRepository.save(subject);
		modelAndView.setViewName("redirect:/admin/displayClassSubjects?classId="+eazyClass.getClassId());
		return modelAndView;
	}
	
	@GetMapping("/deleteSubjectFromClass")
	public ModelAndView deleteSubjectFromClass(Model model, @RequestParam int subjectId, @RequestParam int classId) {
		ModelAndView modelAndView = new ModelAndView();
		EazyClass eazyClass = eazyClassRepository.findById(classId).get();
		Subjects subject = subjectsRepository.findById(subjectId).get();
		subject.getClasses().remove(eazyClass);
		subjectsRepository.save(subject);
		modelAndView.setViewName("redirect:/admin/displayClassSubjects?classId="+eazyClass.getClassId());
		return modelAndView;
	}
	
	@GetMapping("/makeClassTeacher")
	public ModelAndView makeClassTeacher(Model model, @RequestParam Long teacherId, @RequestParam int classId) {
		ModelAndView modelAndView = new ModelAndView();
		Teacher teacher = teacherRepository.findById(teacherId).get();
		EazyClass eazyClass = eazyClassRepository.findById(classId).get();
		eazyClass.setClassTeacher(teacher);
		eazyClassRepository.save(eazyClass);
		modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
		return modelAndView;
	}
	/*
	 * @GetMapping("/searchStudents") public ModelAndView searchStudents(Model
	 * model, @ModelAttribute("person") Person person) { ModelAndView modelAndView =
	 * new ModelAndView(); List<Person> persons =
	 * personRepository.findByName(person.getName());
	 * modelAndView.addObject("students", persons);
	 * modelAndView.setViewName("redirect:/admin/allStudents?name="+person.getName()
	 * ); return modelAndView; }
	 */
	 

}
