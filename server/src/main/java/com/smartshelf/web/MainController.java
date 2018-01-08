package com.smartshelf.web;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.dao.ItemDao;
import com.smartshelf.model.Box;
import com.smartshelf.model.Item;
import com.smartshelf.services.ClientConnection;

@Controller
public class MainController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ItemDao itemDao; 
	@Autowired 
	private BoxDao boxDao; 
	
	@Autowired
	private ClientConnection clientConnection;
	
	@Autowired
	private HttpServletRequest request;
	
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String index(Model model, @RequestParam("searchParam") Optional<String> searchParam) {
    	
    	// add views to index.html
    	setMaster(model, "searchTemplate");
    	
    	if( searchParam.isPresent() && !searchParam.get().isEmpty() ) {
    		
    		setDetail(model, "detailResultTemplate");
    		
    		System.out.println(searchParam.get());
    		model.addAttribute("searchParam", searchParam.get());
    		
    		List<Item> result = itemDao.findWithKeyword(searchParam.get()); 
    		model.addAttribute("searchResult", result); 
    		
    		model.addAttribute("currentSignals", clientConnection);
    	}
    	
        return "index";
    }
    
    @RequestMapping(value = "/searchdetail", method = RequestMethod.GET)
    public @ResponseBody String getSearchDetail(Model model, @RequestParam("searchParam") Optional<String> searchParam) throws Exception {
    	
    	if( searchParam.isPresent() && !searchParam.get().isEmpty() ) {
    		
    		setDetail(model, "detailResultTemplate");
    		model.addAttribute("searchParam", searchParam.get()); 
    		
    		List<Item> result = itemDao.findWithKeyword(searchParam.get()); 
    		model.addAttribute("searchResult", result); 
    		
    		return "detailResultTemplate";
    	
    	}
    	
    	throw new Exception("No search parameter was defined.");
    }
    
    @RequestMapping(value = { "/qrscanner" }, method = RequestMethod.GET)
    public String qrcodescanner(Model model) {
    	
    	setViews(model, "qrscanner", null);    	
    	return "index";
    }
    
    @RequestMapping(value = "/qrscanner/info/box/{boxid}", method = RequestMethod.GET)
    public String getInfo(Model model, @PathVariable("boxid") String boxid) {
    	model.addAttribute("serverurl", "localhost");
    	Box b = null; 
    	try {
    		long id = Long.parseLong(boxid);
        	boxDao.setEntityClass(Box.class);
        	b = boxDao.findById(id); 
    	} catch(Exception e) {
    		log.debug(String.format("Bad box id submited. (%s)", boxid));
    	}
    	
    	if( b != null ) {
    		setViews(model, null, "datasheet");
    		model.addAttribute("boxid", b.id); 
    		model.addAttribute("itemname", b.item.name);
    		model.addAttribute("datasheet", b.item.datasheet);
    	}
    	else 
    	{
    		setViews(model, "qrscanner", "errormsg"); 
    		model.addAttribute("errormsg", "No box with the given ID found. Maybe this QR-Code is corrupt.");
    	}
    	
    	
    	return "index";
    }
    
    @RequestMapping(value = "/item/findbyid/{id}", method = RequestMethod.GET)
    public @ResponseBody Item getItem(@PathVariable("id") long id) {
    	itemDao.setEntityClass(Item.class);
    	return itemDao.findById(id);
    }
    
    @RequestMapping(value = "/box/findbyid/{boxid}", method = RequestMethod.GET)
    public @ResponseBody Box getBox(@PathVariable("boxid") long boxid) {
    	boxDao.setEntityClass(Box.class);
    	return boxDao.findById(boxid);             
    }
    
    @RequestMapping(value = "/box/signal/start/{boxid}/{color}", method = RequestMethod.POST)
    public @ResponseBody Boolean signalBox(@PathVariable("boxid") long boxid, @PathVariable("color") String color) {
    	
    	log.info("Publish blink signal for box: " + boxid);
    	
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	
    	if( con != null ) {
    		return con.startBlinkCommand(boxid, color);
    	}
    	
    	return false;
    }
    
    private Model setDetail(Model model, String detail) {
    	model.addAttribute("detailview", detail);
    	return model; 
    }
    
    private Model setMaster(Model model, String master) {
    	model.addAttribute("masterview", master);
    	return model;
    }
    
    private Model setViews(Model model, String master, String detail) {
    	model.addAttribute("masterview", master);
    	model.addAttribute("detailview", detail);
    	
    	return model;
    }
    
    /***
     * Set the variable 'serverurl' in the model for every request
     * @return
     */
    @ModelAttribute("serverurl")
    public String getLocalAddr() {
        return request.getLocalAddr();
    }
    
}