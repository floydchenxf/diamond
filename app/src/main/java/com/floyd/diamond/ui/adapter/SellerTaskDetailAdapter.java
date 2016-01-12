package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
import com.floyd.diamond.ui.activity.ExpressActivity;
import com.floyd.diamond.ui.seller.process.SellerTaskProcessActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-26.
 */
public class SellerTaskDetailAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private List<SellerTaskDetailVO> taskItems = new ArrayList<SellerTaskDetailVO>();
    private Context mContext;

    public SellerTaskDetailAdapter(Context context, ImageLoader mImageLoader) {
        this.mContext = context;
        this.mImageLoader = mImageLoader;
    }

    public void addAll(List<SellerTaskDetailVO> tasks, boolean isClear) {
        if (isClear) {
            taskItems.clear();
        }
        taskItems.addAll(tasks);
        this.notifyDataSetChanged();
    }

    public List<SellerTaskDetailVO> getData() {
        return this.taskItems;
    }


    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public SellerTaskDetailVO getItem(int position) {
        return taskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.seller_task_detail_item, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.task_title);
            holder.taskPicView = (NetworkImageView) convertView.findViewById(R.id.task_pic);
            holder.taskStatusView = (CheckedTextView) convertView.findViewById(R.id.task_status);
            holder.publishTimeView = (TextView) convertView.findViewById(R.id.publish_time_view);
            holder.goodsStatusView = (TextView) convertView.findViewById(R.id.goods_status_view);
            holder.goodsOrderNoView = (TextView) convertView.findViewById(R.id.goods_order_no_view);
            holder.moteNicknameView = (TextView) convertView.findViewById(R.id.mote_nick_name_view);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        final SellerTaskDetailVO taskItemVO = getItem(position);
        holder.titleView.setText(taskItemVO.title);
        holder.taskPicView.setImageUrl(taskItemVO.avartUrl, mImageLoader);

        String expressNo = taskItemVO.expressNo;
        if (TextUtils.isEmpty(expressNo)) {
            holder.goodsOrderNoView.setVisibility(View.GONE);
            holder.goodsOrderNoView.setOnClickListener(null);
        } else {
            holder.goodsOrderNoView.setVisibility(View.VISIBLE);
            holder.goodsOrderNoView.setText("运单编号：" + expressNo);
            holder.goodsOrderNoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderIntent = new Intent(mContext, ExpressActivity.class);
                    orderIntent.putExtra(ExpressActivity.EXPRESS_MOTE_TASK_ID, taskItemVO.moteTaskId);
                    mContext.startActivity(orderIntent);
                }
            });
        }

        String nickName = taskItemVO.nickname;
        if (TextUtils.isEmpty(nickName)) {
            holder.moteNicknameView.setVisibility(View.GONE);
        } else {
            holder.moteNicknameView.setVisibility(View.VISIBLE);
            holder.moteNicknameView.setText("模特：" + nickName);
        }

        int status = taskItemVO.status;
        if (status == 5) {
            holder.goodsStatusView.setVisibility(View.VISIBLE);
            holder.goodsStatusView.setText("商品：自购");
        } else if (status == 6) {
            holder.goodsStatusView.setVisibility(View.VISIBLE);
            holder.goodsStatusView.setText("商品：退回");
        } else {
            holder.goodsStatusView.setVisibility(View.GONE);
        }

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SellerTaskProcessActivity.class);
                it.putExtra(SellerTaskProcessActivity.SELLER_MOTE_TASK_ID, taskItemVO.moteTaskId);
                mContext.startActivity(it);
            }
        };

        int finishStatus = taskItemVO.finishStatus;
        if (finishStatus == 1) {
            holder.taskStatusView.setText("已结束");
            holder.taskStatusView.setTextColor(Color.parseColor("#333333"));
            holder.taskStatusView.setChecked(false);
        } else {
            if (status >=5 ) {
                holder.taskStatusView.setChecked(true);
                holder.taskStatusView.setTextColor(Color.WHITE);
                holder.taskStatusView.setText("确认并满意");
            } else {
                holder.taskStatusView.setChecked(true);
                holder.taskStatusView.setTextColor(Color.WHITE);
                holder.taskStatusView.setText("进行中");
            }
        }

        holder.publishTimeView.setVisibility(View.GONE);
        holder.taskStatusView.setOnClickListener(click);
        return convertView;
    }


    public static class ViewHolder {
        public NetworkImageView taskPicView;//图片
        public TextView titleView; //标题
        public CheckedTextView taskStatusView; //状态
        public TextView moteNicknameView; //模特别名
        public TextView goodsOrderNoView; //订单编号
        public TextView goodsStatusView; //商品状态
        public TextView publishTimeView; //防单时间
    }

}
