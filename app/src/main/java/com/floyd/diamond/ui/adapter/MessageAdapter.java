package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.MessageQueue;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.Message;
import com.floyd.diamond.bean.MyBaseAdapter;
import com.floyd.diamond.bean.MyImageCache;
import com.floyd.diamond.bean.MyImageLoader;
import com.floyd.diamond.bean.MyViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MessageAdapter extends MyBaseAdapter {
    private List<Message.DataEntity>allMessage;
    private RequestQueue queue;
    private Context context;

    //private int[]imgId={R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5};

    public MessageAdapter(Context context, List list, int layoutResId) {
        super(context, list, layoutResId);
        this.allMessage = list;
        this.context = context;
    }

    @Override
    public void fillData(MyViewHolder myViewHolder, int position) {
        ImageView imageView = ((ImageView) myViewHolder.findViewById(R.id.listview_img));//获取控件
        String imgUrl = allMessage.get(position).getImgUrl();//图片的地址
        if (GlobalParams.isDebug){
            Log.e("TAG",imgUrl);
        }
        queue = Volley.newRequestQueue(context);
        MyImageLoader loader = new MyImageLoader(queue, imgUrl, imageView, context);//加载图片
       // imageView.setImageResource(imgId[position]);
    }
}
