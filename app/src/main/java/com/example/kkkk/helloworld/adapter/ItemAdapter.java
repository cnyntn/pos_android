package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkkk.helloworld.BaseViewHolder;
import com.example.kkkk.helloworld.R;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ItemAdapter extends BaseAdapter {
    public String[] img_text_0 = {"是否包装送纸", "是否培训", "是否包含故障排除","是否包含单面付", "主动换机", "被动换机","主动撤机","被动撤机"};//,
    private Context mContext;

    final int itemLength = 8;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList={0,0,0,0,0,0,0,0};//这个数组用来存放item的点击状态
    public ItemAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        //for (int i =0;i<itemLength;i++){
        //    clickedList[i]=0;      //初始化item点击状态的数组
        //}
    }

    public void setSeclection(int posiTion) {
        clickTemp = posiTion;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text_0.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.item);
        tv.setText(img_text_0[position]);
        //tv.setBackgroundResource(R.drawable.frame);
        //tv.setTextColor(mContext.getResources().getColor(R.color.textColor_2));
        if(clickTemp==position){
            if (clickedList[position]==0){
                tv.setBackgroundResource(R.drawable.choose);
                tv.setTextColor(mContext.getResources().getColor(R.color.textColor_1));
                clickedList[position]=1;
            }
            else {
                tv.setBackgroundResource(R.drawable.frame);
                tv.setTextColor(mContext.getResources().getColor(R.color.textColor_2));
                clickedList[position]=0;
            }
        }


        return convertView;
    }




}

