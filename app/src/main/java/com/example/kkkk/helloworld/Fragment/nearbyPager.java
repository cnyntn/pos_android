package com.example.kkkk.helloworld.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.kkkk.helloworld.Activity.UserSaetActivity;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.adapter.NearbyGridAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * Created by kkkk on 2017/11/5.
 */

public class nearbyPager extends Fragment {

    @BindView(R.id.grid_nearby)
    GridView gridNearby;
    @BindView(R.id.main_swipe)
    WaveSwipeRefreshLayout  refresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nearby,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    @SuppressWarnings("ResourceType")
    private void initView() {
        final NearbyGridAdapter aAdapter=new NearbyGridAdapter(getContext());
        gridNearby.setAdapter(aAdapter);
        gridNearby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aAdapter.setSeclection(position);
                aAdapter.notifyDataSetChanged();
                //Toast.makeText(getContext(), "点击了"+position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), UserSaetActivity.class));

            }
        });
        //refresh.setColorSchemeResources(R.color.textColor_1);
        refresh.setColorSchemeColors(Color.WHITE);
        refresh.setWaveColor(Color.argb(170,0, 191, 143));
        refresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                },2000);
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

