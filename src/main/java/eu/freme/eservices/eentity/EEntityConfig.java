package eu.freme.eservices.eentity;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import eu.freme.eservices.eentity.api.EEntityService;

@SpringBootApplication
@ComponentScan("eu.freme.eservices.eentity.api")
public class EEntityConfig {
	
	@Bean
	public EEntityService getEntityApi(){
		return new EEntityService();
	}
}
