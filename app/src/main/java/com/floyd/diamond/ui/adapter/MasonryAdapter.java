package com.floyd.diamond.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.bean.Model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>{
    //private List<Model> modles;
    private int[]modles;
    private  MyOnItemClickListener myOnItemClickListener;//点击事件监听

    public MasonryAdapter(int[]list) {
        modles=list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homechooseitem_layout, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
        masonryView.imageView.setImageResource(modles[position]);
        masonryView.likecount.setText("1024");
        masonryView.name.setText("yoyo");
        masonryView.place.setText("黑龙江");
    }
    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return modles.length;
    }

    public class MasonryView extends  RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;//模特图片
        TextView likecount;//收藏次数
        TextView name;//姓名
        TextView place;//归属地

        public MasonryView(View itemView){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
            likecount= (TextView) itemView.findViewById(R.id.likecount);
            name= ((TextView) itemView.findViewById(R.id.name));
            place= ((TextView) itemView.findViewById(R.id.place));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();//获取位置
            //此处处理具体点击
            if (myOnItemClickListener != null) {
                myOnItemClickListener.onItemClick(v,layoutPosition);
                // Toast.makeText(context,"你眼瞎啊，没看见点击的是第"+layoutPosition+"条Message啊",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public interface  MyOnItemClickListener{
        void onItemClick(View view, int postion);//具体的点击事件
    }



}
