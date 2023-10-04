package com.gpate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gpate.model.QViewSaldoContrato;
import com.gpate.model.ViewSaldoContrato;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

@RepositoryRestResource(collectionResourceRel = "viewSaldoContrato", path = "viewSaldoContrato")
@CrossOrigin("*")
public interface ViewSaldoContratoRepository extends JpaRepository<ViewSaldoContrato, Long>,
		QuerydslPredicateExecutor<ViewSaldoContrato>, QuerydslBinderCustomizer<QViewSaldoContrato> {

	@Override
	default void customize(QuerydslBindings bindings, QViewSaldoContrato root) {
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
