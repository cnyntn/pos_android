package com.example.kkkk.helloworld.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.rollviewAdapter;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends BaseAppActivity {

    @BindView(R.id.roll_view_pager)
    RollPagerView rollViewPager;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.merchantname)
    TextView merchantname;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.btn_do)
    Button btnDo;
    private RollPagerView mRollViewPager;
    @BindView(R.id.back)
    ImageButton back;
    String uuid;
    ProgressDialog mDialog;
    public String[] status_0 = {"未开始", "进行中","审核通过"};
    String address_;
    String latlng_;
    public int[] imgs_0= {R.drawable.status_bg_green, R.drawable.status_bg_orange,R.drawable.status_bg_blue};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");
        getTaskDetail(uuid);
        btnDo = (Button) findViewById(R.id.btn_do);
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

        //设置播放时间间隔
        mRollViewPager.setPlayDelay(2000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new rollviewAdapter(mRollViewPager));

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        mRollViewPager.setHintView(new IconHintView(this, R.drawable.dot_1, R.drawable.dot_2, 24));



    }

    private void initView() {

        mDialog = new ProgressDialog(TaskDetailActivity.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
    }

    @OnClick({R.id.back,R.id.btn_do})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_do:
                Intent intent = new Intent(TaskDetailActivity.this, DoTaskActivity.class);
                intent.putExtra("uuid",uuid);
                intent.putExtra("address",address_);
                intent.putExtra("latlng",latlng_);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getTaskDetail(String uuid) {
        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).TaskDetail(uuid)
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
                                String merchant_ = json.getString("merchant");
                                JSONObject merchant = JSON.parseObject(merchant_);
                                type.setText(json.getString("type"));
                                remark.setText(json.getString("remark"));
                                setStatus(json.getString("status"));
                                merchantname.setText(merchant.getString("name"));
                                address.setText(merchant.getString("address"));
                                name.setText(merchant.getString("linkerName"));
                                mobile.setText(merchant.getString("linkerMobile"));

                                JSONObject locationInfo_ = JSON.parseObject(merchant.getString("locationInfo"));
                                String coordinates_=locationInfo_.getString("coordinates");
                                address_=merchant.getString("address");
                                latlng_=coordinates_;
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

    private void setStatus(String status_){
        switch (status_){
            case "1":
                status.setText(status_0[0]);
                status.setBackgroundResource(imgs_0[0]);
                break;
            case "2":
                status.setText(status_0[1]);
                status.setBackgroundResource(imgs_0[1]);
                break;
            case "3":
                status.setText(status_0[2]);
                status.setBackgroundResource(imgs_0[2]);
                break;
            default:
                status.setText("未知");
                status.setBackgroundResource(imgs_0[0]);
                break;
        }
    }

}
