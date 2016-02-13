package com.iknow.view.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import com.iknow.mobilegcm.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PullAdapter extends BaseAdapter {

	//private LinkedList<String> linkedList;
	private LinkedList<HashMap<String, Object>> linkedList;
	private LayoutInflater mInflater;
	HttpClient client;
	private String likeid="";
	private String isLike="";
	private String likeNum;
	Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
	SharedPreferences settings;
	SharedPreferences.Editor edit;
	public PullAdapter(LinkedList<HashMap<String, Object>> linkedList, Map<Integer, String> isCheckMap, Context context) {
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
			//TextPaint tp = holder.qtitle.getPaint(); 
		//	tp.setFakeBoldText(true);
		//	holder.question = (TextView) convertView.findViewById(R.id.qcontent);
			holder.qtime = (TextView) convertView.findViewById(R.id.qtime);
			holder.username = (TextView) convertView.findViewById(R.id.username);
			TextPaint tp = holder.username.getPaint(); 
			tp.setFakeBoldText(true);
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
		
		
		holder.username.setOnClickListener(new CompoundButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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

	private static class ViewHolder {
		TextView qtitle;        //数据显示区域
	//	TextView question;
	//	TextView qinfo;
		ImageView qtag;
		ImageView userimg;
		TextView qtag2;
		CheckBox like;
		TextView likenum;
		TextView qtime;
		TextView username;
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
		
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"likeNumForQuestion.php?isLike="+isLike+"&id="+likeid); 
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
