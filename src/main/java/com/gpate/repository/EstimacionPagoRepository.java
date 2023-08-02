package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.EstimacionPago;
import com.gpate.model.QEstimacionPago;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

@RepositoryRestResource(collectionResourceRel = "estimacionPago", path = "estimacionPago")
@CrossOrigin("*")
public interface EstimacionPagoRepository extends JpaRepository<EstimacionPago, Long>,
		QuerydslPredicateExecutor<EstimacionPago>, QuerydslBinderCustomizer<QEstimacionPago> {
	
	@Override
	default void customize(QuerydslBindings bindings, QEstimacionPago root) {
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
