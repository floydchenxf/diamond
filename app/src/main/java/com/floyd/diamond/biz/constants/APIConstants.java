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
    public static final String API_ADD_FOLLOW = "api/follow/addFollow"; //关注模特
    public static final String API_CANCEL_FOLLOW = "api/follow/acancelFollow"; //取消关注模特
    public static final String API_ADV_DETAIL_INFO = "api/index/getAdvertDetail";//广告详情
    public static final String API_MOTE_DETAIL_INFO = "api/user/getMoteInfo";//模特详情
    public static final String API_MOTE_TASK_PICS = "api/taskPic/getMoteTaskPics";
    public static final String API_MOTE_TASK_SEARCH = "api/task/search";//任务查询
    public static final String API_NEW_MOTE_TASK = "api/task/newMoteTask"; //模特接单
    public static final String API_MOTE_MY_TASK = "api/motetask/getMyTaskList";//模特我的任务
    public static final String CHOOSEMOTE="api/index/filterMote";//筛选模特
    public static final String API_INDEX_INFO = "api/index/getIndexInfo";//获取首页导航栏目
    public static final String API_ADD_ORDER_NO = "api/task/addOrderNo"; //模特添加订单
    public static final String API_FINISH_SHOW_PIC = "api/task/finishShowPic";//模特儿完成晒图
    public static final String API_SELF_BUY = "api/task/selfBuy";//模特儿自购商品
    public static final String API_RETURN_ITEM = "api/task/returnItem";//模特儿退回商品
    public static final String API_UPLOAD_IMAGE = "api/taskPic/uploadImage"; //模特儿上传照片
    public static final String API_REMOVE_IMAGE_URL="api/taskPic/removeImageUrl";//模特儿删除图片

    public static final String API_TASK_PROCESS="api/task/getMoteTaskProcess";//模特任务进程
}
