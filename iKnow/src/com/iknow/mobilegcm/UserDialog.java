package com.iknow.mobilegcm;


import com.androidexample.mobilegcm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UserDialog extends Dialog{
	public interface OnCustomDialogListener{
        public void back(String name);
}

private String name;

EditText etName;
ImageView userimg;
public UserDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        
}

@Override
protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
       userimg = (ImageView)findViewById(R.id.dialoguserimg);
      // dialoguser
        
      //  setTitle(name); 
        
}


}

