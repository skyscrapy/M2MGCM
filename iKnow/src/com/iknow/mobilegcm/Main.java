package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexample.mobilegcm.R;
//import com.androidexample.mobilegcm.GridViewExample.LongOperation;
import com.google.android.gcm.GCMRegistrar;

public class Main extends Activity {
	
	// label to display gcm messages
	TextView lblMessage;
	Controller aController;
	TextView appTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        appTitle = (TextView)findViewById(R.id.title);
        TextPaint tp = appTitle.getPaint(); 
        tp.setFakeBoldText(true);
        appTitle.setText("iKnow");
		/******************* Intialize Database *************/
		DBAdapter.init(this);
		
		// Get Global Controller Class object 
		// (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(Main.this,
					"網路連接錯誤",
					"請檢查您的網路連接", false);
			// stop executing code by return
			return;
		}
	
		//Check device contains self information in sqlite database or not. 
		int vDevice = DBAdapter.validateDevice();	
		
		if(vDevice > 0)
		{	
			
			// Launch Main Activity
			Intent i = new Intent(getApplicationContext(), MainPage.class);
			startActivity(i);
			finish();
		}
		else
		{
			String deviceIMEI = "";
			if(Config.SECOND_SIMULATOR){
				
				//Make it true in CONFIG if you want to open second simutor
				// for testing actually we are using IMEI number to save a unique device
				
				deviceIMEI = "000000000000001";
			}	
			else
			{
			  // GET IMEI NUMBER      
			 TelephonyManager tManager = (TelephonyManager) getBaseContext()
			    .getSystemService(Context.TELEPHONY_SERVICE);
			  deviceIMEI = tManager.getDeviceId(); 
			}
			
			/******* Validate device from server ******/
			// WebServer Request URL
	        String serverURL = Config.YOUR_SERVER_URL+"validate_device.php";
	        
	        // Use AsyncTask execute Method To Prevent ANR Problem
	        LongOperation serverRequest = new LongOperation(); 
	        
	        serverRequest.execute(serverURL,deviceIMEI,"","");
			
		}	
		
	}		
	
	
	// Class with extends AsyncTask class
	public class LongOperation  extends AsyncTask<String, Void, String> {
	         
	        // Required initialization
	    	
	       //private final HttpClient Client = new DefaultHttpClient();
	       // private Controller aController = null;
	        private String Error = null;
	        private ProgressDialog Dialog = new ProgressDialog(Main.this); 
	        String data =""; 
	        int sizeData = 0;  
	        
	        
	        protected void onPreExecute() {
	            // NOTE: You can call UI Element here.
	             
	            //Start Progress Dialog (Message)
	           
	            Dialog.setMessage("驗證設備中..");
	            Dialog.show();
	            
	        }
	 
	        // Call after onPreExecute method
	        protected String doInBackground(String... params) {
	        	
	        	/************ Make Post Call To Web Server ***********/
	        	BufferedReader reader=null;
	        	String Content = "";
		             // Send data 
		            try{
		            	
		            	// Defined URL  where to send data
			               URL url = new URL(params[0]);
		            	
			            // Set Request parameter
			            if(!params[1].equals(""))
		               	   data +="&" + URLEncoder.encode("data1", "UTF-8") + "="+params[1].toString();
			            if(!params[2].equals(""))
			               	   data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();	
			            if(!params[3].equals(""))
			               	   data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();
		              Log.i("GCM",data);
			            
			          // Send POST data request
		   
		              URLConnection conn = url.openConnection(); 
		              conn.setDoOutput(true); 
		              OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
		              wr.write( data ); 
		              wr.flush(); 
		          
		              // Get the server response 
		               
		              reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		              StringBuilder sb = new StringBuilder();
		              String line = null;
		            
			            // Read Server Response
			            while((line = reader.readLine()) != null)
			                {
			                       // Append server response in string
			                       sb.append(line + "\n");
			                }
		                
		                // Append Server Response To Content String 
		               Content = sb.toString();
		            }
		            catch(Exception ex)
		            {
		            	Error = ex.getMessage();
		            }
		            finally
		            {
		                try
		                {
		     
		                    reader.close();
		                }
		   
		                catch(Exception ex) {}
		            }
	        	
	            /*****************************************************/
	            return Content;
	        }
	         
	        protected void onPostExecute(String Content) {
	            // NOTE: You can call UI Element here.
	             
	            // Close progress dialog
	            Dialog.dismiss();
	            
	            if (Error != null) {
	                 
	                 
	            } else {
	              
	            	// Show Response Json On Screen (activity)
	            	
	             /****************** Start Parse Response JSON Data *************/
	            	aController.clearUserData();
	            	
	            	JSONObject jsonResponse;
	                      
	                try {
	                      
	                     /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
	                     jsonResponse = new JSONObject(Content);
	                      
	                     /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
	                     /*******  Returns null otherwise.  *******/
	                     JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
	                      
	                     /*********** Process each JSON Node ************/
	  
	                     int lengthJsonArr = jsonMainNode.length();  
	  
	                     for(int i=0; i < lengthJsonArr; i++) 
	                     {
	                         /****** Get Object for each JSON node.***********/
	                         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	                          
	                         /******* Fetch node values **********/
	                         String Status       = jsonChildNode.optString("status").toString();
	                         
	                         Log.i("GCM","---"+Status);
	                         
	                         // IF server response status is update
	                         if(Status.equals("update")){
	                            
	                        	String RegID      = jsonChildNode.optString("regid").toString();
	                            String Name       = jsonChildNode.optString("name").toString();
	                            String Email      = jsonChildNode.optString("email").toString();
	                            String IMEI       = jsonChildNode.optString("imei").toString();
	                            
	                           // add device self data in sqlite database
	                            DBAdapter.addDeviceData(Name, Email,RegID, IMEI);
	                            
	                            // Launch GridViewExample Activity
	                			Intent i1 = new Intent(getApplicationContext(), MainPage.class);
	                			startActivity(i1);
	                			finish();
	                           
	                            Log.i("GCM","---"+Name);
	                         }
	                         else if(Status.equals("install")){  
	                        	
	                        	 // Launch RegisterActivity Activity
		                		Intent i1 = new Intent(getApplicationContext(), Registertest.class);
		                		startActivity(i1);
		                		finish();
	                        	 
	                         }
	                         
	                        
	                    }
	                     
	                 /****************** End Parse Response JSON Data *************/     
	                   
	                      
	                 } catch (JSONException e) {
	          
	                     e.printStackTrace();
	                 }
	  
	                 
	             }
	        }
	         
	    }

	
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

}
