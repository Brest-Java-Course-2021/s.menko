package com.luv2code.springdemo.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZonedDateTime;

public class LocalDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    ConverterFromDateToString converter;

    public LocalDateTimeSerializer() {
        this.converter =new  ConverterFromDateToString();
    }
    @Override
    public void serialize(ZonedDateTime arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        arg1.writeString(converter.convert(arg0));
    }
}
