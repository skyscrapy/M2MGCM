package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.androidexample.mobilegcm.R;

import android.content.Context;
import android.content.SharedPreferences;
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


public class AnswerAdapter extends BaseAdapter {  
    //   private List<String> items;  
	 private LinkedList<HashMap<String, Object>> linkedList;
       private LayoutInflater inflater;  
       private String likeid="";
       private String isLike="";
       HttpClient client;
       Map<Integer, String> isCheckMap =  new HashMap<Integer, String>();
   	SharedPreferences settings;
   	SharedPreferences.Editor edit;  
       public AnswerAdapter(LinkedList<HashMap<String, Object>> linkedList, Map<Integer, String> isCheckMap, Context context) {  
          
           this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
           this.linkedList = linkedList;
           this.settings = context.getSharedPreferences("aid",Context.MODE_PRIVATE);
   			this.edit = settings.edit();
   			this.isCheckMap = isCheckMap;
       
       }  
         
       @Override  
       public int getCount() {  
           return linkedList.size();  
       }  
     
       @Override  
       public Object getItem(int position) {  
           return linkedList.get(position);
       }  
     
       @Override  
       public long getItemId(int position) {  
           return position;  
       }  
     
       @Override  
       public View getView(final int position, View convertView, ViewGroup parent) {  
       	 ViewHolder_1 holder1 = null;
       //	ViewHolder_2 holder2 = null;
       	String type = (String)linkedList.get(position).get("type");
        //   if (convertView == null) {  
                  if(type.equals("first") && Integer.parseInt((String)linkedList.get(position).get("likenumber"))>1)
                  {   	  
                	  holder1 = new ViewHolder_1();   
                  	  convertView = inflater.inflate(R.layout.list_item_bestanswer, null);
               
           //	else
           	//	convertView = inflater.inflate(R.layout.list_answer_item, null);
	                 holder1.answer = (TextView)convertView.findViewById(R.id.answer);
	                 holder1.user = (TextView)convertView.findViewById(R.id.auser);
	                 holder1.time = (TextView)convertView.findViewById(R.id.atime);
	                 holder1.like = (CheckBox) convertView.findViewById(R.id.like);
					 holder1.likenum = (TextView) convertView.findViewById(R.id.likeNum);	
					 holder1.userImg = (ImageView) convertView.findViewById(R.id.userimg);
					 convertView.setTag(holder1);
                  }
                  else
                  {
                	//  holder2 = new ViewHolder_2(); 
                	  holder1 = new ViewHolder_1();  
                	  convertView = inflater.inflate(R.layout.list_answer_item, null);
                	  holder1.answer = (TextView)convertView.findViewById(R.id.answer);
                	  holder1.user = (TextView)convertView.findViewById(R.id.auser);
                	  holder1.time = (TextView)convertView.findViewById(R.id.atime);
                	  holder1.like = (CheckBox) convertView.findViewById(R.id.like);
                	  holder1.likenum = (TextView) convertView.findViewById(R.id.likeNum);	
                	  holder1.userImg = (ImageView) convertView.findViewById(R.id.userimg);
       				//  convertView.setTag(holder1);
                  }
        //   }
        //   else {
        	   
        	   
        	//   if(type.equals("first"))
           //    {   	 
        		 //  holder1 = (ViewHolder_1) convertView.getTag();
           //    }
        	//   else
        	//   {
        		//   Log.v("type",type);
        	//	   holder2 = (ViewHolder_2) convertView.getTag();
        	//   }
   	//	}
          
	            holder1.like.setOnCheckedChangeListener(null);
	       		holder1.like.setTag((String)linkedList.get(position).get("radioId"));
	       		if(isCheckMap!=null && isCheckMap.containsKey(Integer.parseInt((String)linkedList.get(position).get("radioId"))))
	       		{
	       		//	holder.like.setChecked(isCheckMap.get(Integer.parseInt((String)linkedList.get(position).get("radioId"))));
	       			holder1.like.setChecked(true);
	       			Log.v("llradio",(String)linkedList.get(position).get("radioId"));
	       		}
	       		else
	       		{
	       			Log.v("llradio",(String)linkedList.get(position).get("radioId"));
	       			holder1.like.setChecked(false);
	       		}
	       		holder1.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	                   @Override
	                   public void onCheckedChanged(CompoundButton buttonView, 
	                           boolean isChecked) { 
	                   	int radiaoId = Integer.parseInt(buttonView.getTag().toString());
	                   	Log.v("radioId",String.valueOf(radiaoId));
	                   	if(isChecked){ 
	                           Log.v("tag","checked");
	                           isCheckMap.put(radiaoId, "1");
	                           edit.putString(String.valueOf(radiaoId), "1");
	                      
	                           edit.apply();
	                         isLike = "1";
	                           likeid = (String)linkedList.get(position).get("aid");
	                           updateLike();
	                         
	                           ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1)));
	                        
