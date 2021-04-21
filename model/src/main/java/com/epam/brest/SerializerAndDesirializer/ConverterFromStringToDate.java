package com.epam.brest.SerializerAndDesirializer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class ConverterFromStringToDate implements Converter<String, ZonedDateTime> {
    
	@Override
    public ZonedDateTime convert(String dataFromForm) {
    	if(dataFromForm=="") return null;
    	dataFromForm+=":00";
        ZonedDateTime result = ZonedDateTime.parse(dataFromForm, DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Europe/Minsk")));
        
        return result;
        
    }
}
