package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.QViewSaldoContratoTotal;
import com.gpate.model.ViewSaldoContratoTotal;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

@RepositoryRestResource(collectionResourceRel = "viewSaldoContratoTotal", path = "viewSaldoContratoTotal")
@CrossOrigin("*")
public interface ViewSaldoContratoTotalRepository extends JpaRepository<ViewSaldoContratoTotal, Long>,
QuerydslPredicateExecutor<ViewSaldoContratoTotal>, QuerydslBinderCustomizer<QViewSaldoContratoTotal> {
	
	@Override
	default void customize(QuerydslBindings bindings, QViewSaldoContratoTotal root) {
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
