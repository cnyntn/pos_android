package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.example.kkkk.helloworld.R;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext;
    private LatLng latLng;
    private TextView seat;
    private String agentName;


    public  InfoWinAdapter(Context context){
        mContext=context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infowindow, null);
        //navigation = (LinearLayout) view.findViewById(R.id.navigation_LL);
        //call = (LinearLayout) view.findViewById(R.id.call_LL);
        seat = (TextView) view.findViewById(R.id.name);
        //addrTV = (TextView) view.findViewById(R.id.addr);
        //ts= (LinearLayout) view.findViewById(R.id.ts);

        seat.setText(agentName);
        //addrTV.setText(String.format(snippet));

        //navigation.setOnClickListener(this);
        //call.setOnClickListener(this);
        //if (agentName.equals("司机信息")){
        //    ts.setVisibility(View.GONE);
        //}
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }
    }



}
