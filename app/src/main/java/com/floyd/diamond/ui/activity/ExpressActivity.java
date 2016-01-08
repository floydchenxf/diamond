package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.ExpressInfoVO;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

import java.util.List;

public class ExpressActivity extends Activity implements View.OnClickListener {

    public static final String EXPRESS_MOTE_TASK_ID = "_express_mote_task_id_";

    private DataLoadingView dataLoadingView;
    private LoginVO loginVO;
    private long moteTaskId;
    private View emptyView;
    private TextView titleNameView;

    private LinearLayout expressInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        emptyView = findViewById(R.id.empty_info);
        titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setText(R.string.title_express);
        expressInfoLayout = (LinearLayout) findViewById(R.id.express_info_layout);
        dataLoadingView = new DefaultDataLoadingView();
        loginVO = LoginManager.getLoginInfo(this);
        moteTaskId = getIntent().getLongExtra(EXPRESS_MOTE_TASK_ID, 5970);
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        findViewById(R.id.title_back).setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        dataLoadingView.startLoading();
        String token = loginVO == null ? "" : loginVO.token;
        MoteManager.fetchExpressInfos(token, moteTaskId).startUI(new ApiCallback<List<ExpressInfoVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (code == APIError.API_CONTENT_EMPTY) {
                    dataLoadingView.loadSuccess();
                    expressInfoLayout.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    return;
                }
                dataLoadingView.loadFail();
            }

            @Override
            public void onSuccess(List<ExpressInfoVO> expressInfoVOs) {
                dataLoadingView.loadSuccess();
                if (expressInfoVOs == null || expressInfoVOs.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    return;
                }

                emptyView.setVisibility(View.GONE);
                expressInfoLayout.removeAllViews();
                int idx = 0;
                for (ExpressInfoVO vo : expressInfoVOs) {
                    View v = View.inflate(ExpressActivity.this, R.layout.express_info_item, null);
                    LinearLayout itemLayout = (LinearLayout) v.findViewById(R.id.express_item_layout);
                    View line = itemLayout.findViewById(R.id.line);
                    TextView timeView = (TextView) itemLayout.findViewById(R.id.express_time_view);
                    TextView contentView = (TextView) itemLayout.findViewById(R.id.express_content_view);

                    if (idx == 0) {
                        line.setVisibility(View.GONE);
                    } else {
                        line.setVisibility(View.VISIBLE);
                    }

                    timeView.setText(vo.ftime);
                    contentView.setText(vo.context);
                    expressInfoLayout.addView(itemLayout);
                    idx++;
                }

            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.act_ls_fail_layout:
                loadData();
                break;
        }

    }
}
