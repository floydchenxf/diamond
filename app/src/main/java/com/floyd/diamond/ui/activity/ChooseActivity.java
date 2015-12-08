package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;

/**
 * Created by Administrator on 2015/11/24.
 */
public class ChooseActivity extends Activity {
    private TextView back;
    private ImageView moreProvince,jiantou_up;
    private LinearLayout changeProvince;
    private CheckBox allChoose,beijing,shanghai,tianjin,chongqing,jiangsu,zhejiang,liaoning,heilongjiang,jilin,shandong,anhui,hebei,henan,hubei,
            hunan,jiangxi,shanxi3,shanxi1,sichuan,qinghai, hainan,guangdong,guizhou,fujian,taiwan,gansu,yunnan,neimenggu,ningxia,xinjiang,xizang,
            guangxi,xianggang,aomen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);
        initView();
    }
    //初始化操作
    public void initView(){
        back= ((TextView) findViewById(R.id.left));
        moreProvince= ((ImageView) findViewById(R.id.moreProvince));
        jiantou_up= ((ImageView) findViewById(R.id.jiantou_up));
        allChoose= ((CheckBox) findViewById(R.id.allChoose));
        changeProvince= ((LinearLayout) findViewById(R.id.changeProvince));
        changeProvince.setVisibility(View.GONE);
        beijing= ((CheckBox) findViewById(R.id.beijing));
        shanghai= ((CheckBox) findViewById(R.id.shanghai));
        tianjin= ((CheckBox) findViewById(R.id.tianjin));
        chongqing= ((CheckBox) findViewById(R.id.chongqing));
        jiangsu= ((CheckBox) findViewById(R.id.jiangsu));
        zhejiang= ((CheckBox) findViewById(R.id.zhejiang));
        liaoning= ((CheckBox) findViewById(R.id.liaoning));
        heilongjiang= ((CheckBox) findViewById(R.id.heilongjiang));
        jilin= ((CheckBox) findViewById(R.id.jilin));
        shandong= ((CheckBox) findViewById(R.id.shandong));
        anhui= ((CheckBox) findViewById(R.id.anhui));
        hebei= ((CheckBox) findViewById(R.id.hebei));
        henan= ((CheckBox) findViewById(R.id.henan));
        hubei= ((CheckBox) findViewById(R.id.hubei));
        hunan= ((CheckBox) findViewById(R.id.hunan));
        jiangxi= ((CheckBox) findViewById(R.id.jiangxi));
        shanxi3= ((CheckBox) findViewById(R.id.shanxi3));
        shanxi1= ((CheckBox) findViewById(R.id.shanxi1));
        sichuan= ((CheckBox) findViewById(R.id.sichuan));
        qinghai= ((CheckBox) findViewById(R.id.qinghai));
        hainan= ((CheckBox) findViewById(R.id.hainan));
        guangdong= ((CheckBox) findViewById(R.id.guangdong));
        guizhou= ((CheckBox) findViewById(R.id.guizhou));
        fujian= ((CheckBox) findViewById(R.id.fujian));
        taiwan= ((CheckBox) findViewById(R.id.taiwan));
        gansu= ((CheckBox) findViewById(R.id.gansu));
        yunnan= ((CheckBox) findViewById(R.id.yunnan));
        neimenggu= ((CheckBox) findViewById(R.id.neimenggu));
        ningxia= ((CheckBox) findViewById(R.id.ningxia));
        xinjiang= ((CheckBox) findViewById(R.id.xinjiang));
        xizang= ((CheckBox) findViewById(R.id.xizang));
        guangxi= ((CheckBox) findViewById(R.id.guangxi));
        xianggang= ((CheckBox) findViewById(R.id.xianggang));
        aomen= ((CheckBox) findViewById(R.id.aomen));

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
        if (GlobalParams.isDebug){
            Log.e("allChoose",allChoose.isChecked()+"");
        }

        allChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allChoose.isChecked()){
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
                }else{
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


    }





}
