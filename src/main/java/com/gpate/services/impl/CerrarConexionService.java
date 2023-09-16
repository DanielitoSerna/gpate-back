package com.gpate.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CerrarConexionService {

	@Value("${database.name}")
	private String userBucketPath;

	@Autowired
	private EntityManager entityManager;

	public void cerrarConexion() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pg_terminate_backend(pg_stat_activity.pid)" + " FROM pg_stat_activity" + " WHERE datname = "
				+ "'" + userBucketPath + "'" + "  AND pid <> pg_backend_pid()");

		Query query = entityManager.createNativeQuery(sql.toString());
		System.out.println(query.getResultList());
	}

}
