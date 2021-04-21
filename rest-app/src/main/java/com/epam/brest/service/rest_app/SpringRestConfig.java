package com.epam.brest.service.rest_app;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.epam.brest.SerializerAndDesirializer.ConverterFromDateToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromMultipartToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromStringToDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "com.epam.brest")
public class SpringRestConfig implements WebMvcConfigurer{
	
	@Autowired
	DataSource dataSource;
	
	@Override
    public void addFormatters(FormatterRegistry formatterRegistry)
    {
        formatterRegistry.addConverter(new ConverterFromStringToDate());
        formatterRegistry.addConverter(new ConverterFromDateToString());
        formatterRegistry.addConverter(new ConverterFromMultipartToString());
    }
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper()
           .registerModule(new Jdk8Module());
        return mapper;
    }

}
