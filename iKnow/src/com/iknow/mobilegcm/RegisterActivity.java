package com.iknow.mobilegcm;

import com.androidexample.mobilegcm.R;
import com.iknow.mobilegcm.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	// UI elements
	EditText txtName; 
	EditText txtEmail;
	
	// Register button
	Button btnRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************* Intialize Database *************/
		DBAdapter.init(this);
		
		setContentView(R.layout.activity_register);
		
		//Get Global Controller Class object
		final Controller aController = (Controller) getApplicationContext();
		
		// Check if Internet Connection present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (Config.YOUR_SERVER_URL == null || 
			Config.GOOGLE_SENDER_ID == null || 
			Config.YOUR_SERVER_URL.length() == 0 || 
			Config.GOOGLE_SENDER_ID.length() == 0) 
		{
			
			// GCM sernder id / server url is missing
			aController.showAlertDialog(RegisterActivity.this, 
					"Configuration Error!",
					"Please set your Server URL and GCM Sender ID", 
					false);
			
			// stop executing code by return
			 return;
		}
		
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		// Click event on Register button
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {  
				// Get data from EditText 
				String name = txtName.getText().toString(); 
				String email = txtEmail.getText().toString(); 
				
				// Check if user filled the form
				if(name.trim().length() > 0)
				{
					if(email.trim().length() > 0 && email.matches("[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+") ){
					
					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					
					// Registering on server					
					// Sending registraiton details to MainActivity
					i.putExtra("name", name);
					i.putExtra("email", email);
					startActivity(i);
					finish();
					}
					else
					{
						aController.showAlertDialog(RegisterActivity.this, 
							"Your Email Invalid!", 
							"Please enter valid e-mail", 
							false);
					}
				}
				else{
						aController.showAlertDialog(RegisterActivity.this, 
							"Your Name Error!", 
							"Please enter valid name", 
							false);	
				}
			}
		});
	}

}
