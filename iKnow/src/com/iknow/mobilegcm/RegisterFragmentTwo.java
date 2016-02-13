package com.iknow.mobilegcm;

import com.androidexample.mobilegcm.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;


public class RegisterFragmentTwo extends Fragment{
	//CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10 ;
	Button btnRegister;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		return inflater.inflate(R.layout.fragment_registertwo, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
	//	checkBox2 = (CheckBox)this.getView().findViewById(R.id.checkLife);
	//	Drawable []drawables = new Drawable[4];
		btnRegister = (Button)this.getView().findViewById(R.id.btnRegister);
		btnRegister.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(event.getAction() == MotionEvent.ACTION_UP){ 
					btnRegister.setScaleX(1.0f);
					btnRegister.setScaleY(1.0f);
					
                }   
                if(event.getAction() == MotionEvent.ACTION_DOWN){  	  
                	btnRegister.setScaleX(0.9f);
                	btnRegister.setScaleY(0.9f);
                }  
				
                return false;
				
			}
		});
		//drawables = checkBox2.getCompoundDrawables();  //此处取的是android:drawableTop的图片
       // drawables[1].setBounds(0, 0, 120, 120);
    //    Resources res = this.getResources();
      //  Drawable myImage = res.getDrawable(R.drawable.life1);
	//	checkBox2.setCompoundDrawablesWithIntrinsicBounds(null, myImage, null, null);
		/*checkBox1 = (CheckBox)this.getView().findViewById(R.id.checkComputer);
		Drawable []drawables = new Drawable[4];
        drawables = checkBox1.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox1.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
        checkBox2 = (CheckBox)this.getView().findViewById(R.id.checkLife);
		drawables = new Drawable[4];
        drawables = checkBox2.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox2.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
        checkBox3 = (CheckBox)this.getView().findViewById(R.id.checkHealth);
		drawables = new Drawable[4];
        drawables = checkBox3.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox3.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        */
     //   checkBox4 = (CheckBox)this.getView().findViewById(R.id.checkSports);
	//	drawables = new Drawable[4];
    //    drawables = checkBox4.getCompoundDrawables();  //此处取的是android:drawableTop的图片
    //    drawables[1].setBounds(0, 0, 120, 120);
     //   checkBox4.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        /*
        checkBox5 = (CheckBox)this.getView().findViewById(R.id.checkDigital);
		drawables = new Drawable[4];
        drawables = checkBox5.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox5.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
        checkBox6 = (CheckBox)this.getView().findViewById(R.id.checkBusiness);
		drawables = new Drawable[4];
        drawables = checkBox6.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox6.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        */
      //  checkBox7 = (CheckBox)this.getView().findViewById(R.id.checkEducation);
		//drawables = new Drawable[4];
     //   drawables = checkBox7.getCompoundDrawables();  //此处取的是android:drawableTop的图片
     //   drawables[1].setBounds(0, 0, 120, 120);
     //   checkBox7.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        /*
        checkBox8 = (CheckBox)this.getView().findViewById(R.id.checkLiterature);
		drawables = new Drawable[4];
        drawables = checkBox8.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox8.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
        checkBox9 = (CheckBox)this.getView().findViewById(R.id.checkEntertainment);
		drawables = new Drawable[4];
        drawables = checkBox9.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 120, 120);
        checkBox9.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        */
      //  checkBox10 = (CheckBox)this.getView().findViewById(R.id.checkEmotion);
	//	drawables = new Drawable[4];
     //   drawables = checkBox10.getCompoundDrawables();  //此处取的是android:drawableTop的图片
       // drawables[1].setBounds(0, 0, 120, 120);
      
        
	}
}
