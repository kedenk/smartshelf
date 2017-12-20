package com.smartshelf.mqtt;

public interface MqttService {

	public void connect(); 
	
	public void disconnect(); 
	
	public void subscribe(String topic); 
	
	public void unsubscribe(String topic);
	
	public void publish(String topic, byte[] payload); 
}
