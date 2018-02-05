package com.example.kkkk.helloworld.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kkkk.helloworld.adapter.MyFragmentPagerAdapter;
import com.example.kkkk.helloworld.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class noticeActivity extends BaseAppActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TextView send;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        //one = mTabLayout.getTabAt(0);
        //two = mTabLayout.getTabAt(1);

        //设置Tab的图标，假如不需要则把下面的代码删去
        //one.setIcon(R.mipmap.ic_launcher);
        //two.setIcon(R.mipmap.ic_launcher);

        send= (TextView) findViewById(R.id.sendnotice);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(noticeActivity.this,sendnoticeActivity.class);
                startActivity(intent);
            }
        });
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
}
