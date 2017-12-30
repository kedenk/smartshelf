
package com.smartshelf.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService {


  public static void main(String[] args)  {
	  String topic = "Topic";
	  String msg = "hiii";
	  publishMqtt objPublished = new publishMqtt();
	  objPublished.publishTopic(topic, msg);
	  

    
  }
}

