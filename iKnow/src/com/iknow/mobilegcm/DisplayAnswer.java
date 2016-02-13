package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import com.androidexample.mobilegcm.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class DisplayAnswer extends Activity {

	String qid;
	public String []a_answer = new String[500];
	public String []a_status = new String[500];
    public String []q_id = new String[500];
    public String []a_user = new String[500];
    public String []a_id = new String[500];
    public String []a_time = new String[500];
    public String []a_likenum = new String[500];
    public String []a_point = new String[500];
    public String quserPoint="";
    public final static String EXTRA_MESSAGE = "com.example.myapp.MESSAGE";
	HttpClient client;
    public Handler myHandler;
    public String res;
    public String question;
	public String qtime;
	public String quser;
	public String qtitle;
	TextView appTitle;
	int alength = 0;
	ListView listView;
	AnswerAdapter adapter;
	 Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
		SharedPreferences settings;
		SharedPreferences.Editor edit;
	
	  //  private List<String> mDataSourceList = new ArrayList<String>();  
	    private LinkedList<HashMap<String, Object>> mDataSourceList = new LinkedList<HashMap<String, Object>>();
	  //  private List<FragmentTransaction> mBackStackList = new ArrayList<FragmentTransaction>();  
	    private TextView texttitle;
	    private TextView textquestion;
	    private TextView textuser;
	    private TextView texttime;
	    private EditText editanswer;
	    private Button sendanswer;
	    private TextView nodata;
	    private ImageView userLevel;
	    Controller aController;
	    public int qqid;
	    String answertoquestion="";
	    public String imei;
	 //   int alength;
	    String alengthString;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		 
		 settings = this.getSharedPreferences("aid",Context.MODE_PRIVATE);
			//this.isCheckMap = settings.getAll();
			edit = settings.edit();
			Map<String, ?> allContent = settings.getAll();
				for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
		            isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
		        } 
				
		Intent intent = getIntent();
		qid = intent.getStringExtra("qid");   // if not obtaining the qid, then return -1 instead
		question = intent.getStringExtra("question");
		qtime = intent.getStringExtra("qtime");
		quser = intent.getStringExtra("quser");
		qtitle = intent.getStringExtra("qtitle");
		setContentView(R.layout.activity_display_answer);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		appTitle = (TextView)findViewById(R.id.title);
		TextPaint tp = appTitle.getPaint();
		tp.setFakeBoldText(true);
		
		appTitle.setText("問題查看");
		new QuserPoint().execute();
		new GetAnswer().execute();
		listView = (ListView) findViewById(R.id.fragment_list);  
		adapter = new AnswerAdapter(mDataSourceList,isCheckMap, this.getApplicationContext());
        listView.setAdapter(adapter);  
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        
        aController = (Controller)getApplicationContext();
     //   Toast.makeText(getActivity(), answer[0], Toast.LENGTH_SHORT).show();
       
        
        TelephonyManager tManager = (TelephonyManager)getBaseContext()
			    .getSystemService(Context.TELEPHONY_SERVICE);
		imei = tManager.getDeviceId();
        nodata = (TextView)findViewById(R.id.nodata);
        //添加数据到ListView  
        
        userLevel = (ImageView)findViewById(R.id.userlevel);
        texttitle= (TextView)findViewById(R.id.txt_title);
        textquestion = (TextView)findViewById(R.id.txt_question);
        textuser = (TextView)findViewById(R.id.txt_user);
        TextPaint tp2 = textuser.getPaint();
        tp2.setFakeBoldText(true);
        texttime = (TextView)findViewById(R.id.txt_time);
        texttitle.setText(qtitle);
        textquestion.setText(question);
        textuser.setText(quser);
        texttime.setText(qtime);
      //  textview.setText(qtitle+" \n"+question+" \n"+quser+"  "+qtime);
        //列表页面的ListView  
        
        listView.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
               
                  
                  
            }  
        });  
        
        sendanswer = (Button) findViewById(R.id.sendanswer);
        
        
        editanswer = (EditText) findViewById(R.id.editanswer);
        
        sendanswer.setOnClickListener(new View.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				answertoquestion = editanswer.getText().toString();
				if(answertoquestion.trim().length()>1)
		        {
			        // send question info. including destination, title, question, proposer's imei to server
			        new AnswerOperation().execute();
			        editanswer.setText("");  // clear so as to prevent repeated proposal
		        }
		        else
		        {
		        	aController.showAlertDialog(DisplayAnswer.this, 
							"您的回答不符要求!", 
							"請重新填寫，回答要大於1個字", 
							false);
		        }
				
				
				
			}
        
        	
        }
        		
        		
        		);
		
	}
    
     

	    public class GetAnswer extends AsyncTask<Void,Void,JSONArray> 
	       { 
	
	    	private ProgressDialog Dialog = new ProgressDialog(DisplayAnswer.this);
	       
	        
	        
	        protected void onPreExecute() {
	        
	            Dialog.setMessage("Loading...");
	            Dialog.show();
	            
	        }
	    	@Override
	    	public JSONArray doInBackground(Void... params)
	    	{
				client = new DefaultHttpClient(); 
				StringBuilder builder = new StringBuilder();  
				TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
				String deviceid = tm.getDeviceId();
				qqid = Integer.parseInt(qid);
				HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"UserAnswerToQuestion.php?qid="+qqid); 
				try { 
				HttpResponse response = client.execute(myget); 
				Log.v("status","success");
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
				for (String s = reader.readLine(); s != null; s = reader.readLine()) { 
				builder.append(s); 
				} 
			 
				JSONArray json = new JSONArray(builder.toString());
				return json;
				}
				catch (Exception e) { 
					Log.v("status","failure");
				e.printStackTrace(); 
				}
				return null;
	    	}
	    	@Override
	    	public void onPostExecute(JSONArray json)
	    	{
	    		if ((Dialog != null) && Dialog.isShowing())
				 {
					 Dialog.dismiss();
				 }
			    alength = json.length();
			    try
			    {
					for( int i=0;i<json.length();i++ )
					{
						a_id[i] = json.getJSONObject(i).getString("id");
						a_answer[i] = json.getJSONObject(i).getString("answer");
						a_user[i] = json.getJSONObject(i).getString("user");
						a_status[i]   = json.getJSONObject(i).getString("status");
						a_time[i] = json.getJSONObject(i).getString("created_time");
						a_likenum[i] = json.getJSONObject(i).getString("like_num");
						a_point[i] = json.getJSONObject(i).getString("point");
					}
					
					if(alength!=0)
			        {
			        	nodata.setVisibility(View.GONE);
			        	int likeMaxIndex = 0;
			        	int likeMax=0;
			        	for(int i=0, count=alength; i<count; i++){  
				            if(a_answer[i]!=null)
				        	{
				            	if(Integer.parseInt(a_likenum[i])>likeMax)
				            	{
				            		likeMax = Integer.parseInt(a_likenum[i]);
				            		likeMaxIndex = i;
				            	}
				        	}
				        }			        	
			        	HashMap<String, Object> map = new HashMap<String, Object>(); 
		            	map = new HashMap<String, Object>();
		            	map.put("answer", a_answer[likeMaxIndex]);
		            	map.put("auser", a_user[likeMaxIndex]);
		            	map.put("atime", a_time[likeMaxIndex]);
		            	map.put("likenumber",a_likenum[likeMaxIndex]);
		            	map.put("aid",a_id[likeMaxIndex]);
		            	map.put("radioId", a_id[likeMaxIndex]);
		            	map.put("point", a_point[likeMaxIndex]);
		            	map.put("type","first");
		            	mDataSourceList.add(map); 
				        for(int i=0, count=alength; i<count; i++){  
				            if(a_answer[i]!=null && i!=likeMaxIndex)
				        	{
				           // 	HashMap<String, Object> map = new HashMap<String, Object>(); 
				            	map = new HashMap<String, Object>();
				            	map.put("answer", a_answer[i]);
				            	map.put("auser", a_user[i]);
				            	map.put("atime", a_time[i]);
				            	map.put("likenumber",a_likenum[i]);
				            	map.put("aid",a_id[i]);
				            	map.put("radioId", a_id[i]);
				            	map.put("point", a_point[i]);
				            	map.put("type","common");
				            	mDataSourceList.add(map);  
				        	}
				        }
				        
			        }
			        else
			        {
			        	
			        	listView.setEmptyView(nodata);
			        }
					
			    }
			    catch(Exception e)
			    {
			    	Log.v("status","failure");
					e.printStackTrace(); 
			    }
			    adapter.notifyDataSetChanged();
				super.onPostExecute(json);
	    	}
			
		
		
	}
	
