package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.Care;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModelInfo;
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

    public CareAdapter1(Context context, ImageLoader imageLoader,ArrayList<String> deleteModel) {
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.deleteModel=deleteModel;
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

            holder.likecount1= ((CheckBox) convertView.findViewById(R.id.likecount1));
            holder.likecount2= ((CheckBox) convertView.findViewById(R.id.likecount2));
            holder.likecount1.setVisibility(View.INVISIBLE);
            holder.likecount2.setVisibility(View.INVISIBLE);

            holder.moteImage1 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_1);
            holder.moteImage2 = (NetworkImageView) convertView.findViewById(R.id.mote_pic_2);

            holder.carebg_1= ((TextView) convertView.findViewById(R.id.bg_recycle_1));
            holder.carebg_2= ((TextView) convertView.findViewById(R.id.bg_recycle_2));


            holder.name1 = (TextView) convertView.findViewById(R.id.name_1_1);
            holder.name2 = (TextView) convertView.findViewById(R.id.name_2_1);
//            holder.addressView1 = (TextView) convertView.findViewById(R.id.address_1);

            holder.address1 = (TextView) convertView.findViewById(R.id.address_1_2);
            holder.address2 = (TextView) convertView.findViewById(R.id.address_2_2);
//            holder.addressView2 = (TextView) convertView.findViewById(R.id.address_2);

            holder.item_1=convertView.findViewById(R.id.product_type_item_1);
            holder.item_2=convertView.findViewById(R.id.product_type_item_2);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        MoteTypeTaskVO vo = getItem(position);
        final Care.DataEntity.DataListEntity dataEntity_1 = vo.moteCareVO1;
        final Care.DataEntity.DataListEntity dataEntity_2 = vo.moteCareVO2;

        if (dataEntity_1 != null) {

            setLayout(holder.carebg_1, deleteModel, dataEntity_1);

//            holder.carebg_1.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            holder.carebg_1.setTag("cb" + dataEntity_1.getMoteId());

            holder.item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, dataEntity_1);
                    //此处处理具体点击
//                        myOnItemClickListener.onItemClick(v, dataEntity_1);
                }
            });




            holder.name1.setText(dataEntity_1.getNickname());
//            holder.address1.setText(dataEntity_1.getArea() + "");
            String imgUrl = dataEntity_1.getAvartUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            if (imgUrl != null) {
                holder.moteImage1.setImageUrl(imgUrl, mImageLoader);
            }
        }else{
            holder.item_1.setVisibility(View.INVISIBLE);
        }

        if (dataEntity_2 != null) {

            setLayout(holder.carebg_2,deleteModel,dataEntity_2);

//            holder.carebg_2.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            holder.carebg_2.setTag("cb"+dataEntity_2.getMoteId());
            holder.item_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, dataEntity_2);
                    //此处处理具体点击
//                        myOnItemClickListener.onItemClick(v, dataEntity_2);
                }
            });

            holder.name2.setText(dataEntity_2.getNickname());
//            holder.address2.setText(dataEntity_2.getArea() + "");
            String imgUrl = dataEntity_2.getAvartUrl();
            if (GlobalParams.isDebug) {
                Log.e("TAG", imgUrl + "");
            }
            if (imgUrl != null) {
                holder.moteImage2.setImageUrl(imgUrl, mImageLoader);
            }
        }else{
            holder.item_2.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    public void setLayout(View v, final ArrayList<String> deleteModel, final Care.DataEntity.DataListEntity dataListEntity){
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
    public void setMyOnItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener=onItemClickListener;

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
        void setText(String tag, boolean isChecked,int position);
    }
}

