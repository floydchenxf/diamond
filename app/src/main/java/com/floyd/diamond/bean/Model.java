package com.floyd.diamond.bean;

/**
 * Created by Administrator on 2015/11/24.
 */

/**
 * 模特的个人信息
 */
public class Model {
    private String name;//姓名
    private String place;//归属地
    private String img;//图片
    private int likecount;//收藏数

    public String getImg() {
        return img;
    }

    public int getLikecount() {
        return likecount;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
