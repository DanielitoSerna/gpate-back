package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.QViewContrato;
import com.gpate.model.QViewContratoTotal;
import com.gpate.model.ViewContratoTotal;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

@RepositoryRestResource(collectionResourceRel = "viewContratos", path = "viewContratos")
@CrossOrigin("*")
public interface ViewContratoTotalRepository extends JpaRepository<ViewContratoTotal, Long>,
QuerydslPredicateExecutor<ViewContratoTotal>, QuerydslBinderCustomizer<QViewContratoTotal> {
	
	@Override
	default void customize(QuerydslBindings bindings, QViewContratoTotal root) {
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
