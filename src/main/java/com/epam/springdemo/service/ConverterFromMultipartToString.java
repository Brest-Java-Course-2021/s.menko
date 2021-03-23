package com.luv2code.springdemo.service;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConverterFromMultipartToString implements Converter<MultipartFile, String> {
    @Override
    public String convert(MultipartFile file) {
    	
        return  file.getOriginalFilename();
    }
}