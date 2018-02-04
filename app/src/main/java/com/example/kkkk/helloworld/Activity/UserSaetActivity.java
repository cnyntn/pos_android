package com.example.kkkk.helloworld.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.kkkk.helloworld.util.AMapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSaetActivity extends BaseAppActivity implements AMap.OnMapClickListener, AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter {
    MapView mMapView = null;
    //设置希望展示的地图缩放级别
    CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
    private AMap aMap;
    private InfoWinAdapter adapter;
    private Marker oldMarker;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.talk)
    TextView talk;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersaet);
        ButterKnife.bind(this);
        //phone= (TextView) findViewById(R.id.phone);
        //talk= (TextView) findViewById(R.id.talk);
        //phone.setOnClickListener(this);
        //talk.setOnClickListener(this);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //initView();
        init();
        aMap.moveCamera(mCameraUpdate);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(25.0109100000,102.7361000000)));
        setUserMarker(25.0109100000,102.7361000000,"昆明市官渡区宝海路181号星河明居");
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
        adapter = new InfoWinAdapter(getBaseContext());
        aMap.setInfoWindowAdapter(adapter);
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
        Marker marker;
        marker=aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(lat, lng)))
                .title(origin)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        marker.showInfoWindow();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        oldMarker = marker;
        return false;
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
    }
    @OnClick({R.id.back,R.id.phone,R.id.talk})
    void click(View v){
        switch (v.getId()){
            case R.id.phone:
                call("1212");
                break;
            case R.id.talk:
                //Toast.makeText(this, "不在线", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), ChatActivity.class).putExtra("userId", "782818448"));
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
}
