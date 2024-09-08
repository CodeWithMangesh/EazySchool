package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.EazyClass;
import com.easybytes.easyschool.model.Subjects;

@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, Integer>{
	
	List<Subjects> findByStatus(String status);
	
	List<Subjects> findByClasses(EazyClass eazyClass);

}
