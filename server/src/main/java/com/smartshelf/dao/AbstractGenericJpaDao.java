package com.smartshelf.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractGenericJpaDao<Entity, PrimaryKey extends Serializable>
	extends AbstractGenericDao<Entity, PrimaryKey>
	implements GenericJpaDao<Entity, PrimaryKey> {

	@PersistenceContext
	private EntityManager entityManager; 
	
	protected final EntityManager getEntityManager() {
		return entityManager; 
	}
	
	@Override
	public PrimaryKey create(Entity newPersistentObject) {
	entityManager.persist(newPersistentObject);
		return getPrimaryKey(newPersistentObject);
	}
	 
	@Override
	public void save(Entity persistentObject) {
		entityManager.merge(persistentObject);
	}
	 
	@Override
	public void remove(Entity persistentObject) {
		entityManager.remove(persistentObject);
	}
	 
	@Override
	public Entity findByPrimaryKey(PrimaryKey key) {
		return entityManager.find(entityClass, key);
	}
}
