package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.androidexample.mobilegcm.R;
import com.iknow.view.PullToRefreshListView;
import com.iknow.view.PullToRefreshBase.OnRefreshListener;
import com.iknow.view.adapter.PullAdapter;
import com.iknow.view.task.PullTask;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import java.util.ArrayList;  

import android.app.ListActivity;  
import android.os.Bundle;  
import android.os.Handler;  
import android.util.Log;  
import android.view.View;  
import android.widget.AbsListView;  
import android.widget.AbsListView.OnScrollListener;  
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;  
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
public class DisplayUserQuestion extends Activity{

	public final static String EXTRA_MESSAGE = "com.example.myapp.MESSAGE";
	HttpClient client;
	public String res;
	public Handler myHandler;
	TextView appTitle;
  
    private QuestionAdapter adapter;    
    private Handler handler = new Handler();
    
    private LinkedList<HashMap<String,Object>> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	
	private int qlength;
   private String []q_title = new String[500];
   private String []q_question = new String[500];
   private String []q_user = new String[500];
   private String []q_id = new String[500];
   private String []q_likenumber = new String[500];
   private String []q_time = new String[500];
   private String []q_tag = new String[500];
   private String []q_userpoint = new String[500];
   Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
	SharedPreferences settings;
	SharedPreferences.Editor edit;
   
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	settings = this.getSharedPreferences("qid",Context.MODE_PRIVATE);
	//this.isCheckMap = settings.getAll();
	edit = settings.edit();
	
