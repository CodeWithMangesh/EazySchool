package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
	List<Question> findByTeacherIdAndStatus(Long teacherId, String status);
	
	List<Question> findByPersonId(int personId);

}
