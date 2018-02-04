package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkkk.helloworld.BaseViewHolder;
import com.example.kkkk.helloworld.R;

/**
 * Created by kkkk on 2017/11/5.
 */

public class NearbyGridAdapter extends BaseAdapter {
    public String[] name_ = {"小王", "大王", "老王"};
    public String[] mobile_ = {"311", "311", "311"};
    public String[] distance_ = {"3", "4", "5"};
    public int[] imgs_0= {R.drawable.head_icon, R.drawable.head_icon, R.drawable.head_icon};
    private Context mContext;

    final int itemLength = 4;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList=new int[itemLength];//这个数组用来存放item的点击状态

    public NearbyGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        for (int i =0;i<itemLength;i++){
            clickedList[i]=0;      //初始化item点击状态的数组
        }
    }

    public void setSeclection(int posiTion) {
        clickTemp = posiTion;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name_.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nearby_item, parent, false);
        }
        TextView name = BaseViewHolder.get(convertView, R.id.name);
        TextView mobile = BaseViewHolder.get(convertView, R.id.mobile);
        TextView distance = BaseViewHolder.get(convertView, R.id.distance);
        ImageView img=BaseViewHolder.get(convertView, R.id.img);
        img.setBackgroundResource(imgs_0[position]);
        name.setText(name_[position]);
        mobile.setText(mobile_[position]);
        distance.setText("距离约"+distance_[position]+"km");
        return convertView;
    }

}


