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
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Courses extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int courseId;
	
	
	private String name;
	
	private String fees;
	
	@ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<Person> persons = new HashSet<Person>();

}
