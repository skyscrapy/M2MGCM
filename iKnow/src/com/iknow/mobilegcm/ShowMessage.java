package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexample.mobilegcm.R;


public class ShowMessage extends Activity {
	
	// UI elements
	TextView question;
	
	EditText txtMessage; 
	TextView aCount;
	// Send Message button
	Button btnSend;
	final static int max_anum = 1000;
	// label to display gcm messages
	//TextView lblMessage;
	Controller aController;
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	String name;
	String message;
    String UserDeviceIMEI;
    String qid = "";
	/**************  Intialize Variables *************/
    public  ArrayList<UserData> CustomListViewValuesArr = new ArrayList<UserData>();
    TextView output = null;
    ShowMessage activity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_message);
		question = (TextView)findViewById(R.id.Question);
		aCount = (TextView)findViewById(R.id.Acount);
        txtMessage = (EditText) findViewById(R.id.editAnswer);
		btnSend    = (Button) findViewById(R.id.send);
		/******************* Intialize Database *************/
	//	DBAdapter.init(this);
		
		// Get Global Controller Class object 
		// (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(ShowMessage.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.DISPLAY_MESSAGE_ACTION));

		 activity  = this;

		 
		 
		 Bundle extras = getIntent().getExtras();
        String questionTitle="";
        String questionContent="";
        qid = "";
		String user = "";
		String time = "";
		qid = extras.getString("qid");
		questionTitle = extras.getString("title");
		questionContent = extras.getString("question");
		user = extras.getString("user");
		time = extras.getString("qtime");
		question.setText("  "+questionTitle+" \n"+questionContent+" \n"+"              "+user+"  "+time);   
		// the codes above is to obtain questions info. from pendingintent.
		// the UI part is left to you.
		// We should display the title, content, user, and time of the question received.
		
		// seems useless with broadcast receiver
		
        
		txtMessage.addTextChangedListener(new TextWatcher() {
			  private CharSequence temp;
			  private int selectionStart;
			  private int selectionEnd;
			  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			  }

			  public void onTextChanged(CharSequence s, int start, int before, int count) {
			   temp = s;
			  }

			  public void afterTextChanged(Editable s) {
			   int number = max_anum - s.length();
			   aCount.setText("" + number);
			   selectionStart = txtMessage.getSelectionStart();
			   selectionEnd = txtMessage.getSelectionEnd();
			   if (temp.length() > max_anum) {
			    s.delete(selectionStart - 1, selectionEnd);
			    int tempSelection = selectionEnd;
			    txtMessage.setText(s);
			    txtMessage.setSelection(tempSelection);// set the cursor to the last position of the edittext
			   }
			  }
			 });
		
     // Click event on Register button
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {  
				// Get data from EditText 
				String message = txtMessage.getText().toString(); 
				 
				// WebServer Request URL
		        String serverURL = Config.YOUR_SERVER_URL+"answerpush.php";
		       
		        
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
		        
		        // Use AsyncTask execute Method To Prevent ANR Problem
		        new LongOperation().execute(serverURL,qid,message,deviceIMEI); 
                     		        
		        txtMessage.setText("");
		    Toast.makeText(getApplicationContext(),"Your answer is submitted.", Toast.LENGTH_LONG).show();
		    	  
		 
			}
		});
		
	}		
		
	
	// Create a broadcast receiver to get message and show on screen 
 private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String newQuestion = intent.getExtras().getString("question");
			String newName = intent.getExtras().getString("user");
			String newTitle = intent.getExtras().getString("title");
			String newIMEI = intent.getExtras().getString("imei");
			String qid     = intent.getExtras().getString("qid");
			Log.i("GCMBroadcast","Broadcast called."+newIMEI);
			
			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());
			
			String msg = question.getText().toString();
			msg = newName+" : "+newTitle+""+newQuestion+" "+"\n"+msg;
			// Display message on the screen
			question.setText(msg);
			//lblMessage.append("\n"+newName+" : "+newMessage);			
			
			
			Toast.makeText(getApplicationContext(), 
					"Got Message: " + newTitle+newQuestion, 
					Toast.LENGTH_LONG).show();
			
			
			 //CustomListViewValuesArr.clear();
			 int rowCount = DBAdapter.validateNewMessageUserData(newIMEI);
			 Log.i("GCMBroadcast", "rowCount:"+rowCount);
             if(rowCount <= 1 ){
		        	final UserData schedSpinner = new UserData();
		            
		            /******* Firstly take data in model object ********/
		        	schedSpinner.setName(newName);
		        	schedSpinner.setIMEI(newIMEI);
		             
		              
//		          /******** Take Model Object in ArrayList **********/
		          CustomListViewValuesArr.add(schedSpinner);
		       
		          
		        }
		        
		        //CustomListViewValuesArr.addAll(SpinnerUserData);
		        
		        
			
			/************************************/
			
			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};
	
	
	/*********** Send message *****************/
	
	public class LongOperation  extends AsyncTask<String, Void, String> {
        
    	// Required initialization
    	
        //private final HttpClient Client = new DefaultHttpClient();
       // private Controller aController = null;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ShowMessage.this); 
        String data  = ""; 
        int sizeData = 0;  
        
        
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //Start Progress Dialog (Message)
           
            Dialog.setMessage("Please wait..");
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
	               	   data +="&" + URLEncoder.encode("data1", "UTF-8") + "="+params[1].toString();  // qid
		            if(!params[2].equals(""))
		               data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();	 // content
		            if(!params[3].equals(""))
			           data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();	 //  user imei
		        //    if(!params[4].equals(""))
				 //          data +="&" + URLEncoder.encode("data4", "UTF-8") + "="+params[4].toString();  // user
		            
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
         
        protected void onPostExecute(String Result) {
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
            Dialog.dismiss();
            
            if (Error != null) {
            	Toast.makeText(getBaseContext(), "Error: "+Error, Toast.LENGTH_LONG).show();  
                 
            } else {
              
            	// Show Response Json On Screen (activity)
            	 Toast.makeText(getBaseContext(), "Message sent."+Result, Toast.LENGTH_LONG).show();  
                 
             }
        }
         
    }
	
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	@Override
	protected void onDestroy() {
		try {
			// Unregister Broadcast Receiver
//			unregisterReceiver(mHandleMessageReceiver);
			
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
