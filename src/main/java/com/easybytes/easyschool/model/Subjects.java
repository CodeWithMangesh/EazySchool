package com.easybytes.easyschool.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Subjects extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int subjectId;
	
	private String name;
	
	private String status;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "subjects_classes", 
    			joinColumns = {@JoinColumn(name = "subject_id", referencedColumnName = "subjectId")},
    			inverseJoinColumns = {@JoinColumn(name= "class_id", referencedColumnName = "classId")})
    private Set<EazyClass> classes = new HashSet<EazyClass>();
	
	/*
	 * @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity
	 * = Teacher.class)
	 * 
	 * @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId",nullable
	 * = true) private Teacher teacher;
	 */
}
