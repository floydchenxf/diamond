package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.Model;
import com.floyd.diamond.bean.MyImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> implements CompoundButton.OnCheckedChangeListener{
    private ChangeText changeText;
    private Context context;
    private MyOnItemClickListener myOnItemClickListener;//点击事件监听
    private List<Model.DataEntity> modelsList;
    private RequestQueue queue;

    public MasonryAdapter(List<Model.DataEntity>modelsList,Context context,ChangeText changeText) {
        this.modelsList = modelsList;
        this.context=context;
        this.changeText=changeText;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.homechooseitem_layout, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
        //masonryView.imageView.setImageResource(modelsList.get(position).getAvatarUrl());
        masonryView.likecount.setText(modelsList.get(position).getFollowNum() + "");
        masonryView.likecount.setTag("cb" + position);
        masonryView.likecount.setOnCheckedChangeListener(this);
        masonryView.name.setText(modelsList.get(position).getNickname());
        masonryView.place.setText(modelsList.get(position).getArea()+"");
        String imgUrl=modelsList.get(position).getAvatarUrl();
        if (GlobalParams.isDebug){
            Log.e("TAG",imgUrl+"");
        }
        if (imgUrl!=null){
            queue= Volley.newRequestQueue(context);
            MyImageLoader loader=new MyImageLoader(queue,imgUrl,masonryView.imageView,context);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (changeText!=null){

            changeText.setText((String) buttonView.getTag(),isChecked);
        }
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }

    public class MasonryView extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;//模特图片
        CheckBox likecount;//收藏次数
        TextView name;//姓名
        TextView place;//归属地

        public MasonryView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.masonry_item_img);
            likecount = (CheckBox) itemView.findViewById(R.id.likecount);
            name = ((TextView) itemView.findViewById(R.id.name));
            place = ((TextView) itemView.findViewById(R.id.place));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();//获取位置
            //此处处理具体点击
            if (myOnItemClickListener != null) {
                myOnItemClickListener.onItemClick(v, layoutPosition);
            }
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int postion);//具体的点击事件
    }


    public static interface ChangeText{
        void setText(String tag, boolean isChecked);
    }


}
