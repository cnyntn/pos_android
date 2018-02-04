package com.example.kkkk.helloworld.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.InfoWinAdapter;
import com.example.kkkk.helloworld.model.bean.PositionInfo;
import com.example.kkkk.helloworld.util.AMapUtil;
import com.example.kkkk.helloworld.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnMarkerClickListener {
    MapView mMapView = null;
    //设置希望展示的地图缩放级别
    CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
    private AMap aMap;
    private InfoWinAdapter adapter;
    private Marker oldMarker;
    private AMapLocationClient locationClientSingle = null;
    private ProgressDialog mDialog;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.signin)
    LinearLayout signin;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.back)
    ImageButton back;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initData();
        init();
        startSingleLocation();
        aMap.moveCamera(mCameraUpdate);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(new java.util.Date());
        time.setText(date);
    }

    @OnClick({R.id.signin,R.id.back})
    void click(View v){
        switch (v.getId()){
            case R.id.signin:
                sign.setText("签到完成");
                Resources resource = (Resources) getBaseContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.remember_pwd);
                sign.setTextColor(csl);
                img.setImageResource(R.drawable.check_in_over);
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setOnMapClickListener(this);
            setUpMap();
        }
        //自定义InfoWindow
        aMap.setOnMarkerClickListener(this);
        //adapter = new InfoWinAdapter(getBaseContext());
        //aMap.setInfoWindowAdapter(adapter);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.kongbai));
        // 自定义精度范围的圆形边框颜色
        //myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        //myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        //myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }
    private void setUserMarker(Double lat, Double lng, String origin) {
        // 有打点效果
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(lat, lng)))
                .title(origin)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
    }
    /**
     * 启动单次客户端定位
     */
    void startSingleLocation(){
        mDialog.show();
        if(null == locationClientSingle){
            locationClientSingle = new AMapLocationClient(this.getApplicationContext());
        }

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //使用单次定位
        locationClientOption.setOnceLocation(true);
        // 地址信息
        locationClientOption.setNeedAddress(true);
        locationClientOption.setLocationCacheEnable(false);
        locationClientSingle.setLocationOption(locationClientOption);
        locationClientSingle.setLocationListener(locationSingleListener);
        locationClientSingle.startLocation();
    }

    /**
     * 单次客户端的定位监听
     */
    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mDialog.dismiss();
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("单次定位完成\n");
            sb.append("回调时间: " + Utils.formatUTC(callBackTime, null) + "\n");
            if(null == aMapLocation){
                sb.append("定位失败：location is null!!!!!!!");
            } else {
                //sb.append(Utils.getLocationStr(location));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude())));
                setUserMarker(aMapLocation.getLatitude(),aMapLocation.getLongitude(),aMapLocation.getAddress());
                address.setText(aMapLocation.getAddress());
                company.setText(aMapLocation.getPoiName());
                PositionInfo info = new PositionInfo();
                info.setAccuracy(aMapLocation.getAccuracy());
                info.setAddress(aMapLocation.getAddress());
                info.setAngle(aMapLocation.getBearing());
                info.setCity(aMapLocation.getCity());
                info.setDeviceID("xxx");
                info.setDistrict(aMapLocation.getDistrict());
                info.setLatitude(aMapLocation.getLatitude());
                info.setLongitude(aMapLocation.getLongitude());
                info.setPoiName(aMapLocation.getPoiName());
                info.setProvince(aMapLocation.getProvince());
                info.setTimestamp(Utils.formatUTC(aMapLocation.getTime(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != locationClientSingle){
            locationClientSingle.onDestroy();
            locationClientSingle = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        //点击地图上没marker 的地方，隐藏inforwindow
        if (oldMarker != null) {
            oldMarker.hideInfoWindow();
            //oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_normal));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        oldMarker = marker;
        return false;
    }

}
