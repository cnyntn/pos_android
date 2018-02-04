package com.example.kkkk.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kkkk.helloworld.BaseViewHolder;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.model.bean.WarringMsg;

import java.util.List;

/**
 * @author http://blog.csdn.net/finddreams
 * @Description:gridview的Adapter
 */
public class attentionAdapter extends BaseAdapter {
    public String[] title = {"工作调度通知", "工作调度通知","工作调度通知","工作调度通知", "工作调度通知","工作调度通知"};
    public String[] name = {"小王", "小王","小王","小王", "小王","小王"};
    public String[] status = {"等待处理", "确认收到"};
    public String[] time = {"2017 9-10", "2017 9-10","2017 9-10","2017 9-10", "2017 9-10","2017 9-10"};
    public int[] imgs_0= {R.drawable.status_bg_orange, R.drawable.status_bg_green};//,
    private Context mContext;
    List<WarringMsg> list;
    final int itemLength = 4;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList=new int[itemLength];//这个数组用来存放item的点击状态

    public attentionAdapter(Context mContext,List<WarringMsg> list) {
        super();
        this.mContext = mContext;
        this.list=list;
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
        return list.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attention_item, parent, false);
        }
        TextView wordtitle = BaseViewHolder.get(convertView, R.id.wordtitle);
        TextView wordname = BaseViewHolder.get(convertView, R.id.wordname);
        TextView wordtime = BaseViewHolder.get(convertView, R.id.wordtime);
        TextView wordstatus = BaseViewHolder.get(convertView, R.id.wordstatus);
        String[] time=list.get(position).getCreateTime().split(" ");
        wordtitle.setText(list.get(position).getTitle());
        wordname.setText("发起人："+list.get(position).getCreateUser().getNickname());
        wordtime.setText(time[0]);
        switch (list.get(position).getStatus()){
            case 0:
                wordstatus.setText(status[0]);
                wordstatus.setBackgroundResource(imgs_0[0]);
                break;
            case 1:
                wordstatus.setText(status[1]);
                wordstatus.setBackgroundResource(imgs_0[1]);
                break;
            default:
                wordstatus.setText("未知状态");
                wordstatus.setBackgroundResource(imgs_0[0]);
                break;
        }
        return convertView;
    }

}
