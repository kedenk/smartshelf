package com.smartshelf.services;

public class BlinkCommandMsg {

	public long boxid; 
	public int color; 
	
	public BlinkCommandMsg() {
		
	};
	
	public BlinkCommandMsg(long boxid, int color) {
		this.boxid = boxid; 
		this.color = color; 
	}
}
