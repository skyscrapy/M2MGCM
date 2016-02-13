package com.iknow.mobilegcm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;  
import java.util.HashMap;
import java.util.LinkedList;  

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.iknow.mobilegcm.Recommend.GetInitialData;
import com.iknow.mobilegcm.SendPushNotification.SendOperation;
import com.iknow.view.PullToRefreshListView;
import com.iknow.view.PullToRefreshBase.OnRefreshListener;
import com.iknow.view.adapter.PullAdapter;
import com.iknow.view.task.PullTask;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.androidexample.mobilegcm.R;

import android.os.AsyncTask;  
import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.app.Activity;  
import android.app.AlertDialog.Builder;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;  
import android.widget.ArrayAdapter;  
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
public class Topics1 extends Fragment{
	LinkedList<HashMap<String,Object>> mListItems;
	private Handler myhandler = new Handler();
	ListView listView;
	PullToRefreshListView mPullRefreshListView;
	private HttpClient client;
	private int responseCode;
	Topics1Adapter adapter;
	private String []q_title = new String[500];
	private String []q_id = new String[500];
	private String []typenum = new String[12];
    private int initializedLength=0;
    String[] strings = {"科技","生活","健康","運動","電玩","時事","學術","美食","娛樂",
            "心情", "定位","其他"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.getLayoutInflater(savedInstanceState);
		return inflater.inflate(R.layout.fragment_topics1, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	//	startUrlCheck();
		
		mPullRefreshListView = (PullToRefreshListView) this.getView().findViewById(R.id.listview_topics);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		
		listView = mPullRefreshListView.getRefreshableView();
		mListItems = new LinkedList<HashMap<String,Object>>();
		listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		new GetNumInfo().execute();
		new GetTopicsData().execute();
		adapter = new Topics1Adapter(mListItems,getActivity());
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
		swingBottomInAnimationAdapter.setInitialDelayMillis(300);
		swingBottomInAnimationAdapter.setAbsListView(listView);
		
		listView.setAdapter(swingBottomInAnimationAdapter);
	//	listView.setAdapter(swingBottomInAnimationAdapter);
		
		
		
	//	loadmore(null);
		
		
	}
	

	

	
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
		//	latestQtitle.clear();
		//	latestQuser.clear();
		//	latestQtime.clear();
			if(mPullRefreshListView.getRefreshType()==1)
			{
				mListItems.clear();
				new GetNumInfo().execute();
				new GetTopicsData().execute();
				
		//		Log.v("latest",latestQtitle[0]+" "+latestQuser[0]);
					
				//new GetLatestTask().execute();	
			}
			mPullRefreshListView.onRefreshComplete();
		
		}
	};
	
	private void initViews() {
		//notice:here should be 12 coz extra should also be added 
		for(int i=0; i<12; i++){  
	          //  if(mStrings[i]!=null)
	        	{
	            	HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	            	map.put("type", strings[i]);
	            	map.put("number", typenum[i]);
	            	map.put("title1", q_title[3*i]);
	            	map.put("title2", q_title[3*i+1]);
	            	map.put("title3", q_title[3*i+2]);
	            	map.put("qid1", q_id[3*i]);
	            	map.put("qid2", q_id[3*i+1]);
	            	map.put("qid3", q_id[3*i+2]);
	            	
	            	mListItems.add(map);  
	        	}
	        }  
		
		
		
	}
	
	public class GetNumInfo extends AsyncTask<Void,Void, String>
	{
		@Override
		public String doInBackground(Void...params)
		{
			HttpParams connParams = new BasicHttpParams();                 
			HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
			HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
		client = new DefaultHttpClient(); 
		StringBuilder builder = new StringBuilder();  

		//HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"TopicsQuestion.php"); 
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"Topics2.php?"); 
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
	
				String temp = (builder.toString());
				return temp;
				
			}
		}
		catch (Exception e) { 
			Log.v("status","failure");
			
		e.printStackTrace(); 
		} 
		
		return null;
		}
		@Override
		public void onPostExecute(String temp)
		{
			
			String[] strall;
			strall = temp.split("\\^");
			if(strall.length==12)
			{
			Log.v("temp1",strall[0]);
			for(int i=0;i<12;i++)
			{
				typenum[i] = strall[i];
			}
	//		initViews();
			
			}
			else
			{
				AlertDialog.Builder builder = new Builder(getActivity());
				//	      builder.setIcon(android.R.drawable.btn_star_big_on);
					      builder.setTitle("連接失敗");
					      builder.setMessage("是否重試？");
					      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					    
					    public void onClick(DialogInterface dialog, int id) {
					    	new GetNumInfo().execute();


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
			super.onPostExecute(temp);
		}
	}
	
	
	
	public class GetTopicsData extends AsyncTask<Void,Void,JSONArray>
	{

		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute()
		{
			Dialog.setMessage("Loading...");
			Dialog.show();
			
		}
		@Override
		protected JSONArray doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpParams connParams = new BasicHttpParams();                 
			HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
			HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
		client = new DefaultHttpClient(); 
		StringBuilder builder = new StringBuilder();  

		//HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"TopicsQuestion.php"); 
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"TopicsQuestion.php?id="+0); 
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
	
				JSONArray json = new JSONArray(builder.toString());
				return json;
				
			}
		}
		catch (Exception e) { 
			Log.v("status","failure");
			
		e.printStackTrace(); 
		} 
		
		return null;
		
		
	   } 
		@Override
		protected void onPostExecute(JSONArray json)
		{
			 if ((Dialog != null) && Dialog.isShowing())
			 {
				 Dialog.dismiss();
			 }
		//	initializedLength = json.length();
			if(json!=null)
			{
			for( int i=0;i<json.length();i++ )
			{
				try
				{
			
				q_title[i]=json.getJSONObject(i).getString("title");
				q_id[i] = 	json.getJSONObject(i).getString("id");
				Log.v("info:",q_title[i]);
			
				}
				catch(Exception e)
				{
					Log.v("status","failure");
					e.printStackTrace();
				}
			}
			initViews();
		       adapter.notifyDataSetChanged();
			}
			else
			{
				AlertDialog.Builder builder = new Builder(getActivity());
				   //   builder.setIcon(android.R.drawable.btn_star_big_on);
				      builder.setTitle("連接失敗");
				      builder.setMessage("是否重試？");
				      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				    
				    public void onClick(DialogInterface dialog, int id) {
				    	new GetTopicsData().execute();


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
		       super.onPostExecute(json);
			
		}
	
	}
}
