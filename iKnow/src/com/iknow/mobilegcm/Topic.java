package com.iknow.mobilegcm;

import java.util.ArrayList;

import com.androidexample.mobilegcm.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Topic extends Fragment{
	
	Resources resources;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabNew, tvTabHot;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    public final static int num = 2 ; 
    Fragment home1;
    Fragment home2;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_topic, null);
		resources = getResources();
     //   InitWidth(view);
        InitTextView(view);
        InitViewPager(view);
        TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
        tvTabHot.setTextColor(resources.getColor(R.color.deepsilver));
        animation.setFillAfter(true);
        animation.setDuration(300);
     //   ivBottomLine.startAnimation(animation);
		return view;
	}
	
	  private void InitTextView(View parentView) {
	        tvTabNew = (TextView) parentView.findViewById(R.id.tv_tab_1);
	        tvTabHot = (TextView) parentView.findViewById(R.id.tv_tab_2);

	        tvTabNew.setOnClickListener(new MyOnClickListener(0));
	        tvTabHot.setOnClickListener(new MyOnClickListener(1));
	    }

	    private void InitViewPager(View parentView) {
	        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
	        fragmentsList = new ArrayList<Fragment>();

	        home1 = new Topics1();
	        home2 = new Topics2();

	        fragmentsList.add(home1);
	        fragmentsList.add(home2);
	        
	        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	        mPager.setCurrentItem(0);
	        
	    }

	    private void InitWidth(View parentView) {
	    //    ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
	        bottomLineWidth = ivBottomLine.getLayoutParams().width;
	        DisplayMetrics dm = new DisplayMetrics();
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	        int screenW = dm.widthPixels;
	        offset = (int) ((screenW / num - bottomLineWidth) / 2);
	        int avg = (int) (screenW / num);
	        position_one = avg + offset;
	        
	        
	    }

	    public class MyOnClickListener implements View.OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            mPager.setCurrentItem(index);
	        }
	    };

	    public class MyOnPageChangeListener implements OnPageChangeListener {

	        @Override
	        public void onPageSelected(int arg0) {
	            Animation animation = null;
	            switch (arg0) {
	            case 0:
	                if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, offset, 0, 0);
	                    tvTabHot.setTextColor(resources.getColor(R.color.deepsilver));
	                   
	                } 
	                tvTabNew.setTextColor(resources.getColor(R.color.red));
	              
	               
	                break;
	            case 1:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(offset, position_one, 0, 0);
	                    tvTabNew.setTextColor(resources.getColor(R.color.deepsilver)); 
	                } 
	                tvTabHot.setTextColor(resources.getColor(R.color.red));
	                
	                break;
	            }
	            currIndex = arg0;
	            animation.setFillAfter(true);
	            animation.setDuration(300);
	      //      ivBottomLine.startAnimation(animation);
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    }

}
