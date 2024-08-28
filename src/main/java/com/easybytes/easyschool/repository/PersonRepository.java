package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Roles;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

	Person readByEmail(String email);
	
	//added by mangesh
	List<Person> findAllByRoles(Roles roles);
	
	List<Person> findByNameAndRoles(String name, Roles roles);

}
