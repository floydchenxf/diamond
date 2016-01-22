package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.DLCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.RangeSeekBar;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class ChooseActivity1 extends Activity {
    private LinearLayout back;
    private ImageView moreProvince, jiantou_up;
    private LinearLayout changeProvince;
    private CheckBox allChoose, beijing, shanghai, tianjin, chongqing, jiangsu, zhejiang, liaoning, heilongjiang, jilin, shandong, anhui, hebei, henan, hubei,
            hunan, jiangxi, shanxi3, shanxi1, sichuan, qinghai, hainan, guangdong, guizhou, fujian, gansu, yunnan, neimenggu, ningxia, xinjiang, xizang,
            guangxi, gugan, biaozhi, fengman;
    private int[]areas={110000,120000,130000,140000,150000,210000,220000,230000,310000,320000,330000,340000,350000,360000,370000,410000,420000,430000,440000,450000,460000,500000,510000,520000,530000,540000,610000,620000,630000,640000,650000};
    private boolean isScreen;
    private RadioButton boy, girl;
    private List<String> shapesList;
    private List<String> provincesList;
    private TextView search;
    private RequestQueue queue;
    private LoginVO vo;
    private DLCondition.DataEntity dlEntity;
    private List<String> shapes;
    private List<String> areaids;
    private RangeSeekBar seekBar_age;
    private RangeSeekBar seekBar_height;
    private RangeSeekBar seekBar_credit;
    private DataLoadingView dataLoadingView;
    private boolean isFirstShape = true;
    private boolean isFirshArea = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            clickCondition();


            if (GlobalParams.isDebug) {
                Log.e("TAG_areaids", areaids.toString());
                Log.e("TAG_shapes", shapes.toString());
                if (vo != null) {
                    Log.e("TAG",vo.token+"");
                }
            }

            searchMote();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);

        initView();

        if (vo == null) {
            setData();
        } else {
            setDataUser();
        }

    }

    //获取默认的筛选条件(未登录的)
    public void setData() {
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_FILTER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug) {
                    Log.e("weidenglu", response);
                }
                Gson gson = new Gson();
                DLCondition choiceCondition = gson.fromJson(response, DLCondition.class);

                dlEntity = choiceCondition.getData();

                seekBar_age.setSelectedMaxValue(dlEntity.getAgeMax());
                seekBar_age.setSelectedMinValue(dlEntity.getAgeMin());

                seekBar_height.setSelectedMaxValue(dlEntity.getHeightMax());
                seekBar_height.setSelectedMinValue(dlEntity.getHeightMin());

                seekBar_credit.setSelectedMaxValue(dlEntity.getCreditMax());
                seekBar_credit.setSelectedMinValue(dlEntity.getCreditMin());

//                seekBarAge.setProgressLow(dlEntity.getAgeMin());
//                seekBarAge.setProgressHigh(dlEntity.getAgeMax());

//                seekBarHeight.setProgressHigh(dlEntity.getHeightMax() - 80);
//                seekBarHeight.setProgressLow(dlEntity.getHeightMin() - 80);
//
//                seekBarCredit.setProgressLow(dlEntity.getCreditMin());


                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    //获取保存的筛选条件（已登录）
    public void setDataUser() {

        if (GlobalParams.isDebug) {
            Log.e("token", vo.token + "");
        }
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_FILTER;
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug) {
                    Log.e("TAG_denglu", response);
                }
                Gson gson = new Gson();
                DLCondition choiceCondition = gson.fromJson(response, DLCondition.class);
