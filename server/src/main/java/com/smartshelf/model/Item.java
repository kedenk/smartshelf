package com.smartshelf.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "item")
@SequenceGenerator(name="item_seq", initialValue=1, allocationSize=100)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "item_seq")
	public long id; 
	
	@Column(nullable = false)
	public String name; 
	
	@Column
	public String description; 
	
	@Column
	public int amount; 
	
	@OneToMany
	public List<ItemDatasheet> datasheet;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<ItemDatasheet> getDatasheet() {
		return datasheet;
	}

	public void setDatasheet(List<ItemDatasheet> datasheet) {
		this.datasheet = datasheet;
	}
}
