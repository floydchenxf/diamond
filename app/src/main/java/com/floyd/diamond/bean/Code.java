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
