package com.easybytes.easyschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybytes.easyschool.model.Courses;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer>{

}
