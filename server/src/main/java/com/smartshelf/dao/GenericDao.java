package com.smartshelf.dao;

import java.io.Serializable;

public interface GenericDao<Entity, PrimaryKey extends Serializable> {

	PrimaryKey getPrimaryKey(Entity persistentObject);
	
	PrimaryKey create(Entity newPersistentObject);

	void save(Entity persistentObject);
 
	void remove(Entity persistentObject);
 
	void remove(PrimaryKey key);

	Entity findByPrimaryKey(PrimaryKey key);
}
