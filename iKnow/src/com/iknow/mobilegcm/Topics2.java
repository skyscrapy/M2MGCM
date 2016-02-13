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
import com.iknow.mobilegcm.Topics1.GetNumInfo;
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
import android.text.TextPaint;
import android.text.format.DateUtils;  
import android.widget.ArrayAdapter;  
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
public class Topics2 extends Fragment{

    ListView listView;
    LinkedList<HashMap<String,Object>> mListItems;
    HttpClient client;
    String[] strings = {"科技","生活","健康","運動","電玩","時事","學術","美食","娛樂",
            "心情", "定位","其他"};
    int[] image = {
            R.drawable.computer,
            R.drawable.life,
            R.drawable.health,
            R.drawable.sports,
            R.drawable.digital,
            R.drawable.business,
            R.drawable.education,
            R.drawable.literature,
            R.drawable.entertainment,
            R.drawable.emotion,
            R.drawable.location,
            R.drawable.nulll
    };
    String []typenumber = new String[12];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.getLayoutInflater(savedInstanceState);
		return inflater.inflate(R.layout.fragment_topics2, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mListItems = new LinkedList<HashMap<String,Object>>();
		new GetTypeInfo().execute();
		listView = (ListView)this.getView().findViewById(R.id.listviewfortopics2);
		listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		TopicsAdapter adapter = new TopicsAdapter(mListItems, this.getActivity() );
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			 public void onItemClick(AdapterView<?> parent, View view, 
					 int position, long id) {
					 // TODO Auto-generated method stub
				 Intent intent = new Intent(getActivity(),DisplayTypeQuestion.class);
				 intent.putExtra("type", strings[position]);
				 startActivity(intent);
			}
		});
		
		
	}
	@Override 
	public void onResume()
	{
		super.onResume();
	}
private void initViews() {
		
		for(int i=0; i<12; i++){  
	          //  if(mStrings[i]!=null)
	        	{
	            	HashMap<String, Object> map = new HashMap<String, Object>(); 
	            	map = new HashMap<String, Object>();
	            	map.put("type", strings[i]);
	            	map.put("number", typenumber[i]);
	            	
	            	mListItems.add(map);  
	        	}
	        }  
	}
	
	
	public class GetTypeInfo extends AsyncTask<Void,Void, String>
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
			for(int i=0;i<12;i++)
			{
				typenumber[i] = strall[i];
			}
			initViews();
			}
			else
			{
				AlertDialog.Builder builder = new Builder(getActivity());
				//	      builder.setIcon(android.R.drawable.btn_star_big_on);
					      builder.setTitle("連接失敗");
					      builder.setMessage("是否重試？");
					      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					    
					    public void onClick(DialogInterface dialog, int id) {
					    	new GetTypeInfo().execute();


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
	
	

	public class TopicsAdapter extends BaseAdapter {

		//private LinkedList<String> linkedList;
		private LinkedList<HashMap<String, Object>> linkedList;
		private LayoutInflater mInflater;
		public TopicsAdapter(LinkedList<HashMap<String, Object>> linkedList, Context context) {
			mInflater = LayoutInflater.from(context);
			this.linkedList = linkedList;
		}
		
		@Override
		public int getCount() {
			return linkedList.size();
		}

		@Override
		public Object getItem(int position) {
			//return linkedList.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.list_item_topics2, null);
				holder.typeimg = (ImageView)convertView.findViewById(R.id.typeimg);
				holder.typename = (TextView)convertView.findViewById(R.id.typename);
				TextPaint tp = holder.typename.getPaint(); 
				tp.setFakeBoldText(true);
				holder.typenum = (TextView)convertView.findViewById(R.id.typenum);
					
					convertView.setTag(holder);
				}else {
					holder = (ViewHolder) convertView.getTag();
				}
			
				if(linkedList.size()>0)
				{
						holder.typename.setText((String)linkedList.get(position).get("type"));
						holder.typenum.setText((String)linkedList.get(position).get("number")+"個問題");
						holder.typeimg.setImageDrawable(getResources().getDrawable(image[position]));
						Log.v("position",String.valueOf(position));
						
				}
			
				return convertView;
		}

		private class ViewHolder {
			ImageView typeimg;
			TextView typename;
			TextView typenum;
		}
	}
		
}
