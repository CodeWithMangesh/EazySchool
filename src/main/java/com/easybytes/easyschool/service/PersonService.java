package com.easybytes.easyschool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Roles;
import com.easybytes.easyschool.repository.PersonRepository;
import com.easybytes.easyschool.repository.RolesRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public boolean createNewPerson(Person person) {
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

}
