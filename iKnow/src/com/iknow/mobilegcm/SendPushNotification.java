package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import com.androidexample.mobilegcm.R;
import com.iknow.mobilegcm.DBAdapter;
import com.iknow.mobilegcm.Recommend.GetInitialData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
public class SendPushNotification extends Fragment {
	int[] arr_images = {R.drawable.nulll,R.drawable.computer,R.drawable.life,R.drawable.health,R.drawable.sports,R.drawable.digital
			,R.drawable.business,R.drawable.education,R.drawable.literature,R.drawable.entertainment,R.drawable.emotion,R.drawable.location};
   int []subs = {R.string.nulll,R.string.computer,R.string.life,R.string.health,R.string.sports,R.string.digital
			,R.string.business,R.string.education,R.string.literature,R.string.entertainment,R.string.emotion,R.string.location};
   String[] strings = {"其他","科技","生活","健康","運動","電玩","時事","學術","美食","娛樂",
            "心情", "定位"};
   HttpClient client;
	// UI elements
	EditText txtMessage; 
	EditText txtTitle;
	TextView txtCount;
	TextView txtNumber;
	final static int max_tnum = 15;
	final static int max_qnum = 1000;
	private String tag;
	private String longitude="";
	private String latitude="";
	// Register button
	Button btnSend;
	//private ArrayAdapter<CharSequence> adapter;
	String sendfrom;   // to get imei
	String imei = "All";   // send to ALL users registered
   
	String question; 
	String title;
     //int[] phones = {   R.string.iphone, R.string.android,  R.string.blackberry};
 
	private SpinnerAdapter adapter;
	Controller aController = null;
	private Spinner spinner ;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		return inflater.inflate(R.layout.send_push_notification, container, false);

	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
	        DBAdapter.init(getActivity());
			
			//setContentView(R.layout.send_push_notification);
			
		//	location = "";
			
			//Get Global Controller Class object (see application tag in AndroidManifest.xml)
			aController = (Controller) getActivity().getApplicationContext();
			
			// Check if Internet Connection present
			if (!aController.isConnectingToInternet()) {
				
				// Internet Connection is not present
				aController.showAlertDialog(getActivity(),
						"網路連接錯誤",
						"請檢查您的網路連接", false);
				
				// stop executing code by return
				return;
			}
			
			TelephonyManager tManager = (TelephonyManager) getActivity().getBaseContext()
				    .getSystemService(Context.TELEPHONY_SERVICE);
			sendfrom = tManager.getDeviceId();  
			
		/*	adapter = ArrayAdapter.createFromResource ( this ,
	                R.array.types ,
	                android.R.layout. simple_spinner_item );
	      */
			
			
			adapter =  new SpinnerAdapter(getActivity(),R.layout.row,strings);
	   //    adapter.setDropDownViewResource(android.R.layout. simple_spinner_dropdown_item );

	      
		//	View rootView = inflater.inflate(R.layout.send_push_notification, container,false); 
	       spinner = (Spinner)this.getView().findViewById(R.id.Tag );
	       spinner.setAdapter(adapter);
	       spinner.setPrompt( "請選擇問題:" );
	      // spinner.setBackgroundColor(getResources().getColor(R.color.red));
	       
