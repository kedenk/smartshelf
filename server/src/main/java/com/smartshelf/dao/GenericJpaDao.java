package com.smartshelf.dao;

import java.io.Serializable;

public interface GenericJpaDao<Entity, PrimaryKey 
	extends Serializable> extends GenericDao<Entity, PrimaryKey> {

}
