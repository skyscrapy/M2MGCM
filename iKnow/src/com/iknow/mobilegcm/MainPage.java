package com.iknow.mobilegcm;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.androidexample.mobilegcm.R;


public class MainPage extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    private RadioGroup rgs;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    private long mExitTime;
    public String hello = "hello ";
    TextView appTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main_page);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        appTitle = (TextView)findViewById(R.id.title);
        TextPaint tp = appTitle.getPaint(); 
        tp.setFakeBoldText(true);
        fragments.add(new Recommend());
        fragments.add(new Topic());
        fragments.add(new SendPushNotification());
        fragments.add(new UserInfo());
    //    fragments.add(new UserInfo());
   //     fragments.add(new TabBFm());
    ////    fragments.add(new TabCFm());
    //    fragments.add(new TabDFm());
     //   fragments.add(new TabEFm());


        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
RadioButton tab_rb_a = (RadioButton)findViewById(R.id.tab_rb_a);
        Drawable []drawables = new Drawable[4];
        drawables = tab_rb_a.getCompoundDrawables();  //此处取的是android:drawableTop的图片
        drawables[1].setBounds(0, 0, 80, 80);
        tab_rb_a.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
RadioButton tab_rb_b = (RadioButton)findViewById(R.id.tab_rb_b);
        
drawables = tab_rb_b.getCompoundDrawables();  //此处取的是android:drawableTop的图片
drawables[1].setBounds(0, 0, 80, 80);
        tab_rb_b.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
RadioButton tab_rb_c = (RadioButton)findViewById(R.id.tab_rb_c);
        
drawables = tab_rb_c.getCompoundDrawables();  //此处取的是android:drawableTop的图片
drawables[1].setBounds(0, 0, 80, 80);
        tab_rb_c.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
RadioButton tab_rb_d = (RadioButton)findViewById(R.id.tab_rb_d);
        
drawables = tab_rb_d.getCompoundDrawables();  //此处取的是android:drawableTop的图片
drawables[1].setBounds(0, 0, 80, 80);
        tab_rb_d.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
        //fragments.get(3).getView().findViewById(R.id.editanswer);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
                if(index==0)
                {
                	appTitle.setText("iKnow");
                }
                else if(index==2)
                {
                	appTitle.setText("分類");
                }
                else if(index==4)
                {
                	appTitle.setText("提問");
                }
                else if(index==6)
                {
                	appTitle.setText("用戶");
                }
            }
        });
        

    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Object mHelperUtils;
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                } else {
                        finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
}

}