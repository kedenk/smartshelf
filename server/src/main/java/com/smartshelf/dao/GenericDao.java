package com.smartshelf.dao;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

public interface GenericDao<T extends Serializable> {

	public T findById(long id); 
	
	public List<T> findAll(); 
	
	public void create(T entity);
	
	public T update(T entity);
	
	public void delete(final T entity);
	
	public void deleteById(final long id);
}
