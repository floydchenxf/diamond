package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.seller.SellerWalletRecordVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 16-1-10.
 */
public class SellerWalletRecordAdapter extends BaseAdapter {

    private Context mContext;

    private List<SellerWalletRecordVO> records = new ArrayList<SellerWalletRecordVO>();

    public SellerWalletRecordAdapter(Context context) {
        this.mContext = context;
    }

    public void addAll(List<SellerWalletRecordVO> list, boolean clear) {
        if (clear) {
            this.records.clear();
        }

        this.records.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public SellerWalletRecordVO getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.seller_wallet_record_item, null);
            holder = new ViewHolder();
            holder.payDayView = (TextView) convertView.findViewById(R.id.pay_day_view);
            holder.payTimeView = (TextView) convertView.findViewById(R.id.pay_time_view);
            holder.drawMoneyView = (TextView) convertView.findViewById(R.id.draw_money_view);
            holder.payDrawView = (TextView) convertView.findViewById(R.id.pay_draw_view);
            holder.drawDetailLayout = convertView.findViewById(R.id.pay_detail_info_layout);
            holder.alipayIdView = (TextView) convertView.findViewById(R.id.alipay_id_view);
            holder.alipayNoView = (TextView) convertView.findViewById(R.id.alipay_no_view);
            holder.alipayNameView = (TextView) convertView.findViewById(R.id.alipay_name_view);
            holder.walletStatusView = (ImageView) convertView.findViewById(R.id.wallet_status_view);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        SellerWalletRecordVO sellerWalletRecordVO = getItem(position);
        int type = sellerWalletRecordVO.type;
        long createTime = sellerWalletRecordVO.creatTime;
        String timeString = DateUtil.getDateTime(createTime);
        String[] times = timeString.split(" ");
        holder.payDayView.setText(times[0]);
        holder.payTimeView.setText(times[1]);
        holder.drawMoneyView.setText("￥" + sellerWalletRecordVO.freezeChange);
        if (type == 1) {
            holder.drawMoneyView.setTextColor(Color.parseColor("#4193c0"));
            holder.drawDetailLayout.setVisibility(View.GONE);
            holder.payDrawView.setVisibility(View.VISIBLE);
            holder.payDrawView.setText(sellerWalletRecordVO.reason);
            holder.payDrawView.setTextSize(12);
            holder.walletStatusView.setImageResource(R.drawable.publish_task);
        } else if (type == 2) {
            holder.drawMoneyView.setTextColor(Color.parseColor("#d4377e"));
            holder.drawDetailLayout.setVisibility(View.GONE);
            holder.payDrawView.setVisibility(View.VISIBLE);
            holder.payDrawView.setText(sellerWalletRecordVO.reason);
            holder.payDrawView.setTextSize(12);
            holder.walletStatusView.setImageResource(R.drawable.self_buy);
        } else if (type == 3) {
            holder.drawDetailLayout.setVisibility(View.GONE);
            holder.payDrawView.setVisibility(View.VISIBLE);
            holder.payDrawView.setText(sellerWalletRecordVO.reason);
            holder.payDrawView.setTextSize(24);
            if (sellerWalletRecordVO.reduceCashStatus == 1) {
                holder.drawMoneyView.setTextColor(Color.parseColor("#666666"));
                holder.walletStatusView.setImageResource(R.drawable.draw_money_process);
            } else if (sellerWalletRecordVO.reduceCashStatus == 2) {
                holder.drawMoneyView.setTextColor(Color.parseColor("#4193c0"));
                holder.walletStatusView.setImageResource(R.drawable.draw_money_finish);
            } else {
                holder.drawMoneyView.setTextColor(Color.parseColor("#666666"));
                holder.walletStatusView.setImageResource(R.drawable.draw_money_process);
            }
        } else if (type == 4) {
            holder.drawMoneyView.setTextColor(Color.parseColor("#d4377e"));
            holder.drawDetailLayout.setVisibility(View.GONE);
            holder.payDrawView.setVisibility(View.VISIBLE);
            holder.payDrawView.setText(sellerWalletRecordVO.reason);
            holder.payDrawView.setTextSize(12);
            holder.walletStatusView.setImageResource(R.drawable.add_cash);
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView payDayView; //创建天
        public TextView payTimeView; //创建时间
        public TextView drawMoneyView; //提现金额
        public TextView payDrawView; //提现描述

        public View drawDetailLayout;
        public TextView alipayIdView; //支付宝ID
        public TextView alipayNoView; //支付流水号
        public TextView alipayNameView; //支付宝名称
        public ImageView walletStatusView;
    }
}
