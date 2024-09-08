package com.easybytes.easyschool.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Teacher extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long teacherId;
	
	private String name;
	
	private String email;
	
	/* private String subjectSpecialization; */
	
	/*
	 * @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY) private
	 * Set<EazyClass> classes;
	 */
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "teacher_classes", 
    			joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")},
    			inverseJoinColumns = {@JoinColumn(name= "class_id", referencedColumnName = "classId")})
    private Set<EazyClass> classes = new HashSet<EazyClass>();
	
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Marks> marks = new HashSet<>();
	
	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Subjects.class)
    @JoinColumn(name = "subject_id", referencedColumnName = "subjectId",nullable = true)
	private Subjects subject;

}
