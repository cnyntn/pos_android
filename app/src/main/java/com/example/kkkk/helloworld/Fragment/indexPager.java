package com.example.kkkk.helloworld.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.Activity.NowTaskActivity;
import com.example.kkkk.helloworld.Activity.SignInActivity;
import com.example.kkkk.helloworld.Activity.noticeActivity;
import com.example.kkkk.helloworld.Activity.noticedetailActivity;
import com.example.kkkk.helloworld.adapter.GridViewAdapter;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.IndexGridAdapter;
import com.example.kkkk.helloworld.adapter.attentionAdapter;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.example.kkkk.helloworld.model.bean.WarringMsg;
import com.example.kkkk.helloworld.starItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkkk on 2017/11/5.
 */

public class indexPager extends Fragment {

    List<starItem> starList = new ArrayList<starItem>();
    List<WarringMsg> warringList = new ArrayList<WarringMsg>();
    GridView gridView;
    GridView grdView2;
    GridView grid_attention;
    Intent intent;
    starItem item;
    WarringMsg warringItem;
    private int page = 0;
    private ProgressDialog mDialog;
    GridViewAdapter adapter;
    attentionAdapter aAdapter;
    JSONArray warringList_temp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_index,container,false);
        gridView = (GridView) view.findViewById(R.id.grid);
        grdView2 = (GridView) view.findViewById(R.id.grid_);
        grid_attention = (GridView) view.findViewById(R.id.grid_attention);
        initView();
        //setData();

        return view;
    }


    private void initView(){
        mDialog = new ProgressDialog(getActivity());
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
        final IndexGridAdapter gridadapter=new IndexGridAdapter(getContext());
        grdView2.setAdapter(gridadapter);
        grdView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridadapter.setSeclection(position);
                gridadapter.notifyDataSetChanged();
                switch (position) {
                    case 0:
                        intent=new Intent(getActivity(),SignInActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(getActivity(),noticeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(getActivity(),NowTaskActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        grid_attention.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aAdapter.setSeclection(position);
                aAdapter.notifyDataSetChanged();
                JSONObject json = warringList_temp.getJSONObject(position);
                //warringItem = new WarringMsg();
                //JSONObject uuid= JSON.parseObject(json.getString("uuid"));
                Intent intent=new Intent(getActivity(),noticedetailActivity.class);
                //Bundle bundle=new Bundle();
                //bundle.putString("usrname",user.getString("username"));
                //bundle.putString("title",json.getString("title"));
                //bundle.putString("content",json.getString("content"));
                //bundle.putString("time",json.getString("createTime"));
                intent.putExtra("uuid",json.getString("uuid"));
                startActivity(intent);

            }
        });
        getStarStaff();
        getWarringMsg();

    }

    /**设置数据*/
    private void setData() {
        item = new starItem();
        item.setStaffName("小王");
        starList.add(item);
        item = new starItem();
        item.setStaffName("大王");
        starList.add(item);
        item = new starItem();
        item.setStaffName("老王");
        starList.add(item);
        starList.addAll(starList);

    }

    /**设置GirdView参数，绑定数据*/
    private void setGridView(List<starItem> starList) {
        int size = starList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        adapter = new GridViewAdapter(getContext(), starList);
        gridView.setAdapter(adapter);
    }

    private void getStarStaff(){
            mDialog.show();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            RetrofitHttp.getRetrofit(builder.build()).StarStaffList()
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
                                    item = new starItem();
                                    JSONArray list_temp = JSON.parseArray(data);
                                    starList.clear();
                                        //sIDs.clear();
                                        for (int i = 0; i < list_temp.size(); i++) {
                                            JSONObject json = list_temp.getJSONObject(i);
                                            item = new starItem();
                                            item.setStaffName(json.getString("username"));
                                            starList.add(item);
                                        }
                                    //starList.addAll(starList);
                                    setGridView(starList);
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

    private void getWarringMsg(){
        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).WarringMsg()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        mDialog.dismiss();
                        try {
                            String result = response.body().string();
                            JSONObject jsonObject = JSON.parseObject(result);
                            String msg = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            JSONObject data_ = JSON.parseObject(data);
                            String list =data_.getString("list");
                            if (jsonObject.getString("code").equals("failure")) {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                warringList_temp = JSON.parseArray(list);
                                warringList.clear();
                                    //sIDs.clear();
                                    for (int i = 0; i < warringList_temp.size(); i++) {
                                        JSONObject json = warringList_temp.getJSONObject(i);
                                        warringItem = new WarringMsg();
                                        JSONObject user= JSON.parseObject(json.getString("createUser"));
                                        WarringMsg.CreateUserBean userBean=new WarringMsg.CreateUserBean();
                                        userBean.setUuid(user.getString("uuid"));
                                        userBean.setMobile(user.getString("mobile"));
                                        userBean.setNickname(user.getString("nickname"));
                                        userBean.setUsername(user.getString("username"));
                                        warringItem.setUuid(json.getString("uuid"));
                                        warringItem.setTitle(json.getString("title"));
                                        warringItem.setContent(json.getString("content"));
                                        warringItem.setCreateTime(json.getString("createTime"));
                                        warringItem.setStatus(json.getInteger("status"));
                                        warringItem.setUrgency(json.getInteger("urgency"));
                                        warringItem.setCreateUser(userBean);
                                        warringList.add(warringItem);

                                    }
                                aAdapter=new attentionAdapter(getContext(),warringList);
                                grid_attention.setAdapter(aAdapter);

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

}

