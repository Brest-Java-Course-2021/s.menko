package com.luv2code.springdemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.luv2code.springdemo.service.CustomerService;
import com.luv2code.springdemo.service.UserService;

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








