package com.epam.brest.service.web_app.config;

import com.epam.brest.SerializerAndDesirializer.ConverterFromDateToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromMultipartToString;
import com.epam.brest.SerializerAndDesirializer.ConverterFromStringToDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.brest")
public class ApplicationConfig implements WebMvcConfigurer {

	@Override
    public void addFormatters(FormatterRegistry formatterRegistry)
    {
        formatterRegistry.addConverter(new ConverterFromStringToDate());
        formatterRegistry.addConverter(new ConverterFromDateToString());
        formatterRegistry.addConverter(new ConverterFromMultipartToString());
    }
	
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }
    
    @Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper()
           .registerModule(new Jdk8Module());
        return mapper;
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/resources/**")
          .addResourceLocations("/resources/","file:web-app-1.0-SNAPSHOT/resources/"); 
        
    }	
	
	@Bean  
    public InternalResourceViewResolver jspViewResolver() {  
    InternalResourceViewResolver resolver  = new InternalResourceViewResolver();  
    resolver.setPrefix("/WEB-INF/view/");  
    resolver.setSuffix(".jsp");  
    return resolver;
	}
	
}
