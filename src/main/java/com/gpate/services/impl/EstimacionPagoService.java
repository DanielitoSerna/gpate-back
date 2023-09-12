package com.gpate.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.IEstimacionPagoService;

@Service
public class EstimacionPagoService implements IEstimacionPagoService {
	
	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;

	@Override
	public String eliminarEstimacionPago(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
