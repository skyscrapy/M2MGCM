package com.iknow.mobilegcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexample.mobilegcm.R;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	Controller aController;
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	public static String name;
	public static String email;
	public static String imei;
	public static String interest1;
	public static String interest2;
	public static String interest3;
	TextView appTitle;
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
	
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        appTitle = (TextView)findViewById(R.id.title);
        TextPaint tp = appTitle.getPaint(); 
        tp.setFakeBoldText(true);
        appTitle.setText("‘]É‘");
		// Get Global Controller Class object 
		aController = (Controller) getApplicationContext();
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {	
			// Internet Connection is not present
			aController.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}
		
		String deviceIMEI = "";
		
		  // GET IMEI NUMBER      
		 TelephonyManager tManager = (TelephonyManager) getBaseContext()
		    .getSystemService(Context.TELEPHONY_SERVICE);
		  deviceIMEI = tManager.getDeviceId(); 
		
		 
		 // Getting name, email from intent
		Intent i = getIntent();
		
		name = i.getStringExtra("name");
		email = i.getStringExtra("email");		
		imei  = deviceIMEI;
		interest1 = i.getStringExtra("interest1");
		interest2 = i.getStringExtra("interest2");
		interest3 = i.getStringExtra("interest3");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest permissions was properly set 
		GCMRegistrar.checkManifest(this);

		
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			
			 Log.i("GCM K", "--- Regid = ''"+regId);
			// Register with GCM			
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
			
		} else {
			
			// Device is already registered on GCM Server
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				
				final Context context = this;
				// Skips registration.				
				Toast.makeText(getApplicationContext(), 
						"ƒ˙“—Ωõ‘]É‘ﬂ^", 
						Toast.LENGTH_LONG).show();
				Log.i("GCM K", "Already registered with GCM Server");
				Intent i1 = new Intent(getApplicationContext(), MainPage.class);
    			startActivity(i1);
    			finish();
				//GCMRegistrar.unregister(contex1t);
				
			} else {
				
				Log.i("GCM K", "-- gO for registration--");
				
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					 private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);	
					 
					 @Override
					 protected void onPreExecute()
					 {
						 Dialog.setMessage("‘]É‘÷–..");
				         Dialog.show();
					 }
					@Override
					protected Void doInBackground(Void... params) {
						
						// Register on our server
						// On server creates a new user
						aController.register(context, name, email, regId,imei, interest1, interest2, interest3); 
						
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						Dialog.dismiss();
						mRegisterTask = null;
						
						finish();
					}

				};
				
				// execute AsyncTask
				mRegisterTask.execute(null, null, null);
			}
		}
	}		

	
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
