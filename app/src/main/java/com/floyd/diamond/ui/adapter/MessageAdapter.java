package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.floyd.diamond.R;
import com.floyd.diamond.bean.MyBaseAdapter;
import com.floyd.diamond.bean.MyViewHolder;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MessageAdapter extends MyBaseAdapter {
    private int[]imgId;
    public MessageAdapter(Context context, int[] list, int layoutResId) {
        super(context, list, layoutResId);
        this.imgId=list;
    }

    @Override
    public void fillData(MyViewHolder myViewHolder, int position) {
        ImageView imageView= ((ImageView) myViewHolder.findViewById(R.id.listview_img));
        imageView.setImageResource(imgId[position]);
    }
}
