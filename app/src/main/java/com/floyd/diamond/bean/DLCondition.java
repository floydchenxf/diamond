package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/1/13.
 */
public class DLCondition {


    /**
     * userId : 1072
     * gender : 0
     * ageMin : 20
     * ageMax : 30
     * heightMin : 80
     * heightMax : 80
     * shapes : 2,1
     * shapesList : [{"id":1,"type":1,"name":"骨感","sort":1,"show":true,"createTime":1448781177000},{"id":2,"type":1,"name":"标致","sort":2,"show":true,"createTime":1448781201000},{"id":3,"type":1,"name":"丰满","sort":3,"show":false,"createTime":1448781215000}]
     * creditMin : 0
     * creditMax : 0
     * levels : null
     * areaids : 500000,210000,310000
     * areaList : [{"id":110000,"name":"北京","pid":0,"show":false},{"id":120000,"name":"天津","pid":0,"show":false},{"id":130000,"name":"河北","pid":0,"show":false},{"id":140000,"name":"山西","pid":0,"show":false},{"id":150000,"name":"内蒙","pid":0,"show":false},{"id":210000,"name":"辽宁","pid":0,"show":true},{"id":220000,"name":"吉林","pid":0,"show":false},{"id":230000,"name":"黑龙江","pid":0,"show":false},{"id":310000,"name":"上海","pid":0,"show":true},{"id":320000,"name":"江苏","pid":0,"show":false},{"id":330000,"name":"浙江","pid":0,"show":false},{"id":340000,"name":"安徽","pid":0,"show":false},{"id":350000,"name":"福建","pid":0,"show":false},{"id":360000,"name":"江西","pid":0,"show":false},{"id":370000,"name":"山东","pid":0,"show":false},{"id":410000,"name":"河南","pid":0,"show":false},{"id":420000,"name":"湖北","pid":0,"show":false},{"id":430000,"name":"湖南","pid":0,"show":false},{"id":440000,"name":"广东","pid":0,"show":false},{"id":450000,"name":"广西","pid":0,"show":false},{"id":460000,"name":"海南","pid":0,"show":false},{"id":500000,"name":"重庆","pid":0,"show":true},{"id":510000,"name":"四川","pid":0,"show":false},{"id":520000,"name":"贵州","pid":0,"show":false},{"id":530000,"name":"云南","pid":0,"show":false},{"id":540000,"name":"西藏","pid":0,"show":false},{"id":610000,"name":"陕西","pid":0,"show":false},{"id":620000,"name":"甘肃","pid":0,"show":false},{"id":630000,"name":"青海","pid":0,"show":false},{"id":640000,"name":"宁夏","pid":0,"show":false},{"id":650000,"name":"新疆","pid":0,"show":false}]
     * createTime : 1452679446000
     * updateTime : 1452679446000
     */

    private DataEntity data;
    /**
     * data : {"userId":1072,"gender":0,"ageMin":20,"ageMax":30,"heightMin":80,"heightMax":80,"shapes":"2,1","shapesList":[{"id":1,"type":1,"name":"骨感","sort":1,"show":true,"createTime":1448781177000},{"id":2,"type":1,"name":"标致","sort":2,"show":true,"createTime":1448781201000},{"id":3,"type":1,"name":"丰满","sort":3,"show":false,"createTime":1448781215000}],"creditMin":0,"creditMax":0,"levels":null,"areaids":"500000,210000,310000","areaList":[{"id":110000,"name":"北京","pid":0,"show":false},{"id":120000,"name":"天津","pid":0,"show":false},{"id":130000,"name":"河北","pid":0,"show":false},{"id":140000,"name":"山西","pid":0,"show":false},{"id":150000,"name":"内蒙","pid":0,"show":false},{"id":210000,"name":"辽宁","pid":0,"show":true},{"id":220000,"name":"吉林","pid":0,"show":false},{"id":230000,"name":"黑龙江","pid":0,"show":false},{"id":310000,"name":"上海","pid":0,"show":true},{"id":320000,"name":"江苏","pid":0,"show":false},{"id":330000,"name":"浙江","pid":0,"show":false},{"id":340000,"name":"安徽","pid":0,"show":false},{"id":350000,"name":"福建","pid":0,"show":false},{"id":360000,"name":"江西","pid":0,"show":false},{"id":370000,"name":"山东","pid":0,"show":false},{"id":410000,"name":"河南","pid":0,"show":false},{"id":420000,"name":"湖北","pid":0,"show":false},{"id":430000,"name":"湖南","pid":0,"show":false},{"id":440000,"name":"广东","pid":0,"show":false},{"id":450000,"name":"广西","pid":0,"show":false},{"id":460000,"name":"海南","pid":0,"show":false},{"id":500000,"name":"重庆","pid":0,"show":true},{"id":510000,"name":"四川","pid":0,"show":false},{"id":520000,"name":"贵州","pid":0,"show":false},{"id":530000,"name":"云南","pid":0,"show":false},{"id":540000,"name":"西藏","pid":0,"show":false},{"id":610000,"name":"陕西","pid":0,"show":false},{"id":620000,"name":"甘肃","pid":0,"show":false},{"id":630000,"name":"青海","pid":0,"show":false},{"id":640000,"name":"宁夏","pid":0,"show":false},{"id":650000,"name":"新疆","pid":0,"show":false}],"createTime":1452679446000,"updateTime":1452679446000}
     * success : true
     */

