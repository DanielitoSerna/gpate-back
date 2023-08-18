package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.Contrato;
import com.gpate.model.QContrato;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.util.List;


@RepositoryRestResource(collectionResourceRel = "contratos", path = "contratos")
@CrossOrigin("*")
public interface ContratoRepository extends JpaRepository<Contrato, Long>, QuerydslPredicateExecutor<Contrato>,
		QuerydslBinderCustomizer<QContrato> {

	@Override
	default void customize(QuerydslBindings bindings, QContrato root) {
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}
	
	public List<Contrato> findByFolio(String folio);

}