	Map<String, ?> allContent = settings.getAll();
	for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
        isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
    } 
	
	 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	setContentView(R.layout.activity_display_question);
	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
	appTitle = (TextView)findViewById(R.id.title);
	TextPaint tp = appTitle.getPaint();
	tp.setFakeBoldText(true);
	appTitle.setText("您的提問");
	
	
	Intent intent = getIntent();
	qlength = Integer.parseInt(intent.getStringExtra("qlength"));
 
 mListItems = new LinkedList<HashMap<String,Object>>();
 new GetUserQuestion().execute();
 mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.list);
	mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
	mListView = mPullRefreshListView.getRefreshableView();
 //adapter = new QuestionAdapter(this);
 
 adapter = new QuestionAdapter(mListItems,isCheckMap,this);
	SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
	swingBottomInAnimationAdapter.setInitialDelayMillis(300);
	swingBottomInAnimationAdapter.setAbsListView(mListView);
 
	mListView.setAdapter(swingBottomInAnimationAdapter);
	mListView.setOnItemClickListener(new OnItemClickListener(){
		//here notice that we have initialized questions and latest questions, they are stored in different arrays.
		// So I may resolve this problem to avoid data inconsistency. 
		 @Override  
		   public void onItemClick(AdapterView<?> parent, View view,  
		     int position, long id) {
			 int qid = 10000;
	//		   if(q_id.get(position)!=null)
//			   qid = Integer.parseInt(q_id.get(position));
			 if(q_id[qlength-position]!=null)
				   qid = Integer.parseInt(q_id[qlength-position]);
		//	    Toast.makeText(getActivity(), String.valueOf(initializedLength-position),Toast.LENGTH_LONG).show();
			  Intent i = new Intent(getApplicationContext(), DisplayAnswer.class);
			  if(q_question[qlength-position]!=null)
			  {
				  i.putExtra("qid",q_id[qlength-position]);
				  i.putExtra("question", q_question[qlength-position]);
				  i.putExtra("quser", q_user[qlength-position]);
				  i.putExtra("qtitle", q_title[qlength-position]);
				  i.putExtra("qtime", q_time[qlength-position]);
				  startActivity(i);
			  }
			 
			 
			 
		 	}
		 }
			);  
 
	
 
	}
	
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
		//	latestQtitle.clear();
		//	latestQuser.clear();
		//	latestQtime.clear();
			
	//		Log.v("latest",latestQtitle[0]+" "+latestQuser[0]);
			isCheckMap.clear();
			Map<String, ?> allContent = settings.getAll();
			for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
		        isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
		    } 
		
		new PullTaskForUserQuestion().execute();
		
		
		}
	};
	public class QuestionAdapter extends BaseAdapter {  
		private LinkedList<HashMap<String, Object>> linkedList;
		private LayoutInflater mInflater;
		HttpClient client;
		private String likeid="";
		private String isLike="";
		private String likeNum;
		Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
		SharedPreferences settings;
		SharedPreferences.Editor edit;
		public QuestionAdapter(LinkedList<HashMap<String, Object>> linkedList, Map<Integer, String> isCheckMap, Context context) {
			mInflater = LayoutInflater.from(context);
			this.linkedList = linkedList;
			this.settings = context.getSharedPreferences("qid",Context.MODE_PRIVATE);
			//this.isCheckMap = settings.getAll();
			this.edit = settings.edit();
		//	Map<String, ?> allContent = settings.getAll();
		//	for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
	     //       this.isCheckMap.put(Integer.parseInt(entry.getKey()), entry.getValue().toString()); 
	     //   } 
			this.isCheckMap = isCheckMap;
		}
		
		@Override
		public int getCount() {
			return linkedList.size();
		}

		@Override
		public Object getItem(int position) {
			return linkedList.get(position);
			//return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.list_recommend_item, null);
				
				holder.qtitle = (TextView) convertView.findViewById(R.id.qtitle2);
			//	holder.question = (TextView) convertView.findViewById(R.id.qcontent);
				holder.qtime = (TextView) convertView.findViewById(R.id.qtime);
				holder.username = (TextView) convertView.findViewById(R.id.username);
				holder.qtag2 = (TextView) convertView.findViewById(R.id.qtag2);
				holder.qtag = (ImageView) convertView.findViewById(R.id.qtag);
				holder.like = (CheckBox) convertView.findViewById(R.id.likeClick);
				holder.likenum = (TextView) convertView.findViewById(R.id.likeNum);	
				holder.userimg = (ImageView) convertView.findViewById(R.id.userImg);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();	
			}
			
			
			holder.like.setOnCheckedChangeListener(null);
		//	Log.v("llradio",(String)linkedList.get(position).get("radioId"));
			holder.like.setTag((String)linkedList.get(position).get("radioId"));
			if(isCheckMap!=null && isCheckMap.containsKey(Integer.parseInt((String)linkedList.get(position).get("radioId"))))
			{
			//	holder.like.setChecked(isCheckMap.get(Integer.parseInt((String)linkedList.get(position).get("radioId"))));
				holder.like.setChecked(true);
				Log.v("llradio",(String)linkedList.get(position).get("radioId"));
			}
			else
			{
				Log.v("llradio",(String)linkedList.get(position).get("radioId"));
				holder.like.setChecked(false);
			}
			holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, 
	                    boolean isChecked) { 
	            	int radiaoId = Integer.parseInt(buttonView.getTag().toString());
	            	Log.v("radioId",String.valueOf(radiaoId));
	            	if(isChecked){ 
	                    Log.v("tag","checked");
	                    isCheckMap.put(radiaoId, "1");
	                    edit.putString(String.valueOf(radiaoId), "1");
	                 //   settings.edit().putString("321", "1");
	                  //  edit.commit();
	                    edit.apply();
	                    isLike = "1";
	                    Log.v("isLikeval",isLike);
	                    likeid = (String)linkedList.get(position).get("qid");
	                    updateLike();
	                    /*
	                   // holder.likenum.setText(String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1)));
	                    HashMap<String, Object> map = new HashMap<String, Object>(); 
	                    map.put("likenumber",(String)String.valueOf(Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1));
	                    map.put("title", (String)linkedList.get(position).get("title"));
		            	
		         //  	map.put("qinfo", q_user.get(i)+" "+q_time.get(i));
		            	map.put("qid",(String)linkedList.get(position).get("qid"));
		            	map.put("qinfo", (String)linkedList.get(position).get("qinfo"));
		            	map.put("qtag", (String)linkedList.get(position).get("qtag"));
	                   // map.put(key, value);
	                    linkedList.set(position, map);
	                    */
	                    ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1)));
	                  //  holder.likenum.setText((String)String.valueOf(Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1));
	                    notifyDataSetChanged();
	            	}else{ 
	            		isCheckMap.remove(radiaoId);
	            		edit.remove(String.valueOf(radiaoId));
	            		//edit.commit();
	            		edit.apply();
	                	Log.v("tag","unchecked"); 
	                	isLike = "0";
	                	Log.v("isLikeval",isLike);
	                    likeid = (String)linkedList.get(position).get("qid");
	                    updateLike();
	                    /*
	                    HashMap<String, Object> map = new HashMap<String, Object>(); 
	                    map.put("likenumber",(String)String.valueOf(Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1));

	                    map.put("title", (String)linkedList.get(position).get("title"));
		            	
		     //     	map.put("qinfo", q_user.get(i)+" "+q_time.get(i));
		         //   	map.put("qid",(String)linkedList.get(position).get("qid"));
		            	map.put("qinfo", (String)linkedList.get(position).get("qinfo"));
		            	map.put("qtag", (String)linkedList.get(position).get("qtag"));
	             //       linkedList.set(position, map);
	               */
	                 //   holder.likenum.setText((String)String.valueOf(Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1));
	                    ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1)));
	                    notifyDataSetChanged();
	                    //holder.likenum.setText(String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1)));
	                
	            	} 
	            	
	            }
			});
			
			if(linkedList.size()>0){
				holder.qtitle.setText((String)linkedList.get(position).get("title"));
			//	holder.question.setText((String)linkedList.get(position).get("question"));
				holder.qtime.setText((String)linkedList.get(position).get("qtime"));
				holder.username.setText((String)linkedList.get(position).get("quser"));
				holder.qtag2.setText((String)linkedList.get(position).get("qtag"));
				holder.likenum.setText((String)linkedList.get(position).get("likenumber"));
				
				int p = Integer.parseInt((String)linkedList.get(position).get("point"));
				if(p<=10)
				{
					holder.userimg.setImageResource(R.drawable.lv1);
				}
				else if(p<=30)
				{
					holder.userimg.setImageResource(R.drawable.lv2);
				}
				else if(p<=60)
		       	  {
					holder.userimg.setImageResource(R.drawable.lv3);
		       	  }
		       	  else if(p<=100)
		       	  {
		       		holder.userimg.setImageResource(R.drawable.lv4);
		       	  }
		       	  else if(p<=200)
		       	  {
		       		holder.userimg.setImageResource(R.drawable.lv5);
		       	  }
		       	  else
		       	  {
		       		holder.userimg.setImageResource(R.drawable.lv6);
		       	  }
				
				
			}
			return convertView;
		}

		private class ViewHolder {
			TextView qtitle;        //数据显示区域
		//	TextView question;
		//	TextView qinfo;
			ImageView qtag;
			TextView qtag2;
			CheckBox like;
			TextView likenum;
			TextView qtime;
			TextView username;
			ImageView userimg;
		}
		
		private void updateLike() 
		 {
		new Thread(){
			public void run(){
			//	HttpParams connParams = new BasicHttpParams();                 
			//	HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
			//	HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
			client = new DefaultHttpClient(); 
			StringBuilder builder = new StringBuilder();  
			
			HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"LikeNum.php?isLike="+isLike+"&id="+likeid); 
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
			 
				String res = builder.toString();
			    Log.v("res",res);
			}
			
			
			 
			 
			} catch (Exception e) { 
				Log.v("status","failure");
				
			e.printStackTrace(); 
			} 
			
			
			
		

			}
			}.start();
		 }
	}
	
