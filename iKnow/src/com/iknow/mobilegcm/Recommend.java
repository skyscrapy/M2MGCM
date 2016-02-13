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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;  
import java.util.HashMap;
import java.util.LinkedList;  
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.iknow.mobilegcm.Topics1.GetNumInfo;
import com.iknow.mobilegcm.Topics1.GetTopicsData;
import com.iknow.view.PullToRefreshListView;
import com.iknow.view.PullToRefreshBase.OnRefreshListener;
import com.iknow.view.adapter.PullAdapter;
import com.iknow.view.task.PullTask;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import android.os.AsyncTask;  
import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;  
import android.app.AlertDialog.Builder;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;  
import android.widget.ArrayAdapter;  
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
public class Recommend extends Fragment {
	
	HttpClient client;
	int responseCode;
	public Handler myHandler;
	private Handler handler = new Handler();
	public String res;
	public String username;
	public String userPoint;
	
	private LinkedList<HashMap<String,Object>> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private PullAdapter pullAdapter;
	// private LinkedList<String>q_title;
	//   private LinkedList<String>q_question;
	//   private LinkedList<String>q_user;
	//   private LinkedList<String>q_id;
	//   private LinkedList<String>q_time;
	private String []q_title = new String[500];
	   private String []q_question = new String[500];
	   private String []q_user = new String[500];
	   private String []q_id = new String[500];
	   private String []q_time = new String[500];
	   private String[]q_tag = new String[500];
	   private String[]q_likenumber = new String[500];
	   private String[]q_point = new String[500];
	   private String[]q_userpoint =new String[500];
	   
	 private String []latestQtitle  = new String[500];
	 private String[]latestQuser = new String[500];
  	 private String[]latestQtime = new String[500];
  	 private String[]latestQid = new String[500];
  	private String[]latestQuestion = new String[500];
  	private String[]latestQtag = new String[500];
  	private String[]latestQlikenumber = new String[500];
  	private String[]latestQpoint = new String[500];
	//   private LinkedList<String> latestQtitle;
//		private LinkedList<String>latestQuser;
	//	private LinkedList<String>latestQtime;
  	 private int initializedLength=0;
  	 private int latestLength=0;
	 private int requestqid = 0;
	 private int latestId;
	
	 Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
		SharedPreferences settings;
		SharedPreferences.Editor edit;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.getLayoutInflater(savedInstanceState);
		return inflater.inflate(R.layout.fragment_recommend, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	//	startUrlCheck();
	//	final Controller aController = (Controller) getActivity().getApplicationContext();
		settings = this.getActivity().getSharedPreferences("qid",Context.MODE_PRIVATE);
		//this.isCheckMap = settings.getAll();
		edit = settings.edit();
		Map<String, ?> allContent = settings.getAll();
			for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
	            isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
	        } 
		
		mListItems = new LinkedList<HashMap<String,Object>>();
		new GetInitialData().execute();
	 mPullRefreshListView = (PullToRefreshListView) this.getView().findViewById(R.id.pullrefresh);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		
	 
		
		pullAdapter = new PullAdapter(mListItems,isCheckMap, getActivity());
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(pullAdapter);
		swingBottomInAnimationAdapter.setInitialDelayMillis(300);
		swingBottomInAnimationAdapter.setAbsListView(mListView);
		
