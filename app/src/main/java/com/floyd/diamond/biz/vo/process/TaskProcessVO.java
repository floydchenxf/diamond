package com.floyd.diamond.biz.vo.process;

import com.floyd.diamond.biz.vo.mote.TaskItemVO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by floyd on 15-12-13.
 *
 * * {"id":222,"userId":122,"taskId":113,"createTime":1444989969000,"updateTime":1445830018000,
 * "orderNo":"1338159613054947","expressCompanyId":"顺丰","expressNo":"国通2749157197",
 * "selfBuyFee":23300,"status":8,"finishStatus":1,"acceptedTime":1444990140000,
 * "orderNoTime":1444990349000,"showPicTime":1445261905000,"uploadPicTime":1445569090000,"returnItemTime":1445569121000,
 * "finishStatusTime":1445830018000,
 * "selfBuyTime":null,"picApproveStatus":0},
 * "address":"杭州市江干区碑亭路53号新九天女装1A620",
 * "task":{"id":113,"userId":112,"title":"上衣女2015新款韩版女装韩国东大门大码格子宽松毛呢外套中长款女","url":"https://item.taobao.com/item.htm?spm=a1z10.1-c.w4004-12154375427.2.xf6ZwA&id=43216315861","price":198.0,"priceFen":0,"shotFee":35.0,"shotFeeFen":0,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/16/308d13e3-938b-457e-882b-b04fca69eefb.jpg","selfBuyOff":95.0,"shotDesc":"生活化些 外景","gender":0,"shape":"1","heightMin":160,"heightMax":168,"ageMin":20,"ageMax":28,"modelerLevel":0,"selfBuyRate":0,"number":10,"shotAreaId":0,"status":2,"createTime":1444984310000,"updateTime":1448589338000,"finishTime":null,"payTime":null,"totalFee":2330.0,"totalFeeFen":0,"nickname":"","oldUrl":"https://item.taobao.com/item.htm?spm=a1z10.1-c.w4004-12154375427.2.xf6ZwA&id=43216315861","isAccepted":false,"acceptNumber":0,"followNum":0,"sendType":2,"itemType":4,"freeMail":1,"areaIds":"","finishStatus":0,"areaName":null},
 * "seller":{"id":112,"phoneNumber":"18668125786","password":"893BFA615C9DD21A853BE4E9104F97C0","avartUrl":null,"nickname":null,"gender":null,"birdthday":null,"birdthdayStr":null,"height":null,"weight":null,"areaId":null,"wangwang":null,"alipayId":null,"alipayName":null,"status":2,"type":2,"shopName":"卡芙","email":"526930748@qq.com","weixin":"18668125786","address":"杭州市江干区碑亭路53号新九天女装1A620","referee":"","remindFee":0.0,"freezeFee":0.0,"remindFeeFen":null,"freezeFeeFen":null,"deviceId":"","loginType":null,"ip":null,"smsCode":null,"shape":null,"age":null,"finishNum":null,"selfBuyRate":null,"goodEvalRate":null,"followNum":null,"totalIncome":null,"moteLevel":null,"provinceId":null,"provinceName":null,"cityId":null,"cityName":null,"districtId":null,"districtName":null,"moteType":null,"realName":null,"idNumber":null,"msgSwitch":null,"authenStatus":0,"authenPic1":null,"authenPic2":null,"authenPic3":null,"returnItemMobile":null},
 * "picNum":6,
 * "user":{"id":122,"phoneNumber":"13738556175","password":"EDF006BB81E0F9FDD836AC8390406534","avartUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/16/4dea0993-3cb0-49b3-989e-18b3f68c880f.jpg","nickname":"胡大心","gender":0,"birdthday":776275200000,"birdthdayStr":null,"height":168,"weight":50,"areaId":null,"wangwang":"胡潆心1997","alipayId":"13738556175","alipayName":"胡潆心","status":2,"type":1,"shopName":null,"email":null,"weixin":null,"address":null,"referee":null,"remindFee":0.0,"freezeFee":0.0,"remindFeeFen":null,"freezeFeeFen":null,"deviceId":"","loginType":null,"ip":null,"smsCode":null,"shape":null,"age":22,"finishNum":null,"selfBuyRate":null,"goodEvalRate":null,"followNum":null,"totalIncome":null,"moteLevel":null,"provinceId":null,"provinceName":null,"cityId":null,"cityName":null,"districtId":null,"districtName":null,"moteType":1,"realName":null,"idNumber":null,"msgSwitch":null,"authenStatus":0,"authenPic1":null,"authenPic2":null,"authenPic3":null,"returnItemMobile":null},
 * "picList":[{"id":270,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/4c0d6ac7-eff5-40f3-aa04-90fc23e89cbb.jpeg","sort":0,"createTime":1445261931000,"updateTime":1445261931000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":273,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/22b7c019-c5aa-4902-a9dc-3ed82d30ae73.jpeg","sort":0,"createTime":1445261970000,"updateTime":1445261970000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":274,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/19/436d8a68-0ea9-4071-abf0-79a6a7d348ba.jpeg","sort":0,"createTime":1445261983000,"updateTime":1445261983000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":462,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/83679223-91d0-4e46-853e-e891403f714d.jpeg","sort":0,"createTime":1445568999000,"updateTime":1445568999000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},
 * {"id":463,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/21068797-6059-4a2b-86fb-27241d27cd6f.jpeg","sort":0,"createTime":1445569046000,"updateTime":1445569046000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false},{"id":464,"moteTaskId":222,"imgUrl":"http://qmmt2015.b0.upaiyun.com/2015/10/23/19949bbb-13d4-45d2-801e-2ee4860429f1.jpeg","sort":0,"createTime":1445569090000,"updateTime":1445569090000,"userId":122,"taskId":113,"upvote":0,"isUpvoted":false}]},"success":true}
 */
public class TaskProcessVO implements Serializable {
    public boolean isFollow;
    public ProcessMoteTaskVO moteTask;
    public String address;
    public TaskItemVO task;
    public int picNum;
    public List<ProcessPicVO> picList;
    public ProcessSellerVO seller;
}
