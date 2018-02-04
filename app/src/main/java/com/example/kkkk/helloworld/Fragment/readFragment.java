package com.example.kkkk.helloworld.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.Activity.noticedetailActivity;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.readGridAdapter;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.example.kkkk.helloworld.starItem;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/11/6.
 */

public class readFragment extends Fragment {

    GridView gridView;
    private ProgressDialog mDialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_read,container,false);

        gridView = (GridView) view.findViewById(R.id.grid_read);
        initView();
        getReadList();
        return view;
    }

    private void initView() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
    }

    private void getReadList(){
        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).ReadList(App.getInstance().getMyToken(),"1")
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
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                loadList(data);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadList(final String data){
        final readGridAdapter gridadapter=new readGridAdapter(getContext(),data);
        gridView.setAdapter(gridadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridadapter.setSeclection(position);
                gridadapter.notifyDataSetChanged();
                //Toast.makeText(getContext(), "已读通告"+position, Toast.LENGTH_SHORT).show();
                JSONObject data_ = JSON.parseObject(data);
                String list =data_.getString("list");
                JSONArray list_temp = JSON.parseArray(list);
                JSONObject json = list_temp.getJSONObject(position);
                Intent intent=new Intent(getActivity(),noticedetailActivity.class);
                intent.putExtra("uuid",json.getString("uuid"));
                startActivity(intent);

            }
        });
    }
}
