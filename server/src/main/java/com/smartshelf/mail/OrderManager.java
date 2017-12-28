package com.smartshelf.mail;

import com.smartshelf.model.Box;

public interface OrderManager {

	public void sendMail(String to, Box box) throws Exception; 
}
