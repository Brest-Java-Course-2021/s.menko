package com.luv2code.springdemo.service;

import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;

@Component("conversionService")
public class ApplicationConversionService extends DefaultFormattingConversionService{
	
	
	 public ApplicationConversionService(){
	        super(); 
	        addConverter(new ConverterFromStringToDate());
	        addConverter(new ConverterFromDateToString());
	        addConverter(new ConverterFromMultipartToString());
	    }

}
