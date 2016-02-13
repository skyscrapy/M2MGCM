package com.iknow.mobilegcm;

import com.androidexample.mobilegcm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserIntroDialog extends Dialog{
	public interface OnCustomDialogListener{
        public void back(String name);
}

private String name;
private OnCustomDialogListener customDialogListener;
EditText etName;

public UserIntroDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
}

@Override
protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
       
        setTitle(name); 
        etName = (EditText)findViewById(R.id.edit);
        Button ensureBtn = (Button) findViewById(R.id.editensure);
        Button cancelBtn = (Button) findViewById(R.id.editcancel);
        ensureBtn.setOnClickListener(ensureListener);
        cancelBtn.setOnClickListener(cancelListener);
}

private View.OnClickListener ensureListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
                customDialogListener.back(String.valueOf(etName.getText()));
            UserIntroDialog.this.dismiss();
        }
};

private View.OnClickListener cancelListener = new View.OnClickListener() {
    
    @Override
    public void onClick(View v) {
        //    customDialogListener.back(String.valueOf(etName.getText()));
        UserIntroDialog.this.dismiss();
    }
};
}
