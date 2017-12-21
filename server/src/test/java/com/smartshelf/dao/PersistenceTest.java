package com.smartshelf.dao;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smartshelf.model.Item;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PersistenceTest {

	@Autowired
	public ItemDao itemDao;
	
	@Autowired
	public BoxDao boxDao;
	
	@Before
	public void init() {
		//this.context = new ClassPathXmlApplicationContext("DaoBeans.xml");
	}
	
	// creates a random item object
	public Item createRandomItemObj() {
		
		Item i = new Item(); 
		//i.amount = new Random().nextInt(100); 
		i.description = "test description " + new Random().nextInt(893298172);
		i.name = "test name " + new Random().nextInt(893298172);
		
		return i; 
	}
	
	@Test
	@Transactional
	//@Rollback(true)
	public void commonDbTest() {

		Assert.assertNotNull(itemDao);
		Assert.assertNotNull(boxDao);
		
		Item toStore = createRandomItemObj(); 
		itemDao.create( toStore );
		
		List<Item> result = itemDao.findWithKeyword( toStore.name );
		
		if( !result.stream().anyMatch( x -> x.name.equals(toStore.name)) ) {
			//Assert.fail("Stored entity NOT available in table.");
		}
	}
}
