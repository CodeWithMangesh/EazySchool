package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.Marks;
import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Teacher;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long>{
	
	/*
	 * List<Marks> findByStudentId(int studentId);
	 * 
	 * List<Marks> findByTeacherIdAndEazyClassId(Long teacherId, int classId);
	 */	
	List<Marks> findByPerson(Person person);
	
	List<Marks> findByTeacher(Teacher teacher);

}
