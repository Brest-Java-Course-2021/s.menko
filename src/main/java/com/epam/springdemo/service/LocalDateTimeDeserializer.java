package com.luv2code.springdemo.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZonedDateTime;

public class LocalDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    ConverterFromStringToDate converter;

    public LocalDateTimeDeserializer() {
        this.converter = new ConverterFromStringToDate();
    }

    @Override
    public ZonedDateTime deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
        System.out.println("Внутри сериализатора"+arg0.getText());
        System.out.println("Внутри сериализатора"+converter.convert(arg0.getText()));
        return converter.convert(arg0.getText());
    }
}