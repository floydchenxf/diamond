package com.floyd.diamond.biz.vo.seller;

/**
 * Created by floyd on 16-1-10.
 */
public class SellerWalletRecordVO {
    public float afterFreezeFee;
    public float beforeFreezeFee;
    public String reason;
    public long userId;
    public float freezeChange; //冻结金额
    public float remindChange;
    public long creatTime;
    public int type;
    public float beforeRemindFee;
    public float afterRemindFee;
    public int reduceCashStatus;
}
