package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hy on 2016/1/16.
 */
public class HomeChooseActivity1 extends Activity implements View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;
    private int pageNo = 1;
    private static final int PAGE_SIZE = 10;
    private ImageLoader mImageLoader;
    private ListView mListView;
    private TextView emptyView;
    private HomeChooseAdapter1 homeChooseAdapter;
    private boolean isClear;
    private List<ModelInfo.DataEntity> modelsList;
    private LinearLayout choose;
    private LoginVO loginVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();

        loadData(true);

        homeChooseAdapter = new HomeChooseAdapter1(this, mImageLoader, new HomeChooseAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View v, ModelInfo.DataEntity dataEntity) {

                Intent it = new Intent(HomeChooseActivity1.this, MoteDetailActivity.class);
                it.putExtra("moteId", dataEntity.getId());
                startActivity(it);
            }
        }, new HomeChooseAdapter1.ChangeText() {
            @Override
            public void setText(String tag, boolean isChecked, final ModelInfo.DataEntity dataEntity) {
                boolean isLogin=LoginManager.isLogin(HomeChooseActivity1.this);
                if (!isLogin){
                    return;
                }
                LoginVO loginVO = LoginManager.getLoginInfo(HomeChooseActivity1.this);
                    final CheckBox cb = (CheckBox) mListView.findViewWithTag(tag);
                    if (cb != null) {
                        if (isChecked) {

                            MoteManager.addFollow(dataEntity.getId(), loginVO.token).startUI(new ApiCallback<Integer>() {
                                @Override
                                public void onError(int code, String errorInfo) {
                                    Toast.makeText(HomeChooseActivity1.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Integer aBoolean) {
                                    Toast.makeText(HomeChooseActivity1.this, "关注成功", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(HomeChooseActivity1.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(Boolean num) {
                                    Toast.makeText(HomeChooseActivity1.this, "取消关注成功", Toast.LENGTH_SHORT).show();
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

    public void init(){
        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeChooseActivity1.this,ChooseActivity1.class));
            }
        });
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), HomeChooseActivity1.this);
        emptyView = (TextView)findViewById(R.id.empty_info);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        findViewById(R.id.left).setOnClickListener(this);
        mPullToRefreshListView= ((PullToRefreshListView) findViewById(R.id.gridview_filter));
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                isClear = true;
                pageNo = 1;
                loadData(true);
                break;
        }

    }

    private void loadData(final boolean isFirst) {
//        if (loginVO == null) {
//            Toast.makeText(this, "未登录状态无法获取数据", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null?"":loginVO.token;
        MoteManager.filterMoteList(pageNo, PAGE_SIZE, token).startUI(new ApiCallback<List<ModelInfo.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!HomeChooseActivity1.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(HomeChooseActivity1.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(List<ModelInfo.DataEntity> modelInfo) {
                if (!HomeChooseActivity1.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDialog.dismiss();
                    }
                }

                List<ModelInfo.DataEntity> taskItemVOs = modelInfo;
                if ((taskItemVOs == null || taskItemVOs.isEmpty()) && pageNo == 1){
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);

                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);

                    List<MoteTypeTaskVO> typeTasks = new ArrayList<MoteTypeTaskVO>();
                    int idx = 0;
                    for (ModelInfo.DataEntity vo: taskItemVOs) {
                        if (idx % 2 == 0) {
                            MoteTypeTaskVO moteTypeTaskVO = new MoteTypeTaskVO();
                            moteTypeTaskVO.moteItemVO1 = vo;
                            typeTasks.add(moteTypeTaskVO);
                        } else {
                            typeTasks.get(idx/2).moteItemVO2 = vo;
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
