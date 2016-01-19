package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.Care;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hy on 2016/1/17.
 */
public class CareAdapter1 extends BaseAdapter {

    private List<MoteTypeTaskVO> productTypeVOs = new ArrayList<MoteTypeTaskVO>();
    private Context mContext;
    private ImageLoader mImageLoader;
    private OnItemClickListener itemClickListener;
    private ChangeText changeText;
    private ArrayList<String> deleteModel;

    public CareAdapter1(Context context, ImageLoader imageLoader, ArrayList<String> deleteModel) {
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.deleteModel = deleteModel;
    }

    public void addAll(List<MoteTypeTaskVO> productTypeVOs, boolean isClear) {
        if (isClear) {
            this.productTypeVOs.clear();
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
            convertView = View.inflate(mContext, R.layout.homechooseitem, null);
            holder = new ViewHolder();

            holder.likecount1 = ((CheckBox) convertView.findViewById(R.id.likecount1));
            holder.likecount2 = ((CheckBox) convertView.findViewById(R.id.likecount2));
            holder.likecount1.setVisibility(View.INVISIBLE);
            holder.likecount2.setVisibility(View.INVISIBLE);

            holder.moteImage1 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_1);
            holder.moteImage2 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_2);


            holder.carebg_1 = ((TextView) convertView.findViewById(R.id.bg_recycle_1));
            holder.carebg_2 = ((TextView) convertView.findViewById(R.id.bg_recycle_2));


            holder.name1 = (TextView) convertView.findViewById(R.id.name_1_1);
            holder.name2 = (TextView) convertView.findViewById(R.id.name_2_1);

            holder.address1 = (TextView) convertView.findViewById(R.id.address_1_2);
            holder.address2 = (TextView) convertView.findViewById(R.id.address_2_2);

            holder.item_1 = convertView.findViewById(R.id.product_type_item_1);
            holder.item_2 = convertView.findViewById(R.id.product_type_item_2);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.moteImage1.setDefaultImageResId(R.drawable.tupian);
        holder.moteImage2.setDefaultImageResId(R.drawable.tupian);

        MoteTypeTaskVO vo = getItem(position);

        if (vo.moteCareVO1 != null) {
            holder.item_1.setVisibility(View.VISIBLE);
            setLayout(holder.carebg_1, deleteModel, vo.moteCareVO1);
            holder.carebg_1.setTag("cb" + vo.moteCareVO1.getMoteId());

            final Care.DataEntity.DataListEntity dataEntity_1 = vo.moteCareVO1;
            holder.item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, dataEntity_1);
                    }
                }
            });


            holder.name1.setText(vo.moteCareVO1.getNickname());
            String imgUrl = vo.moteCareVO1.getPreviewUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            if (imgUrl != null) {
                holder.moteImage1.setImageUrl(imgUrl, mImageLoader);
            }
        } else {
            holder.item_1.setVisibility(View.INVISIBLE);
        }

        if (vo.moteCareVO2 != null) {
            holder.item_2.setVisibility(View.VISIBLE);
            setLayout(holder.carebg_2, deleteModel, vo.moteCareVO2);

            holder.carebg_2.setTag("cb" + vo.moteCareVO2.getMoteId());
            final Care.DataEntity.DataListEntity dataEntity_2 = vo.moteCareVO2;
            holder.item_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, dataEntity_2);
                    }
                }
            });

            holder.name2.setText(vo.moteCareVO2.getNickname());
            String imgUrl = vo.moteCareVO2.getPreviewUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            if (imgUrl != null) {
                holder.moteImage2.setImageUrl(imgUrl, mImageLoader);
            }
        } else {
            holder.item_2.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    public void setLayout(View v, final ArrayList<String> deleteModel, final Care.DataEntity.DataListEntity dataListEntity) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                deleteModel.remove(dataListEntity.getMoteId());

            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Care.DataEntity.DataListEntity dataEntity);
    }

    public void setMyOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;

    }

//    public interface MyOnItemClickListener {
//        void onItemClick(View view, Care.DataEntity.DataListEntity dataEntity);//具体的点击事件
//    }
//
//    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
//        this.myOnItemClickListener = myOnItemClickListener;
//    }


    public static class ViewHolder {


        public NetworkImageView moteImage1;
        public NetworkImageView moteImage2;

        public CheckBox likecount1;
        public CheckBox likecount2;
//        public TextView addressView1;

        public TextView name1;
        public TextView name2;
//        public TextView addressView2;

        public TextView address1;
        public TextView address2;

        public TextView carebg_1;
        public TextView carebg_2;

        private View item_1;
        private View item_2;


    }

    public static interface ChangeText {
        void setText(String tag, boolean isChecked, int position);
    }
}

