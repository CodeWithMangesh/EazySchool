package com.easybytes.easyschool.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionTO {
	
	private int questionId;
	
	private Long teacherId;
	
	private int personId;
	
	private String que;
	
	private String ans;
	
	private String status;
	
	private String studentName;
	
	private String teacherName;
	
	

}
