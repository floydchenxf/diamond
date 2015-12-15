package com.floyd.diamond.biz.vo.process;

/**
 * Created by floyd on 15-12-16.
 */
public class ProcessStatus {
    public static final int FOLLOW = 0; //关注
    public static final int ACCEPT_ORDER = 1; //接单
    public static final int FILL_ORDER = 2; //填写订单
    public static final int FINISH_BUY_GOODS = 3; //购买商品
    public static final int UPLOAD_PICS = 4; //上传图片
    public static final int SELF_BUY_GOODS = 5;//自购商品
    public static final int RETURN_GOODS = 6; //退回商品
    public static final int CONFLICT = 7;//存在冲突
    public static final int CONFIRM_RETURN_GOODS = 8;//确认退回商品
}
