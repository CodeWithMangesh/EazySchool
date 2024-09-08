package com.easybytes.easyschool.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectsTO {
	
	private Subjects subject;
	
	private Teacher teacher;
	
	private Set<EazyClass> eazyClass;
	
	private String className;

}
