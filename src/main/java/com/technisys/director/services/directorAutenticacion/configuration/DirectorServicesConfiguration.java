package com.technisys.director.services.directorAutenticacion.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.technisys.director.core.tasks.pool.DirectorTaskPool;
import org.springframework.beans.factory.annotation.Value;
@Configuration
public class DirectorServicesConfiguration {
	
	@Value("#{new Integer('${service.director.pools}')}")
	private int pools;	
	
	@Bean(name = "commonTaskPool")
	public DirectorTaskPool commonTaskPool() {
		return new DirectorTaskPool(pools);
	}
	
	@Bean(name = "splitAndJoinTaskPool")
	@Scope("prototype")
	public DirectorTaskPool splitAndJoinTaskPool() {
		return new DirectorTaskPool();
	}
	
}
