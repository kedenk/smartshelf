package com.smartshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartshelf.mail.OrderManager;
import com.smartshelf.model.Box;
import com.smartshelf.model.LEDColor;
import com.smartshelf.services.ClientConnection;

@Controller
public class OrderController extends AbstractController {

	class OrderResponse {
	
		public Boolean success; 
		public String message = ""; 
		
		public OrderResponse() {
			this.success = true; 
		}
		
		public void setError(String message) {
			this.message = message; 
			this.success = false; 
		}
	}
	
	@Autowired
	private OrderManager orderManager;
	
	@RequestMapping(value = "/order/box/empty", method = RequestMethod.POST)
    public @ResponseBody OrderResponse markBoxEmpty(@RequestParam("boxid") String boxid) throws Exception {
    	
    	log.info("Box marked as empty: Box " + boxid);
    	
    	OrderResponse resp = new OrderResponse(); 
    	
    	long _boxid = Long.valueOf(boxid);
    	Box box = this.boxDao.findById(_boxid); 
    	if( box == null ) {
    		resp.setError("No box with given ID found.");
    	}
    	
    	orderManager.orderSupplies(box);
    	
    	// mark box as empty with visual feedback
    	ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	if( con != null ) {
    		String color = LEDColor.RED.toString();
    		con.startBlinkCommand(_boxid, color);
    		con.addEmptyMarkedBox(_boxid);
    	}
    	
    	return resp;
    }
	
	@RequestMapping(value = "/order/box/notempty", method = RequestMethod.POST)
	public @ResponseBody OrderResponse marBoxNotEmpty(@RequestParam("boxid") String boxid) {
		
		// TODO add amount parameter do declaration
		// TODO add given amount to amount in database
		
		ClientConnection con = (ClientConnection)this.context.getBean(ClientConnection.class); 
    	if( con != null ) {
    		String color = LEDColor.RED.getStr();
    		con.stopBlinkCommand(Long.valueOf(boxid), color);
    		con.removeEmptyMarkedBox(Long.valueOf(boxid));
    	}
    	
    	return new OrderResponse();
	}
}
