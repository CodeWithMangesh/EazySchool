package com.easybytes.easyschool.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LeaveRequests extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_id")
	private int leaveId;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "student_id", referencedColumnName = "personId", nullable = true)
	private Person student;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "teacher_id", referencedColumnName = "teacherId", nullable = true)
	private Teacher teacher;
	
	@NotBlank(message="reason must not be blank")
	@Size(min=20, message="reason must be at least 20 characters long")
	private String reason;
	
	private String status;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	@CreatedDate
	private LocalDateTime requestDate;

}
