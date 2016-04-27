package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.DepthPageTransformer;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ZoomOutPageTransformer;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.IndexManager;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.H5Activity;
import com.floyd.diamond.ui.adapter.MessageAdapter;
import com.floyd.diamond.ui.adapter.NewFregAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    private FragmentPagerAdapter fAdapter;                               //定义adapter

    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表

    private Message_ourFregment messageFragment;
    private NewFregAdapter newFregAdapter;
    private TextView tv;
    private boolean isStart=true;
    private boolean isStart1=true;
    private boolean isShow=true;
    private boolean isShow1=true;
    private Activity mContext;
    int j=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //注册广播
        registerBoradcastReceiver();

        View view = inflater.inflate(R.layout.newmeassage_layout, container, false);

        initControls(view);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext=activity;

    }

    /**
     * 初始化各控件
     *
     * @param view
     */
    private void initControls(View view) {
        tv = ((TextView) view.findViewById(R.id.tv));

        tab_FindFragment_title = (TabLayout) view.findViewById(R.id.message_tablayout);
        vp_FindFragment_pager = (ViewPager) view.findViewById(R.id.vp_FindFragment_pager);
        vp_FindFragment_pager.setOffscreenPageLimit(2);

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            j=i;
            //初始化各fragment
            messageFragment = new Message_ourFregment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            messageFragment.setArguments(bundle);
            list_fragment.add(messageFragment);
        }
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("平台通告");
        list_title.add("模特通告");
        list_title.add("商家通告");

        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
//        //为TabLayout添加tab名称
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));

        fAdapter = new NewFregAdapter(getActivity().getSupportFragmentManager(), list_fragment, list_title);

        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        vp_FindFragment_pager.setPageTransformer(true, new ZoomOutPageTransformer());//给pager切换添加动画
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);//将TabLayout和ViewPager关联起来。
        tab_FindFragment_title.setTabsFromPagerAdapter(fAdapter);//给Tabs设置适配器
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals("hide")) {
                isStart = true;
                TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(200);
                tab_FindFragment_title.setVisibility(View.INVISIBLE);
                if (!isStart1) {
                    tab_FindFragment_title.startAnimation(mHiddenAction);
                    isStart1 = true;
                }
//
                tv.setText("");

            } else if (action.equals("show")) {
                tv.setTextColor(Color.WHITE);
                tv.setText(list_title.get(vp_FindFragment_pager.getCurrentItem()));

                // 向上运动100
                TranslateAnimation transAni = new TranslateAnimation(0, 0, 100, 0);
                transAni.setDuration(200); // 设置持续时间
                if (isStart) {
                    tv.startAnimation(transAni);
                    isStart = false;
                }
                TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                mShowAction.setDuration(500);

                tab_FindFragment_title.setVisibility(View.VISIBLE);
                if (isStart1) {
                    tab_FindFragment_title.startAnimation(mShowAction);
                    isStart1 = false;
                }

            }
            vp_FindFragment_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

//                        Intent intent1 = new Intent("load");
//                        getActivity().sendBroadcast(intent1);

                    if (action.equals("show")) {
                        tv.setText(list_title.get(position));
                        if (isShow) {
                            Intent intent = new Intent("go");
                            mContext.sendBroadcast(intent);
                            isShow = false;
                            isShow1 = true;
                        }


                    } else {
                        tv.setText("");
                        if (isShow1) {
                            Intent intent = new Intent("wait");
                            mContext.sendBroadcast(intent);
                            isShow1 = false;
                            isShow = true;
                        }

                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("hide");
        myIntentFilter.addAction("show");
        //注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
