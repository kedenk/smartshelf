package com.smartshelf.mqtt;

public interface MqttService {

	public void connect(); 
	
	public void disconnect(); 
	
	public void subscribe(); 
	
	public void publish(); 
}
