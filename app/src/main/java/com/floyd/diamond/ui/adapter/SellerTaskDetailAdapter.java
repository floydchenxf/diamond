package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
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
            holder.taskStatusView = (TextView) convertView.findViewById(R.id.task_status);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        final SellerTaskDetailVO taskItemVO = getItem(position);
        holder.titleView.setText(taskItemVO.title);
        holder.taskPicView.setImageUrl(taskItemVO.avartUrl, mImageLoader);
        holder.taskStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SellerTaskProcessActivity.class);
                it.putExtra(SellerTaskProcessActivity.SELLER_MOTE_TASK_ID, taskItemVO.moteTaskId);
                mContext.startActivity(it);
            }
        });
        int status = taskItemVO.status;

        return convertView;
    }


    public static class ViewHolder {
        public NetworkImageView taskPicView;
        public TextView titleView;
        public TextView taskStatusView;
    }

}
