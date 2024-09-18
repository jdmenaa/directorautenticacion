package com.technisys.director.services.directorAutenticacion.configuration.health;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.technisys.director.core.utils.SpringContext;
import com.technisys.workflow.actions.translate.TranslateAction;


@Component
public class TranslateActionHealthIndicator implements HealthIndicator {

	private static Logger logger = LoggerFactory.getLogger(TranslateActionHealthIndicator.class);

    @Override
    public Health health() {         	
    	if (!checkTranslatorApiStatus()) {
    		return Health.down().withException(new Exception("Can't connect to translator API (TranslatorApi)")).build();
        }
        return Health.up().build();
    }    
	
    /**
     * @return the translate action status
     */
    public boolean checkTranslatorApiStatus() {
    	try {
        	return ((TranslateAction)SpringContext.getBean("translation-request")).getStatus();
		} catch (IOException e) {
			logger.error("Error getting translate action status.", e);
		}
    	return false;
    }
}