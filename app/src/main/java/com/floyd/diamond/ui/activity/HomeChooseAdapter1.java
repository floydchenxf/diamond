package com.floyd.diamond.ui.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hy on 2016/1/16.
 */
public class HomeChooseAdapter1 extends BaseAdapter {

    private List<MoteTypeTaskVO> productTypeVOs = new ArrayList<MoteTypeTaskVO>();
    private Context mContext;
    private OnItemClickListener itemClickListener;
    private ImageLoader mImageLoader;
    private ChangeText changeText;

    public HomeChooseAdapter1(Context context, ImageLoader imageLoader, OnItemClickListener itemClickListener,ChangeText changeText) {
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.itemClickListener=itemClickListener;
        this.changeText=changeText;
    }

    public void addAll(List<MoteTypeTaskVO> productTypeVOs, boolean isClear) {
        if (isClear) {
            this.productTypeVOs.clear();
        }

        this.productTypeVOs.addAll(productTypeVOs);
        this.notifyDataSetChanged();
    }

//    public void updateAcceptStatus(long taskId) {
//        for (MoteTypeTaskVO vo:productTypeVOs) {
//            if (vo.moteItemVO1 != null && vo.moteItemVO1.getId() == taskId) {
//                vo.moteItemVO1.isAccepted = true;
//            }
//
//            if (vo.productItemVO2 != null && vo.productItemVO2.id == taskId) {
//                vo.productItemVO2.isAccepted = true;
//            }
//        }
//    }

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
            convertView = View.inflate(mContext, R.layout.homechooseitem, null);
            holder = new ViewHolder();

            holder.likecount1 = (CheckBox) convertView.findViewById(R.id.likecount1);
            holder.likecount2 = (CheckBox) convertView.findViewById(R.id.likecount2);

            holder.moteImage1 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_1);
            holder.moteImage2 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_2);

            holder.name1 = (TextView) convertView.findViewById(R.id.name_1_1);
            holder.name2 = (TextView) convertView.findViewById(R.id.name_2_1);
//            holder.addressView1 = (TextView) convertView.findViewById(R.id.address_1);

            holder.address1 = (TextView) convertView.findViewById(R.id.address_1_2);
            holder.address2 = (TextView) convertView.findViewById(R.id.address_2_2);

            holder.item_1=convertView.findViewById(R.id.product_type_item_1);
            holder.item_2=convertView.findViewById(R.id.product_type_item_2);
//            holder.addressView2 = (TextView) convertView.findViewById(R.id.address_2);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        holder.moteImage1.setDefaultImageResId(R.drawable.tupian);
        holder.moteImage2.setDefaultImageResId(R.drawable.tupian);

        MoteTypeTaskVO vo = getItem(position);

        if (vo.moteItemVO1!=null){
            holder.item_1.setVisibility(View.VISIBLE);
            if (!vo.moteItemVO1.isIsFollow()){
                holder.likecount1.setChecked(false);
            } else {
                holder.likecount1.setChecked(true);
            }
            holder.likecount1.setText(vo.moteItemVO1.getFollowNum() + "");
            holder.likecount1.setTag("cb" + vo.moteItemVO1.getId());
            final ModelInfo.DataEntity dataEntity_1 = vo.moteItemVO1;
            holder.item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, dataEntity_1);
                }
            });
            holder.likecount1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (changeText != null) {

                        changeText.setText((String) buttonView.getTag(), isChecked, dataEntity_1);
                    }
                }
            });

            holder.name1.setText(vo.moteItemVO1.getNickname());
            if (vo.moteItemVO1.getArea()!=null){
                holder.address1.setText(vo.moteItemVO1.getArea() + "");
            }else{
                holder.address1.setText("未知");
            }

            String imgUrl = vo.moteItemVO1.getPreviewUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            holder.moteImage1.setImageUrl(imgUrl, mImageLoader);
        }else{
            holder.item_1.setVisibility(View.INVISIBLE);
        }

        if (vo.moteItemVO2!=null){
            holder.item_2.setVisibility(View.VISIBLE);
            if (!vo.moteItemVO2.isIsFollow()){
                holder.likecount2.setChecked(false);
            }
            holder.likecount2.setText(vo.moteItemVO2.getFollowNum() + "");
            holder.likecount2.setTag("cb" + vo.moteItemVO2.getId());
            final ModelInfo.DataEntity dataEntity_2 = vo.moteItemVO2;
            holder.likecount2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (changeText != null) {

                        changeText.setText((String) buttonView.getTag(), isChecked, dataEntity_2);
                    }
                }
            });
            holder.item_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, dataEntity_2);
                }
            });
            if (vo.moteItemVO2.isIsFollow()) {
                holder.likecount2.setChecked(true);
            }

            holder.name2.setText(vo.moteItemVO2.getNickname());
            if (vo.moteItemVO2.getArea()!=null){
                holder.address2.setText(vo.moteItemVO2.getArea() + "");
            }else{
                holder.address2.setText("未知");
            }

            String imgUrl = vo.moteItemVO2.getPreviewUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            holder.moteImage2.setImageUrl(imgUrl, mImageLoader);
        }else{
            holder.item_2.setVisibility(View.INVISIBLE);
        }



        return convertView;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, ModelInfo.DataEntity dataEntity);
    }

    public static class ViewHolder {


        public NetworkImageView moteImage1;
        public NetworkImageView moteImage2;

        private View item_1;
        private View item_2;

        public CheckBox likecount1;
        public CheckBox likecount2;
//        public TextView addressView1;

        public TextView name1;
        public TextView name2;
//        public TextView addressView2;

        public TextView address1;
        public TextView address2;


    }

    public static interface ChangeText {
        void setText(String tag, boolean isChecked,ModelInfo.DataEntity dataEntity);
    }
}
