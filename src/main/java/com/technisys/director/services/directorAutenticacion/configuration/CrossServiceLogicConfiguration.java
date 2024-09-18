package com.technisys.director.services.directorAutenticacion.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import com.technisys.commons.logging.aspect.LoggingAspect;
import com.technisys.director.core.service.cross.ServicePostProcess;
import com.technisys.director.core.service.cross.ServicePreProcess;
import com.technisys.director.entrypoint.services.EntryPointServicePostProcess;
import com.technisys.director.entrypoint.services.EntryPointServicePreProcess;

/**
 * Configuration of cross-cutting service implementation
 */
@Configuration
public class CrossServiceLogicConfiguration {

	// Enable and configure these methods if a specific cross-service logic is
	// required
	@Bean
	@Primary
	ServicePreProcess crossServiceLogicPreProcess() {
		return new EntryPointServicePreProcess();
	}

	@Bean
	@Primary
	ServicePostProcess crossServiceLogicPostProcess() {
		return new EntryPointServicePostProcess();
	}

	@Bean
	public LoggingAspect loggingAspect() {
		return new LoggingAspect();
	}

}
