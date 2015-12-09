package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.TaskItemVO;
import com.floyd.diamond.biz.vo.MoteTypeTaskVO;

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
            productTypeVOs.clear();
        }

        this.productTypeVOs.addAll(productTypeVOs);
        this.notifyDataSetChanged();
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
            holder.productItemLayout1 = convertView.findViewById(R.id.product_type_item_1);
            holder.productItemLayout2 = convertView.findViewById(R.id.product_type_item_2);

            holder.priductImage1 = (NetworkImageView) convertView.findViewById(R.id.product_pic_1);
            holder.productImage2 = (NetworkImageView) convertView.findViewById(R.id.product_pic_2);

            holder.priceView11 = (TextView) convertView.findViewById(R.id.price_1_1);
            holder.priceView12 = (TextView) convertView.findViewById(R.id.price_1_2);
            holder.addressView1 = (TextView) convertView.findViewById(R.id.address_1);

            holder.priceView21 = (TextView) convertView.findViewById(R.id.price_2_1);
            holder.priceView22 = (TextView) convertView.findViewById(R.id.price_2_2);
            holder.addressView2 = (TextView) convertView.findViewById(R.id.address_2);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        MoteTypeTaskVO vo = getItem(position);
        if (vo.productItemVO1 == null) {
            holder.productItemLayout1.setVisibility(View.GONE);
            holder.productItemLayout1.setOnClickListener(null);
        } else {

            TaskItemVO itemVO1 = vo.productItemVO1;
            holder.productItemLayout1.setVisibility(View.VISIBLE);
            holder.productItemLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoteTaskTypeAdapter.this.itemClickListener != null) {
                        MoteTaskTypeAdapter.this.itemClickListener.onItemClick(v, position);
                    }
                }
            });

            holder.addressView1.setText(itemVO1.areaid+"");
            holder.priductImage1.setImageUrl(itemVO1.imgUrl, mImageLoader);
            holder.priceView11.setText(itemVO1.price+"");
            holder.priceView12.setText(itemVO1.totalFee+"");
        }

        if (vo.productItemVO2 == null) {
            holder.productItemLayout2.setVisibility(View.GONE);
            holder.productItemLayout1.setOnClickListener(null);
        } else {
            holder.productItemLayout2.setVisibility(View.VISIBLE);
            holder.productItemLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoteTaskTypeAdapter.this.itemClickListener != null) {
                        MoteTaskTypeAdapter.this.itemClickListener.onItemClick(v, position);
                    }
                }
            });

            TaskItemVO itemVO2 = vo.productItemVO2;
            holder.productItemLayout2.setVisibility(View.VISIBLE);
            holder.productItemLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoteTaskTypeAdapter.this.itemClickListener != null) {
                        MoteTaskTypeAdapter.this.itemClickListener.onItemClick(v, position);
                    }
                }
            });

            holder.addressView2.setText(itemVO2.areaid+"");
            holder.productImage2.setImageUrl(itemVO2.imgUrl, mImageLoader);
            holder.priceView21.setText(itemVO2.price+"");
            holder.priceView22.setText(itemVO2.totalFee+"");
        }

        return convertView;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public static class ViewHolder {

        public View productItemLayout1;
        public View productItemLayout2;

        public NetworkImageView priductImage1;
        public NetworkImageView productImage2;

        public TextView priceView11;
        public TextView priceView12;
        public TextView addressView1;

        public TextView priceView21;
        public TextView priceView22;
        public TextView addressView2;


    }
}
