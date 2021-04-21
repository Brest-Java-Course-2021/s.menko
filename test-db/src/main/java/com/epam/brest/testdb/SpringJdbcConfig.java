package com.epam.brest.testdb;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
public class SpringJdbcConfig {

    @Bean
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testDB;MODE=MySQL") 
                .addScript("create-test-db.sql")
                .addScript("init-test-db.sql")
                .build();
        
    }
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(h2DataSource());
        sessionFactory.setPackagesToScan(
          "com.epam.brest.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }
	
	 private final Properties hibernateProperties() {
	        Properties hibernateProperties = new Properties();
	        hibernateProperties.setProperty(
	          "hibernate.show_sql", "true");
	        hibernateProperties.setProperty(
	          "hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

	        return hibernateProperties;
	    }
	 
	 	@Bean
		@Autowired
		public HibernateTransactionManager transactionManagerHibernate(SessionFactory sessionFactory) {
			
			HibernateTransactionManager txManager = new HibernateTransactionManager();
			txManager.setSessionFactory(sessionFactory);

			return txManager;
		}
	 	
	 	@Bean
		public PasswordEncoder encoder() {
		    return new BCryptPasswordEncoder();
		}


    
  

}
