package com.smartshelf.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartshelf.mqtt.MessageHandler;
import com.smartshelf.mqtt.MqttClientImpl;
import com.smartshelf.mqtt.MqttMessageHandler;
import com.smartshelf.mqtt.MqttService;

@Configuration
public class MqttConfig {

	private static MqttService mqttClient = new MqttClientImpl(); 
	
	private static List<String> topics; 
	
	public MqttConfig() {
		
		topics = new ArrayList<String>(); 
		topics.add("devices/weight"); 
	}
	
	@Bean
	public MqttService mqttClient() {
		
		return mqttClient;
	}
	
	@Bean
	public MqttMessageHandler mqttMessageHandler() {
		return new MessageHandler(); 
	}
	
	public static List<String> getStdTopics() {
		return topics; 
	}
}
