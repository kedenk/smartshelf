package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class subscriberMqtt {

	public static void main(String[] args) throws MqttException {
		// TODO Auto-generated method stub
		publishMqtt sub = new publishMqtt();
		String topic = "Topic";
		sub.subscribeTopic(topic);

	}
}
