package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.starItem;

import java.util.List;

/**
 * Created by kkkk on 2017/11/5.
 */

public class GridViewAdapter extends BaseAdapter {
    Context context;
    List<starItem> list;
    private Integer[] imgs = {
            R.drawable.head,R.drawable.head,R.drawable.head,
            R.drawable.head,R.drawable.head,R.drawable.head,
    };
    public GridViewAdapter(Context _context, List<starItem> _list) {
        this.list = _list;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.starstaff_item, null);
        TextView tvCity = (TextView) convertView.findViewById(R.id.tvStar);
        ImageView img= (ImageView) convertView.findViewById(R.id.ItemImage);
        starItem city = list.get(position);

      tvCity.setText(city.getStaffName());
        img.setImageResource(R.drawable.head);
        return convertView;
    }
}