public class QuserPoint  extends AsyncTask<String, Void, String> {
	        
	        private String Error = null;
	        private ProgressDialog Dialog = new ProgressDialog(DisplayAnswer.this); 
	        
	        
	        protected void onPreExecute() {
	            // NOTE:  call UI Element here.
	             
	            //Start Progress Dialog (Message)
	           
	            Dialog.setMessage("Loading...");
	            Dialog.show();
	            
	        }
	 
	        // Call after onPreExecute method
	        protected String doInBackground(String... params) {
	        	
	        	HttpParams connParams = new BasicHttpParams();                 
	    		HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
	    		HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
	    	client = new DefaultHttpClient(); 
	    	StringBuilder builder = new StringBuilder();  
	    	String url = Config.YOUR_SERVER_URL+"UserPoint.php?user="+quser;
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
	         
	        protected void onPostExecute(String point) {
	            // NOTE: call UI Element here.
	             
	            // Close progress dialog
	        	if ((Dialog != null) && Dialog.isShowing())
	        	Dialog.dismiss();
	            
	        	quserPoint = point;
	        	
	        	int p = Integer.parseInt(quserPoint);
				if(p<=10)
				{
					userLevel.setImageResource(R.drawable.lv1);
				}
				else if(p<=30)
				{
					userLevel.setImageResource(R.drawable.lv2);
				}
				else if(p<=60)
		       	  {
					userLevel.setImageResource(R.drawable.lv3);
		       	  }
		       	  else if(p<=100)
		       	  {
		       		userLevel.setImageResource(R.drawable.lv4);
		       	  }
		       	  else if(p<=200)
		       	  {
		       		userLevel.setImageResource(R.drawable.lv5);
		       	  }
		       	  else
		       	  {
		       		userLevel.setImageResource(R.drawable.lv6);
		       	  }
	        	
	        	
	        	
	        	
	        	super.onPostExecute(point);
	        }
	        
	         
	    }
	
	
	
	
	
	
	 
	 
	    public class AnswerOperation  extends AsyncTask<String, Void, String> {
	        
