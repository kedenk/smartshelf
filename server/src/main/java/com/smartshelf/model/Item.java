package com.smartshelf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id; 
	
	@Column(nullable = false)
	public String name; 
	
	@Column
	public String description; 
	
	@Column
	public int amount; 
}