	       spinner.setSelection(0, true);
	      tag="無";
	     //  spinner.setCanceledOnTouchOutside(true);
	       spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	           public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {    
	        	   tag = ((TextView)view.findViewById(R.id.company)).getText().toString();
	        	   Toast.makeText(getActivity().getApplicationContext(),tag , Toast.LENGTH_LONG).show();
	        	   if( tag.equalsIgnoreCase("定位"))
	        	   {
	        		   GPSTracker gps = new GPSTracker(getActivity().getApplicationContext());
	        		   
	        		  // if(gps.canGetLocation()) a="1";
	        		  // else a="2";
	        		//   Toast.makeText(getApplicationContext(),a , Toast.LENGTH_LONG).show();
	        		   longitude = String.valueOf(gps.getLongitude());
	        		   latitude = String.valueOf(gps.getLatitude());
	        		   Toast.makeText(getActivity().getApplicationContext(), gps.getLongitude()+" "+gps.getLatitude(), Toast.LENGTH_LONG).show();
	        		   if(!gps.canGetLocation())
	        		   {
	        			   AlertDialog.Builder builder = new Builder(getActivity());
	        			      builder.setIcon(android.R.drawable.btn_star_big_on);
	        			      builder.setTitle("提示");
	        			      builder.setMessage("是否開啟GPS？");
	        			      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
	        			    
	        			    public void onClick(DialogInterface dialog, int id) {
	                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                            startActivity(intent); 


	        			      }
	        			    
	        			   });
	        			      builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        		            public void onClick(DialogInterface dialog, int id) {
	        		                dialog.cancel();
	        		            }}
	        			      );
	        			      AlertDialog alert = builder.create();
	        			        alert.show();
	        			 
	        		   }
	        	   }
	        	  
	        	   
	        	   
	           }    

	           public void onNothingSelected(AdapterView<?> arg0) {      
	           } 
	           
	       });  
			
			// Getting name, email from intent
	//		Intent i = getActivity().getIntent();
			
			//final String name = i.getStringExtra("name");
	//		final String imei = i.getStringExtra("imei");
	//		final String sendfrom = i.getStringExtra("sendfrom");
			
			txtMessage = (EditText) this.getView().findViewById(R.id.txtMessage);
			txtCount = (TextView) this.getView().findViewById(R.id.txtCount);
			btnSend    = (Button) this.getView().findViewById(R.id.btnSend);
			txtTitle = (EditText) this.getView().findViewById(R.id.txtTitle);
			txtNumber = (TextView)this.getView().findViewById(R.id.txtNumber);
			
			txtMessage.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE);
			txtMessage.setSingleLine(false);
			txtMessage.setHorizontalScrollBarEnabled(false);
			
			txtTitle.addTextChangedListener(new TextWatcher() {
				  private CharSequence temp;
				  private int selectionStart;
				  private int selectionEnd;
				  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				  }

				  public void onTextChanged(CharSequence s, int start, int before, int count) {
				   temp = s;
				  }

				  public void afterTextChanged(Editable s) {
				   int number = max_tnum - s.length();
				   txtCount.setText("" + number);
				   selectionStart = txtTitle.getSelectionStart();
				   selectionEnd = txtTitle.getSelectionEnd();
				   if (temp.length() > max_tnum) {
				    s.delete(selectionStart - 1, selectionEnd);
				    int tempSelection = selectionEnd;
				    txtTitle.setText(s);
				    txtTitle.setSelection(tempSelection);// set the cursor to the last position of the edittext
				   }
				  }
				 });
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
				   int number = max_qnum - s.length();
				   txtNumber.setText("" + number);
				   selectionStart = txtTitle.getSelectionStart();
				   selectionEnd = txtTitle.getSelectionEnd();
				   if (temp.length() > max_qnum) {
				    s.delete(selectionStart - 1, selectionEnd);
				    int tempSelection = selectionEnd;
				    txtMessage.setText(s);
				    txtMessage.setSelection(tempSelection);//设置光标在最后
				   }
				  }
				 });
			
			
			//sendTo.setText("Send To : "+name);
			
			
			
			btnSend.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					if(event.getAction() == MotionEvent.ACTION_UP){ 
						btnSend.setScaleX(1.0f);
						btnSend.setScaleY(1.0f);
						
	                }   
	                if(event.getAction() == MotionEvent.ACTION_DOWN){  	  
	                	btnSend.setScaleX(0.9f);
						btnSend.setScaleY(0.9f);
	                }  
					
	                return false;
					
				}
			});
			// Click event on send button
			btnSend.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {  
					// Get data from EditText 
					question = txtMessage.getText().toString(); 
					title = txtTitle.getText().toString();
			Log.v("IMEI",imei);
					// WebServer Request URL
			        String serverURL = Config.YOUR_SERVER_URL+"sendpush.php"; 
			        if(title.trim().length()>=3 && question.trim().length()>=3)
			        {
				        // send question info. including destination, title, question, proposer's imei to server
				        new SendOperation().execute();
				       
			        }
			        else
			        {
			        	aController.showAlertDialog(getActivity(), 
								"您的問題不符要求!", 
								"請重新填寫，標題大於2個字，內容大於2個字", 
								false);
			        }
				}
			});
	        
	        
	        
	 }
	 
	 
	 
	 
	 
	 
	 /*
	 
	public class SendOperation  extends AsyncTask<String, Void, String> {
        
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(getActivity()); 
        String data  = ""; 
        int sizeData = 0;  
        
        
        protected void onPreExecute() {
            // NOTE:  call UI Element here.
             
            //Start Progress Dialog (Message)
           
            Dialog.setMessage("請稍後...");
            Dialog.show();
            
        }
 
        // Call after onPreExecute method
        protected String doInBackground(String... params) {
        	
        	
        	BufferedReader reader=null;
        	String Content = "";
	             // Send data 
	            try{
	            	
	            	// Defined URL  where to send data
		            URL url = new URL(params[0]);  // server URL
	            	
		            // Set Request parameter
		            if(!params[1].equals(""))
	               	   data +="&" + URLEncoder.encode("data1", "UTF-8") + "="+params[1].toString();   // imei
		            if(!params[2].equals(""))
		               data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();	  // title
		            if(!params[3].equals(""))
			           data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();	  // question
		            if(!params[4].equals(""))
				           data +="&" + URLEncoder.encode("data4", "UTF-8") + "="+params[4].toString();	// sendfrom imei
		            if(!params[5].equals(""))
				           data +="&" + URLEncoder.encode("data5", "UTF-8") + "="+params[5].toString();	// tag of question
		            if(!params[6].equals(""))
		            	   data +="&" + URLEncoder.encode("data6", "UTF-8") + "="+params[6].toString();
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
        	
            
	            Log.v("status","doinback");
            return Content;
        }
         
        protected void onPostExecute(String Result) {
            // NOTE: call UI Element here.
             
            // Close progress dialog
            Dialog.dismiss();
            
            if (Error != null) {
            	Toast.makeText(getActivity().getBaseContext(), "發送錯誤!請重試", Toast.LENGTH_LONG).show();  
                 
            } else {
              
            	// Show Response Json On Screen (activity)
            	 Toast.makeText(getActivity().getBaseContext(), "成功發送！", Toast.LENGTH_LONG).show();  
                 
             }
            
            Log.v("status","finished");
            
        }
        
        protected void onDestroy()
        {
        	
        }
         
    }
    */
	 
	 public class SendOperation  extends AsyncTask<String, Void, String> {
	        
	        private String Error = null;
	        private ProgressDialog Dialog = new ProgressDialog(getActivity());
	        String data  = ""; 
	        int sizeData = 0;  
	        
	        
	        protected void onPreExecute() {
	            // NOTE:  call UI Element here.
	             
	            //Start Progress Dialog (Message)
	           
	            Dialog.setMessage("發送中請稍後...");
	            Dialog.show();
	            
	        }
	 
	        // Call after onPreExecute method
	        protected String doInBackground(String... params) {
	        	
	        	HttpParams connParams = new BasicHttpParams();                 
	    		HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
	    		HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
	    	client = new DefaultHttpClient(); 
	    	StringBuilder builder = new StringBuilder();  
	    String url = Config.YOUR_SERVER_URL+"sendpush.php?data1="+imei+"&data2="+title+"&data3="+question+"&data4="+sendfrom+"&data5="+tag+"&data6="+longitude+"&data7="+latitude;
	    url = url.replaceAll(" ", "%20");
	    	HttpGet myget = new HttpGet(url); 
	    
	    	try { 
	    	HttpResponse response = client.execute(myget); 
	    	Log.v("status","success");
	    	if( response.getStatusLine().getStatusCode()==200 )
	    	{
	    		System.out.println(response.getStatusLine().getStatusCode());
	    		
	    		
	    		HttpEntity entity = response.getEntity();
	    		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
	    		for (String s = reader.readLine(); s != null; s = reader.readLine()) { 
	    		builder.append(s); 
	    		} 
	    		}
	    	
	    	
	    	
	    	 
	    	 
	    	} catch (Exception e) { 
	    		Log.v("status","failure");
	    		
	    	e.printStackTrace(); 
	    	} 
	            
		            Log.v("status","doinback");
	            return builder.toString();
	        }
	         
	        protected void onPostExecute(String Result) {
	            // NOTE: call UI Element here.
	             
	            // Close progress dialog
	        	if(Dialog.isShowing()&&Dialog!=null)
	            Dialog.dismiss();
	        	Log.v("string",Result+"1");
	            if(Result.equals("success"))
	            {
		            //if (Error != null) {
		            	 
		                 
		         //   } else {
		              
		            	// Show Response Json On Screen (activity)
		            	 Toast.makeText(getActivity().getBaseContext(), "成功發送！", Toast.LENGTH_LONG).show();  
		            	 txtTitle.setText("");  // clear so as to prevent repeated proposal
					        txtMessage.setText(""); // clear
		           //  }
		            
		            Log.v("status","finished");
	            }
	            else
	            {
	            	//Toast.makeText(getActivity().getBaseContext(), "發送錯誤!請重試", Toast.LENGTH_LONG).show(); 
	            	AlertDialog.Builder builder = new Builder(getActivity());
			//	      builder.setIcon(android.R.drawable.btn_star_big_on);
				      builder.setTitle("連接失敗");
				      builder.setMessage("是否重試？");
				      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				    
				    public void onClick(DialogInterface dialog, int id) {
				    	new SendOperation().execute();


				      }
				    
				   });
				      builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }}
				      );
				      AlertDialog alert = builder.create();
				        alert.show();
	            }
	            
	        }
	        
	        protected void onDestroy()
	        {
	        	
	        }
	         
	    }
	 
	 
	public void turnGPSOn()
	{
	     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	     intent.putExtra("enabled", true);
	     getActivity().sendBroadcast(intent);

	    String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        getActivity().sendBroadcast(poke);


	    }
	}
	public class SpinnerAdapter extends ArrayAdapter<String>{
		 
	    public SpinnerAdapter(Context context, int textViewResourceId,   String[] objects) {
	        super(context, textViewResourceId, objects);
	    }

	    @Override
	    public View getDropDownView(int position, View convertView,ViewGroup parent) {
	        return getCustomView(position, convertView, parent);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        return getCustomView(position, convertView, parent);
	    }

	    public View getCustomView(int position, View convertView, ViewGroup parent) {

	    	
	        LayoutInflater inflater= getActivity().getLayoutInflater();
	        View row=inflater.inflate(R.layout.row, parent, false);
	        TextView label=(TextView)row.findViewById(R.id.company);
	        
	        label.setText(strings[position]);
	        label.setTextSize(20);

	        ImageView icon=(ImageView)row.findViewById(R.id.image);
	        icon.setImageResource(arr_images[position]);

	        return row;
	        }
	    }
	
}


