package com.floyd.diamond.biz.vo.seller;

import com.floyd.diamond.biz.vo.IKeepClassForProguard;

/**
 * Created by floyd on 15-12-30.
 */
public class SellerWalletVO implements IKeepClassForProguard {
    public float selfBuyMoneyTotal;//自购总金额
    public float remindFee;//帐号余额
    public float unFinishMoney;//未完结任务金额
    public int selfBuyNumTotal;//模特自购数量
    public float shotFeeTotal;//佣金
    public float addCashTotal;//累计充值金额
}