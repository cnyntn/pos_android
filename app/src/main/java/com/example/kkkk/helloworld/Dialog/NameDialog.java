package com.example.kkkk.helloworld.Dialog;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kkkk.helloworld.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Administrator on 2017/11/7.
 */
public class NameDialog extends Dialog implements View.OnClickListener {

    MaterialEditText name;
    Button yes;
    Intent intent;
    String nn;
    public static final String BROADCAST_ACTION_NAME = "com.example.kkk.name";

    public NameDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
   protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.dialog_name);
         //按空白处不能取消动画
         setCanceledOnTouchOutside(true);

         //初始化界面控件
         initView();

     }

    /**
.     * 初始化界面控件
.     */
    private void initView() {
        name= (MaterialEditText) findViewById(R.id.name);
        yes= (Button) findViewById(R.id.yes);
        name.setOnClickListener(this);
        yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                String name_=name.getText().toString();
                name.setText("");
                intent = new Intent();
                intent.setAction(BROADCAST_ACTION_NAME);
                intent.putExtra("name",name_);
                getContext().sendBroadcast(intent);
                dismiss();
                break;
        }
    }

}

