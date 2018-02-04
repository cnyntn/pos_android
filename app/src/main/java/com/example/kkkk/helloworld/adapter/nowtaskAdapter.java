package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.BaseViewHolder;
import com.example.kkkk.helloworld.R;

/**
 * @author http://blog.csdn.net/finddreams
 * @Description:gridview的Adapter
 */
public class nowtaskAdapter extends BaseAdapter {
    public String[] name = {"日常维护", "日常维护","新装"};
    public String[] code = {"B8156151615", "B8156151615","B8156151615"};
    public String[] user = {"王经理", "王经理","王经理"};
    public String[] status = {"未开始", "进行中","审核通过"};
    public int[] imgs_0= {R.drawable.status_bg_green, R.drawable.status_bg_orange,R.drawable.status_bg_blue};//,
    private Context mContext;

    final int itemLength = 4;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList=new int[itemLength];//这个数组用来存放item的点击状态
    JSONObject json;
    String data;
    JSONArray list_temp;
    public nowtaskAdapter(Context mContext, String data) {
        super();
        this.mContext = mContext;
        for (int i =0;i<itemLength;i++){
            clickedList[i]=0;      //初始化item点击状态的数组
        }
        this.data=data;
        JSONObject data_ = JSON.parseObject(data);
        String list =data_.getString("list");
        list_temp = JSON.parseArray(list);
    }

    public void setSeclection(int posiTion) {
        clickTemp = posiTion;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nowtask_item, parent, false);
        }
        TextView wordname = BaseViewHolder.get(convertView, R.id.name);
        TextView wordcode = BaseViewHolder.get(convertView, R.id.code);
        TextView worduser = BaseViewHolder.get(convertView, R.id.user);
        TextView wordstatus = BaseViewHolder.get(convertView, R.id.status);
        JSONObject json = list_temp.getJSONObject(position);
        JSONObject user= JSON.parseObject(json.getString("createUser"));
        JSONObject merchant= JSON.parseObject(json.getString("merchant"));

        wordname.setText(json.getString("type"));
        wordcode.setText(merchant.getString("machineCode"));
        worduser.setText(user.getString("nickname"));
        //wordstatus.setText(status[position]);
        switch (json.getString("status")){
            case "1":
                wordstatus.setText(status[0]);
                wordstatus.setBackgroundResource(imgs_0[0]);
                break;
            case "2":
                wordstatus.setText(status[1]);
                wordstatus.setBackgroundResource(imgs_0[1]);
                break;
            case "3":
                wordstatus.setText(status[2]);
                wordstatus.setBackgroundResource(imgs_0[2]);
                break;
            default:
                wordstatus.setText("未知");
                wordstatus.setBackgroundResource(imgs_0[1]);
                break;
        }

        return convertView;
    }

}
