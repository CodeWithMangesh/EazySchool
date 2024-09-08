package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.EazyClass;
import com.easybytes.easyschool.model.Subjects;
import com.easybytes.easyschool.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	Teacher readByEmail(String email);

	@Query(value = "SELECT t.* FROM teacher_classes p JOIN teacher t ON t.teacher_id = p.teacher_id WHERE p.class_id = ?1 ORDER BY t.name", nativeQuery = true)
	List<Teacher> findByClassNativeSQL(@Param("classId") int classId);
	
	Teacher findBySubject(Subjects subject);

}
