package com.floyd.diamond.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.ui.view.YWPopupWindow;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = "MyFragment";

    private YWPopupWindow ywPopupWindow;

    private TextView editHeadButton;
    private TextView editProfileButton;
    private TextView cancelButton;
    private ImageView headView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);

        headView = (ImageView) view.findViewById(R.id.mine_touxiang);
        ywPopupWindow = new YWPopupWindow(this.getActivity());
        ywPopupWindow.initView(headView, R.layout.popup_edit_head, R.dimen.edit_head_bar_heigh, new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                editHeadButton = (TextView) v.findViewById(R.id.edit_head);
                editHeadButton.setOnClickListener(MyFragment.this);
                editProfileButton = (TextView) v.findViewById(R.id.edit_profile);
                editProfileButton.setOnClickListener(MyFragment.this);
                cancelButton = (TextView) v.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(MyFragment.this);
            }
        });
        headView.setOnClickListener(this);
        LoginVO loginVO  = LoginManager.getLoginInfo(this.getActivity());
        MoteManager.fetchMoteInfoJob(loginVO.accessToken).startUI(new ApiCallback<ApiResult<MoteInfoVO>>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(ApiResult<MoteInfoVO> moteInfoVOApiResult) {
                Log.i(TAG, "---" + moteInfoVOApiResult);

            }

            @Override
            public void onProgress(int progress) {

            }
        });

        return view;
    }

    @Override
    public boolean onBackPressed() {
        ywPopupWindow.hidePopUpWindow();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_touxiang:
                ywPopupWindow.showPopUpWindow();
                break;
            case R.id.cancel_button:
                ywPopupWindow.hidePopUpWindow();
                break;
            case R.id.edit_head:
                break;
            case R.id.edit_profile:
                break;
        }
    }
}
