package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.bean.ChooseCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.SeekBarPressure;
import com.floyd.diamond.bean.SeekBarPressure1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class ChooseActivity extends Activity {
    private LinearLayout back;
    private ImageView moreProvince, jiantou_up;
    private LinearLayout changeProvince;
    private CheckBox allChoose, beijing, shanghai, tianjin, chongqing, jiangsu, zhejiang, liaoning, heilongjiang, jilin, shandong, anhui, hebei, henan, hubei,
            hunan, jiangxi, shanxi3, shanxi1, sichuan, qinghai, hainan, guangdong, guizhou, fujian, taiwan, gansu, yunnan, neimenggu, ningxia, xinjiang, xizang,
            guangxi, xianggang, aomen, gugan, biaozhi, fengman;
    private SeekBarPressure seekBarAge, seekBarCredit;
    private SeekBarPressure1  seekBarHeight;
    private boolean isScreen;
    private RadioButton boy, girl;
    private ChooseCondition chooseCondition;
    private List<String> shapesList;
    private List<String> provincesList;
    private TextView search;

    //    private int [] shapesList={1,2,3};
//    private int [] provincesList={110000,310000,120000,500000,320000,330000,210000,230000,220000,
//            370000,340000,130000,410000,420000,430000,360000,610000,
//            140000,510000,630000,460000,440000,520000,350000,710000,
//            620000,530000,150000,640000,650000,540000,450000,810000,820000,000000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);
        initView();
    }

    //初始化操作
    public void initView() {
        chooseCondition = new ChooseCondition();
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
        taiwan = ((CheckBox) findViewById(R.id.taiwan));
        gansu = ((CheckBox) findViewById(R.id.gansu));
        yunnan = ((CheckBox) findViewById(R.id.yunnan));
        neimenggu = ((CheckBox) findViewById(R.id.neimenggu));
        ningxia = ((CheckBox) findViewById(R.id.ningxia));
        xinjiang = ((CheckBox) findViewById(R.id.xinjiang));
        xizang = ((CheckBox) findViewById(R.id.xizang));
        guangxi = ((CheckBox) findViewById(R.id.guangxi));
        xianggang = ((CheckBox) findViewById(R.id.xianggang));
        aomen = ((CheckBox) findViewById(R.id.aomen));
        seekBarAge = (SeekBarPressure) findViewById(R.id.seekBar_age);
        seekBarHeight = ((SeekBarPressure1) findViewById(R.id.seekBar_height));
        seekBarCredit = ((SeekBarPressure) findViewById(R.id.seekBar_manyi));
        gugan = ((CheckBox) findViewById(R.id.gugan));
        biaozhi = ((CheckBox) findViewById(R.id.biaozhi));
        fengman = ((CheckBox) findViewById(R.id.fengman));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseActivity.this, ChooseResultActivity.class);
                intent.putExtra("chooseCondition", (Serializable) chooseCondition);
                startActivity(intent);
            }
        });

        seekBarAge.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {
                isScreen = true;
            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {

                chooseCondition.setAgeMin((int) progressLow);

                chooseCondition.setAgeMax((int) progressHigh);

            }

            @Override
            public void onProgressAfter() {
                isScreen = false;
            }
        });
        seekBarHeight.setOnSeekBarChangeListener(new SeekBarPressure1.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(SeekBarPressure1 seekBar, double progressLow, double progressHigh) {

                chooseCondition.setHeightMin((int) progressLow);

                chooseCondition.setHeightMax((int) progressHigh);
            }

            @Override
            public void onProgressAfter() {

            }
        });

        seekBarCredit.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {

                chooseCondition.setCreditMin((int) progressLow);

                chooseCondition.setCreditMax((int) progressHigh);
            }

            @Override
            public void onProgressAfter() {

            }
        });

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
                    taiwan.setChecked(true);
                    gansu.setChecked(true);
                    yunnan.setChecked(true);
                    neimenggu.setChecked(true);
                    ningxia.setChecked(true);
                    xinjiang.setChecked(true);
                    xizang.setChecked(true);
                    guangxi.setChecked(true);
                    xianggang.setChecked(true);
                    aomen.setChecked(true);
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
                    taiwan.setChecked(false);
                    gansu.setChecked(false);
                    yunnan.setChecked(false);
                    neimenggu.setChecked(false);
                    ningxia.setChecked(false);
                    xinjiang.setChecked(false);
                    xizang.setChecked(false);
                    guangxi.setChecked(false);
                    xianggang.setChecked(false);
                    aomen.setChecked(false);
                }

            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boy.isChecked()) {
                    chooseCondition.setGender(1);
                }
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (girl.isChecked()) {
                    chooseCondition.setGender(0);
                }
            }
        });

        gugan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gugan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        shapesList.add(gugan.getTag() + "");
                        chooseCondition.setShapesList(shapesList);
                    } else {
                        shapesList.add("," + gugan.getTag());
                        chooseCondition.setShapesList(shapesList);
                    }

                } else {
                    shapesList.remove(gugan.getTag() + "");
                    chooseCondition.setShapesList(shapesList);
                }

            }
        });


        biaozhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biaozhi.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        shapesList.add(biaozhi.getTag() + "");
                        chooseCondition.setShapesList(shapesList);
                    } else {
                        shapesList.add("," + biaozhi.getTag());
                        chooseCondition.setShapesList(shapesList);
                    }

                } else {
                    shapesList.remove(biaozhi.getTag() + "");
                    chooseCondition.setShapesList(shapesList);
                }

            }
        });

        fengman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fengman.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        shapesList.add(fengman.getTag() + "");
                        chooseCondition.setShapesList(shapesList);
                    } else {
                        shapesList.add("," + fengman.getTag());
                        chooseCondition.setShapesList(shapesList);
                    }

                } else {
                    shapesList.remove(fengman.getTag() + "");
                    chooseCondition.setShapesList(shapesList);
                }

            }
        });


        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beijing.isChecked()) {
                    if (chooseCondition.getShapesList() == null) {
                        provincesList.add(beijing.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + beijing.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(beijing.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(shanghai.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + shanghai.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(shanghai.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tianjin.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(tianjin.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + tianjin.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(tianjin.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chongqing.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(chongqing.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + chongqing.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(chongqing.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        jiangsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangsu.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(jiangsu.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + jiangsu.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(jiangsu.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        zhejiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhejiang.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(zhejiang.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + zhejiang.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(zhejiang.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        liaoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liaoning.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(liaoning.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + liaoning.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(liaoning.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        heilongjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (heilongjiang.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(heilongjiang.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + heilongjiang.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(heilongjiang.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        jilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jilin.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(jilin.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + jilin.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(jilin.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        shandong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shandong.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(shandong.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + shandong.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(shandong.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        anhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (anhui.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(anhui.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + anhui.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(anhui.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        hebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hebei.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(hebei.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + hebei.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(hebei.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        henan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (henan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(henan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + henan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(henan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        hubei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hubei.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(hubei.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + hubei.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(hubei.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        hunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hunan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(hunan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + hunan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(hunan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        jiangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangxi.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(jiangxi.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + jiangxi.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(jiangxi.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(tianjin.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + tianjin.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(tianjin.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        shanxi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi3.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(shanxi3.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + shanxi3.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(shanxi3.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        shanxi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi1.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(shanxi1.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + shanxi1.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(shanxi1.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        sichuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sichuan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(sichuan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + sichuan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(sichuan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        qinghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qinghai.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(qinghai.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + qinghai.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(qinghai.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        hainan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hainan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(hainan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + hainan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(hainan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        guangdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangdong.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(guangdong.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + guangdong.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(guangdong.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        guizhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guizhou.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(guizhou.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + guizhou.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(guizhou.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        fujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fujian.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(fujian.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + fujian.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(fujian.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        taiwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taiwan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(taiwan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + taiwan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(taiwan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        gansu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gansu.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(gansu.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + gansu.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(gansu.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        yunnan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yunnan.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(yunnan.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + yunnan.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(yunnan.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });
        neimenggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (neimenggu.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(neimenggu.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + neimenggu.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(neimenggu.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        ningxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ningxia.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(ningxia.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + ningxia.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(ningxia.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        xinjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xinjiang.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(xinjiang.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + xinjiang.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(xinjiang.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        xizang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xizang.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(xizang.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + xizang.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(xizang.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        guangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangxi.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(guangxi.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + guangxi.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(guangxi.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        xianggang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xianggang.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(xianggang.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + xianggang.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(xianggang.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });

        aomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aomen.isChecked()) {
                    if (chooseCondition.getShapesList()== null) {
                        provincesList.add(aomen.getTag() + "");
                        chooseCondition.setProvincesList(provincesList);
                    } else {
                        provincesList.add("," + aomen.getTag());
                        chooseCondition.setShapesList(provincesList);
                    }

                } else {
                    provincesList.remove(aomen.getTag() + "");
                    chooseCondition.setShapesList(provincesList);
                }

            }
        });


    }


}
