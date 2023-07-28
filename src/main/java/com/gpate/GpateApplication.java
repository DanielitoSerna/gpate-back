package com.gpate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.gpate.model.Contrato;

@SpringBootApplication
public class GpateApplication implements RepositoryRestConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(GpateApplication.class, args);
	}
	
	@Override
	public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config, final CorsRegistry cors) {
		config.exposeIdsFor(Contrato.class);
	}

}
