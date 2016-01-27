package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-9.
 */
public class MoteTaskTypeAdapter extends BaseAdapter {

    private List<MoteTypeTaskVO> productTypeVOs = new ArrayList<MoteTypeTaskVO>();
    private Context mContext;
    private OnItemClickListener itemClickListener;
    private ImageLoader mImageLoader;

    public MoteTaskTypeAdapter(Context context, ImageLoader imageLoader, OnItemClickListener itemClickListener) {
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.itemClickListener = itemClickListener;
    }

    public void addAll(List<MoteTypeTaskVO> productTypeVOs, boolean isClear) {
        if (isClear) {
            this.productTypeVOs.clear();
        }

        this.productTypeVOs.addAll(productTypeVOs);
        this.notifyDataSetChanged();
    }

    public void updateAcceptStatus(long taskId) {
        for (MoteTypeTaskVO vo:productTypeVOs) {
            if (vo.productItemVO1 != null && vo.productItemVO1.id == taskId) {
                vo.productItemVO1.isAccepted = true;
            }

            if (vo.productItemVO2 != null && vo.productItemVO2.id == taskId) {
                vo.productItemVO2.isAccepted = true;
            }
        }
    }

    public List<MoteTypeTaskVO> getData() {
        return this.productTypeVOs;
    }

    @Override
    public int getCount() {
        return productTypeVOs.size();
    }

    @Override
    public MoteTypeTaskVO getItem(int position) {
        return productTypeVOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.product_type_item, null);
            holder = new ViewHolder();
            holder.finishStatusView1 = (TextView) convertView.findViewById(R.id.finish_status_view1);
            holder.finishStatusView2 = (TextView) convertView.findViewById(R.id.finish_status_view2);
            holder.productItemLayout1 = convertView.findViewById(R.id.product_type_item_1);
            holder.productItemLayout2 = convertView.findViewById(R.id.product_type_item_2);

            holder.productImage1 = (NetworkImageView) convertView.findViewById(R.id.product_pic_1);
            holder.productImage2 = (NetworkImageView) convertView.findViewById(R.id.product_pic_2);

            holder.priceView11 = (TextView) convertView.findViewById(R.id.price_1_1);
            holder.priceView12 = (TextView) convertView.findViewById(R.id.price_1_2);

            holder.priceView21 = (TextView) convertView.findViewById(R.id.price_2_1);
            holder.priceView22 = (TextView) convertView.findViewById(R.id.price_2_2);

            holder.ding1= ((ImageView) convertView.findViewById(R.id.ding_1));
            holder.ding2= ((ImageView) convertView.findViewById(R.id.ding_2));

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        MoteTypeTaskVO vo = getItem(position);
        if (vo.productItemVO1 == null) {
            holder.productItemLayout1.setVisibility(View.INVISIBLE);
            holder.productItemLayout1.setOnClickListener(null);
            holder.finishStatusView1.setVisibility(View.GONE);
        } else {
            final TaskItemVO itemVO1 = vo.productItemVO1;
            if (itemVO1.isFinish()) {
//                holder.finishStatusView1.setText("已被抢完");
                holder.finishStatusView1.setVisibility(View.VISIBLE);
            } else if (itemVO1.acceptStauts == 2) {
//                holder.finishStatusView1.setText("条件不符");
                holder.finishStatusView1.setVisibility(View.VISIBLE);
            } else if (itemVO1.acceptStauts == 3) {
//                holder.finishStatusView1.setText("已完成");
                holder.finishStatusView1.setVisibility(View.VISIBLE);
            } else {
                holder.finishStatusView1.setVisibility(View.GONE);
            }

            holder.productItemLayout1.setVisibility(View.VISIBLE);
            holder.productItemLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoteTaskTypeAdapter.this.itemClickListener != null) {
                        MoteTaskTypeAdapter.this.itemClickListener.onItemClick(v, itemVO1);
                    }
                }
            });

            if (!itemVO1.isDirect){
                holder.ding1.setVisibility(View.INVISIBLE);
            } else {
                holder.ding1.setVisibility(View.VISIBLE);
            }

            holder.productImage1.setDefaultImageResId(R.drawable.tupian);
            holder.productImage1.setImageUrl(itemVO1.getPreviewImageUrl(), mImageLoader);
            holder.priceView11.setText("商品售价：" + itemVO1.price + "");
            holder.priceView12.setText("酬金："+itemVO1.shotFee+"");
        }

        if (vo.productItemVO2 == null) {
            holder.productItemLayout2.setVisibility(View.INVISIBLE);
            holder.productItemLayout2.setOnClickListener(null);
            holder.finishStatusView2.setVisibility(View.GONE);
        } else {
            final TaskItemVO itemVO2 = vo.productItemVO2;
            holder.productItemLayout2.setVisibility(View.VISIBLE);
            holder.productItemLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoteTaskTypeAdapter.this.itemClickListener != null) {
                        MoteTaskTypeAdapter.this.itemClickListener.onItemClick(v, itemVO2);
                    }
                }
            });

            if (itemVO2.isFinish()) {
//                holder.finishStatusView2.setText("已被抢完");
                holder.finishStatusView2.setVisibility(View.VISIBLE);
            } else if (itemVO2.acceptStauts == 2) {
//                holder.finishStatusView2.setText("条件不符");
                holder.finishStatusView2.setVisibility(View.VISIBLE);
            }else if (itemVO2.acceptStauts == 3) {
//                holder.finishStatusView2.setText("已完成");
                holder.finishStatusView2.setVisibility(View.VISIBLE);
            } else {
                holder.finishStatusView2.setVisibility(View.GONE);
            }

            if(!itemVO2.isDirect){
                holder.ding2.setVisibility(View.INVISIBLE);
            } else {
                holder.ding2.setVisibility(View.VISIBLE);
            }

            holder.productImage2.setDefaultImageResId(R.drawable.tupian);
            holder.productImage2.setImageUrl(itemVO2.getPreviewImageUrl(), mImageLoader);
            holder.priceView21.setText("商品售价："+itemVO2.price+"");
            holder.priceView22.setText("酬金："+itemVO2.shotFee+"");
        }

        return convertView;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, TaskItemVO taskItemVO);
    }

    public static class ViewHolder {

        public View productItemLayout1;
        public View productItemLayout2;

        public NetworkImageView productImage1;
        public NetworkImageView productImage2;

        public TextView priceView11;
        public TextView priceView12;

        public TextView priceView21;
        public TextView priceView22;

        public TextView finishStatusView1;
        public TextView finishStatusView2;

        public ImageView ding1,ding2;

    }
}
