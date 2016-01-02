package com.floyd.diamond.biz.vo.seller;

import com.floyd.diamond.biz.vo.IKeepClassForProguard;
import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-11-29.
 */
public class SellerInfoVO implements IKeepClassForProguard {
    public String area;//区域
    public String avartUrl;//avatarUrl;//头像
    public String nickname;//别名
    public String shopName;//店铺名称
    public int orderNum;//放单数
    public String credit;//信用等级

    public String getPreviewUrl() {
        return CommonUtil.getImage_200(this.avartUrl);
    }
}
