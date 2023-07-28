package com.gpate.services;

import java.util.List;

import com.gpate.model.Contrato;

public interface IContratoRepositoryCustom {

	public List<Contrato> getContractsByFilters(String proyecto, String folio, String especialidad, String proveedor,
			String estado);

}
