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
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.bean.MyImageLoader;
import com.floyd.diamond.utils.CommonUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {
    private ChangeText changeText;
    private Context context;
    private MyOnItemClickListener myOnItemClickListener;//点击事件监听
    private MyImageLoader myImageLoader;
    private List<ModelInfo.DataEntity> allModel;
    private RequestQueue queue;

    public MasonryAdapter(List<ModelInfo.DataEntity> allModel, Context context, ChangeText changeText) {
        this.allModel = allModel;
        this.context = context;
        this.changeText = changeText;
        RequestQueue queue = Volley.newRequestQueue(context);
        this.myImageLoader = new MyImageLoader(queue);
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.homechooseitem_layout, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, final int position) {
        if (allModel.get(position).isIsFollow()){
            masonryView.likecount.setChecked(true);
            masonryView.likecount.setText(allModel.get(position).getFollowNum() + "");
            masonryView.likecount.setTag("cb" + position);
            masonryView.likecount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (changeText != null) {

                        changeText.setText((String) buttonView.getTag(), isChecked, position);
                    }
                }
            });
        }
        masonryView.likecount.setText(allModel.get(position).getFollowNum() + "");
        masonryView.likecount.setTag("cb" + position);
        masonryView.likecount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (changeText != null) {

                    changeText.setText((String) buttonView.getTag(), isChecked,position);
                }
            }
        });
        masonryView.name.setText(allModel.get(position).getNickname());
        masonryView.place.setText(allModel.get(position).getArea() + "");
        String imgUrl = allModel.get(position).getAvatarUrl();
        if (GlobalParams.isDebug) {
            Log.e("TAG", imgUrl + "");
        }
        if (imgUrl != null) {
            myImageLoader.bindView(CommonUtil.getImage_400(imgUrl), masonryView.imageView);
        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return allModel.size();
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


    public static interface ChangeText {
        void setText(String tag, boolean isChecked,int position);
    }


}
