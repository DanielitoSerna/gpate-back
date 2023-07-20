package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.Contrato;
import com.querydsl.core.types.EntityPath;


@RepositoryRestResource(collectionResourceRel = "contratos", path = "contratos")
@CrossOrigin("*")
public interface ContratoRepository extends JpaRepository<Contrato, Integer>,
	QuerydslPredicateExecutor<Contrato>{

}
