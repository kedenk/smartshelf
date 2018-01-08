package com.smartshelf.dao;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.smartshelf.model.Box;
import com.smartshelf.model.Item;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PersistenceTest {

	@Autowired
	public ItemDao itemDao;
	
	@Autowired
	public BoxDao boxDao;
	
	@BeforeTransaction
	public void readyCheck() {
		
		Assert.assertNotNull(itemDao);
		Assert.assertNotNull(boxDao);
		
		boxDao.setEntityClass(Box.class);
		itemDao.setEntityClass(Item.class);
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
	@Rollback(true)
	public void commonDbTest() {

		Item toStore = createRandomItemObj(); 
		itemDao.create( toStore );
		long toStoreId = toStore.getId(); 
		
		List<Item> result = itemDao.findWithKeyword( toStore.name );
		
		if( !result.stream().anyMatch( x -> x.name.equals(toStore.name)) ) {
			Assert.fail("Stored entity NOT available in table.");
		}
		
		itemDao.delete(toStore);
		Item tmp = itemDao.findById(toStoreId);
		
		Assert.assertNull(tmp);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateEntityTest() {
		
		int ammountToSet = 544864; 
		
		Box b = boxDao.findById(100); 
		Assert.assertNotNull(b);
		
		b.amount = ammountToSet; 
		Box saved = boxDao.save(b);
		Assert.assertNotNull(saved);
		
		b = boxDao.findById(100); 
		Assert.assertNotNull(b);
		
		Assert.assertEquals(ammountToSet, saved.amount);
		Assert.assertEquals(ammountToSet, b.amount);
	}
}
