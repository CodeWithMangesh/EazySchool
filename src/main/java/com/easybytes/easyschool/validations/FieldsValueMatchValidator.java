package com.easybytes.easyschool.validations;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanWrapperImpl;

import com.easybytes.easyschool.annotation.FieldsValueMatch;
import com.easybytes.easyschool.annotation.PasswordValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

	private String field;
	private String fieldMatch;

	@Override
	public void initialize(FieldsValueMatch constraintAnnotation) {
		this.field = constraintAnnotation.field();
		this.fieldMatch = constraintAnnotation.fieldMatch();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
		Object fieldValueMatch = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

		/*
		 * if (fieldValue != null) { if (fieldValue.toString().startsWith("$2a")) {
		 * return true; } else { return fieldValue.equals(fieldValueMatch); } } else {
		 * return fieldValueMatch == null; }
		 */
		if (fieldValue != null) {
			return fieldValue.equals(fieldValueMatch);

		} else {
			return fieldValueMatch == null;
		}
	}

}
