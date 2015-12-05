package com.floyd.diamond.biz.constants;

/**
 * Created by floyd on 15-11-28.
 */
public class APIConstants {
    public static final String HOST = "http://120.27.132.126:9999/mote/";
    public static final String API_VERIFY_CODE = "api/user/sendVerifyCode";//发送验证码
    public static final String API_CHANGE_PASSWORD_VERIFY_CODE = "api/user/changePasswordByVerifyCode";//修改密码的验证码
    public static final String API_USER_LOGIN = "api/user/login";//登录
    public static final String API_REG_USER = "api/user/register";//注册
    public static final String API_MY_MOTE_INFO = "api/user/myMoteInfo";//模特信息
    public static final String API_MY_SELLER_INFO = "api/user/mySellerInfo";//卖家信息

    public static final String API_UPDATE_MONTE_AVART = "api/user/updateMoteAvart";//更新头像

    public static final String API_GET_ADVERT_LIST = "api/index/getAdvertList";//获取广告

    public static final String API_GET_MOTE_LIST = "api/index/getMoteList"; //获取首页模特信息
}
