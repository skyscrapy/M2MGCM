package com.iknow.view.task;

import java.util.HashMap;
import java.util.LinkedList;

import com.iknow.view.PullToRefreshListView;

import android.os.AsyncTask;
import android.widget.BaseAdapter;

public class PullTask extends AsyncTask<Void, Void, String>{

	private PullToRefreshListView pullToRefreshListView;  //实现下拉刷新与上拉加载的ListView
	private int pullState;               //记录判断，上拉与下拉动作
	private BaseAdapter baseAdapter;     //ListView适配器，用于提醒ListView数据已经更新
	private LinkedList<HashMap<String, Object>> linkedList;
//	private LinkedList<String>qtitle;
//	private LinkedList<String>quser;
//	private LinkedList<String>qtime;
//	private LinkedList<String>latestQtitle;
//	private LinkedList<String>latestQuser;
//	private LinkedList<String>latestQtime;
	private String[]qid;
	private String[]qtitle;
	private String[]quser;
	private String[]qtime;
	private String[]qtag;
	private String[]qlikenumber;
	private String[]latestQid;
	private String[]latestQtitle;
	private String[]latestQuser;
	private String[]latestQtime;
	private String[]latestQtag;
	private String[]latestQlikenumber;
	private int initializedLength;
	private int latestLength;
	public PullTask(PullToRefreshListView pullToRefreshListView, int pullState,
			BaseAdapter baseAdapter, LinkedList<HashMap<String, Object>> linkedList, 
	//		LinkedList<String> q_title, LinkedList<String> q_user, LinkedList<String> q_time,	LinkedList<String> latestQtitle, LinkedList<String> latestQuser, LinkedList<String> latestQtime) {
			String []q_id, String[] q_title, String[] q_user, String[] q_time,	String []q_tag,String []q_likenumber,String[] latestQid, String[] latestQtitle, String[] latestQuser, String[] latestQtime, String []latestQtag,String []latestQlikenumber, int initializedLength, int latestLength) {
		this.pullToRefreshListView = pullToRefreshListView;
		this.pullState = pullState;
		this.baseAdapter = baseAdapter;
		this.linkedList = linkedList;
		this.qid = q_id;
		this.qtitle = q_title;
		this.quser = q_user;
		this.qtime = q_time;
		this.qtag = q_tag;
		this.latestQid = latestQid;
		this.latestQtitle = latestQtitle;
		this.latestQuser = latestQuser;
		this.latestQtime = latestQtime;
		this.latestQtag = latestQtag;
		this.initializedLength = initializedLength;
		this.latestLength = latestLength;
		this.latestQlikenumber = latestQlikenumber;
		this.qlikenumber = q_likenumber;
	}
	
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
		if(pullState == 1) {
		/*	
	        for (int i = 0; i < 500; i++) {  
	        	if(latestQtitle.get(i)!=null)
	        	//	adapter.addItem(i+" "+q_title[i]);  
	        	{
	        		HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	            	map.put("title", latestQtitle.get(i));
	            	map.put("qinfo", latestQuser.get(i)+" "+latestQtime.get(i));
	            	linkedList.addFirst(map);
	        	}
	        }
	        
	        
	        for(int i=latestQtitle.size();i>0;i--)
	        {
	        	qtitle.addFirst(latestQtitle.get(i-1));
	        	quser.addFirst(latestQuser.get(i-1));
	        	qtime.addFirst(latestQtime.get(i-1));
	        	
	        }
	      */
			
			
			for (int i = 0; i < latestLength; i++) {  
	        	if(latestQtitle[i]!=null)
			//	if(latestQtitle.get(i)!=null)
				//	adapter.addItem(i+" "+q_title[i]);  
	        	{
	        		HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	            	map.put("qid", latestQid[i]);
	            	map.put("title", latestQtitle[i]);
	            	map.put("quser", latestQuser[i]);
	            	map.put("qtime", latestQtime[i]);
	            	map.put("qtag", latestQtag[i]);
	            	map.put("likenumber", latestQlikenumber[i]);
	            //	map.put("radioId", (initializedLength-1-i));
	            	map.put("radioId", latestQid[i]);
	            //	map.put("qinfo", latestQuser.get(i)+" "+latestQtime.get(i));
	            	linkedList.addFirst(map);
	        	}
	        }
		}
		if(pullState == 2) {
			
			int count = linkedList.size();  
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
			for (int i = initializedLength-count-1; i >initializedLength-count-10&&i>=0; i--) {  
	        	if(qtitle[i]!=null)
		//		if(qtitle.get(i)!=null)
	        	//	adapter.addItem(i+" "+q_title[i]);  
	        	{
	        		HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	            	map.put("title", qtitle[i]);
	            	map.put("quser", quser[i]);
	            	map.put("qtime", qtime[i]);
	                map.put("qtag", qtag[i]);
	                map.put("likenumber", qlikenumber[i]);
	                map.put("qid", qid[i]);
	                map.put("radioId", (qid[i]));
		         //   map.put("qinfo", quser.get(i)+" "+qtime.get(i));
	            	linkedList.addLast(map);
	        		
	        	}
	        }
		}
		baseAdapter.notifyDataSetChanged();
		pullToRefreshListView.onRefreshComplete();
		super.onPostExecute(result);
	}
}
