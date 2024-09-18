package com.technisys.director.services.directorAutenticacion.configuration.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.technisys.director.core.utils.SpringContext;
import com.technisys.workflow.actions.techlog.TechlogClientAction;


@Component
public class TechlogActionHealthIndicator implements HealthIndicator {

	private static Logger logger = LoggerFactory.getLogger(TechlogActionHealthIndicator.class);

    @Override
    public Health health() {
    	if (!checkTechlogServerConnectionStatus()) {
    		return Health.down().withException(new Exception("Can't connect to MQ/JMS Server (TechlogClient)")).build();
        }
        return Health.up().build();
    }
	
    public boolean checkTechlogServerConnectionStatus() {
    	try {
        	return ((TechlogClientAction)SpringContext.getBean("techlog-request")).getStatus();
		} catch (Exception e) {
			logger.error("Error getting techlog action status.", e);
		}
    	return false;
    }
}