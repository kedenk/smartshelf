package com.smartshelf.mqtt;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements MqttMessageHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String strEncoding = "UTF-8"; 
	
	// after the last '/' will be the boxid
	private final String BOX_WEIGHT = "devices/weight/"; 
	
	@Override
	public void handleMessage(String topic, MqttMessage message) {
		
		try {
			
			log.info(String.format("Handle message from topic '%s': %s", topic, this.convertPayload(message.getPayload())));
			
			
			if( topic.startsWith( this.BOX_WEIGHT) ) {
				
				this.handleWeightMeasurement(topic, message);
			} 
			
			
		} catch (UnsupportedEncodingException e) {

			log.error(e.getMessage());
		}
		
	}
	
	private void handleWeightMeasurement(String topic, MqttMessage message) {
		
		// last part of the topic is the box id
		String[] topic_parts = topic.split("/"); 
		String str_boxId = topic_parts[topic_parts.length - 1];
		long boxId = -1; 
		
		try {
		if( str_boxId != null ) {
			boxId = Long.parseLong(str_boxId); 
		}
		} catch(NumberFormatException e) {
			log.warn(String.format("Topic contains no parsable box id (%s).", topic));
			return; 
		}
		
		log.error("NOT IMPLEMENTED YET!!!!!!");
	}

	private String convertPayload(byte[] payload) throws UnsupportedEncodingException {
		
		return new String(payload, strEncoding); 
	}
}