    private boolean success;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class DataEntity {
        private int userId;
        private int gender;
        private int ageMin;
        private int ageMax;
        private int heightMin;
        private int heightMax;
        private String shapes;
        private int creditMin;
        private int creditMax;
        private Object levels;
        private String areaids;
        private long createTime;
        private long updateTime;
        /**
         * id : 1
         * type : 1
         * name : 骨感
         * sort : 1
         * show : true
         * createTime : 1448781177000
         */

        private List<ShapesListEntity> shapesList;
        /**
         * id : 110000
         * name : 北京
         * pid : 0
         * show : false
         */

        private List<AreaListEntity> areaList;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setAgeMin(int ageMin) {
            this.ageMin = ageMin;
        }

        public void setAgeMax(int ageMax) {
            this.ageMax = ageMax;
        }

        public void setHeightMin(int heightMin) {
            this.heightMin = heightMin;
        }

        public void setHeightMax(int heightMax) {
            this.heightMax = heightMax;
        }

        public void setShapes(String shapes) {
            this.shapes = shapes;
        }

        public void setCreditMin(int creditMin) {
            this.creditMin = creditMin;
        }

        public void setCreditMax(int creditMax) {
            this.creditMax = creditMax;
        }

        public void setLevels(Object levels) {
            this.levels = levels;
        }

        public void setAreaids(String areaids) {
            this.areaids = areaids;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setShapesList(List<ShapesListEntity> shapesList) {
            this.shapesList = shapesList;
        }

        public void setAreaList(List<AreaListEntity> areaList) {
            this.areaList = areaList;
        }

        public int getUserId() {
            return userId;
        }

        public int getGender() {
            return gender;
        }

        public int getAgeMin() {
            return ageMin;
        }

        public int getAgeMax() {
            return ageMax;
        }

        public int getHeightMin() {
            return heightMin;
        }

        public int getHeightMax() {
            return heightMax;
        }

        public String getShapes() {
            return shapes;
        }

        public int getCreditMin() {
            return creditMin;
        }

        public int getCreditMax() {
            return creditMax;
        }

        public Object getLevels() {
            return levels;
        }

        public String getAreaids() {
            return areaids;
        }

        public long getCreateTime() {
            return createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public List<ShapesListEntity> getShapesList() {
            return shapesList;
        }

        public List<AreaListEntity> getAreaList() {
            return areaList;
        }

        public static class ShapesListEntity {
            private int id;
            private int type;
            private String name;
            private int sort;
            private boolean show;
            private long createTime;

            public void setId(int id) {
                this.id = id;
            }

            public void setType(int type) {
                this.type = type;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setShow(boolean show) {
                this.show = show;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public int getType() {
                return type;
            }

            public String getName() {
                return name;
            }

            public int getSort() {
                return sort;
            }

            public boolean isShow() {
                return show;
            }

            public long getCreateTime() {
                return createTime;
            }
        }

        public static class AreaListEntity {
            private int id;
            private String name;
            private int pid;
            private boolean show;

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public void setShow(boolean show) {
                this.show = show;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getPid() {
                return pid;
            }

            public boolean isShow() {
                return show;
            }
        }
    }
}
