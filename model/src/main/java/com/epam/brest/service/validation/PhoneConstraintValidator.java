package com.epam.brest.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PhoneConstraintValidator 
	implements ConstraintValidator<CheckPhone, String> {


	@Override
	public boolean isValid(String phoneField, 
						ConstraintValidatorContext theConstraintValidatorContext) {

		 if(phoneField == null) {
	            return false;
	        }
	        return phoneField.matches("[0-9()-]{6,}");
	}
}








