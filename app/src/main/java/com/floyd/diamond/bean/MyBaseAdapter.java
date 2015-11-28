package com.floyd.diamond.bean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2015/11/24.
 */


public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private Context context;//上下文
    private int[]list;//数据源
    private  int layoutResId;//资源文件id

    public MyBaseAdapter(Context context, int[] list, int layoutResId) {
        this.context = context;
        this.list = list;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {

        if (list != null) {
            return  list.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = MyViewHolder.getHolder(convertView, context, layoutResId);
        fillData(myViewHolder,position);//在这填充数据,发现需要给具体某个位置的view填数据,需要知道view和位置
        return myViewHolder.getmConvertView();
    }
    //填充数据的抽象方法,因为每个listview需要填充的控件 内容等都不一样,需要抽取出来
    public abstract  void fillData(MyViewHolder myViewHolder, int position);
}

