package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.floyd.diamond.bean.Care;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.bean.SwipeRefreshLayout;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.CareAdapter;
import com.floyd.diamond.ui.adapter.CareAdapter1;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.utils.CommonUtil;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/19.
 */
public class CareActivity extends Activity implements View.OnClickListener {
    private LinearLayout back;//返回按钮
    private LinearLayout find;//查找模特
    private RequestQueue queue;
    private List<Care.DataEntity.DataListEntity> modelsList;
    private List<Care.DataEntity.DataListEntity> allModel;//大集合
    private int pageNo = 1;//当前页数
    private static final int PAGE_SIZE = 10;
    private ImageLoader mImageLoader;
    private TextView care;
    private PullToRefreshListView mPullToRefreshListView;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;
    private ListView mListView;
    private TextView emptyView;
    private boolean isClear;
    private TextView edit;
    private boolean needClear;
    private LoginVO loginVO;
    private String editOrdelete = "编辑";
    private boolean isFirst = true;
    private ArrayList<String> deleteModel;//取消关注的模特
    private com.floyd.diamond.bean.SwipeRefreshLayout swipeRefreshLayout;
    private CareAdapter1 adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            adapter.notifyDataSetChanged();

            if (editOrdelete.equals("编辑")) {
                adapter.setMyOnItemClickListener(new CareAdapter1.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, Care.DataEntity.DataListEntity dataEntity) {
                        Intent it = new Intent(CareActivity.this, MoteDetailActivity.class);
                        it.putExtra("moteId", dataEntity.getMoteId());
                        startActivity(it);
                    }
                });
            } else {
                adapter.setMyOnItemClickListener(new CareAdapter1.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, Care.DataEntity.DataListEntity dataEntity) {
                        final TextView bg_recycle = (TextView) mListView.findViewWithTag("cb" + dataEntity.getMoteId());
                        bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight()));

                        // Toast.makeText(CareActivity.this, "要删除了哦~~", Toast.LENGTH_SHORT).show();

                        deleteModel.add(dataEntity.getMoteId() + "");
                    }
                });
            }

//            if (editOrdelete.equals("编辑")) {
//                adapter.setMyOnItemClickListener(new CareAdapter1.MyOnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, Care.DataEntity.DataListEntity dataEntity) {
//                        Intent it = new Intent(CareActivity.this, MoteDetailActivity.class);
//                        it.putExtra("moteId", dataEntity.getMoteId());
//                        startActivity(it);
//                    }
//                });
//            } else {
//                adapter.setMyOnItemClickListener(new CareAdapter1.MyOnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, Care.DataEntity.DataListEntity dataEntity) {
//                        final TextView bg_recycle = (TextView) mListView.findViewWithTag("cb" + dataEntity.getMoteId());
//                        bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight()));
//
//                        // Toast.makeText(CareActivity.this, "要删除了哦~~", Toast.LENGTH_SHORT).show();
//
//                        deleteModel.add(dataEntity.getMoteId() + "");
//
//                    }
//                });
//            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();

        loadData(true);

//        //设置adapter
        adapter = new CareAdapter1(this, mImageLoader, deleteModel);
        mListView.setAdapter(adapter);

    }

    public void init() {
        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        loginVO = LoginManager.getLoginInfo(this);
        care = ((TextView) findViewById(R.id.center));
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), CareActivity.this);
        emptyView = (TextView) findViewById(R.id.empty_info);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        mPullToRefreshListView = ((PullToRefreshListView) findViewById(R.id.gridview_filter));
        mListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
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

        care.setText("我的关注");
        care.setTextColor(Color.WHITE);
        care.setTextSize(16);
        edit = ((TextView) findViewById(R.id.edit));
        edit.setBackgroundColor(Color.parseColor("#d4377e"));
        edit.setText("编辑");
        edit.setTextColor(Color.WHITE);
        edit.setTextSize(10);
        back = ((LinearLayout) findViewById(R.id.left));
        find = ((LinearLayout) findViewById(R.id.right));
        queue = Volley.newRequestQueue(CareActivity.this);
        modelsList = new ArrayList<>();//用于存放每一页的模特
        allModel = new ArrayList<>();//用于存储所有的模特
        deleteModel = new ArrayList<>();//取消关注的模特


        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击跳转到筛选模特界面
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOrdelete = edit.getText().toString();
                if (editOrdelete.equals("编辑")) {
                    edit.setText("删除");
                    editOrdelete = "删除";

                    handler.sendEmptyMessage(2);
                } else if (editOrdelete.equals("删除")) {
                    if (deleteModel.size() == 0) {
                        Toast.makeText(CareActivity.this, "~~请选择取消关注的模特~~", Toast.LENGTH_SHORT).show();
                    } else {
                        if (GlobalParams.isDebug) {
                            Log.e("deleteModel", deleteModel.toString());
                        }

                        Intent intent = new Intent(CareActivity.this, CareDialogActivity.class);
                        intent.putStringArrayListExtra("deleteList", deleteModel);
                        startActivity(intent);

                        deleteModel.clear();

                        edit.setText("编辑");
                        editOrdelete = "编辑";

                        handler.sendEmptyMessage(3);

                    }

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_ls_fail_layout:
                isClear = true;
                pageNo = 1;
                loadData(true);
                break;
        }

    }

    private void loadData(final boolean isFirst) {

        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        LoginVO loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null ? "" : loginVO.token;
        MoteManager.careMoteDelete(pageNo, PAGE_SIZE, token).startUI(new ApiCallback<Care.DataEntity>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!CareActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(CareActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(Care.DataEntity modelInfo) {
                if (!CareActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDialog.dismiss();
                    }
                }


                List<Care.DataEntity.DataListEntity> taskItemVOs = modelInfo.getDataList();
//                if (taskItemVOs.size()==0){
//                    Toast.makeText(CareActivity.this,"~你还没有关注模特，赶紧去关注吧~",Toast.LENGTH_SHORT).show();
//                }
                if ((taskItemVOs == null || taskItemVOs.isEmpty()) && pageNo == 1) {
//                    emptyView.setVisibility(View.VISIBLE);
                    Toast.makeText(CareActivity.this,"~你还没有关注模特，赶紧去关注吧~",Toast.LENGTH_SHORT).show();
                    mPullToRefreshListView.setVisibility(View.GONE);

                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);

                    List<MoteTypeTaskVO> typeTasks = new ArrayList<MoteTypeTaskVO>();
//
                    int idx = 0;
                    for (Care.DataEntity.DataListEntity vo : taskItemVOs) {
                        if (idx % 2 == 0) {
                            MoteTypeTaskVO moteTypeTaskVO = new MoteTypeTaskVO();
                            moteTypeTaskVO.moteCareVO1 = vo;
                            typeTasks.add(moteTypeTaskVO);
                        } else {
                            typeTasks.get(idx / 2).moteCareVO2 = vo;
                        }

                        idx++;
                    }

                    if (!typeTasks.isEmpty()) {
                        adapter.addAll(typeTasks, isClear);
                    }

                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }


}