	        private String Error = null;
	        private ProgressDialog Dialog = new ProgressDialog(DisplayAnswer.this); 
	        
	        
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
	    	String url = Config.YOUR_SERVER_URL+"answerpushformain.php?qid="+qid+"&answer="+answertoquestion+"&imei="+imei;
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
	         
	        protected void onPostExecute(String message) {
	            // NOTE: call UI Element here.
	             
	            // Close progress dialog
	        	if ((Dialog != null) && Dialog.isShowing())
	        	Dialog.dismiss();
	            
	    //        Toast.makeText(DisplayAnswer.this.getBaseContext(), message, Toast.LENGTH_LONG).show();
	            String[] StringAll;
	    		StringAll = message.split("\\^");
	            
	            
	            if (Error != null) {
	            	Toast.makeText(DisplayAnswer.this.getBaseContext(), "發送錯誤!請重試", Toast.LENGTH_LONG).show();  
	                 
	            } else {
	              
	            	// Show Response Json On Screen (activity)
	            	 Toast.makeText(DisplayAnswer.this.getBaseContext(), "成功發送！", Toast.LENGTH_LONG).show();  
	                 
	             }
	            
	            HashMap<String, Object> map = new HashMap<String, Object>(); 
            	map = new HashMap<String, Object>();
            	map.put("answer", answertoquestion);
            	map.put("auser", StringAll[0]);
            	map.put("atime", StringAll[1]);
            	map.put("aid",StringAll[2]);
            	map.put("radioId", StringAll[2]);
            	map.put("likenumber","0");
            	map.put("point", StringAll[3]);
            	map.put("type", "common");
            	mDataSourceList.add(map); 
	            adapter.notifyDataSetChanged();
        //    	listView.notifyDataSetChanged();
	            Log.v("status","finished");
	            
	          
	        }
	        
	        protected void onDestroy()
	        {
	        	
	        }
	         
	    }
	
	

}
