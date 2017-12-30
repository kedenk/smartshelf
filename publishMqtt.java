package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class publishMqtt {
	public void publishTopic(String topic, String mess) {

		MqttClient client;
		try {
			client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			client.connect();
			MqttMessage message = new MqttMessage();
			message.setPayload(mess.getBytes());
			MqttTopic objTopic = client.getTopic(topic);
			MqttDeliveryToken token = objTopic.publish(message);

			System.out.println(token);
			System.out.println("\tMessage '" + mess + " to " + topic);

			client.disconnect();

			System.out.println("== END PUBLISHER ==");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void subscribeTopic(String Topic) throws MqttException {
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		client.connect();
		client.subscribe(Topic);

	}

}
