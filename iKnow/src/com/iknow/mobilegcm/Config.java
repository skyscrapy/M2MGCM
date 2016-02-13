package com.iknow.mobilegcm;

public interface Config {

	// When you are using two simulator for testing this application.
	// Make SECOND_SIMULATOR value true when opening/installing application in second simulator
	// Actually we are validating/saving device data on IMEI basis.
	// if it is true IMEI number change for second simulator
	
	static final boolean SECOND_SIMULATOR = false;
	
	// CONSTANTS
	
	// Server Url absolute url where php files are placed.
    static final String YOUR_SERVER_URL = "http://192.168.56.1/";
     
	// Google project id
    static final String GOOGLE_SENDER_ID = "639861649263"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    // Broadcast reciever name to show gcm registration messages on screen 
    static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_REGISTRATION_MESSAGE";
    
    // Broadcast reciever name to show user messages on screen
    static final String DISPLAY_MESSAGE_ACTION =
        "com.androidexample.gcm.DISPLAY_MESSAGE";

    // Parse server message with this name
    static final String EXTRA_MESSAGE = "message";
    
    
		
	
}
