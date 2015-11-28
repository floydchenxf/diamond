package com.floyd.diamond.bean;

/**
 * Created by Administrator on 2015/11/24.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public class MyViewHolder {
    private View mConvertView;
    private Context context;
    private int layoutResID;//资源id

    //在上面只是声明了一个view,没有实例化,如果要调用get方法返回的话为null,所以需要实例化mConvertView;我们在构造函数中,顺便初始化mConvertView,保证每个holder中都有具体的mConvertView
    public MyViewHolder(Context context, int layoutResID) {
        this.context = context;
        this.layoutResID = layoutResID;
        mConvertView = LayoutInflater.from(context).inflate(layoutResID, null);
        mConvertView.setTag(this);//将holder自己设置为它内部view的标记,方便用内部view就可以获取到holder
    }

    public View getmConvertView() {
        return mConvertView;
    }

    //返回viewholder,用于获取它内部的mConvertView
    public static MyViewHolder getHolder(View convertView, Context context, int layoutResID) {
        MyViewHolder myViewHolder = null;
        if (convertView == null) {
            myViewHolder = new MyViewHolder(context, layoutResID);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        return myViewHolder;
    }

    //根据id去查找对应的控件
    public View findViewById(int viewId) {
        return mConvertView.findViewById(viewId);
    }

}

