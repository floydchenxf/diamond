package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.seller.SellerTaskItemVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-26.
 */
public class SellerTaskAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private List<SellerTaskItemVO> taskItems = new ArrayList<SellerTaskItemVO>();
    private Context mContext;
    private static final int TIME_EVENT = 1;

    private StatusCallback statusCallback;

    public SellerTaskAdapter(Context context, ImageLoader mImageLoader, StatusCallback statusCallback) {
        this.mContext = context;
        this.mImageLoader = mImageLoader;
        this.statusCallback = statusCallback;
    }

    public void addAll(List<SellerTaskItemVO> tasks, boolean isClear) {
        if (isClear) {
            taskItems.clear();
        }
        taskItems.addAll(tasks);
        this.notifyDataSetChanged();
    }

    public List<SellerTaskItemVO> getData() {
        return this.taskItems;
    }


    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public SellerTaskItemVO getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.seller_task_item, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.task_title);
            holder.taskPicView = (NetworkImageView) convertView.findViewById(R.id.task_pic);
            holder.taskNumberView = (TextView) convertView.findViewById(R.id.task_number);
            holder.taskStatusView = (TextView) convertView.findViewById(R.id.task_status);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        final SellerTaskItemVO taskItemVO = getItem(position);
        holder.taskPicView.setImageUrl(taskItemVO.getPreviewImageUrl(), mImageLoader);
        holder.titleView.setText(taskItemVO.title);
        holder.taskNumberView.setVisibility(View.GONE);
        holder.taskStatusView.setVisibility(View.GONE);
        int status = taskItemVO.status;
        if (status == 0) {
            //支付
            holder.taskStatusView.setVisibility(View.VISIBLE);
            holder.taskStatusView.setText("待支付");
            if (statusCallback != null) {
                statusCallback.doCallback(holder.taskStatusView, status);
            }
        } else if (status == 2 || status == 3) {
            //数字
            holder.taskNumberView.setVisibility(View.VISIBLE);
            int number = taskItemVO.number;
            holder.taskNumberView.setText(number + "");

        } else if (status == 4) {
            //再发布
            holder.taskStatusView.setVisibility(View.VISIBLE);
            holder.taskStatusView.setText("已结束");
            if (statusCallback != null) {
                statusCallback.doCallback(holder.taskStatusView, status);
            }
        }

        return convertView;
    }


    public static class ViewHolder {
        public NetworkImageView taskPicView;
        public TextView titleView;
        public TextView taskNumberView;
        public TextView taskStatusView;
    }

    public interface StatusCallback {
        void doCallback(View v, int status);
    }
}
