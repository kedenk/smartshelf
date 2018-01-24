package com.smartshelf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smartshelf.dao.BoxDao;

public class ServiceModeService {

	private static Boolean inServiceMode = false; 
	
	@Autowired
	private ClientConnection clientConnection;
	
	@Autowired
	private BoxDao boxDao;
	
	@Value("${box.threshold.halffull}")
	private int halfFullThreshold; 
	@Value("${box.threshold.full}")
	private int fullThreshold; 
	
	public static Boolean isServiceMode() {
		return inServiceMode;
	}
	
	public void startServiceMode() throws Exception {
	
		if( inServiceMode ) {
			throw new Exception("Service mode already running.");
		}
		
		// shut off all blink signals
		this.clientConnection.stopAllBlinkCommands();
		
		// turn on LEDs for empty boxes
		this.clientConnection.startEmptyBoxSignals();
	}
	
	public void stopServiceMode() {
		
		if( !inServiceMode )
			return; 
		
		this.clientConnection.stopAllBlinkCommands();
		this.clientConnection.startEmptyBoxSignals();
	}
}
