package com.floyd.diamond.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
import com.floyd.diamond.event.SellerTaskEvent;
import com.floyd.diamond.ui.activity.ExpressActivity;
import com.floyd.diamond.ui.activity.NewTaskActivity;
import com.floyd.diamond.ui.seller.process.SellerTaskProcessActivity;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-26.
 */
public class SellerTaskDetailAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private List<SellerTaskDetailVO> taskItems = new ArrayList<SellerTaskDetailVO>();
    private Context mContext;
    private ViewHolder holder;

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

    public void updateStatus(SellerTaskEvent taskEvent) {
        if (taskItems == null || taskItems.isEmpty()) {
            return;
        }

        boolean isChanged = false;
        for (SellerTaskDetailVO vo : taskItems) {
            if (vo.moteTaskId == taskEvent.moteTaskId) {
                vo.status = taskEvent.status;
                vo.finishStatus = taskEvent.finishStatus;
                isChanged = true;
                break;
            }
        }

        if (isChanged) {
            this.notifyDataSetChanged();
        }
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
        holder = null;
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
        holder.taskPicView.setDefaultImageResId(R.drawable.tupian);
        holder.taskPicView.setImageUrl(taskItemVO.avartUrl, mImageLoader);
        holder.taskPicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, NewTaskActivity.class);
                it.putExtra(NewTaskActivity.TASK_TYPE_ITEM_ID, taskItemVO.id);
                mContext.startActivity(it);
            }
        });

        String expressNo = taskItemVO.expressNo;
        if (TextUtils.isEmpty(expressNo)) {
            holder.goodsOrderNoView.setVisibility(View.GONE);
            holder.goodsOrderNoView.setOnClickListener(null);
        } else {
            holder.goodsOrderNoView.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            String expressCompanyName = taskItemVO.expressCompanyName;
            if (TextUtils.isEmpty(expressCompanyName)) {
                sb.append("快递编号：");
            } else {
                sb.append(expressCompanyName+"：");
            }

            sb.append(expressNo);
            holder.goodsOrderNoView.setText(sb.toString());
//            holder.goodsOrderNoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent orderIntent = new Intent(mContext, ExpressActivity.class);
//                    orderIntent.putExtra(ExpressActivity.EXPRESS_MOTE_TASK_ID, taskItemVO.moteTaskId);
//                    mContext.startActivity(orderIntent);
//                }
//            });
        }

        String timeStr = DateUtil.getDateTime(taskItemVO.createTime);
        holder.publishTimeView.setText("接单时间:" + timeStr);
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
        } else if (status >= 6) {
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
        if (finishStatus == 1 || status >= 7) {
            holder.taskStatusView.setText("已结束");
            holder.taskStatusView.setTextColor(Color.parseColor("#999999"));
            holder.taskStatusView.setChecked(false);
        } else {
            if (status == 1) {

            }
            if (status >=5 ) {
                holder.taskStatusView.setChecked(true);
                holder.taskStatusView.setTextColor(Color.WHITE);
                holder.taskStatusView.setText("确定并满意");
//                //弹框显示满意
//                holder.taskPicView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(mContext,"您已确定并满意该任务！",Toast.LENGTH_SHORT).show();
//                        final UIAlertDialog.Builder builder = new UIAlertDialog.Builder(mContext);
//                        SpannableString message = new SpannableString(" 您已确定并满意该任务！");
//                        message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        builder.setMessage(message)
//                                .setCancelable(true)
//                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        AlertDialog dialog = builder.create();
//                        dialog.show();
//                    }
//                });
            } else {
                holder.taskStatusView.setChecked(true);
                holder.taskStatusView.setTextColor(Color.WHITE);
                holder.taskStatusView.setText("进行中");
            }
        }

       final  LoginVO loginVO= LoginManager.getLoginInfo(mContext);
        if (holder.taskStatusView.getText().toString().equals("确定并满意")){
            holder.taskStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                                        SellerManager.finishAndApproveMoteTask(taskItemVO.moteTaskId, 1, loginVO.token).startUI(new ApiCallback<Boolean>() {
                                            @Override
                                            public void onError(int code, String errorInfo) {
                                                Toast.makeText(mContext, errorInfo, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                holder.taskStatusView.setText("已结束");
                                                Toast.makeText(mContext,"您已确定并满意该任务！",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onProgress(int progress) {

                                            }
                                        });
                }
            });

        }

//        holder.taskStatusView.setOnClickListener(click);
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
