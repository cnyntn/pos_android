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
 * @author http://blog.csdn.net/finddreams
 * @Description:gridview的Adapter
 */
public class IndexGridAdapter extends BaseAdapter {
    public String[] img_text_0 = {"签到", "通知公告", "今日任务"};//,
    public int[] imgs_0= {R.drawable.calendar, R.drawable.volume, R.drawable.folder};//,
    private Context mContext;

    final int itemLength = 4;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList=new int[itemLength];//这个数组用来存放item的点击状态

    public IndexGridAdapter(Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mygrid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs_0[position]);
        tv.setText(img_text_0[position]);
        return convertView;
    }

}
