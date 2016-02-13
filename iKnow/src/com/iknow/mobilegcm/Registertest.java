package com.iknow.mobilegcm;

import java.util.ArrayList;
import java.util.List;

import com.androidexample.mobilegcm.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Registertest extends FragmentActivity{
	
	ViewPager viewPager = null;
	List <View>list = new ArrayList<View>();
	String name="";
	String email="";
	int checkSum = 0;
	String interest1="";
	String interest2="";
	String interest3="";
	TextView appTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_registertest);
		
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
	        appTitle = (TextView)findViewById(R.id.title);
	        TextPaint tp = appTitle.getPaint(); 
	        tp.setFakeBoldText(true);
	        appTitle.setText("�]��");
		
		
		DBAdapter.init(this);
		//Get Global Controller Class object
		final Controller aController = (Controller) getApplicationContext();
		
		// Check if Internet Connection present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(Registertest.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			
			// stop executing code by return
			return;
		}
		
		
		viewPager = (ViewPager)findViewById(R.id.pager);
		FragmentManager fragmentManager = getSupportFragmentManager();
		
	/*	View page1 = getLayoutInflater().inflate(R.layout.fragment_registerone,null);
		Button send = (Button)page1.findViewById(R.id.btnRegister);
		send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getApplicationContext(), "registering", Toast.LENGTH_LONG).show();
			}
		}
				);
		*/
		list.add(getLayoutInflater().from(this).inflate(R.layout.fragment_registerone,null));
		list.add(getLayoutInflater().from(this).inflate(R.layout.fragment_registertwo,null));
		viewPager.setAdapter(new MyAdapter(fragmentManager));
		
		
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0)
				{
				View v = list.get(arg0);
                //
               final TextView txtName = (TextView)findViewById(R.id.txtName);
               final TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
               
               name = txtName.getText().toString(); 
       		   email = txtEmail.getText().toString();
				}
				else if(arg0 == 1 )
				{
					View v = list.get(arg0);
					
					final Button iv = (Button)findViewById(R.id.btnRegister);
					
					iv.setOnClickListener(new View.OnClickListener(){
	                    @Override
	                    public void onClick(View v) {
	                    	
	                    	
	               //     	String name = txtName.getText().toString(); 
	        		// email = txtEmail.getText().toString(); 
	        				//Toast.makeText(getApplicationContext(), name+" "+email, Toast.LENGTH_LONG).show();
	        				// Check if user filled the form
	        				if(name.trim().length() > 0)
	        				{
	        					if(email.trim().length() > 0 && email.matches("[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+") ){
	        						
	        					Toast.makeText(getApplicationContext(), "registering", Toast.LENGTH_LONG).show();
	        					// Launch Main Activity
	        					Intent i = new Intent(getApplicationContext(), MainActivity.class);
	        					
	        					// Registering on server					
	        					// Sending registraiton details to MainActivity
	        					i.putExtra("name", name);
	        					i.putExtra("email", email);
	        					Log.v("Interest",interest1+" "+interest2+" "+interest3);
	        					i.putExtra("interest1", interest1);
	        					i.putExtra("interest2", interest2);
	        					i.putExtra("interest3", interest3);
	        					startActivity(i);
	        					finish();
	        					}
	        					else
	        					{
	        						aController.showAlertDialog(Registertest.this, 
	        							"Your Email Invalid!", 
	        							"Please enter valid e-mail", 
	        							false);
	        					}
	        				}
	        				else{
	        						aController.showAlertDialog(Registertest.this, 
	        							"Your Name Error!", 
	        							"Please enter valid name", 
	        							false);	
	        				}
	                    	
	                    	
	                    	
	                    }
				});
				}
				
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if(arg0==0)
				{
					View v = list.get(arg0);
					 final TextView txtName = (TextView)findViewById(R.id.txtName);
		              final TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
				  name = txtName.getText().toString(); 
	       		  email = txtEmail.getText().toString();
				}	
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}	
	
	public void onCheckBoxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkComputer:
	            if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "�Ƽ�";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "�Ƽ�";
	            		}
	            		else
	            		{
	            			interest3 = "�Ƽ�";
	            		}
	            	}
	            	checkSum++;

	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("�Ƽ�"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("�Ƽ�"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("�Ƽ�"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	                // Remove the meat
	            break;
	        case R.id.checkLife:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "����";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "����";
	            		}
	            		else
	            		{
	            			interest3 = "����";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("����"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("����"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("����"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }	            break;
	        case R.id.checkBusiness:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "�r��";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "�r��";
	            		}
	            		else
	            		{
	            			interest3 = "�r��";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("�r��"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("�r��"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("�r��"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);	            
	            	}	           
	        	   break;
	        case R.id.checkEducation:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "�W�g";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "�W�g";
	            		}
	            		else
	            		{
	            			interest3 = "�W�g";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("�W�g"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("�W�g"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("�W�g"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            break;
	        case R.id.checkEmotion:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "����";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "����";
	            		}
	            		else
	            		{
	            			interest3 = "����";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("����"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("����"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("����"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }	            break;
	        case R.id.checkHealth:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "����";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "����";
	            		}
	            		else
	            		{
	            			interest3 = "����";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("����"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("����"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("����"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }	            break;
	        case R.id.checkLiterature:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "��ʳ";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "��ʳ";
	            		}
	            		else
	            		{
	            			interest3 = "��ʳ";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("��ʳ"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("��ʳ"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("��ʳ"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            break;
	        case R.id.checkSports:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "�\��";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "�\��";
	            		}
	            		else
	            		{
	            			interest3 = "�\��";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("�\��"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("�\��"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("�\��"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }	            break;
	        case R.id.checkEntertainment:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "�ʘ�";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "�ʘ�";
	            		}
	            		else
	            		{
	            			interest3 = "�ʘ�";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("�ʘ�"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("�ʘ�"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("�ʘ�"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            break;
	        case R.id.checkDigital:
	        	if (checked)
	            {
	            	if(checkSum<3)
	            	{
	            		if(interest1.equals(""))
	            		{
	            			interest1 = "���";
	            		}
	            		else if(interest2.equals(""))
	            		{
	            			interest2 = "���";
	            		}
	            		else
	            		{
	            			interest3 = "���";
	            		}
	            	}
	            	checkSum++;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            else
	            {
	            	if(interest1.equals("���"))
            		{
            			interest1 = "";
            		}
            		else if(interest2.equals("���"))
            		{
            			interest2 = "";
            		}
            		else if(interest3.equals("���"))
            		{
            			interest3 = "";
            		}
	            	checkSum--;
	            	Log.v("msg",interest1+" "+interest2+" "+interest3);
	            }
	            break;
	        	// TODO: Veggie sandwich
	            
	    }
	}
}




class MyAdapter extends FragmentPagerAdapter
{
	public MyAdapter(FragmentManager fm)
	{
		super(fm);
	}
	@Override
	public Fragment getItem(int i)
	{
		Fragment fragment = null;
		if(i==0)
		{
			fragment = new RegisterFragmentOne();
		}
		if(i==1)
		{
			fragment = new RegisterFragmentTwo();
		}
		return fragment;
	}
	@Override
	public int getCount()
	{
		return 2;
	}
}
