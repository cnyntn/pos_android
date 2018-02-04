package com.example.kkkk.helloworld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.http.RetrofitHttp;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class noticedetailActivity extends BaseAppActivity {
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;
    ProgressDialog mDialog;
    String uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");
        initView();
        getReadDetail(uuid);
    }

    private void initView() {

        mDialog = new ProgressDialog(noticedetailActivity.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
    }

    @OnClick({R.id.back})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getReadDetail(String uuid){
        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).ReadDetail(App.getInstance().getMyToken(),uuid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        mDialog.dismiss();
                        try {
                            String result = response.body().string();
                            JSONObject jsonObject = JSON.parseObject(result);
                            String msg = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            if (jsonObject.getString("code").equals("failure")) {
                                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                JSONObject json = JSON.parseObject(data);
                                String user_ = json.getString("createUser");
                                JSONObject user = JSON.parseObject(user_);
                                name.setText(user.getString("nickname"));
                                title.setText(json.getString("title"));
                                time.setText(json.getString("createTime"));
                                content.setText(json.getString("content"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(getBaseContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
