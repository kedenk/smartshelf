package com.smartshelf;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.smartshelf.config.MqttConfig;
import com.smartshelf.mqtt.MqttCallbackImpl;
import com.smartshelf.mqtt.MqttService;

@SpringBootApplication
public class Application {

	private final static Logger log = LoggerFactory.getLogger(Application.class);
	
	private static ApplicationContext context;
	private static final long CONNECT_TIMEOUT = 10000;
	
    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
        
        startMqttClient();
    }
    
    
    private static void startMqttClient() {
    
    	Thread mqttStarter = new Thread(new Runnable() {
    		
            @Override
            public void run() {
               
            	MqttService client = (MqttService) context.getBean(MqttService.class);
            	while( !client.isConnected() ) {
            		
	            	try {
                		client.connect();
	                	
	                	log.info("Mqtt client connected.");
	                	
	        			client.setMessageCallback(new MqttCallbackImpl());
	
	        			List<String> topics = MqttConfig.getStdTopics();
	        			
	        			for( String topic : topics ) {
	        				client.subscribe(topic);
	        			}
	        			
	        		} catch (MqttException e) {
	        			log.error(e.getMessage());
	        			log.error("Next try to connect in " + CONNECT_TIMEOUT + "ms.");
	        			
	        			try {
							Thread.sleep(CONNECT_TIMEOUT);
						} catch (InterruptedException e1) {
							log.error(e1.getMessage());
						}
	        		}
            	}
            }
        });

        mqttStarter.start();
    }

}