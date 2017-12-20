package com.smartshelf.web;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.dao.ItemDao;
import com.smartshelf.model.Box;
import com.smartshelf.model.Item;

@Controller
public class MainController {

	@Autowired
	private ItemDao itemDao; 
	
	@Autowired 
	private BoxDao boxDao; 
	
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String index(Model model, @RequestParam("searchParam") Optional<String> searchParam) {
    	
    	// add views to index.html
    	model.addAttribute("masterview", "searchTemplate");
    	
    	if( searchParam.isPresent() ) {
    		
    		model.addAttribute("detailview", "detailResultTemplate");
    		
    		System.out.println(searchParam.get());
    		model.addAttribute("searchParam", searchParam.get());
    		
    		List<Item> result = itemDao.findWithKeyword(searchParam.get()); 
    		model.addAttribute("searchResult", result); 
    	}
    	
    	Item item = new Item(); 
    	item.amount = 3; 
    	item.name = "Test"; 
    	item.description = "description";
    	//itemDao.create(item);
    	
    	Box b = new Box(); 
    	b.item = item;
    	
    	//boxDao.create(b);
    	
        return "index";
    }
    
    @RequestMapping(value = { "/qrscanner" }, method = RequestMethod.GET)
    public String qrcodescanner(Model model) {
    	
    	model.addAttribute("masterview", "qrscanner");
    	model.addAttribute("detailview", null);
    	
    	return "index";
    }
    
    @RequestMapping(value = "/item/findbyid/{id}")
    public Item getItemInfo(@PathVariable("id") long id) {
    	
    	Item entity = itemDao.findById(id); 
    	Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (Item) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
            
    	return entity;
    }

}