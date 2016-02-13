package com.iknow.mobilegcm;

import com.androidexample.mobilegcm.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class RegisterFragmentOne extends Fragment{
	TextView notifyslide;
	//final TextView txtName = (TextView)findViewById(R.id.txtName);
  //  final TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
	private void startFlick( View view ){

        if( null == view ){

            return;

        }

        Animation alphaAnimation = new AlphaAnimation( 1, 0 );

        alphaAnimation.setDuration( 800 );

        alphaAnimation.setInterpolator( new LinearInterpolator( ) );

        alphaAnimation.setRepeatCount( Animation.INFINITE );

        alphaAnimation.setRepeatMode( Animation.REVERSE );

        view.startAnimation( alphaAnimation );

    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		
		
		
		return inflater.inflate(R.layout.fragment_registerone, container, false);
	}
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		notifyslide = (TextView)this.getView().findViewById(R.id.notifyslide);
        startFlick(notifyslide);
	}
}
