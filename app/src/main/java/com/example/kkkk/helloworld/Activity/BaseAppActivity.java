package com.example.kkkk.helloworld.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.example.kkkk.helloworld.location.LocationService;
import com.example.kkkk.helloworld.location.LocationStatusManager;
import com.example.kkkk.helloworld.location.PositionInfo;
import com.example.kkkk.helloworld.location.Utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseAppActivity extends AppCompatActivity {

    public static final String RECEIVER_ACTION = "location_in_background";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                PositionInfo locationResult = (PositionInfo) intent.getSerializableExtra("result");
                if (null != locationResult) {
                    uploadAddr(locationResult);
                    //tvResult.setText(locationResult);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);
        startLocationService();
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }

    /**
     * 开始定位服务
     */
    private void startLocationService() {
        getApplicationContext().startService(new Intent(this, LocationService.class));
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService() {
        sendBroadcast(Utils.getCloseBrodecastIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationChangeBroadcastReceiver != null)
            unregisterReceiver(locationChangeBroadcastReceiver);

        if (null != locationClient) {

            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 上报位置信息
     *
     * @param info 位置
     */
    private void uploadAddr(PositionInfo info) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).uploadAddr(App.getInstance().getMyToken(), getJsonStr1(info))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            if (response.body() != null) {
                                String result = response.body().string();
                                System.out.println("===========" + result);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(BaseAppActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private RequestBody getJsonStr1(PositionInfo info) {

        //JSONObject object = new JSONObject();
        //object.put("lat", info.getLat());
        //object.put("lng", info.getLng());
        //object.put("name", info.getName());
        //object.put("address", info.getAddress());
        //object.put("locType", info.getLocType());
        //object.put("accuray", info.getAccuracy());
        //RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), object.toJSONString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JSON.toJSONString(info));
        return body;
    }

}
