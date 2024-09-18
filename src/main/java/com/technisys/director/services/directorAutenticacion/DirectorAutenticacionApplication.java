package com.technisys.director.services.directorAutenticacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.technisys.director","com.technisys.director.services"})
public class DirectorAutenticacionApplication {
	
	public static void main(String[] args) {
		// Set saxon as your transformer.
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		
		SpringApplication.run(DirectorAutenticacionApplication.class, args);
	}
}
