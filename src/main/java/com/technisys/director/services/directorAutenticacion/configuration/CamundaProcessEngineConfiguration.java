package com.technisys.director.services.directorAutenticacion.configuration;

import java.io.IOException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Camunda process engine configuration
 */
@Configuration
public class CamundaProcessEngineConfiguration {
	
	@Value("${camunda.bpm.database.history-level:none}")
	private String historyLevel;
	
	@Value("${camunda.bpm.database.schema-update:false}")
	private String databaseSchemaUpdate;
	
	@Value("${camunda.bpm.database.jdbc-batch-processing:true}")
	private Boolean jdbcBatchProcessing;

	@Autowired
	private ResourcePatternResolver resourceLoader;

	@Bean
	public PlatformTransactionManager transactionManager(DataSource datasource) {
		return new DataSourceTransactionManager(datasource);
	}

	@Bean
	public SpringProcessEngineConfiguration processEngineConfiguration(DataSource datasource, PlatformTransactionManager transactionManager) throws IOException {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		
		Resource[] resources = resourceLoader.getResources("classpath*:processes/*.bpmn");
        String entrypointconfigpath = System.getProperty("config.entrypoint");
        Resource preprocessresource = resourceLoader.getResource("file:"+entrypointconfigpath+"/processes/entryPointPreProcessing.bpmn");
        Resource postprocessresource = resourceLoader.getResource("file:"+entrypointconfigpath+"/processes/entryPointPostProcessing.bpmn");
        resources = addResource(resources,preprocessresource);
        resources = addResource(resources,postprocessresource);
		config.setDeploymentResources(resources);
		
		SpinProcessEnginePlugin spinValuesPlugin = new SpinProcessEnginePlugin();
		config.getProcessEnginePlugins().add(spinValuesPlugin);
		
		config.setDeploymentName("directorAutenticacion-1.0.0");
		config.setDataSource(datasource);
		config.setTransactionManager(transactionManager);
		config.setDatabaseSchemaUpdate(databaseSchemaUpdate);
		config.setHistory(historyLevel);
		config.setJobExecutorActivate(true);
		
		config.setMetricsEnabled(false);

		config.setTaskMetricsEnabled(false);
		
		// para oracle: jdbcBatchProcessing = false
		config.setJdbcBatchProcessing(jdbcBatchProcessing);

		return config;
	}

	Resource[] addResource(Resource[] existingresources, Resource newresource) {
        existingresources  = Arrays.copyOf(existingresources, existingresources.length + 1);
        existingresources[existingresources.length - 1] = newresource;
        return existingresources;
    }
	
	@Bean
	public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration processEngineConfiguration) throws IOException {
		ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
		factoryBean.setProcessEngineConfiguration(processEngineConfiguration);
		return factoryBean;
	}

	@Bean
	public RepositoryService repositoryService(ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService(ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}

	@Bean
	public TaskService taskService(ProcessEngine processEngine) {
		return processEngine.getTaskService();
	}

	@Bean
	public ManagementService managementService(ProcessEngine processEngine) {
		return processEngine.getManagementService();
	}

}