	                           notifyDataSetChanged();
	                   	}else{ 
	                   		isCheckMap.remove(radiaoId);
	                   		edit.remove(String.valueOf(radiaoId));
	                   		//edit.commit();
	                   		edit.apply();
	                       	//Log.v("tag","unchecked"); 
	                       	isLike = "0";
	                       //	Log.v("isLikeval",isLike);
	                           likeid = (String)linkedList.get(position).get("aid");
	                           updateLike();
	                          
	                           ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1)));
	                           notifyDataSetChanged();
	                           
	                       
	                   	} 
	                   	
	                   }
	       		});
	       		TextPaint tp2 = holder1.user.getPaint();
	            tp2.setFakeBoldText(true);
	       		holder1.answer.setText(((String)linkedList.get(position).get("answer")));  
	           
	       		holder1.user.setText((String)linkedList.get(position).get("auser"));  
	           
	       		holder1.time.setText((String)linkedList.get(position).get("atime"));  
	       		holder1.likenum.setText((String)linkedList.get(position).get("likenumber"));  
	       		int p = Integer.parseInt((String)linkedList.get(position).get("point"));
				if(p<=10)
				{
					holder1.userImg.setImageResource(R.drawable.lv1);
				}
				else if(p<=30)
				{
					holder1.userImg.setImageResource(R.drawable.lv2);
				}
				else if(p<=60)
		       	  {
					holder1.userImg.setImageResource(R.drawable.lv3);
		       	  }
		       	  else if(p<=100)
		       	  {
		       		holder1.userImg.setImageResource(R.drawable.lv4);
		       	  }
		       	  else if(p<=200)
		       	  {
		       		holder1.userImg.setImageResource(R.drawable.lv5);
		       	  }
		       	  else
		       	  {
		       		holder1.userImg.setImageResource(R.drawable.lv6);
		       	  }
           
           /*
           else
           {
        	   holder2.like.setOnCheckedChangeListener(null);
        	   holder2.like.setTag((String)linkedList.get(position).get("radioId"));
	       		if(isCheckMap!=null && isCheckMap.containsKey(Integer.parseInt((String)linkedList.get(position).get("radioId"))))
	       		{
	       		//	holder.like.setChecked(isCheckMap.get(Integer.parseInt((String)linkedList.get(position).get("radioId"))));
	       			holder2.like.setChecked(true);
	       			Log.v("llradio",(String)linkedList.get(position).get("radioId"));
	       		}
	       		else
	       		{
	       			Log.v("llradio",(String)linkedList.get(position).get("radioId"));
	       			holder2.like.setChecked(false);
	       		}
	       		holder2.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	                   @Override
	                   public void onCheckedChanged(CompoundButton buttonView, 
	                           boolean isChecked) { 
	                   	int radiaoId = Integer.parseInt(buttonView.getTag().toString());
	                   	Log.v("radioId",String.valueOf(radiaoId));
	                   	if(isChecked){ 
	                           Log.v("tag","checked");
	                           isCheckMap.put(radiaoId, "1");
	                           edit.putString(String.valueOf(radiaoId), "1");
	                      
	                           edit.apply();
	                         isLike = "1";
	                           likeid = (String)linkedList.get(position).get("aid");
	                           updateLike();
	                         
	                           ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))+1)));
	                        
	                           notifyDataSetChanged();
	                   	}else{ 
	                   		isCheckMap.remove(radiaoId);
	                   		edit.remove(String.valueOf(radiaoId));
	                   		//edit.commit();
	                   		edit.apply();
	                       	//Log.v("tag","unchecked"); 
	                       	isLike = "0";
	                       //	Log.v("isLikeval",isLike);
	                           likeid = (String)linkedList.get(position).get("aid");
	                           updateLike();
	                          
	                           ((HashMap)linkedList.get(position)).put("likenumber",String.valueOf((Integer.parseInt((String)linkedList.get(position).get("likenumber"))-1)));
	                           notifyDataSetChanged();
	                           
	                       
	                   	} 
	                   	
	                   }
	       		});
	       		TextPaint tp2 = holder2.user.getPaint();
	            tp2.setFakeBoldText(true);
	            holder2.answer.setText(((String)linkedList.get(position).get("answer")));  
	           
	            holder2.user.setText((String)linkedList.get(position).get("auser"));  
	           
	            holder2.time.setText((String)linkedList.get(position).get("atime"));  
	            holder2.likenum.setText((String)linkedList.get(position).get("likenumber"));  
	       		int p = Integer.parseInt((String)linkedList.get(position).get("point"));
				if(p<=10)
				{
					holder2.userImg.setImageResource(R.drawable.lv1);
				}
				else if(p<=30)
				{
					holder2.userImg.setImageResource(R.drawable.lv2);
				}
				else if(p<=60)
		       	  {
					holder2.userImg.setImageResource(R.drawable.lv3);
		       	  }
		       	  else if(p<=100)
		       	  {
		       		holder2.userImg.setImageResource(R.drawable.lv4);
		       	  }
		       	  else if(p<=200)
		       	  {
		       		holder2.userImg.setImageResource(R.drawable.lv5);
		       	  }
		       	  else
		       	  {
		       		holder2.userImg.setImageResource(R.drawable.lv6);
		       	  }
           }
           */
       		
           return convertView;    
       }  
       class ViewHolder_1{  
           public TextView answer;  
           public TextView user;
           public TextView time;
           public CheckBox like;
           public TextView likenum;
           public ImageView userImg;
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
  		
  		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"likeNumForAnswer.php?isLike="+isLike+"&id="+likeid); 
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