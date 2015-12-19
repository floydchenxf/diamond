package com.floyd.diamond.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/18.
 */
public class ChooseCondition implements Serializable{
    private int gender;
    private int ageMin;
    private int ageMax;
    private int heightMin;
    private int HeightMax;
    private int creditMin;
    private int creditMax;
    private List<String> shapesList;
    private List<String> provincesList;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getHeightMin() {
        return heightMin;
    }

    public void setHeightMin(int heightMin) {
        this.heightMin = heightMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getHeightMax() {
        return HeightMax;
    }

    public void setHeightMax(int heightMax) {
        HeightMax = heightMax;
    }

    public int getCreditMin() {
        return creditMin;
    }

    public void setCreditMin(int creditMin) {
        this.creditMin = creditMin;
    }

    public int getCreditMax() {
        return creditMax;
    }

    public void setCreditMax(int creditMax) {
        this.creditMax = creditMax;
    }

    public List<String> getShapesList() {
        return shapesList;
    }

    public void setShapesList(List<String> shapesList) {
        this.shapesList = shapesList;
    }

    public List<String> getProvincesList() {
        return provincesList;
    }

    public void setProvincesList(List<String> provincesList) {
        this.provincesList = provincesList;
    }
}
