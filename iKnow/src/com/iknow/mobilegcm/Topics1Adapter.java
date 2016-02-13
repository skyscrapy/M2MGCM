package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import com.androidexample.mobilegcm.R;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Topics1Adapter extends BaseAdapter {

	//private LinkedList<String> linkedList;
	private LinkedList<HashMap<String, Object>> linkedList;
	HttpClient client;
	private LayoutInflater mInflater;
	private Context context;
	String qid;
	String question;
	String qtime;
	String quser;
	String qtitle;
	public Topics1Adapter(LinkedList<HashMap<String, Object>> linkedList, Context context) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
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
			convertView = mInflater.inflate(R.layout.list_item_topics1, null);
			holder.qtitle1 = (TextView) convertView.findViewById(R.id.topics1q1);
			holder.qtitle2 = (TextView) convertView.findViewById(R.id.topics1q2);
			holder.qtitle3 = (TextView) convertView.findViewById(R.id.topics1q3);

			holder.type = (TextView)convertView.findViewById(R.id.topics1type);
			TextPaint tp = holder.type.getPaint(); 
			tp.setFakeBoldText(true);
			holder.typenum = (TextView) convertView.findViewById(R.id.topics1typenum);	
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.type.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, DisplayTypeQuestion.class);
					intent.putExtra("type",(String)linkedList.get(position).get("type"));
					context.startActivity(intent);
				}
				
			});
			holder.qtitle1.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					qid = (String)linkedList.get(position).get("qid1");
					new GetQuestionInfo().execute();
					
					
				}
				
			});
			holder.qtitle2.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					qid = (String)linkedList.get(position).get("qid2");
					new GetQuestionInfo().execute();
					
					
				}
				
			});
			holder.qtitle3.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					qid = (String)linkedList.get(position).get("qid3");
					new GetQuestionInfo().execute();
					
					
				}
				
			});
			if(linkedList.size()>0){
				
				//	holder.question.setText((String)linkedList.get(position).get("question"));
				holder.type.setText("      "+(String)linkedList.get(position).get("type"));
				holder.qtitle1.setText((String)linkedList.get(position).get("title1"));
				holder.qtitle2.setText((String)linkedList.get(position).get("title2"));
				holder.qtitle3.setText((String)linkedList.get(position).get("title3"));
				holder.typenum.setText("問題"+(String)linkedList.get(position).get("number")+"則");
				
			}
			return convertView;
	}

	private static class ViewHolder {
		TextView qtitle1;        //数据显示区域
		TextView qtitle2;
		TextView qtitle3;
		TextView type;
	
		TextView typenum;
		
	}
	
	public class GetQuestionInfo extends AsyncTask<Void, Void, String>
	{
		@Override
		public String doInBackground(Void...params)
		{
			client = new DefaultHttpClient(); 
			StringBuilder builder = new StringBuilder();  
			HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"UserQuestionInfo.php?qid="+qid); 
			try { 
			HttpResponse response = client.execute(myget); 
			Log.v("status","success");
			HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
			for (String s = reader.readLine(); s != null; s = reader.readLine()) { 
			builder.append(s); 
			} 
		 
			return builder.toString();
			}
			catch (Exception e) { 
				Log.v("status","failure");
			e.printStackTrace(); 
			}
			return null;
		}
		@Override
		public void onPostExecute(String res)
		{
			String []strall;
			strall = res.split("\\^");
			question = strall[0];
			qtime = strall[1];
			quser =  strall[2];
			qtitle = strall[3];
			
			Intent intent = new Intent(context, DisplayAnswer.class);
			intent.putExtra("qid", qid);
			intent.putExtra("question", question);
			intent.putExtra("qtime", qtime);
			intent.putExtra("quser", quser);
			intent.putExtra("qtitle", qtitle);
			context.startActivity(intent);
			
			
			
		}
	}
	
}
