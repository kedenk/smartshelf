package com.smartshelf.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.mapping.PrimaryKey;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AbstractGenericDao<Entity> {

	@PersistenceContext
	protected EntityManager entityManager; 
		
	public PrimaryKey create(Entity newPersistentObject) {
		entityManager.persist( newPersistentObject );
		entityManager.flush();
		return null;
	}
}
