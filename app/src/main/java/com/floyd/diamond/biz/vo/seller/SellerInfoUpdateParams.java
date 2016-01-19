package com.floyd.diamond.biz.vo.seller;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 15-12-29.
 */
public class SellerInfoUpdateParams {

    public String shopName;
    public String nickname;
    public String chargePersonMobile;
    public String weixin;
    public String qq;
    public String alipayId;
    public String alipayName;
    public String returnItemPerson;
    public String returnItemMobile;
    public String returnItemTelCode;
    public String returnItemTelNumber;
    public long provineId;
    public long cityId;
    public long districtId;
    public String address;
    public int msgSwitch; //msgSwitch(1开 0关)
    public String token;


    public Map<String, String> convert2Map() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("shopName", shopName == null ? "" : shopName);
        params.put("nickname", nickname == null ? "" : nickname);
        params.put("chargePersonMobile", chargePersonMobile == null ? "" : chargePersonMobile);
        params.put("weixin", weixin == null ? "" : weixin);
        params.put("qq", qq == null ? "" : qq);
        params.put("alipayId", alipayId == null ? "" : alipayId);
        params.put("alipayName", alipayName == null ? "" : alipayName);
        params.put("returnItemPerson", returnItemPerson == null ? "" : returnItemPerson);
        params.put("returnItemMobile", returnItemMobile == null ? "" : returnItemMobile);
        params.put("returnItemTelCode", returnItemTelCode == null ? "" : returnItemTelCode);
        params.put("returnItemTelNumber", returnItemTelNumber == null ? "" : returnItemTelNumber);
        params.put("provinceId", provineId == 0l ? "" : provineId + "");
        params.put("cityId", cityId == 0l ? "" : cityId + "");
        params.put("districtId", districtId == 0l ? "" : districtId + "");
        params.put("address", address == null ? "" : address);
        params.put("token", token == null ? "" : token);
        return params;
    }
}
