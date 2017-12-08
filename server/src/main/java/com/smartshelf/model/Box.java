package com.smartshelf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "box")
public class Box {

	public static final String FIND_ALL = "Box.findAll";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id; 
	
	@OneToOne
	public Item item; 
	
	@Column
	public int amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	} 
}
