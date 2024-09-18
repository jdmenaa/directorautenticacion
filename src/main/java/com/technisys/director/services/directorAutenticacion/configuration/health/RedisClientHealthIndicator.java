package com.technisys.director.services.directorAutenticacion.configuration.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.technisys.director.redis.connector.RedisClientConn;


@Component
@DependsOn({"redisClientConn"})
public class RedisClientHealthIndicator implements HealthIndicator {

	private static Logger logger = LoggerFactory.getLogger(RedisClientHealthIndicator.class);

	@Autowired (required = false)
	RedisClientConn redisClientConn;
	
	@Override
	public Health health() {
		if (redisClientConn == null) {
			return Health.down().withException(new Exception("Database (Redis) can't be disabled!")).build();
		}
		if (!checkServiceConfigConnection()) {
			return Health.down().withException(new Exception( "Can't connect to database (Redis)")).build();
		}		
		return Health.up().build();
	}
    
	/**
	 * @return the status of the metadata configuration connection 
	 */
    public boolean checkServiceConfigConnection() {
    	try {
			return redisClientConn.getStatus();
		} catch (Exception e) {
			logger.error("Error getting the status of the metadata configuration connection.", e);
		}
    	return false;
    }
    
   
}