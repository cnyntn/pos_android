package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.attentionNotify;

import java.util.ArrayList;

/**
 * Created by kkkk on 2017/11/5.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<attentionNotify> list;
    private Context context;
    public MyAdapter(Context mContext, ArrayList<attentionNotify> list) {
        context=mContext;
        this.list = list;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attention_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        // 绑定数据
        holder.wordtitle.setText(list.get(position).getWroktitle());
        holder.wordname.setText(list.get(position).getWrokname());
        holder.wordstatus.setText(list.get(position).getWrokstatus());
        holder.wordtime.setText(list.get(position).getWroktime());
        if (list.get(position).getWrokstatus().equals("确认收到")){
            holder.wordstatus.setBackgroundResource(R.drawable.status_bg_green);
        }else {
            holder.wordstatus.setBackgroundResource(R.drawable.status_bg_orange);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView wordtitle;
        TextView wordname;
        TextView wordstatus;
        TextView wordtime;

        public ViewHolder(View itemView) {
            super(itemView);
            wordtitle = (TextView) itemView.findViewById(R.id.wordtitle);
            wordname = (TextView) itemView.findViewById(R.id.wordname);
            wordstatus = (TextView) itemView.findViewById(R.id.wordstatus);
            wordtime = (TextView) itemView.findViewById(R.id.wordtime);
        }
    }


}
