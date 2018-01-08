	package com.smartshelf.model;

import net.minidev.asm.ex.ConvertException;

public enum LEDColor {

	YELLOW(2), 
	BLUE(1), 
	RED(0);
	
	private final int value;
    
    private LEDColor(int value){
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getStr() {
    	return String.valueOf(value);
    }
    
    public static LEDColor getLEDColor(String color) {
    	
    	LEDColor c = null; 
    	
    	switch(color) {
		case "blue": 
			c = LEDColor.BLUE; 
			break; 
		case "yellow": 
			c = LEDColor.YELLOW; 
			break; 
		case "red": 
			c = LEDColor.RED; 
		default: 
			throw new ConvertException("Not supported color given.");
		}
    	
    	return c; 
    }
}
