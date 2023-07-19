package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.Contrato;

@RepositoryRestResource(collectionResourceRel = "contratos", path = "contratos")
@CrossOrigin("*")
public interface ContratoRepository extends JpaRepository<Contrato, Integer> {

}