private void initViews() {
		
		//mListItems.addAll(Arrays.asList(mStrings));
		for(int i=qlength-1; i>=qlength-10&&i>=0; i--){  
            if(q_title[i]!=null)
        	{
            	Log.v("title5",q_title[i]);
            	HashMap<String, Object> map = new HashMap<String, Object>(); 
            	map = new HashMap<String, Object>();
       //     	map.put("title", q_title.get(i));
            	map.put("title", q_title[i]);
            	map.put("question", q_question[i]);
         //  	map.put("qinfo", q_user.get(i)+" "+q_time.get(i));
            	map.put("qid",q_id[i]);
            	map.put("quser", q_user[i]);
            	map.put("qtime", q_time[i]);
            	map.put("qtag", q_tag[i]);
            	map.put("likenumber",q_likenumber[i]);
            	map.put("radioId", q_id[i]);
            	map.put("point", q_userpoint[i]);
            	mListItems.add(map);  
        	}
        }  
		
	}
	public class GetUserQuestion extends AsyncTask<Void,Void,JSONArray>
	{ 
	        @Override
			public JSONArray doInBackground(Void ...params)
			{
				client = new DefaultHttpClient(); 
				StringBuilder builder = new StringBuilder();  
				TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
				String deviceid = tm.getDeviceId();
				HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"UserQuestion.php?userimei="+deviceid); 
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
				try
				{
					for( int i=0;i<json.length();i++ )
					{
						q_title[i] = json.getJSONObject(i).getString("title");
						q_question[i] = json.getJSONObject(i).getString("question");
						q_user[i] = json.getJSONObject(i).getString("user");
						q_id[i]   = json.getJSONObject(i).getString("id");
						q_time[i] = json.getJSONObject(i).getString("created_time");
						q_likenumber[i] = json.getJSONObject(i).getString("like_num");
						q_tag[i] = json.getJSONObject(i).getString("tag");
						q_userpoint[i] = json.getJSONObject(i).getString("point");
						Log.v("qtitle",q_user[i]);
					}
				} 
				catch (Exception e) { 
					Log.v("status","failure");
				e.printStackTrace(); 
				} 
				mListItems.clear();
				initViews();
                
                adapter.notifyDataSetChanged();
                
                super.onPostExecute(json);
			}
				
	}
	
	
	public class PullTaskForUserQuestion extends AsyncTask<Void, Void, String>{

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
				new GetUserQuestion().execute();	
			
			}
			if(mPullRefreshListView.getRefreshType() == 2) {
				
				int count = mListItems.size();  
		     /*   for (int i = count; i < count + 10; i++) {  
		        	if(qtitle.get(i)!=null)
		        	//	adapter.addItem(i+" "+q_title[i]);  
		        	{
		        		HashMap<String, Object> map = new HashMap<String, Object>(); 
		            	map = new HashMap<String, Object>();
		            	map.put("title", qtitle.get(i));
		            	map.put("qinfo", quser.get(i)+" "+qtime.get(i));
		            	linkedList.addLast(map);
		        		
		        	}
		        }
		        */  
				for (int i = qlength-count-1; i >qlength-count-10&&i>=0; i--) {  
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
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	        
	    
	}

