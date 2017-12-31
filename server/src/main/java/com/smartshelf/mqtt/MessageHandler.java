package com.smartshelf.mqtt;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements MqttMessageHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String strEncoding = "UTF-8"; 
	
	private final String MEASUREMENT_TOPIC = "devices/weight"; 
	
	@Override
	public void handleMessage(String topic, MqttMessage message) {
		
		try {
			
			log.debug(String.format("Handle message from topic '%s': %s", topic, this.convertPayload(message.getPayload())));
			
			
			switch(topic) {
			
				case MEASUREMENT_TOPIC:
					handleMeasurement(message); 
					break;
				
				default: 
					log.warn(String.format("Message from undefined topic received: %s", topic));
			}
			
			
		} catch (UnsupportedEncodingException e) {

			log.error(e.getMessage());
		}
		
	}
	
	private void handleMeasurement(MqttMessage message) {
		
		log.error("NOT IMPLEMENTED YET!!!!!!");
	}

	private String convertPayload(byte[] payload) throws UnsupportedEncodingException {
		
		return new String(payload, strEncoding); 
	}
}
