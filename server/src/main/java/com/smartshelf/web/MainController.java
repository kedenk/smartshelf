package com.smartshelf.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartshelf.dao.ItemDao;
import com.smartshelf.model.Item;

@Controller
public class MainController {

	@Autowired
	private ItemDao itemDao; 
	
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String index(Model model, @RequestParam("searchParam") Optional<String> searchParam) {
    	model.addAttribute("msg",
                "a jar packaging example");
    	
    	if( searchParam.isPresent() ) {
    		System.out.println(searchParam.get());
    		model.addAttribute("searchParam", searchParam.get());
    		
    		List<Item> result = itemDao.findWithKeyword("test"); 
    		model.addAttribute("searchResult", result); 
    	}
    	
        return "index";
    }

}