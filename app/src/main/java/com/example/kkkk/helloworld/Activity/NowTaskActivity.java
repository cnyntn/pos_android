package com.example.kkkk.helloworld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.nowtaskAdapter;
import com.example.kkkk.helloworld.adapter.readGridAdapter;
import com.example.kkkk.helloworld.http.RetrofitHttp;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class NowTaskActivity extends BaseAppActivity {
    GridView gridView;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.errorview)
    TextView errorview;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowtask);
        ButterKnife.bind(this);
        gridView = (GridView) findViewById(R.id.grid_nowtask);
        initView();
        getTaskList("1","10");
    }

    private void initView() {
        mDialog = new ProgressDialog(NowTaskActivity.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);

    }
    @OnClick({R.id.back})
    void click(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getTaskList(String page,String size){
        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).TaskList(App.getInstance().getMyToken(),page,size)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        mDialog.dismiss();
                        try {
                            String result = response.body().string();
                            JSONObject jsonObject = JSON.parseObject(result);
                            String msg = jsonObject.getString("message");
                            String data_ = jsonObject.getString("data");
                            JSONObject data = JSON.parseObject(data_);
                            if (jsonObject.getString("code").equals("failure")) {
                                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                if (data.getString("list").equals("[]")){
                                    errorview.setVisibility(View.VISIBLE);
                                }else {
                                    loadList(data_);
                                }
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

    private void loadList(final String data){
        final nowtaskAdapter gridadapter=new nowtaskAdapter(this,data);
        gridView.setAdapter(gridadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridadapter.setSeclection(position);
                gridadapter.notifyDataSetChanged();
                JSONObject data_ = JSON.parseObject(data);
                String list =data_.getString("list");
                JSONArray list_temp = JSON.parseArray(list);
                JSONObject json = list_temp.getJSONObject(position);
                Intent intent=new Intent(NowTaskActivity.this,TaskDetailActivity.class);
                intent.putExtra("uuid",json.getString("uuid"));
                startActivity(intent);

            }
        });
    }
}
