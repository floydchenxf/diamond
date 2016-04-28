package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.MyListView;
import com.floyd.diamond.bean.MyScrollView;
import com.floyd.diamond.bean.TgBean;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.IndexManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.H5Activity;
import com.floyd.diamond.ui.adapter.MessageAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by hy on 2016/4/20.
 */
public class Message_ourFregment extends Fragment implements View.OnClickListener, MyScrollView.OnScrollListener {
    private MyListView mListView;
    private int currentpage = 1;
    private PullToRefreshListView mPullToRefreshListView;
    private boolean needClear = true;
    private MessageAdapter adapter;
    private List<TgBean.DataEntity> advList;
    private ImageLoader mImageLoader;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;
    private int position;
    private TextView message_name;
    private ImageView message_hongdian;
    private MyScrollView myScrollView;
    private TextView message_bg;
    private ImageView message_head;
    private boolean isFirst = true;
    private boolean isFirst1;
    private AlphaAnimation myAnimation_Alpha;
    private AlphaAnimation myAnimation_Alpha1;
//    private NetworkImageView message_head;
    private int screenWidth;

    private static View suspendView;

    private static WindowManager.LayoutParams suspendLayoutParams;

    private int tabLayoutHeight;

    private int myScrollViewTop;

    private int dianLayoutTop;
    private Activity mContext;

    public Message_ourFregment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = ImageLoaderFactory.createImageLoader();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() && mListView.getVisibility() != View.VISIBLE) {
            setData(true); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() && mListView.getVisibility() != View.VISIBLE) {
            setData(true);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
//
        mContext.setContentView(R.layout.empty_layout);
        mListView=null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerBoradcastReceiver();//注册广播

        View view = inflater.inflate(R.layout.fragment_ourmessage, container, false);
        message_bg = ((TextView) view.findViewById(R.id.message_bg));
//        message_head = ((ImageView) view.findViewById(R.id.message_touxiang));
        message_head= ((ImageView) view.findViewById(R.id.meg_touxiang));
        message_name = ((TextView) view.findViewById(R.id.message_name));
        message_hongdian = ((ImageView) view.findViewById(R.id.message_dian));
        myScrollView = (MyScrollView) view.findViewById(R.id.myscrollview);

        myScrollView.setOnScrollListener(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(view, this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(mContext);
//        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mListView = (MyListView) view.findViewById(R.id.listview);
        mListView.setFocusable(false);

//        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
//        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
//            @Override
//            public void onPullDownToRefresh() {
//                needClear = false;
//                currentpage = 1;
//                setData(false);
//                mPullToRefreshListView.onRefreshComplete(true, true);
//            }
//
//            @Override
//            public void onPullUpToRefresh() {
//                needClear = false;
//                mPullToRefreshListView.onRefreshComplete(true, true);
//                currentpage++;
//                setData(false);
//            }
//        });

        adapter = new MessageAdapter(mContext, mImageLoader);
        mListView.setAdapter(adapter);
        init();
        setData(true);
        return view;
    }

    public void init() {

        position = getArguments().getInt("position", 6);
        if (position != 6) {
            if (position == 0) {
                message_hongdian.setImageResource(R.drawable.dian_1);
                message_name.setText("平台通告");
                message_head.setImageResource(R.drawable.our_head);
            } else if (position == 1) {
                message_hongdian.setImageResource(R.drawable.dian_2);
                message_name.setText("模特通告");
                message_head.setImageResource(R.drawable.mote_head);
            } else if (position == 2) {
                message_hongdian.setImageResource(R.drawable.dian_3);
                message_name.setText("商家通告");
                message_head.setImageResource(R.drawable.seller_head);
            }
        } else {
            Toast.makeText(mContext, "喵~~网络君走丢了~~~~", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext=activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //获取数据
    public void setData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        IndexManager.fetchTgLists(currentpage,20,position+2).startUI(new ApiCallback<List<TgBean.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (isFirst) {
                    dataLoadingView.loadFail();
                } else {
                    dataLoadingDialog.dismiss();
                }
//                Toast.makeText(MessageFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(final List<TgBean.DataEntity> advVOs) {

                if (GlobalParams.isDebug){
                    Log.e("TAG",position+2+"");
                }
//                message_head.setImageUrl("头像的url,字段添加后修改", mImageLoader, new BitmapProcessor() {
//                    @Override
//                    public Bitmap processBitmpa(Bitmap bitmap) {
//                        return ImageUtils.getCircleBitmap(bitmap, getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
//                    }
//                });

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (advVOs.get(position).getUrlType()==3){
                            Log.e("TAG","无连接，不可点击");
                        }else{
                            Intent intent = new Intent(mContext, H5Activity.class);
                            String url = advList.get(position).getUrl();
                            H5Activity.H5Data h5Data = new H5Activity.H5Data();
                            h5Data.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                            h5Data.data = url;
                            h5Data.showProcess = true;
                            h5Data.showNav = true;
                            h5Data.canZoom = true;
                            h5Data.title = "通告";
                            intent.putExtra(H5Activity.H5Data.H5_DATA, h5Data);
                            startActivity(intent);
                        }
                    }
                });

                if (isFirst) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDialog.dismiss();
                }
                advList = advVOs;
                adapter.addAll(advVOs, needClear);
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_ls_fail_layout:
                setData(true);
                break;
        }
    }


    @Override
    public void onScroll(int scrollY) {
        tabLayoutHeight = 44;
        dianLayoutTop = message_name.getTop();
        myScrollViewTop = myScrollView.getTop();
        if (GlobalParams.isDebug) {
            Log.e("TAG", tabLayoutHeight + "," + dianLayoutTop + "," + myScrollViewTop);
        }
        if (scrollY >= dianLayoutTop) {
            if (isFirst) {
                Intent intent = new Intent("show");
                mContext.sendBroadcast(intent);

                myAnimation_Alpha = new AlphaAnimation(1.0f, 0.0f);
                myAnimation_Alpha.setDuration(3000);
                message_bg.startAnimation(myAnimation_Alpha);
                message_bg.setVisibility(View.INVISIBLE);


                isFirst = false;
                isFirst1 = true;
            }
        } else {
            if (isFirst1) {
                Intent intent = new Intent("hide");
                mContext.sendBroadcast(intent);

                myAnimation_Alpha1 = new AlphaAnimation(0.0f, 1.0f);
                myAnimation_Alpha1.setDuration(1000);
                message_bg.startAnimation(myAnimation_Alpha1);
                message_bg.setVisibility(View.VISIBLE);

                isFirst1 = false;
                isFirst = true;
            }

        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals("go")) {
                mListView.setFocusable(false);
                myScrollView.smoothScrollTo(0, 380);
                myAnimation_Alpha = new AlphaAnimation(1.0f, 0.0f);
                myAnimation_Alpha.setDuration(3000);
                message_bg.startAnimation(myAnimation_Alpha);
                message_bg.setVisibility(View.INVISIBLE);
            } else if (action.equals("wait")){
                myScrollView.smoothScrollTo(0, 0);
                myAnimation_Alpha1 = new AlphaAnimation(0.0f, 1.0f);
                myAnimation_Alpha1.setDuration(1000);
                message_bg.startAnimation(myAnimation_Alpha1);
                message_bg.setVisibility(View.VISIBLE);
            }

        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("go");
        myIntentFilter.addAction("wait");
//        myIntentFilter.addAction("load");
        //注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }




}

