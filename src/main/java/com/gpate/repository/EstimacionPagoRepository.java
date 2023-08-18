package com.gpate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
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

	@Query(value = "SELECT * FROM estimacion_pago ep WHERE ep.id_contrato =:contrato AND ep.concepto =:concepto ORDER BY ep.id DESC LIMIT 1", nativeQuery = true)
	List<EstimacionPago> getNumeroAbonoByConcepto(@Param("contrato") Long contrato, @Param("concepto") String concepto);

//	@Query(value = "SELECT ep.* FROM estimacion_pago ep WHERE ROW(ep.importe, ep.numero_abono) != ALL (SELECT SUM(ep2.importe), ep2.numero_abono FROM estimacion_pago ep2 WHERE ep2.id_contrato =:contrato and ep2.concepto =:concepto GROUP BY ep2.numero_abono) AND ep.id_contrato =:contrato AND ep.concepto = 'ESTIMACIÓN'", nativeQuery = true)
//	List<EstimacionPago> getNumeroAbonoConceptoAbono(@Param("contrato") Long contrato,
//			@Param("concepto") String concepto);

	@Query(value = "SELECT ep.* FROM gpate.estimacion_pago ep WHERE ep.concepto = 'ESTIMACIÓN' AND ep.id_contrato =:contrato AND (ep.importe_abono is null OR ep.importe_abono < ep.importe) ORDER BY ep.id ASC", nativeQuery = true)
	List<EstimacionPago> getNumeroAbonoByContrato(@Param("contrato") Long contrato);

	List<EstimacionPago> findByNumeroAbonoAndContrato(String numeroAbono, Long contrato);
	
	List<EstimacionPago> findByContrato(Long contrato);

}
