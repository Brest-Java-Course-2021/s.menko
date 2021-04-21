package com.epam.brest.service.impl;

import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;

import com.epam.brest.SerializerAndDesirializer.ConverterFromDateToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromMultipartToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromStringToDate;

@Component("conversionService")
public class ApplicationConversionService extends DefaultFormattingConversionService{
	
	
	 public ApplicationConversionService(){
	        super(); 
	        addConverter(new ConverterFromStringToDate());
	        addConverter(new ConverterFromDateToString());
	        addConverter(new ConverterFromMultipartToString());
	    }

}
