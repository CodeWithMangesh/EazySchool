package com.easybytes.easyschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.LeaveRequests;
import com.easybytes.easyschool.model.Person;
import com.easybytes.easyschool.model.Teacher;

@Repository
public interface LeaveRequestsRepository extends JpaRepository<LeaveRequests, Integer>{
	
	List<LeaveRequests> findByStudent(Person student);
	
	@Query(value = "SELECT l.* FROM leave_REQUESTS l, person p WHERE l.student_id=p.person_id AND l.status= ?2 AND p.class_id = ?1", nativeQuery = true)
	//@Query(value = "SELECT t.* FROM teacher_classes p JOIN teacher t ON t.teacher_id = p.teacher_id WHERE p.class_id = ?1 ORDER BY t.name", nativeQuery = true)
	List<LeaveRequests> findByClassAndStatusNativeSQL(@Param("classId") int classId, String status);

}
