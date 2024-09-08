package com.easybytes.easyschool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Roles;
import com.easybytes.easyschool.model.Teacher;
import com.easybytes.easyschool.repository.PersonRepository;
import com.easybytes.easyschool.repository.RolesRepository;
import com.easybytes.easyschool.repository.TeacherRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	public boolean createNewPerson(Person person) {
		int roleId = person.getRoles().getRoleId();
		if(roleId == 2) {
		boolean isSaved = false;
		Roles role = rolesRepository.getByRoleName(EazySchoolConstants.STUDENT_ROLE);
		person.setRoles(role);
		person.setPwd(passwordEncoder.encode(person.getPwd()));
		person = personRepository.save(person);
		if(null != person && person.getPersonId() > 0) {
			isSaved = true;
		}
		return isSaved;
		}
		else if(roleId == 3) {
			boolean isSaved = false;
			Roles role = rolesRepository.getByRoleName(EazySchoolConstants.TEACHER_ROLE);
			person.setRoles(role);
			person.setPwd(passwordEncoder.encode(person.getPwd()));
			
			Teacher teacher = new Teacher();
			teacher.setName(person.getName());
			teacher.setEmail(person.getEmail());
			Teacher savedTeacher = teacherRepository.save(teacher);
			person.setTeacher(savedTeacher);
			person = personRepository.save(person);
			if(null != person && person.getPersonId() > 0) {
				isSaved = true;
			}
			return isSaved;
		}
		return false;
		
	}

}
