package com.example.kkkk.helloworld.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkkk.helloworld.Dialog.DialogNPV;
import com.example.kkkk.helloworld.Dialog.NameDialog;
import com.example.kkkk.helloworld.Dialog.SexDialog;
import com.example.kkkk.helloworld.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class userinfoActivity extends BaseAppActivity implements View.OnClickListener {
    public static final String BROADCAST_ACTION_AGE = "com.example.kkk.age";
    public static final String BROADCAST_ACTION_NAME = "com.example.kkk.name";
    public static final String BROADCAST_ACTION_SEX = "com.example.kkk.sex";
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PRCIURE = 2;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.icon)
    CircleImageView icon;
    private BroadcastReceiver mBroadcastReceiver;
    IntentFilter intentFilter;
    private DialogNPV mDialogNPV;
    private SexDialog sexDialog;
    private NameDialog nameDialog;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.selectage)
    LinearLayout selectAge;
    @BindView(R.id.selectsex)
    LinearLayout selectSex;
    @BindView(R.id.selectname)
    LinearLayout selectname;
    @BindView(R.id.selecticon)
    LinearLayout selecticon;
    Uri tempUri;
    Bitmap mBitmap;
    Intent data;
    String age_;
    String sex_;
    String name_;
    private boolean nameTag = false;   //广播接受者标识
    private boolean sexTag = false;   //广播接受者标识
    private boolean ageTag = false;   //广播接受者标识
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.back, R.id.selectage, R.id.selectsex, R.id.selectname, R.id.selecticon})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                //Intent intent=new Intent(userinfoActivity.this,MainActivity.class);
                //startActivity(intent);
                getBackData();
                finish();
                break;
            case R.id.selectage:
                registerReceiver(BROADCAST_ACTION_AGE);
                showNPVDialog();
                break;
            case R.id.selectsex:
                registerReceiver(BROADCAST_ACTION_SEX);
                showSexDialog();
                break;
            case R.id.selectname:
               registerReceiver(BROADCAST_ACTION_NAME);
                showNameDialog();
                break;
            case R.id.selecticon:
                showChoosePicDialog();
                break;
            default:
                break;
        }
    }

    //代码中动态注册广播
    private void registerReceiver(String action) {
        switch (action){
            case BROADCAST_ACTION_AGE:
                if (!nameTag) {   //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                    intentFilter = new IntentFilter();
                    intentFilter.addAction(action);
                    this.registerReceiver(mBroadcastReceiver, intentFilter);
                }
                break;
            case BROADCAST_ACTION_NAME:
                if (!sexTag) {   //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                    intentFilter = new IntentFilter();
                    intentFilter.addAction(action);
                    this.registerReceiver(mBroadcastReceiver, intentFilter);
                }
                break;
            case BROADCAST_ACTION_SEX:
                if (!sexTag) {   //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                    intentFilter = new IntentFilter();
                    intentFilter.addAction(action);
                    this.registerReceiver(mBroadcastReceiver, intentFilter);
                }
                break;
        }

    }

    //注销广播
    private void unregisterReceiver() {
        if (nameTag) {   //判断广播是否注册
            nameTag = false;   //Tag值 赋值为false 表示该广播已被注销
            this.unregisterReceiver(mBroadcastReceiver);   //注销广播
        }
        if (sexTag) {   //判断广播是否注册
            sexTag = false;   //Tag值 赋值为false 表示该广播已被注销
            this.unregisterReceiver(mBroadcastReceiver);   //注销广播
        }
        if (ageTag) {   //判断广播是否注册
            ageTag = false;   //Tag值 赋值为false 表示该广播已被注销
            this.unregisterReceiver(mBroadcastReceiver);   //注销广播
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastReceiver = new mBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void showNPVDialog() {
        if (mDialogNPV == null) {
            mDialogNPV = new DialogNPV(this);
        }
        if (mDialogNPV.isShowing()) {
            mDialogNPV.dismiss();
        } else {
            mDialogNPV.setCancelable(true);
            mDialogNPV.setCanceledOnTouchOutside(true);
            mDialogNPV.show();
            Window window = mDialogNPV.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.bottom_menu_animation);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = mDialogNPV.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            mDialogNPV.getWindow().setAttributes(p); //设置生效
            mDialogNPV.initNPV();
        }
    }

    private void showSexDialog() {
        if (sexDialog == null) {
            sexDialog = new SexDialog(this);
        }
        if (sexDialog.isShowing()) {
            sexDialog.dismiss();
        } else {
            sexDialog.setCancelable(true);
            sexDialog.setCanceledOnTouchOutside(true);
            sexDialog.show();
            Window window = sexDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.bottom_menu_animation);
        }

    }

    private void showNameDialog() {
        if (nameDialog == null) {
            nameDialog = new NameDialog(this);
        }
        if (nameDialog.isShowing()) {
            nameDialog.dismiss();
        } else {
            nameDialog.setCancelable(true);
            nameDialog.setCanceledOnTouchOutside(true);
            nameDialog.show();
            Window window = nameDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.bottom_menu_animation);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = nameDialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            nameDialog.getWindow().setAttributes(p); //设置生效
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sex1:
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                break;
            case R.id.sex2:
                Intent openCamerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));
                openCamerIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCamerIntent, TAKE_PICTURE);
                break;
            case R.id.cannel:
                alertDialog.dismiss();
                break;
        }
    }

    public class mBroadcastReceiver extends BroadcastReceiver {

        //接收到广播后自动调用该方法
        @Override
        public void onReceive(Context context, Intent intent) {
            //写入接收广播后的操作
            switch (intent.getAction()) {
                case BROADCAST_ACTION_AGE:
                    age_ = intent.getExtras().getString("age");
                    age.setText(age_);
                    break;
                case BROADCAST_ACTION_NAME:
                    name_ = intent.getExtras().getString("name");
                    if (!name_.equals("")) {
                        name.setText(name_);
                    } else {
                        Toast.makeText(context, "昵称为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BROADCAST_ACTION_SEX:
                    sex_ = intent.getExtras().getString("sex");
                    sex.setText(sex_);
                    break;
            }
        }
    }

    protected void showChoosePicDialog() {
        alertDialog  = new AlertDialog.Builder(userinfoActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_sex);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_menu_animation);
        TextView tv_man = (TextView) window.findViewById(R.id.sex1);
        tv_man.setText("相册");
        TextView tv_woman = (TextView) window.findViewById(R.id.sex2);
        tv_woman.setText("拍照");
        TextView tv_cannel = (TextView) window.findViewById(R.id.cannel);
        tv_cannel.setText("取消");
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
        tv_cannel.setOnClickListener(this);
    }
    /**
     * 裁剪图片
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("err", "cutImage: ");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PRCIURE);
    }

    /**
     * 显示图片
     */
    protected void setImageView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            icon.setImageBitmap(mBitmap);
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getBackData();
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==userinfoActivity.RESULT_OK){
            switch (requestCode){
                case TAKE_PICTURE:
                    cutImage(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData());
                    break;
                case CROP_SMALL_PRCIURE:
                    setImageView(data);
            }
            alertDialog.dismiss();
        }
        //this.data=data;
    }
    protected void getBackData(){
        Bitmap bitmap = null;
        bitmap=mBitmap;
        Bundle backData= new Bundle();
        backData.putParcelable("icon",bitmap);
        backData.putString("age",age.getText().toString());
        backData.putString("name",name.getText().toString());
        backData.putString("sex",sex.getText().toString());
        Intent intent = new Intent();
        intent.putExtras(backData);
        setResult(0,intent);
    }
}
