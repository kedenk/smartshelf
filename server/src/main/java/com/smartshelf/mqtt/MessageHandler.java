package com.smartshelf.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import com.smartshelf.dao.BoxDao;
import com.smartshelf.model.Box;

public class MessageHandler implements MqttMessageHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String strEncoding = "UTF-8"; 
	
	@Autowired
	private BoxDao boxDao; 
	
	@Value("${weight.maxlength}")
	private int MAX_LAST_FLOAT_SIZE;
	
	private static Map<Long, ArrayList<Float>> lastFloats = new HashMap<Long, ArrayList<Float>>();
	
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
	
	@Async
	private void handleWeightMeasurement(String topic, MqttMessage message) {
		
		// last part of the topic is the box id
		String[] topic_parts = topic.split("/"); 
		String str_boxId = topic_parts[topic_parts.length - 1];
		long boxId = -1; 
		
		try {
			if( str_boxId != null ) {
				boxId = Long.parseLong(str_boxId); 
				Box box = this.boxDao.findById(boxId);
				float weight = box.item.getWeight(); 
				String payload = this.convertPayload(message.getPayload()); 
				float fPayload = Float.parseFloat(payload);
				
				addWeight(boxId, fPayload);
				
				box.setAmount((int)getNewWeight(boxId));
				this.boxDao.save(box);
			}
		} catch(NumberFormatException e) {
			log.warn(String.format("Topic contains no parsable box id (%s).", topic));
			return; 
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return;
		}
	}
	
	private void addWeight(long boxid, float weight) {
		
		if( !lastFloats.containsKey(boxid) ) {
			ArrayList<Float> tmp = new ArrayList<Float>();
			tmp.add(weight);
			lastFloats.put(boxid, tmp);
			return;
		} 
		
		if( lastFloats.get(boxid).size() >= MAX_LAST_FLOAT_SIZE) {
			lastFloats.get(boxid).remove(0);
		}
		
		lastFloats.get(boxid).add(weight);
	}
	
	private float getNewWeight(long boxid) {
		
		int length = lastFloats.get(boxid).size();
		float tmpVal = 0.0f; 
		for( Float value : lastFloats.get(boxid) ) {
			tmpVal += value; 
		}
		return tmpVal / length;
	}

	private String convertPayload(byte[] payload) throws UnsupportedEncodingException {
		
		return new String(payload, strEncoding); 
	}
}
