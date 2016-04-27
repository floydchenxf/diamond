package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.DLCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.bean.NickNameMote;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.HomeSearchAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/18.
 */
public class ChooseResultActivity extends Activity implements View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;
    private int pageNo = 1;
    private int pageNum=1;
    private static final int PAGE_SIZE = 10;
    private ImageLoader mImageLoader;
    private ListView mListView;
    private HomeChooseAdapter1 homeChooseAdapter;
    private HomeSearchAdapter homeSearchAdapter;
    private boolean isClear;
    private boolean isClearNickName;
    private List<ModelInfo.DataEntity> modelsList;
    private LinearLayout choose;
    private  LoginVO loginVO;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();


        if (type.equals("1")){

            loadNickNameData(true);
            homeSearchAdapter=new HomeSearchAdapter(this,mImageLoader,new HomeSearchAdapter.OnItemClickListener(){

                @Override
                public void onItemClick(View v, NickNameMote.DataEntity dataEntity) {
                    Intent it = new Intent(ChooseResultActivity.this, MoteDetailActivity.class);
                    it.putExtra("moteId",dataEntity.getId());
                    startActivity(it);

//                    Intent it = new Intent(ChooseResultActivity.this, MoteDetailActivity.class);
//                    it.putExtra("moteId", dataEntity.getId());
//                    if (GlobalParams.isDebug){
//                        Log.e("TAG",dataEntity.getId()+"传值");
//                    }
//                    startActivity(it);

                }
            },new HomeSearchAdapter.ChangeText(){

                @Override
                public void setText(String tag, boolean isChecked, NickNameMote.DataEntity dataEntity) {

                    boolean isLogin=LoginManager.isLogin(ChooseResultActivity.this);
                    final CheckBox cb = (CheckBox) mListView.findViewWithTag(tag);
                    if (!isLogin){
                        return;
                    }
                    loginVO = LoginManager.getLoginInfo(ChooseResultActivity.this);
                    if (cb != null) {
                        if (isChecked) {

                            MoteManager.addFollow(dataEntity.getId(), loginVO.token).startUI(new ApiCallback<Integer>() {
                                @Override
                                public void onError(int code, String errorInfo) {
                                    Toast.makeText(ChooseResultActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Integer aBoolean) {
                                    Toast.makeText(ChooseResultActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    cb.setText((Integer.parseInt(cb.getText().toString()) + 1) + "");
//                                    dataEntity.setIsFollow(true);

                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });

                        } else {
                            MoteManager.cancelOneFollow(dataEntity.getId(), loginVO.token).startUI(new ApiCallback<Boolean>() {
                                @Override
                                public void onError(int code, String errorInfo) {
                                    Toast.makeText(ChooseResultActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Boolean num) {
                                    Toast.makeText(ChooseResultActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                    cb.setText((Integer.parseInt(cb.getText().toString()) - 1) + "");
//                                    dataEntity.setIsFollow(false);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }
                    }
                }
            });
            mListView.setAdapter(homeSearchAdapter);

        }else if (type.equals("2")){

            loadData(true);

            homeChooseAdapter = new HomeChooseAdapter1(this, mImageLoader, new HomeChooseAdapter1.OnItemClickListener() {
                @Override
                public void onItemClick(View v, ModelInfo.DataEntity dataEntity) {

//                Toast.makeText(ChooseResultActivity.this,"点击了哦",Toast.LENGTH_SHORT).show();

                    Intent it = new Intent(ChooseResultActivity.this, MoteDetailActivity.class);
                    it.putExtra("moteId",dataEntity.getId());
                    startActivity(it);
                }
            },new HomeChooseAdapter1.ChangeText() {
                @Override
                public void setText(String tag, boolean isChecked, final ModelInfo.DataEntity dataEntity) {
                    boolean isLogin=LoginManager.isLogin(ChooseResultActivity.this);
                    final CheckBox cb = (CheckBox) mListView.findViewWithTag(tag);
                    if (!isLogin){
                        return;
                    }
                    loginVO = LoginManager.getLoginInfo(ChooseResultActivity.this);
                    if (cb != null) {
                        if (isChecked) {

                            MoteManager.addFollow(dataEntity.getId(), loginVO.token).startUI(new ApiCallback<Integer>() {
                                @Override
                                public void onError(int code, String errorInfo) {
                                    Toast.makeText(ChooseResultActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Integer aBoolean) {
                                    Toast.makeText(ChooseResultActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    cb.setText((Integer.parseInt(cb.getText().toString()) + 1) + "");
                                    dataEntity.setIsFollow(true);

                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });

                        } else {
                            MoteManager.cancelOneFollow(dataEntity.getId(), loginVO.token).startUI(new ApiCallback<Boolean>() {
                                @Override
                                public void onError(int code, String errorInfo) {
                                    Toast.makeText(ChooseResultActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Boolean num) {
                                    Toast.makeText(ChooseResultActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                    cb.setText((Integer.parseInt(cb.getText().toString()) - 1) + "");
                                    dataEntity.setIsFollow(false);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }
                    }

                }
            });
            mListView.setAdapter(homeChooseAdapter);
        }





    }

    public void init(){
        EditText search_jia= ((EditText) findViewById(R.id.et_search_jia));
        // 调整EditText左边的搜索按钮的大小
//        Drawable drawable = getResources().getDrawable(R.drawable.search_red);
//        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
//        search_jia.setCompoundDrawables(drawable, null, null, null);// 只放左边
        search_jia.setOnClickListener(this);
        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseResultActivity.this, ChooseActivity1.class));
            }
        });
//        ((TextView) findViewById(R.id.center)).setText("模特");
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), ChooseResultActivity.this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        findViewById(R.id.left).setOnClickListener(this);
        mPullToRefreshListView= ((PullToRefreshListView) findViewById(R.id.gridview_filter));
        mListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        type=getIntent().getStringExtra("type");

        if (GlobalParams.isDebug){
            Log.e("TAG",type);
        }

        if (type.equals(1+"")){
            mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
                @Override
                public void onPullDownToRefresh() {
                    dataLoadingDialog.show();
                    pageNum++;
                    isClearNickName = false;
                    loadNickNameData(false);
                    mPullToRefreshListView.onRefreshComplete(false, true);
                }

                @Override
                public void onPullUpToRefresh() {
                    dataLoadingDialog.show();
                    pageNum++;
                    isClearNickName = false;
                    loadNickNameData(false);
                    mPullToRefreshListView.onRefreshComplete(false, true);
                }
            });
        }else{
            mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
                @Override
                public void onPullDownToRefresh() {
                    dataLoadingDialog.show();
                    pageNo++;
                    isClear = false;
                    loadData(false);
                    mPullToRefreshListView.onRefreshComplete(false, true);
                }

                @Override
                public void onPullUpToRefresh() {
                    dataLoadingDialog.show();
                    pageNo++;
                    isClear = false;
                    loadData(false);
                    mPullToRefreshListView.onRefreshComplete(false, true);
                }
            });
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                isClear = true;
                if (type==1+""){
                    pageNo = 1;
                    loadNickNameData(true);
                }else{
                    pageNum=1;
                    loadData(true);
                }
                break;
            case R.id.et_search_jia:
                Intent intent=new Intent(ChooseResultActivity.this,HomeSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

    }

    private void loadNickNameData(final boolean isFirst){

        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        String nickName=getIntent().getStringExtra("nickName");
        loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null?"":loginVO.token;
        MoteManager.filterMoteNickNameResult(nickName,token,pageNum,PAGE_SIZE).startUI(new ApiCallback<List<NickNameMote.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!ChooseResultActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(ChooseResultActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(List<NickNameMote.DataEntity> dataEntities) {
                if (!ChooseResultActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDialog.dismiss();
                    }
                }
                List<NickNameMote.DataEntity> taskItemVOs = dataEntities;
                if ((taskItemVOs == null || taskItemVOs.isEmpty()) && pageNum == 1) {
                    mPullToRefreshListView.setVisibility(View.GONE);
                    setContentView(R.layout.activity_chooseresultnull);
                    findViewById(R.id.back_null).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    findViewById(R.id.et_search_jia).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ChooseResultActivity.this, HomeSearchActivity.class));
                        }
                    });
                    findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ChooseResultActivity.this, ChooseActivity1.class));
                        }
                    });

                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);

                    List<MoteTypeTaskVO> typeTasks = new ArrayList<MoteTypeTaskVO>();
                    int idx = 0;
                    for (NickNameMote.DataEntity vo : taskItemVOs) {

                        if (idx % 2 == 0) {
                            MoteTypeTaskVO moteTypeTaskVO = new MoteTypeTaskVO();
                            moteTypeTaskVO.nickItemV01 = vo;
                            typeTasks.add(moteTypeTaskVO);
                            if (GlobalParams.isDebug){
                                Log.e("TAG","0"+moteTypeTaskVO.nickItemV01.getArea());
                            }
                        } else {
                            typeTasks.get(idx / 2).nickItemV02 = vo;
                            if (GlobalParams.isDebug){
                                Log.e("TAG","1");
                            }
                        }

                        idx++;
                    }

                    if (!typeTasks.isEmpty()) {
                        homeSearchAdapter.addAll(typeTasks, isClear);
                    }

                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    private void loadData(final boolean isFirst) {
//        if (loginVO == null) {
//            Toast.makeText(this, "未登录状态无法获取数据", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String type=getIntent().getStringExtra("type");
//        if (type.equals("1")){
//
//        }else if (type.equals("2")){
//
//        }

        final DLCondition.DataEntity dataEntity= (DLCondition.DataEntity) getIntent().getSerializableExtra("chooseCondition");

        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null?"":loginVO.token;
        MoteManager.filterMoteResult(pageNo, PAGE_SIZE, token,dataEntity).startUI(new ApiCallback<List<ModelInfo.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!ChooseResultActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(ChooseResultActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(List<ModelInfo.DataEntity> modelInfo) {
                if (!ChooseResultActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDialog.dismiss();
                    }
                }

                List<ModelInfo.DataEntity> taskItemVOs = modelInfo;
                if ((taskItemVOs == null || taskItemVOs.isEmpty()) && pageNo == 1) {
                    mPullToRefreshListView.setVisibility(View.GONE);
                    setContentView(R.layout.activity_chooseresultnull);
                    ((TextView) findViewById(R.id.content_1)).setText("该条件无匹配模特");
                    ((TextView) findViewById(R.id.content_2)).setText("小技巧：放宽筛选条件更容易筛选到哦");
                    findViewById(R.id.back_null).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    findViewById(R.id.et_search_jia).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ChooseResultActivity.this, HomeSearchActivity.class));
                        }
                    });
                    findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ChooseResultActivity.this,ChooseActivity1.class));
                        }
                    });

                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);

                    List<MoteTypeTaskVO> typeTasks = new ArrayList<MoteTypeTaskVO>();
                    int idx = 0;
                    for (ModelInfo.DataEntity vo : taskItemVOs) {
                        if (idx % 2 == 0) {
                            MoteTypeTaskVO moteTypeTaskVO = new MoteTypeTaskVO();
                            moteTypeTaskVO.moteItemVO1 = vo;
                            typeTasks.add(moteTypeTaskVO);
                        } else {
                            typeTasks.get(idx / 2).moteItemVO2 = vo;
                        }

                        idx++;
                    }

                    if (!typeTasks.isEmpty()) {
                        homeChooseAdapter.addAll(typeTasks, isClear);
                    }
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }
}
