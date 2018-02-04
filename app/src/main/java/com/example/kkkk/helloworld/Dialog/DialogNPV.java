package com.example.kkkk.helloworld.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkkk.helloworld.Activity.userinfoActivity;
import com.example.kkkk.helloworld.R;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;


/**
 * Created by carbs on 2016/7/11.
 */

public class DialogNPV extends Dialog implements View.OnClickListener,
        NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener{

    private static final String TAG = "picker";
    private static final String BROADCAST_ACTION_AGE = "com.example.kkk.age";

    private Context mContext;
    private NumberPickerView mNumberPickerView;
    private String[] mDisplayValues;
    TextView age;
    TextView yes;
    TextView no;
    public DialogNPV(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_npv);
        age= (TextView) findViewById(R.id.age);
        mNumberPickerView = (NumberPickerView) this.findViewById(R.id.picker);
        mNumberPickerView.setOnScrollListener(this);
        mNumberPickerView.setOnValueChangedListener(this);
        mDisplayValues = mContext.getResources().getStringArray(R.array.minute_display);
//        mNumberPickerView.refreshByNewDisplayedValues(mDisplayValues);
        yes= (TextView) findViewById(R.id.yes);
        no= (TextView) findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    // this method should be called after onCreate()
    public void initNPV(){
        mNumberPickerView.refreshByNewDisplayedValues(mDisplayValues);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                getCurrentContent();
                break;
            case R.id.no:
                dismiss();
                break;
        }
    }

    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {
        Log.d(TAG, "onScrollStateChange : " + scrollState);
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        /*String[] content = picker.getDisplayedValues();
        if (content != null) {
            Log.d(TAG,"onValueChange content : " + content[newVal - picker.getMinValue()]);
            Toast.makeText(mContext.getApplicationContext(), "oldVal : " + oldVal + " newVal : " + newVal + "\n" +
                    mContext.getString(R.string.picked_content_is) + content[newVal - picker.getMinValue()], Toast.LENGTH_SHORT)
                    .show();
        }*/
    }
    private void getCurrentContent(){
        String[] content = mNumberPickerView.getDisplayedValues();
       // if (content != null)
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_AGE);
        intent.putExtra("age", content[mNumberPickerView.getValue() - mNumberPickerView.getMinValue()]);
        getContext().sendBroadcast(intent);

        dismiss();
    }

}