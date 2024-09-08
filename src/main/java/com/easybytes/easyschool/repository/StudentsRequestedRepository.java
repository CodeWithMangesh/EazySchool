package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.StudentsRequestedCourses;

@Repository
public interface StudentsRequestedRepository extends JpaRepository<StudentsRequestedCourses, Integer>{

	
	List<StudentsRequestedCourses> findByStatus(String status);
	
	List<StudentsRequestedCourses> findByPersonIdAndStatus(int personId, String status);
}
