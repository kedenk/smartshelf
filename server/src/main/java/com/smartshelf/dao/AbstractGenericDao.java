package com.smartshelf.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;

public class AbstractGenericDao<Entity, PrimaryKey extends Serializable>
	implements GenericDao<Entity, PrimaryKey> {

	protected final static Logger logger = org.slf4j.LoggerFactory.getLogger(GenericDao.class);
	 
	protected final Class<Entity> entityClass;
	 
	protected final Class<PrimaryKey> primaryKeyClass;

	{
		 ParameterizedType type =
		 (ParameterizedType) getClass().getGenericSuperclass();
		 Type[] actualTypeArguments = type.getActualTypeArguments();
		 
		 @SuppressWarnings("unchecked")
		 Class<Entity> entityClass = (Class<Entity>) actualTypeArguments[0];
		 this.entityClass = entityClass;
		 
		 @SuppressWarnings("unchecked")
		 Class<PrimaryKey> primaryKey =
		 (Class<PrimaryKey>) actualTypeArguments[1];
		 this.primaryKeyClass = primaryKey;
	 }
	
	@Override
	public PrimaryKey getPrimaryKey(Entity persistentObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrimaryKey create(Entity newPersistentObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Entity persistentObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Entity persistentObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(PrimaryKey key) {
		
		remove(findByPrimaryKey(key));
	}

	@Override
	public Entity findByPrimaryKey(PrimaryKey key) {
		// TODO Auto-generated method stub
		return null;
	}

}