		mListView.setAdapter(swingBottomInAnimationAdapter);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(new OnItemClickListener(){
			//here notice that we have initialized questions and latest questions, they are stored in different arrays.
			// So I may resolve this problem to avoid data inconsistency. 
			 @Override  
			   public void onItemClick(AdapterView<?> parent, View view,  
			     int position, long id) {
				 int qid = 10000;
		//		   if(q_id.get(position)!=null)
	//			   qid = Integer.parseInt(q_id.get(position));
				 if(q_id[initializedLength-position]!=null)
					   qid = Integer.parseInt(q_id[initializedLength-position]);
			//	    Toast.makeText(getActivity(), String.valueOf(initializedLength-position),Toast.LENGTH_LONG).show();
				  Intent i = new Intent(getActivity(),DisplayAnswer.class);
			/*	  if(q_question[position]!=null)
				  { 
					  i.putExtra("qid",qid);
					  i.putExtra("question", q_question[position]);
					  i.putExtra("quser", q_user[position]);
					  i.putExtra("qtitle", q_title[position]);
					  i.putExtra("qtime", q_time[position]);
					  startActivity(i);
				  }
				*/ 
			//	  if(q_question.get(position)!=null)
			//	  Toast.makeText(getActivity(), q_title[initializedLength-position-1]+" "+q_title[initializedLength-position],Toast.LENGTH_LONG).show();
				  if(q_title[initializedLength-position]!=null)
				  { 
					  i.putExtra("qid",q_id[initializedLength-position]);
				//	  i.putExtra("question", q_question.get(position));
				//	  i.putExtra("quser", q_user.get(position));
				//	  i.putExtra("qtitle", q_title.get(position));
				//	  i.putExtra("qtime", q_time.get(position));
					  i.putExtra("question", q_question[initializedLength-position]);
					  i.putExtra("quser", q_user[initializedLength-position]);
					  i.putExtra("qtitle", q_title[initializedLength-position]);
					  i.putExtra("qtime", q_time[initializedLength-position]);
					  i.putExtra("qtag", q_tag[initializedLength-position]);
					  i.putExtra("qpoint", q_point[initializedLength-position]);
					  startActivity(i);
				  }
				 
				 
				 
			 	}
			 }
				);
	
	
	}
	
	
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			isCheckMap.clear();
			Map<String, ?> allContent = settings.getAll();
			for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
		        isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
		    } 
			new PullTaskForRecommend().execute();
	}
	};
	
	
	public class GetInitialData extends AsyncTask<Void, Void, JSONArray>
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
		
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"Recommend.php?id="+requestqid); 
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
		
		
		 
		 
		} catch (Exception e) { 
			Log.v("status","failure");
			
		e.printStackTrace(); 
		} 
		return null;
		}
		
		
		 @Override  
	        protected void onPostExecute(JSONArray json) {
			 if ((Dialog != null) && Dialog.isShowing())
			 {
				 Dialog.dismiss();
			 }
			 if(json!=null)
	        {
			 initializedLength = json.length();
				for( int i=json.length()-1;i>=0;i-- )
				{
				//    q_title.add(json.getJSONObject(i).getString("title"));
				//	q_question.add(json.getJSONObject(i).getString("question"));
				//	q_user.add( json.getJSONObject(i).getString("user"));
				//	q_id.add( json.getJSONObject(i).getString("id"));
				//	q_time.add( json.getJSONObject(i).getString("created_time"));
					try
					{
						q_title[i]=json.getJSONObject(i).getString("title");
							q_question[i]=json.getJSONObject(i).getString("question");
							q_user[i]=json.getJSONObject(i).getString("user");
							q_id[i]=json.getJSONObject(i).getString("id");
							q_time[i]= json.getJSONObject(i).getString("created_time");
							q_tag[i]= json.getJSONObject(i).getString("tag");
							q_likenumber[i] = json.getJSONObject(i).getString("like_num");
							q_userpoint[i] = json.getJSONObject(i).getString("point");
					}
					catch(Exception e)
					{
						Log.v("status","failure");
						e.printStackTrace();
					}
			//		Message msg = new Message();
			//	    msg.what = 0x123;
			//	    msg.obj = res;
			//	    myHandler.sendMessage(msg);
				//	Log.v("info:",q_title.get(i));
				}
				
				
				
				initViews();
                
                pullAdapter.notifyDataSetChanged();
	        }
			 else
			 {
				 AlertDialog.Builder builder = new Builder(getActivity());
			   //   builder.setIcon(android.R.drawable.btn_star_big_on);
			      builder.setTitle("連接失敗");
			      builder.setMessage("是否重試？");
			      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			    
			    public void onClick(DialogInterface dialog, int id) {
			    	new GetInitialData().execute();


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
		
	
	  
	    private void initViews() {
			
			//mListItems.addAll(Arrays.asList(mStrings));
			for(int i=initializedLength-1; i>=initializedLength-10&&i>=0; i--){  
	           if(q_title[i]!=null)
	        	{
	            	HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	       //     	map.put("title", q_title.get(i));
	            	map.put("title", q_title[i]);
	            	map.put("question", q_question[i]);
	         //  	map.put("qinfo", q_user.get(i)+" "+q_time.get(i));
	            	map.put("qid",q_id[i]);
	            	map.put("qtime", q_time[i]);
	            	map.put("quser", q_user[i]);
	            	map.put("qtag", q_tag[i]);
	            	map.put("likenumber",q_likenumber[i]);
	            	map.put("radioId", q_id[i]);
	            	map.put("point", q_userpoint[i]);
	            	mListItems.add(map);  
	        	}
	        }  
			
		}
	    
	  
	 private class GetLatestTask extends AsyncTask<Void, Void, JSONArray> {  
		  
	        //后台处理部分  
	        @Override  
	        protected JSONArray doInBackground(Void... params) {  
	            // Simulates a background job.  
	        	client = new DefaultHttpClient(); 
				StringBuilder builder = new StringBuilder();  
			//	latestId = Integer.parseInt(q_id.getFirst());
				//latestId = 1;
				latestId = Integer.parseInt(q_id[initializedLength-1]);
				HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"RecommendLatest.php?id="+latestId); 
				try { 
				HttpResponse response = client.execute(myget);
				responseCode = response.getStatusLine().getStatusCode();
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
	  
	        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中  
	       
	        @Override  
	        protected void onPostExecute(JSONArray json) {  
	            //在头部增加新添内容  
        	if(json!=null)
        	{
	        	latestLength = json.length();
				for( int i=json.length()-1;i>=0;i-- )
				{
					try
					{
				    latestQtitle[i] = json.getJSONObject(i).getString("title");
				//	latestQtitle.add(json.getJSONObject(i).getString("title"));
					latestQuser[i] = json.getJSONObject(i).getString("user");
				//	latestQuser.add(json.getJSONObject(i).getString("user"));
					latestQtime[i]   = json.getJSONObject(i).getString("created_time");
					latestQid[i] = json.getJSONObject(i).getString("id");
					latestQuestion[i] = json.getJSONObject(i).getString("question");
					latestQtag[i] = json.getJSONObject(i).getString("tag");
					latestQlikenumber[i] = json.getJSONObject(i).getString("like_num");
					}
					catch(Exception e)
					{
						Log.v("status","failure");
						e.printStackTrace();
					}
				//	latestQtime.add(json.getJSONObject(i).getString("created_time"));
				//	Message msg = new Message();
				  //  msg.what = 0x123;
				   // msg.obj = res;
				   // myHandler.sendMessage(msg);
					//Log.v("info:",q_title[0]+"   "+q_question[0]+" "+q_user[0]+" "+q_time[0]+"  "+q_id[0]);
				}
				for(int i=0;i<latestLength;i++)
				{
					q_title[i+initializedLength] = latestQtitle[i];
					q_user[i+initializedLength] = latestQuser[i];
					q_time[i+initializedLength] = latestQtime[i];
					q_question[i+initializedLength] = latestQuestion[i];
					q_id[i+initializedLength] = latestQid[i];
					q_likenumber[i+initializedLength] = latestQlikenumber[i];
				}
		initializedLength = initializedLength+latestLength;
		//PullTask pullTask =	new PullTask(mPullRefreshListView, mPullRefreshListView.getRefreshType(), pullAdapter, mListItems, q_id, q_title, q_user,q_time,q_tag, q_likenumber, latestQid, latestQtitle,latestQuser,latestQtime,latestQtag,latestQlikenumber, initializedLength, latestLength );
	//	pullTask.execute();
        	}
	            super.onPostExecute(json);  
	        }  
	    }
	 
	 
	 public class PullTaskForRecommend extends AsyncTask<Void, Void, String>{

			@Override
			protected String doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return "StringTest";
			}

			@Override
			protected void onPostExecute(String result) {
				if(mPullRefreshListView.getRefreshType() == 1) {
					
					mListItems.clear();
					new GetInitialData().execute();
					
				}
				if(mPullRefreshListView.getRefreshType() == 2) {
					
					int count = mListItems.size();    
					for (int i = initializedLength-count-1; i >initializedLength-count-10&&i>=0; i--) {  
			        	if(q_title[i]!=null)
				//		if(qtitle.get(i)!=null)
			        	//	adapter.addItem(i+" "+q_title[i]);  
			        	{
			        		HashMap<String, Object> map = new HashMap<String, Object>(); 
			            	map = new HashMap<String, Object>();
			            	map.put("title", q_title[i]);
			            	map.put("quser", q_user[i]);
			            	map.put("qtime", q_time[i]);
			                map.put("qtag", q_tag[i]);
			                map.put("likenumber", q_likenumber[i]);
			                map.put("qid", q_id[i]);
			                map.put("radioId", (q_id[i]));
			                map.put("point", q_userpoint[i]);
				         //   map.put("qinfo", quser.get(i)+" "+qtime.get(i));
			                mListItems.addLast(map);
			        		
			        	}
			        }
				}
				pullAdapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		
			
		}


	
	    
	    
	    
	    
	
}

