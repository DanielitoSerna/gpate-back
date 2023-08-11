package com.gpate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.validators.ContratoValidator;
import com.gpate.validators.EstimacionPagoValidator;

@SpringBootApplication
public class GpateApplication implements RepositoryRestConfigurer {
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;

	public static void main(String[] args) {
		SpringApplication.run(GpateApplication.class, args);
	}
	
	@Override
	public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config, final CorsRegistry cors) {
		config.exposeIdsFor(Contrato.class, EstimacionPago.class);
	}
	
	@Override
    public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener v) {
        v.addValidator("afterCreate", new EstimacionPagoValidator(contratoRepository, estimacionPagoRepository));
        v.addValidator("beforeCreate", new ContratoValidator());
    }
	
}
