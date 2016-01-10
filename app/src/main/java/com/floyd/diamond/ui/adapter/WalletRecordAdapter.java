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
import com.floyd.diamond.biz.vo.mote.MoteWalletRecordVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-29.
 */
public class WalletRecordAdapter extends BaseAdapter {

    private List<MoteWalletRecordVO> moteWalletRecordList = new ArrayList<MoteWalletRecordVO>();
    private Context mContext;

    public WalletRecordAdapter(Context context) {
        this.mContext = context;
    }

    public void addAll(List<MoteWalletRecordVO> records, boolean clear) {
        if (clear) {
            this.moteWalletRecordList.clear();
        }

        this.moteWalletRecordList.addAll(records);
        this.notifyDataSetChanged();
    }

    public List<MoteWalletRecordVO> getWalletRecords() {
        return this.moteWalletRecordList;
    }


    @Override
    public int getCount() {
        return moteWalletRecordList.size();
    }

    @Override
    public MoteWalletRecordVO getItem(int position) {
        return moteWalletRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.wallet_record_item, null);
            holder = new ViewHolder();
            holder.payDayView = (TextView) convertView.findViewById(R.id.pay_day_view);
            holder.payTimeView = (TextView) convertView.findViewById(R.id.pay_time_view);
            holder.drawMoneyView = (TextView) convertView.findViewById(R.id.draw_money_view);
            holder.payDrawView = (TextView) convertView.findViewById(R.id.pay_draw_view);
            holder.drawDetailLayout = convertView.findViewById(R.id.pay_detail_info_layout);
            holder.alipayIdView = (TextView) convertView.findViewById(R.id.alipay_id_view);
            holder.alipayNoView = (TextView) convertView.findViewById(R.id.alipay_no_view);
            holder.alipayNameView = (TextView) convertView.findViewById(R.id.alipay_name_view);
            holder.drawMoneyStatusView = (ImageView) convertView.findViewById(R.id.draw_money_status_view);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        MoteWalletRecordVO moteWalletRecordVO = getItem(position);
        int status = moteWalletRecordVO.status;
        long createTime = moteWalletRecordVO.createTime;
        String timeString = DateUtil.getDateTime(createTime);
        String[] times = timeString.split(" ");
        holder.payDayView.setText(times[0]);
        holder.payTimeView.setText(times[1]);
        holder.drawMoneyView.setText("￥"+moteWalletRecordVO.money);
        if (status == 1) {
            holder.drawMoneyView.setTextColor(Color.parseColor("#666666"));
            holder.drawDetailLayout.setVisibility(View.GONE);
            holder.payDrawView.setVisibility(View.VISIBLE);
            holder.drawMoneyStatusView.setImageResource(R.drawable.draw_money_process);
        } else {
            holder.drawMoneyView.setTextColor(Color.parseColor("#d4377e"));
            holder.drawDetailLayout.setVisibility(View.VISIBLE);
            holder.payDrawView.setVisibility(View.GONE);
            holder.alipayIdView.setText(moteWalletRecordVO.alipayId);
            holder.alipayNameView.setText(moteWalletRecordVO.alipayName);
            holder.alipayNoView.setText(moteWalletRecordVO.alipayNo);
            holder.drawMoneyStatusView.setImageResource(R.drawable.draw_money_finish);
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
        public ImageView drawMoneyStatusView;
    }
}
