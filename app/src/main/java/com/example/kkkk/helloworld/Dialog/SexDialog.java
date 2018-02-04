package com.example.kkkk.helloworld.Dialog;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kkkk.helloworld.R;

/**
 * Created by Administrator on 2017/11/7.
 */
public class SexDialog extends Dialog implements View.OnClickListener {

    TextView sex_1;
    TextView sex_2;
    TextView canel;
    Intent intent;
    public static final String BROADCAST_ACTION_SEX = "com.example.kkk.sex";

    public SexDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
   protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.dialog_sex);
         //按空白处不能取消动画
         setCanceledOnTouchOutside(true);

         //初始化界面控件
         initView();
         //初始化界面数据
         initData();
         //初始化界面控件的事件
         initEvent();

     }

    /**
     * 初始化界面的确定和取消监听器
     */
   private void initEvent() {

     }
    /**
     * 初始化界面控件的显示数据
.     */
    private void initData() {

       }

    /**
.     * 初始化界面控件
.     */
    private void initView() {
        sex_1= (TextView) findViewById(R.id.sex1);
        sex_2= (TextView) findViewById(R.id.sex2);
        canel= (TextView) findViewById(R.id.cannel);
        sex_1.setOnClickListener(this);
        sex_2.setOnClickListener(this);
        canel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sex1:
                intent = new Intent();
                intent.setAction(BROADCAST_ACTION_SEX);
                intent.putExtra("sex", "男");
                getContext().sendBroadcast(intent);
                dismiss();
            break;
            case R.id.sex2:
                intent = new Intent();
                intent.setAction(BROADCAST_ACTION_SEX);
                intent.putExtra("sex", "女");
                getContext().sendBroadcast(intent);
                dismiss();
                break;
            case R.id.cannel:
                dismiss();
                break;
        }
    }

}