//
                dlEntity = choiceCondition.getData();

                if (dlEntity.getGender() == 0) {
                    girl.setChecked(true);
                } else {
                    boy.setChecked(true);
                }

                seekBar_age.setSelectedMaxValue(dlEntity.getAgeMax());
                seekBar_age.setSelectedMinValue(dlEntity.getAgeMin());

                seekBar_height.setSelectedMaxValue(dlEntity.getHeightMax());
                seekBar_height.setSelectedMinValue(dlEntity.getHeightMin());

                seekBar_credit.setSelectedMaxValue(dlEntity.getCreditMax());
                seekBar_credit.setSelectedMinValue(dlEntity.getCreditMin());

                shapes.clear();
                if (dlEntity.getShapes() != null) {
                    String[] shapesLists = dlEntity.getShapes().split(",");
                    for (int i = 0; i < shapesLists.length; i++) {

                        shapes.add(shapesLists[i]);

                        if (shapesLists[i].equals(1 + "")) {
                            gugan.setChecked(true);
                        }
                        if (shapesLists[i].equals(2 + "")) {
                            biaozhi.setChecked(true);
                        }
                        if (shapesLists[i].equals(3 + "")) {
                            fengman.setChecked(true);
                        }
                    }
                }
                areaids.clear();
                if (dlEntity.getAreaids() != null) {
                    String[] areaLists = dlEntity.getAreaids().split(",");
                    for (int i = 0; i < areaLists.length; i++) {

                        areaids.add(areaLists[i]);

                        if (areaLists[i].equals(110000 + "")) {
                            beijing.setChecked(true);
                        }
                        if (areaLists[i].equals(120000 + "")) {
                            tianjin.setChecked(true);
                        }
                        if (areaLists[i].equals(130000 + "")) {
                            hebei.setChecked(true);
                        }
                        if (areaLists[i].equals(140000 + "")) {
                            shanxi1.setChecked(true);
                        }
                        if (areaLists[i].equals(150000 + "")) {
                            neimenggu.setChecked(true);
                        }
                        if (areaLists[i].equals(210000 + "")) {
                            liaoning.setChecked(true);
                        }
                        if (areaLists[i].equals(220000 + "")) {
                            jilin.setChecked(true);
                        }
                        if (areaLists[i].equals(230000 + "")) {
                            heilongjiang.setChecked(true);
                        }
                        if (areaLists[i].equals(310000 + "")) {
                            shanghai.setChecked(true);
                        }
                        if (areaLists[i].equals(320000 + "")) {
                            jiangsu.setChecked(true);
                        }
                        if (areaLists[i].equals(330000 + "")) {
                            zhejiang.setChecked(true);
                        }
                        if (areaLists[i].equals(340000 + "")) {
                            anhui.setChecked(true);
                        }
                        if (areaLists[i].equals(350000 + "")) {
                            fujian.setChecked(true);
                        }
                        if (areaLists[i].equals(360000 + "")) {
                            jiangxi.setChecked(true);
                        }
                        if (areaLists[i].equals(370000 + "")) {
                            shandong.setChecked(true);
                        }
                        if (areaLists[i].equals(410000 + "")) {
                            henan.setChecked(true);
                        }
                        if (areaLists[i].equals(420000 + "")) {
                            hubei.setChecked(true);
                        }
                        if (areaLists[i].equals(430000 + "")) {
                            hunan.setChecked(true);
                        }
                        if (areaLists[i].equals(440000 + "")) {
                            guangdong.setChecked(true);
                        }
                        if (areaLists[i].equals(450000 + "")) {
                            guangxi.setChecked(true);
                        }
                        if (areaLists[i].equals(460000 + "")) {
                            hainan.setChecked(true);
                        }
                        if (areaLists[i].equals(500000 + "")) {
                            chongqing.setChecked(true);
                        }
                        if (areaLists[i].equals(510000 + "")) {
                            sichuan.setChecked(true);
                        }
                        if (areaLists[i].equals(520000 + "")) {
                            guizhou.setChecked(true);
                        }
                        if (areaLists[i].equals(530000 + "")) {
                            yunnan.setChecked(true);
                        }
                        if (areaLists[i].equals(540000 + "")) {
                            xizang.setChecked(true);
                        }
                        if (areaLists[i].equals(610000 + "")) {
                            shanxi3.setChecked(true);
                        }
                        if (areaLists[i].equals(620000 + "")) {
                            gansu.setChecked(true);
                        }
                        if (areaLists[i].equals(630000 + "")) {
                            qinghai.setChecked(true);
                        }
                        if (areaLists[i].equals(640000 + "")) {
                            ningxia.setChecked(true);
                        }
                        if (areaLists[i].equals(650000 + "")) {
                            xinjiang.setChecked(true);
                        }
                        if (areaLists.length==31){
                            allChoose.setChecked(true);
                        }

                    }
                }


                handler.sendEmptyMessage(2);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("token", vo.token);
                return params;
            }
        };

        queue.add(requestLogin);
    }

    //初始化操作
    public void initView() {
        vo = LoginManager.getLoginInfo(this);

        seekBar_age = ((RangeSeekBar) findViewById(R.id.seekBar_age));
        seekBar_height = ((RangeSeekBar) findViewById(R.id.seekBar_height));
        seekBar_credit = ((RangeSeekBar) findViewById(R.id.seekBar_credit));

        dlEntity = new DLCondition.DataEntity();
        shapes = new ArrayList<>();
        areaids = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        shapesList = new ArrayList<>();
        provincesList = new ArrayList<>();
        search = ((TextView) findViewById(R.id.search));
        boy = ((RadioButton) findViewById(R.id.boy));
        girl = ((RadioButton) findViewById(R.id.girl));
        back = ((LinearLayout) findViewById(R.id.guide));
        moreProvince = ((ImageView) findViewById(R.id.moreProvince));
        jiantou_up = ((ImageView) findViewById(R.id.jiantou_up));
        allChoose = ((CheckBox) findViewById(R.id.allChoose));
        changeProvince = ((LinearLayout) findViewById(R.id.changeProvince));
        changeProvince.setVisibility(View.GONE);
        beijing = ((CheckBox) findViewById(R.id.beijing));
        shanghai = ((CheckBox) findViewById(R.id.shanghai));
        tianjin = ((CheckBox) findViewById(R.id.tianjin));
        chongqing = ((CheckBox) findViewById(R.id.chongqing));
        jiangsu = ((CheckBox) findViewById(R.id.jiangsu));
        zhejiang = ((CheckBox) findViewById(R.id.zhejiang));
        liaoning = ((CheckBox) findViewById(R.id.liaoning));
        heilongjiang = ((CheckBox) findViewById(R.id.heilongjiang));
        jilin = ((CheckBox) findViewById(R.id.jilin));
        shandong = ((CheckBox) findViewById(R.id.shandong));
        anhui = ((CheckBox) findViewById(R.id.anhui));
        hebei = ((CheckBox) findViewById(R.id.hebei));
        henan = ((CheckBox) findViewById(R.id.henan));
        hubei = ((CheckBox) findViewById(R.id.hubei));
        hunan = ((CheckBox) findViewById(R.id.hunan));
        jiangxi = ((CheckBox) findViewById(R.id.jiangxi));
        shanxi3 = ((CheckBox) findViewById(R.id.shanxi3));
        shanxi1 = ((CheckBox) findViewById(R.id.shanxi1));
        sichuan = ((CheckBox) findViewById(R.id.sichuan));
        qinghai = ((CheckBox) findViewById(R.id.qinghai));
        hainan = ((CheckBox) findViewById(R.id.hainan));
        guangdong = ((CheckBox) findViewById(R.id.guangdong));
        guizhou = ((CheckBox) findViewById(R.id.guizhou));
        fujian = ((CheckBox) findViewById(R.id.fujian));
        gansu = ((CheckBox) findViewById(R.id.gansu));
        yunnan = ((CheckBox) findViewById(R.id.yunnan));
        neimenggu = ((CheckBox) findViewById(R.id.neimenggu));
        ningxia = ((CheckBox) findViewById(R.id.ningxia));
        xinjiang = ((CheckBox) findViewById(R.id.xinjiang));
        xizang = ((CheckBox) findViewById(R.id.xizang));
        guangxi = ((CheckBox) findViewById(R.id.guangxi));
        gugan = ((CheckBox) findViewById(R.id.gugan));
        biaozhi = ((CheckBox) findViewById(R.id.biaozhi));
        fengman = ((CheckBox) findViewById(R.id.fengman));


        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击弹出更多的省
        moreProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreProvince.setVisibility(View.INVISIBLE);
                changeProvince.setVisibility(View.VISIBLE);
            }
        });
        //点击返回原样
        jiantou_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProvince.setVisibility(View.GONE);
                moreProvince.setVisibility(View.VISIBLE);
            }
        });

        //点击所有的区域都被选中
        if (GlobalParams.isDebug) {
            Log.e("allChoose", allChoose.isChecked() + "");
        }

    }

    //点击search按钮搜索
    public void searchMote() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vo != null) {
                    if (dlEntity != null) {
                        String saveUrl = APIConstants.HOST + APIConstants.API_SAVE_MOTE_FILTER;
                        StringRequest submitCondition = new StringRequest(Request.Method.POST, saveUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                //在这里设置需要post的参数
                                Map<String, String> params = new HashMap<>();
                                params.put("gender", dlEntity.getGender() + "");
                                params.put("ageMin", dlEntity.getAgeMin() + "");
                                params.put("ageMax", dlEntity.getAgeMax() + "");
                                params.put("heightMin", dlEntity.getHeightMin() + "");
                                params.put("heightMax", dlEntity.getHeightMax() + "");
                                params.put("creditMin", dlEntity.getCreditMin() + "");
                                params.put("creditMax", dlEntity.getCreditMax() + "");
//                                params.put("shapes", dlEntity.getShapes().toString().substring(1, dlEntity.getShapes().toString().length() - 1).replace(" ", "") + "");
//                                params.put("areaids", dlEntity.getAreaids().toString().substring(1, dlEntity.getAreaids().toString().length() - 1).replace(" ", "") + "");

//                                var two=test.Replace(",", " ").Trim().Replace(" ", ",");
                                params.put("shapes", dlEntity.getShapes().replace(",", " ").trim().replace(" ", ","));
                                params.put("areaids", dlEntity.getAreaids());
                                String token = "";
                                if (vo != null) {
                                    token = vo.token;
                                }
                                params.put("token", token);
                                return params;
                            }
                        };

                        queue.add(submitCondition);
                    }


                }
                Intent intent = new Intent(ChooseActivity1.this, ChooseResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("chooseCondition", (Serializable) dlEntity);
                startActivity(intent);
                finish();

            }
        });


    }

    //点击改变筛选条件
    public void clickCondition() {
        //年龄刻度尺
        seekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                dlEntity.setAgeMin((int) minValue);
                dlEntity.setAgeMax((int) maxValue);
            }
        });
        // 身高刻度尺
        seekBar_height.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                dlEntity.setHeightMin((int) minValue);
                dlEntity.setHeightMax((int) maxValue);
            }
        });
        // 满意度刻度尺

        seekBar_credit.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                dlEntity.setCreditMin((int) minValue);
                dlEntity.setCreditMax((int) maxValue);
            }
        });
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (boy.isChecked()) {
                    if (GlobalParams.isDebug) {
                        Log.e("areaid", areaids.toString());

                    }
                    dlEntity.setGender(1);
                }
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (girl.isChecked()) {
                    dlEntity.setGender(0);
                }
            }
        });


        gugan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gugan.isChecked()) {
                    shapes.add(1 + "");
                } else {
                    shapes.remove(1 + "");
                }

                dlEntity.setShapes(shapes.toString().substring(1, shapes.toString().length() - 1).replace(" ", ""));

            }
        });


        biaozhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biaozhi.isChecked()) {
                    shapes.add(2 + "");
                } else {
                    shapes.remove(2 + "");
                }

                dlEntity.setShapes(shapes.toString().substring(1, shapes.toString().length() - 1).replace(" ", ""));

            }
        });

        fengman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fengman.isChecked()) {
                    shapes.add(3 + "");
                } else {
                    shapes.remove(3 + "");
                }

                dlEntity.setShapes(shapes.toString().substring(1, shapes.toString().length() - 1).replace(" ", ""));

            }
        });


        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beijing.isChecked()) {
                    areaids.add(beijing.getTag() + "");

                } else {
                    areaids.remove(beijing.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    areaids.add(shanghai.getTag() + "");

                } else {
                    areaids.remove(shanghai.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));

            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tianjin.isChecked()) {
                    areaids.add(tianjin.getTag() + "");

                } else {
                    areaids.remove(tianjin.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chongqing.isChecked()) {
                    areaids.add(chongqing.getTag() + "");

                } else {
                    areaids.remove(chongqing.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        jiangsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangsu.isChecked()) {
                    areaids.add(jiangsu.getTag() + "");
                } else {
                    areaids.remove(jiangsu.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        zhejiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhejiang.isChecked()) {
                    areaids.add(zhejiang.getTag() + "");
                } else {
                    areaids.remove(zhejiang.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        liaoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liaoning.isChecked()) {
                    areaids.add(liaoning.getTag() + "");
                } else {
                    areaids.remove(liaoning.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        heilongjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (heilongjiang.isChecked()) {
                    areaids.add(heilongjiang.getTag() + "");
                } else {
                    areaids.remove(heilongjiang.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        jilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jilin.isChecked()) {
                    areaids.add(jilin.getTag() + "");
                } else {
                    areaids.remove(jilin.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        shandong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shandong.isChecked()) {
                    areaids.add(shandong.getTag() + "");
                } else {
                    areaids.remove(shandong.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        anhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (anhui.isChecked()) {
                    areaids.add(anhui.getTag() + "");
                } else {
                    areaids.remove(anhui.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        hebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hebei.isChecked()) {
                    areaids.add(hebei.getTag() + "");
                } else {
                    areaids.remove(hebei.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        henan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (henan.isChecked()) {
                    areaids.add(henan.getTag() + "");
                } else {
                    areaids.remove(henan.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        hubei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hubei.isChecked()) {
                    areaids.add(hubei.getTag() + "");
                } else {
                    areaids.remove(hubei.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        hunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hunan.isChecked()) {
                    areaids.add(hunan.getTag() + "");
                } else {
                    areaids.remove(hunan.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        jiangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangxi.isChecked()) {
                    areaids.add(jiangxi.getTag() + "");
                } else {
                    areaids.remove(jiangxi.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    areaids.add(tianjin.getTag() + "");
                } else {
                    areaids.remove(tianjin.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        shanxi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi3.isChecked()) {
                    areaids.add(shanxi3.getTag() + "");
                } else {
                    areaids.remove(shanxi3.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        shanxi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi1.isChecked()) {
                    areaids.add(shanxi1.getTag() + "");
                } else {
                    areaids.remove(shanxi1.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        sichuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sichuan.isChecked()) {
                    areaids.add(sichuan.getTag() + "");
                } else {
                    areaids.remove(sichuan.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        qinghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qinghai.isChecked()) {
                    areaids.add(qinghai.getTag() + "");
                } else {
                    areaids.remove(qinghai.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        hainan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hainan.isChecked()) {
                    areaids.add(hainan.getTag() + "");
                } else {
                    areaids.remove(hainan.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        guangdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangdong.isChecked()) {
                    areaids.add(guangdong.getTag() + "");
                } else {
                    areaids.remove(guangdong.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        guizhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guizhou.isChecked()) {
                    areaids.add(guizhou.getTag() + "");
                } else {
                    areaids.remove(guizhou.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        fujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fujian.isChecked()) {
                    areaids.add(fujian.getTag() + "");
                } else {
                    areaids.remove(fujian.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });


        gansu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gansu.isChecked()) {
                    areaids.add(gansu.getTag() + "");
                } else {
                    areaids.remove(gansu.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        yunnan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yunnan.isChecked()) {
                    areaids.add(yunnan.getTag() + "");
                } else {
                    areaids.remove(yunnan.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });
        neimenggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (neimenggu.isChecked()) {
                    areaids.add(neimenggu.getTag() + "");
                } else {
                    areaids.remove(neimenggu.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        ningxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ningxia.isChecked()) {
                    areaids.add(ningxia.getTag() + "");
                } else {
                    areaids.remove(ningxia.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        xinjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xinjiang.isChecked()) {
                    areaids.add(xinjiang.getTag() + "");
                } else {
                    areaids.remove(xinjiang.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        xizang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xizang.isChecked()) {
                    areaids.add(xizang.getTag() + "");
                } else {
                    areaids.remove(xizang.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        guangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangxi.isChecked()) {
                    areaids.add(guangxi.getTag() + "");
                } else {
                    areaids.remove(guangxi.getTag() + "");
                }

                dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
            }
        });

        allChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allChoose.isChecked()) {
                    beijing.setChecked(true);
                    shanghai.setChecked(true);
                    tianjin.setChecked(true);
                    chongqing.setChecked(true);
                    jiangsu.setChecked(true);
                    zhejiang.setChecked(true);
                    liaoning.setChecked(true);
                    heilongjiang.setChecked(true);
                    jilin.setChecked(true);
                    shandong.setChecked(true);
                    anhui.setChecked(true);
                    hebei.setChecked(true);
                    henan.setChecked(true);
                    hubei.setChecked(true);
                    hunan.setChecked(true);
                    jiangxi.setChecked(true);
                    shanxi3.setChecked(true);
                    shanxi1.setChecked(true);
                    sichuan.setChecked(true);
                    qinghai.setChecked(true);
                    hainan.setChecked(true);
                    guangdong.setChecked(true);
                    guizhou.setChecked(true);
                    fujian.setChecked(true);
                    gansu.setChecked(true);
                    yunnan.setChecked(true);
                    neimenggu.setChecked(true);
                    ningxia.setChecked(true);
                    xinjiang.setChecked(true);
                    xizang.setChecked(true);
                    guangxi.setChecked(true);
                    areaids.clear();
                    for (int i=0;i<areas.length;i++){
                        areaids.add(areas[i]+"");
                    }
                    dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
                } else {
                    beijing.setChecked(false);
                    shanghai.setChecked(false);
                    tianjin.setChecked(false);
                    chongqing.setChecked(false);
                    jiangsu.setChecked(false);
                    zhejiang.setChecked(false);
                    liaoning.setChecked(false);
                    heilongjiang.setChecked(false);
                    jilin.setChecked(false);
                    shandong.setChecked(false);
                    anhui.setChecked(false);
                    hebei.setChecked(false);
                    henan.setChecked(false);
                    hubei.setChecked(false);
                    hunan.setChecked(false);
                    jiangxi.setChecked(false);
                    shanxi3.setChecked(false);
                    shanxi1.setChecked(false);
                    sichuan.setChecked(false);
                    qinghai.setChecked(false);
                    hainan.setChecked(false);
                    guangdong.setChecked(false);
                    guizhou.setChecked(false);
                    fujian.setChecked(false);
                    gansu.setChecked(false);
                    yunnan.setChecked(false);
                    neimenggu.setChecked(false);
                    ningxia.setChecked(false);
                    xinjiang.setChecked(false);
                    xizang.setChecked(false);
                    guangxi.setChecked(false);
                    for (int i=0;i<areas.length;i++){
                        areaids.remove(areas[i]+"");
                    }
                    dlEntity.setAreaids(areaids.toString().substring(1, areaids.toString().length() - 1).replace(" ", ""));
                }

            }
        });


    }


}

