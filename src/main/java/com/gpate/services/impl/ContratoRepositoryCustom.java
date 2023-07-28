package com.gpate.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.gpate.model.Contrato;
import com.gpate.services.IContratoRepositoryCustom;

@Repository
public class ContratoRepositoryCustom implements IContratoRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Contrato> getContractsByFilters(String proyecto, String folio, String especialidad, String proveedor,
			String estado) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contrato> cq = cb.createQuery(Contrato.class);

		Root<Contrato> root = cq.from(Contrato.class);
		List<Predicate> predicates = new ArrayList<>();

		if (proyecto != null && !proyecto.isEmpty()) {
			predicates.add(cb.like(cb.upper(root.get("proyecto")), "%"+ proyecto.toUpperCase() +"%" ));
		}

		if (folio != null && !folio.isEmpty()) {
			predicates.add(cb.like(cb.upper(root.get("folio")), "%"+ folio.toUpperCase() +"%" ));
		}

		if (especialidad != null && !especialidad.isEmpty()) {
			predicates.add(cb.like(cb.upper(root.get("especialidad")), "%"+ especialidad.toUpperCase() +"%" ));
		}
		
		if (proveedor != null && !proveedor.isEmpty()) {
			predicates.add(cb.like(cb.upper(root.get("proveedor")), "%"+ proveedor.toUpperCase() +"%" ));
		}
		
		if (estado != null && !estado.isEmpty()) {
			predicates.add(cb.like(cb.upper(root.get("estado")), "%"+ estado.toUpperCase() +"%" ));
		}

		cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cq).getResultList();
	}

}
