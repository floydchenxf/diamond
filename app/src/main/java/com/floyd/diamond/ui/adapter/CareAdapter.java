package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.Care;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.MyImageLoader;
import com.floyd.diamond.utils.CommonUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/12/20.
 */
public class CareAdapter extends RecyclerView.Adapter<CareAdapter.MasonryView>{
    private Context context;
    private MyOnItemClickListener myOnItemClickListener;//点击事件监听
    private List<Care.DataEntity.DataListEntity> allModel;
    private MyImageLoader mImageLoader;
    private List<String>deleteModel;//取消关注的模特

    public CareAdapter(List<Care.DataEntity.DataListEntity> allModel, Context context,List<String>deleteModel) {
        this.allModel = allModel;
        this.context = context;
        this.deleteModel=deleteModel;
        RequestQueue queue = Volley.newRequestQueue(context);
        this.mImageLoader = new MyImageLoader(queue);
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.homechooseitem_layout, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(final MasonryView masonryView, int position) {
        masonryView.bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        masonryView.bg_recycle.setTag("bg" + position);
        Care.DataEntity.DataListEntity entity = allModel.get(position);
        masonryView.name.setText(entity.getNickname());
        String imgUrl = entity.getAvartUrl();
        if (GlobalParams.isDebug) {
            Log.e("TAG", imgUrl + "");
        }
        if (imgUrl != null) {
            mImageLoader.bindView(CommonUtil.getImage_400(imgUrl), masonryView.imageView);
        }
    }

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
        TextView bg_recycle;//删除时弹出的透明黑幕

        public MasonryView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.masonry_item_img);
            likecount = (CheckBox) itemView.findViewById(R.id.likecount);
            likecount.setVisibility(View.INVISIBLE);
            name = ((TextView) itemView.findViewById(R.id.name));
            place = ((TextView) itemView.findViewById(R.id.place));
            bg_recycle = ((TextView) itemView.findViewById(R.id.bg_recycle));
            itemView.setOnClickListener(this);
            bg_recycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(0,0));

                    deleteModel.remove(allModel.get(getLayoutPosition()));
                }
            });

        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();//获取位置
            //此处处理具体点击
            if (myOnItemClickListener != null) {
                myOnItemClickListener.onItemClick(v, layoutPosition);
            }

           // bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(v.getWidth(),v.getHeight()));
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);//具体的点击事件
    }

    public static interface ChangeText{
        void setText(String tag, boolean isChecked);
    }

}

