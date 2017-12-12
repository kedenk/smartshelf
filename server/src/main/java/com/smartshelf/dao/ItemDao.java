package com.smartshelf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.Item;

@Repository
public class ItemDao {

	@PersistenceContext
	private EntityManager em; 
	
	public List<Item> findWithKeyword(String keyword) {
		
		List<Item> test = new ArrayList<Item>(); 
		
		Item i = new Item(); 
		i.amount = 2; 
		i.description = "Best item"; 
		i.name = "WIFI Module"; 
		i.id = 100; 
		
		test.add(i); 
		
		return test; 
	}
}
