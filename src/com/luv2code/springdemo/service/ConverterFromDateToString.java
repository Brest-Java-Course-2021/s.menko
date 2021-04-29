package com.luv2code.springdemo.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ConverterFromDateToString implements Converter<ZonedDateTime, String> {
    @Override
    public String convert(ZonedDateTime data) {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return  data.format(formatter);
    }
}
