package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.DialogCreator;

public class FeedbackActivity extends Activity implements View.OnClickListener {

    private EditText contentView;
    private TextView feedbackView;
    private TextView titleNameView;
    private LoginVO loginVO;
    private Dialog dataLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.title_back).setOnClickListener(this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        loginVO = LoginManager.getLoginInfo(this);
        contentView = (EditText) findViewById(R.id.content);
        feedbackView = (TextView) findViewById(R.id.feedback_button);
        titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setText(R.string.title_feedback);
        titleNameView.setVisibility(View.VISIBLE);
        feedbackView.setOnClickListener(this);
        contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()){

                    feedbackView.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }else{
                    feedbackView.setBackgroundResource(R.drawable.common_round_blue_bg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;
            case R.id.feedback_button:
                final String content = contentView.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "请输入反馈内容!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataLoadingDialog.show();
                MoteManager.addSuggestion(content, loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(FeedbackActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        dataLoadingDialog.dismiss();
                        contentView.setText("");
                        Toast.makeText(FeedbackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

                break;
        }

    }
}
