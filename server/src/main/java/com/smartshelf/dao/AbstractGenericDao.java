package com.smartshelf.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AbstractGenericDao<Entity extends Serializable> {

	@PersistenceContext
	protected EntityManager entityManager; 
	
	protected Class<Entity> entityType; 
	
	AbstractGenericDao() {
	}
	
	public void setEntityClass(Class<Entity> entityType) {
        this.entityType = entityType;
    }
		
	public void create(Entity entity) {
		entityManager.persist( entity );
		entityManager.flush(); 
	}
	
	
	public Entity findById(long id) {
		return entityManager.find(entityType, id);
	}

}