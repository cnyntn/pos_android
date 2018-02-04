package com.example.kkkk.helloworld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.DemoHelper;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.db.DemoDBManager;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.example.kkkk.helloworld.util.StringUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity {
    @BindView(R.id.login_ck)
    CheckBox loginCk;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.forget_password)
    TextView findBtn;
    Intent intent;
    @BindView(R.id.username)
    MaterialEditText username;
    @BindView(R.id.password)
    MaterialEditText password;
    private boolean isRemenber = false;
    private ProgressDialog mDialog;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.back, R.id.login_ck, R.id.btn_login, R.id.forget_password})
    void loginClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String mobile = username.getText().toString();
                String pwd = password.getText().toString();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    Toast.makeText(loginActivity.this, "请输入账号!", Toast.LENGTH_SHORT).show();
                } else if (StringUtil.isNullOrEmpty(pwd)) {
                    Toast.makeText(loginActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                } else {
                    if (isRemenber) {
                        App.getInstance().setName(mobile);
                        App.getInstance().setPwd(pwd);
                    }
                    register(mobile, pwd);
                }
                break;
            case R.id.forget_password:
                intent = new Intent(loginActivity.this, findpwdActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void initView() {
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
        String name = App.getInstance().getName();
        String pwd = App.getInstance().getPwd();
        if (!StringUtil.isNullOrEmpty(name) && !StringUtil.isNullOrEmpty(pwd)) {
            loginCk.isChecked();
            username.setText(name);
            password.setText(pwd);
        } else {
            username.setText("");
            password.setText("");
        }
        loginCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRemenber = true;
                }
            }
        });
    }

    private void Login(final String mobile, final String pwd) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(mobile);
        EMClient.getInstance().login(mobile, pwd, new EMCallBack() {

            @Override
            public void onSuccess() {


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        App.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                /*Intent intent = new Intent(loginActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();*/
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                RetrofitHttp.getRetrofit(builder.build()).login(getJsonStr(mobile, pwd))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response) {
                                mDialog.dismiss();
                                try {
                                    String result = response.body().string();
                                    JSONObject jsonObject = JSON.parseObject(result);
                                    String msg = jsonObject.getString("message");
                                    String Token = jsonObject.getString("data");
                                    Toast.makeText(loginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("code").equals("failure")) {
                                        return;
                                    } else {

                                        App.getInstance().setMyToken(Token);
                                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                mDialog.dismiss();
                                Toast.makeText(loginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private RequestBody getJsonStr(String mobile, String pwd) {
        JSONObject object = new JSONObject();
        object.put("username", mobile);
        object.put("password", pwd);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), object.toJSONString());
        return body;
    }

    public void register(final String mobile, final String pwd) {
        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(pwd)) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(mobile, pwd);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // save current user
                                DemoHelper.getInstance().setCurrentUserName(mobile);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                Login(mobile,pwd);
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                    Login(mobile,pwd);
                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }
}
