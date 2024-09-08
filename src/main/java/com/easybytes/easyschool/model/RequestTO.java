package com.easybytes.easyschool.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTO {
	
	private int requestId;
	
	private Person person;
	
	private Courses courses;
	
	private String status;

}
