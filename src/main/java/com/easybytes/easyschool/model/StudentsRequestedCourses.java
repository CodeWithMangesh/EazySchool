package com.easybytes.easyschool.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudentsRequestedCourses {
	@Id
	private int requestedId;
	private int personId;
	private int courseId;
	private String status;

}
