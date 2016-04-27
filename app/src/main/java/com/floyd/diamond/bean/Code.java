package com.floyd.diamond.bean;

/**
 * Created by hy on 2016/3/17.
 */
public class Code {

    /**
     * id : 1
     * version : 1.0
     * name : 模特1.0版本
     */

    private int id;
    private String version;
    private String name;

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
