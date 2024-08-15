package com.easybytes.easyschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

	Person readByEmail(String email);

}
