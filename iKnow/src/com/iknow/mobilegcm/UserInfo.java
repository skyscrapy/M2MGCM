package com.iknow.mobilegcm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import com.iknow.mobilegcm.Recommend.GetInitialData;
import com.iknow.mobilegcm.ToggleButton.OnToggleStateChangedListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class UserInfo extends Fragment implements OnClickListener{
	private Handler mHandler;
	String deviceIMEI = "";
	String temp = "";
	String username = "";
	String asked = "";
	String answered = "";
	String userpoint="";
	int userpointint;
	String userrate = "";
	String selfintro = "";
	String askednumber="";
	String answerednumber="";
	
	HttpClient client;
	TextView userinfo;
	ImageView userlevelimg;
	TextView userlevel;
	TextView user_point;
	TextView rate;
	ImageView editimg;
	TextView self_intro;
	TextView asknum;
	TextView answernum;
	
	
	TextView userinterest1;
	TextView userinterest2;
	TextView userinterest3;
	
	TextView ask;
	TextView answer;
	
	ToggleButton toggleButton;
	ImageView askedinfo;
	ImageView answeredinfo;
	TextView askedmsg;
	TextView answeredmsg;
	String[] strarray = new String[3]; 
	SharedPreferences settings;
	SharedPreferences.Editor edit;
	boolean state = false;
	boolean notification_Enable = true;
	boolean notification_Disable = false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		UserIntroDialog dialog = new UserIntroDialog(this.getActivity(),"請輸入您的簡介",new UserIntroDialog.OnCustomDialogListener() {
            @Override
            public void back(String msg) {
            	selfintro = msg;

            	if(msg!=null&&!msg.equals(""))
            	{   self_intro.setText(msg);
                    
            	}
            	else
            	{
            		self_intro.setText("這個人很懶什麼都沒留下");
            	}
            	updateSelfIntro();
            }
    }
	);
		dialog.setCanceledOnTouchOutside(true);
	     dialog.show();
	}
	 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		return inflater.inflate(R.layout.user_info, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		settings = this.getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
		edit = settings.edit();
		state = settings.getBoolean("notification", true);
		
		  // GET IMEI NUMBER      
		 TelephonyManager tManager = (TelephonyManager) getActivity().getBaseContext()
		    .getSystemService(Context.TELEPHONY_SERVICE);
		  deviceIMEI = tManager.getDeviceId();
		 
		  toggleButton = (ToggleButton)this.getView().findViewById(R.id.toggle);
		  toggleButton.setInitState(state);
		  toggleButton.setImageResource(R.drawable.bkg_switch, R.drawable.btn_slip);
		  
		  
		  userinfo = (TextView)this.getView().findViewById(R.id.username);
		  TextPaint tp = userinfo.getPaint(); 
		  tp.setFakeBoldText(true);
		  userlevelimg = (ImageView)this.getView().findViewById(R.id.userlevelimg);
	//	  userlevel = (TextView)this.getView().findViewById(R.id.userlevel);
		  user_point = (TextView)this.getView().findViewById(R.id.user_point);
		  userinterest1 = (TextView)this.getView().findViewById(R.id.interest1);
		  userinterest2 = (TextView)this.getView().findViewById(R.id.interest2);
		  userinterest3 = (TextView)this.getView().findViewById(R.id.interest3);
		  editimg = (ImageView)this.getView().findViewById(R.id.editimg);
		  self_intro = (TextView)this.getView().findViewById(R.id.selfintro);
		  
		  asknum = (TextView)this.getView().findViewById(R.id.askednum);
		  answernum = (TextView)this.getView().findViewById(R.id.answerednum);
		  
		  ask = (TextView)this.getView().findViewById(R.id.asked);
		  answer = (TextView)this.getView().findViewById(R.id.answered);
		  TextPaint tp2 = ask.getPaint();
		  tp2.setFakeBoldText(true);
		  ask.setText("已提出問題");
		  TextPaint tp3 = answer.getPaint();
		  tp3.setFakeBoldText(true);
		  answer.setText("已回答問題");
		  askedinfo = (ImageView)this.getView().findViewById(R.id.askedinfo);
		  answeredinfo = (ImageView)this.getView().findViewById(R.id.answeredinfo);
		  askedmsg = (TextView)this.getView().findViewById(R.id.askedmsg);
		  answeredmsg = (TextView)this.getView().findViewById(R.id.answeredmsg);
//			 userinfo.setText("3333");
	//		askedinfo.setText("111");
	//		answeredinfo.setText("aaa");
		  
          new GetUserInfo().execute();
          
          
        //  startFlick(userinfo);
          
          
          toggleButton.setOnStateChangedListener(new OnToggleStateChangedListener() {
              @Override
              public void changed(boolean isOn) {
                 if(isOn)
                 {
                	 edit.putBoolean("notification", true);
                	 edit.commit();
                 }
                 else
                 {
                	 edit.putBoolean("notification", false);
                	 edit.commit();
                 }
              }
          });
          
          
          
          editimg.setOnClickListener(this);
			
			
         
          
          
          asknum.setOnClickListener(new  View.OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				if(strarray[3].equals("1"))
  				{
  					Intent intent = new Intent(getActivity(), DisplayUserQuestion.class);
  					intent.putExtra("qlength", String.valueOf(askednumber));
  					startActivity(intent);
  				
  				}
  				else
  				{
  					Toast.makeText(getActivity(), "還沒有提過問題哦,快點提問賺積分吧！", Toast.LENGTH_LONG).show();
  				}
  				
  			}
  		}
          		  
          		 
          		  );
          
          answernum.setOnClickListener(new  View.OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				if(strarray[4].equals("1"))
  				{
  					Intent intent = new Intent(getActivity(), DisplayUserAnswer.class);
  					intent.putExtra("alength", String.valueOf(answerednumber));
  					startActivity(intent);
  				
  				}
  				else
  				{
  					Toast.makeText(getActivity(), "還沒有回答問題哦，快點回答賺積分吧！", Toast.LENGTH_LONG).show();
  				}
  				
  			}
  		}
          		 
          		  );
          
          askedinfo.setOnClickListener(new  View.OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				if(strarray[3].equals("1"))
  				{
  					Intent intent = new Intent(getActivity(), DisplayUserQuestion.class);
  					intent.putExtra("qlength", String.valueOf(askednumber));
  					startActivity(intent);
  				
  				}
  				else
  				{
  					Toast.makeText(getActivity(), "還沒有提過問題哦,快點提問賺積分吧！", Toast.LENGTH_LONG).show();
  				}
  				
  			}
  		}
          		  
          		 
          		  );
          
          answeredinfo.setOnClickListener(new  View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if(strarray[4].equals("1"))
    				{
    					Intent intent = new Intent(getActivity(), DisplayUserAnswer.class);
    					intent.putExtra("alength", String.valueOf(answerednumber));
    					startActivity(intent);
    				
    				}
    				else
    				{
    					Toast.makeText(getActivity(), "還沒有回答問題哦，快點回答賺積分吧！", Toast.LENGTH_LONG).show();
    				}
    				
    			}
    		}
            		 
            		  );
	
          askedmsg.setOnClickListener(new  View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if(strarray[3].equals("1"))
    				{
    					Intent intent = new Intent(getActivity(), DisplayUserQuestion.class);
    					intent.putExtra("qlength", String.valueOf(askednumber));
    					startActivity(intent);
    				
    				}
    				else
    				{
    					Toast.makeText(getActivity(), "還沒有提過問題哦,快點提問賺積分吧！", Toast.LENGTH_LONG).show();
    				}
    				
    			}
    		}
            		  
            		 
            		  );
            
            answeredmsg.setOnClickListener(new  View.OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {
      				// TODO Auto-generated method stub
      				if(strarray[4].equals("1"))
      				{
      					Intent intent = new Intent(getActivity(), DisplayUserAnswer.class);
      					intent.putExtra("alength", String.valueOf(answerednumber));
      					startActivity(intent);
      				
      				}
      				else
      				{
      					Toast.makeText(getActivity(), "還沒有回答問題哦，快點回答賺積分吧！", Toast.LENGTH_LONG).show();
      				}
      				
      			}
      		}
              		 
              		  );
	}
	/*
	private void startUrlCheck() 
    { 
		new Thread(){
		public void run(){
		client = new DefaultHttpClient(); 
		StringBuilder builder = new StringBuilder();  
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"userinfo.php?imei="+deviceIMEI); 
		try { 
		HttpResponse response = client.execute(myget); 
		Log.v("status","success");
		HttpEntity entity = response.getEntity();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
		
		temp = reader.readLine();
		//Log.v("username",username);
		mHandler.obtainMessage(0x123, temp).sendToTarget();
		
		} catch (Exception e) { 
			Log.v("status","failure");
		e.printStackTrace(); 
		} 
		}
		}.start();
		}
	*/
	@Override
    public void onResume()
	{
		super.onResume();
		updateUi();
    }
	
	
	public void updateUi()
	{
		new GetUserInfo().execute();
	}
	public void updateSelfIntro()
	{
		new Thread(){
			public void run(){
			//	HttpParams connParams = new BasicHttpParams();                 
			//	HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
			//	HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
			client = new DefaultHttpClient(); 
			StringBuilder builder = new StringBuilder();  
			String url = Config.YOUR_SERVER_URL+"SelfIntro.php?imei="+deviceIMEI+"&selfintro="+selfintro;
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
	public class GetUserInfo extends AsyncTask<Void, Void, String>
	{

		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute()
		{
			Dialog.setMessage("Loading...");
			Dialog.show();
			
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpParams connParams = new BasicHttpParams();                 
			HttpConnectionParams.setConnectionTimeout(connParams,5*1000);                 
			HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
		client = new DefaultHttpClient(); 
		StringBuilder builder = new StringBuilder();  
		
		HttpGet myget = new HttpGet(Config.YOUR_SERVER_URL+"userinfo.php?imei="+deviceIMEI); 
		try { 
			HttpResponse response = client.execute(myget); 
			Log.v("status","success");
			HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
			for (String s = reader.readLine(); s != null; s = reader.readLine()) { 
				builder.append(s); 
				}
			//temp = reader.readLine();
			temp = builder.toString();
			Log.v("USERinfo",temp);
			return temp;
			} 
		catch (Exception e) { 
				Log.v("status","failure");
			e.printStackTrace(); 
			} 
		return null;
		}
		
		
		 @Override  
	        protected void onPostExecute(String temp) {
			 if ((Dialog != null) && Dialog.isShowing())
			 {
				 Dialog.dismiss();
			 }
		 
			 strarray = temp.split("\\^");  // Notice that double\ is needed to tackle escape character
		if(strarray.length == 10)
		{
       	  for(int i=0;i<strarray.length;i++)
       		  Log.v("tempinfo",strarray[i]);
       	  username = strarray[0];
       	  userinfo.setText(username);
       	  userpoint = strarray[1];
       	  selfintro = strarray[2];
       	 //user_point.setText(userpoint);
       	  userpointint = Integer.parseInt(userpoint);
       	  editimg.setImageResource(R.drawable.edit);
       	  
       	 
       	  if(selfintro!=null && !selfintro.equals(""))
       	  {
       		  self_intro.setText(selfintro);
       	  }
       	  else
       	  {
       		  self_intro.setText("這個人很懶什麼都沒留下...");
       	  }
       	  if(userpointint<=10)
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv1);
       		  //userlevel.setText("見習級");
       	  }
       	  else if(userpointint<=30)
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv2);
       	//	userlevel.setText("入門級");
       	  }
       	  else if(userpointint<=60)
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv3);
       	//	userlevel.setText("精英級");
       	  }
       	  else if(userpointint<=100)
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv4);
       	//	userlevel.setText("專家級");
       	  }
       	  else if(userpointint<=200)
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv5);
       	//	userlevel.setText("大師級");
       	  }
       	  else
       	  {
       		  userlevelimg.setImageResource(R.drawable.lv6);
       		//userlevel.setText("傳說級");
       	  }
       	  
       	  if( strarray[3].equals("1") )
       	  {
       		   askednumber = strarray[5];
       	  }
       	  else
       	  {
       		  askednumber="0";
       		  Toast.makeText(getActivity(), "還沒有提過問題哦,快點提問賺積分吧！", Toast.LENGTH_LONG).show();
       	  }
       	  if( strarray[4].equals("1") )
       	  {
       		   answerednumber = strarray[6];
       	  }
       	  else
       	  {
       		  answerednumber="0";
       		Toast.makeText(getActivity(), "還沒有回答問題哦，快點回答賺積分吧！", Toast.LENGTH_LONG).show();
       	  }
       	  
       	  
       	 user_point.setText("積    分："+userpoint);
      	  
      	  asknum.setText(askednumber+"題");
      	  answernum.setText(answerednumber+"題");
       	  
       	  
       	  if(strarray[7]!=null&&!strarray[7].equals(" "))
       	  userinterest1.setText("擅長 : "+strarray[7]);
       	if(strarray[8]!=null&&!strarray[8].equals(" "))
       	  userinterest2.setText("            "+strarray[8]);
       	if(strarray[9]!=null&&!strarray[9].equals(" "))  
       	userinterest3.setText("            "+strarray[9]);
		 }
		 else
		 {
			 AlertDialog.Builder builder = new Builder(getActivity());
		     // builder.setIcon(android.R.drawable.btn_star_big_on);
		      builder.setTitle("連接失敗");
		      builder.setMessage("是否重試？");
		      builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
		    
		    public void onClick(DialogInterface dialog, int id) {
		    	new GetUserInfo().execute();


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
	
	
	

	
}
