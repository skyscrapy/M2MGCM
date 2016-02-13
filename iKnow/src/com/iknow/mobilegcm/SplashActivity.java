package com.iknow.mobilegcm;

import com.androidexample.mobilegcm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable(){ 
			 
	         @Override
	         public void run() { 
	             Intent mainIntent = new Intent(SplashActivity.this,Main.class); 
	             SplashActivity.this.startActivity(mainIntent); 
	                 SplashActivity.this.finish(); 
	         } 
	             
	        }, 1000); 
		
	}

}
