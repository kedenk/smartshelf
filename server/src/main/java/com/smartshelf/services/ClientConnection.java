package com.smartshelf.services;

import static org.mockito.Matchers.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshelf.model.LEDColor;
import com.smartshelf.mqtt.MqttService;

public class ClientConnection {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MqttService mqttService; 
	
	private final String BLINK_COMMAND_START_TOPIC = "devices/blink/start";
	private final String BLINK_COMMAND_STOP_TOPIC = "devices/blink/stop";
	
	@Value("${client.blink.time}")
	private long blinkTime; 
	
	private final int corePoolSize = 2;
	
	private static List<BlinkCommandMsg> currentSignals = new ArrayList<BlinkCommandMsg>();
	private static List<BlinkCommandMsg> selectedSignals = new ArrayList<BlinkCommandMsg>();
	
	private static List<LEDColor> availableColors = Arrays.asList(LEDColor.getLEDColors());

	/***
	 * Send a LED blink command to the specified topic
	 * @param boxid
	 * @param color
	 */
	public Boolean startBlinkCommand(long boxid, String color) {

		BlinkCommandMsg msg = createBlinkCommandMsg(boxid, color); 
		
		String jsonMsg = this.buildBlinkCommandMsg(msg);
		if( jsonMsg != null ) {
			log.info(String.format("Send blink start command for box %s. Message: %s", boxid, jsonMsg));
			this.mqttService.publish(BLINK_COMMAND_START_TOPIC, jsonMsg.getBytes());
			
			try {
				addBlinkSignal(msg);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
			// setup blink stop timer
			this.setBlinkStopTimer(msg);
			return true;
		}
		
		return false; 
	}
	
	/***
	 * Turns of all LEDs except of the selected ones.
	 * @param boxid
	 * @return
	 */
	public Boolean selectOneSignal(long boxid) {
		
		for(BlinkCommandMsg entry : currentSignals) {
			
			if( entry.boxid == boxid ) {
				selectedSignals.add(entry);
				continue;
			}
			this.stopBlinkCommand(Long.valueOf(entry.boxid), LEDColor.getLEDColor(entry.color).toString());
		}
		
		return true; 
	}
	
	public Boolean stopBlinkCommand(long boxid, String color) {
		
		BlinkCommandMsg msg = createBlinkCommandMsg(boxid, color); 
		
		this.stopBlinkCommand(msg);
		return true;
	}
	
	public void stopBlinkCommand(BlinkCommandMsg msg) {
		
		String jsonMsg = this.buildBlinkCommandMsg(msg);
		if( jsonMsg != null ) {
			log.info(String.format("Send blink stop command for box %s. Message: %s", msg.boxid, jsonMsg));
			this.mqttService.publish(BLINK_COMMAND_STOP_TOPIC, jsonMsg.getBytes());
			
			removeBlinkSignal(msg);
		}
	}
	
	/***
	 * Build a BlinkCommandMsg to send to Arduino client
	 * @param boxid
	 * @param color
	 * @return
	 */
	private String buildBlinkCommandMsg(BlinkCommandMsg msg) {
	
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonMsg = null;
		try {
			jsonMsg = mapper.writeValueAsString(msg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		
		return jsonMsg; 
	}
	
	private static BlinkCommandMsg createBlinkCommandMsg(long boxid, String color) {
		
		LEDColor c = LEDColor.getLEDColor(color); 
		return new BlinkCommandMsg(boxid, c.getValue());
	}
	
	private void setBlinkStopTimer(BlinkCommandMsg msg) {
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(corePoolSize);
		
		Runnable task = new Runnable() {

			@Override
			public void run() {
				
				stopBlinkCommand(msg);
			}
		};
		
		executor.schedule(task, this.blinkTime, TimeUnit.SECONDS);
		executor.shutdown();
	}
	
	private static void addBlinkSignal(BlinkCommandMsg msg) throws Exception {
		
		if( !isBlinkSignal(msg) ) {
			
			currentSignals.add(msg);
			
		} else {
			throw new Exception("Blink signal already sent.");
		}
	}
	
	private static void removeBlinkSignal(BlinkCommandMsg msg) {
		
		if( isBlinkSignal(msg) ) {
			
			currentSignals.remove(msg);
			selectedSignals.remove(msg);
		}
	}
	
	public static Boolean isBlinkSignal(BlinkCommandMsg msg) {
	
		return currentSignals.stream().filter(x -> x.boxid == msg.boxid && x.color == msg.color).findFirst().isPresent(); 
	}
	
	public static Boolean isBlinkSignal(long boxid, String color) {
		
		BlinkCommandMsg msg = createBlinkCommandMsg(boxid, color); 
		return isBlinkSignal(msg);
	}
	
	public static List<BlinkCommandMsg> getCurrentSignals() {
		return currentSignals;
	}
	
	public static List<BlinkCommandMsg> getSelectedSignals() {
		return selectedSignals;
	}
	
	public static Map<Long, Integer> getSelectedSignalsAsMap() {
		Map<Long, Integer> result = new HashMap<Long, Integer>();
		for( BlinkCommandMsg msg : selectedSignals ) {
			result.put(msg.boxid, msg.color);
		}
		
		return result;
	}
	
	/***
	 * Returns a available color to select. If no free colors are available a Exception is thrown
	 * @return
	 * @throws Exception
	 */
	public static LEDColor getFreeColor() throws Exception {
		
		for( LEDColor color : availableColors ) {
			if( currentSignals.stream().anyMatch(m -> m.color == color.getValue()) ) {
				continue; 
			}
			return color; 
		}
		
		throw new Exception("No available colors. Please wait until a color is free");
	}
}