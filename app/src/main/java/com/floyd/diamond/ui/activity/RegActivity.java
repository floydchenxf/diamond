package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;

public class RegActivity extends Activity implements View.OnClickListener {

    private CheckedTextView sellerView;
    private CheckedTextView modelView;
    private EditText phoneNumberView;
    private TextView backView;
    private TextView nextStepView;

    private int checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        sellerView = (CheckedTextView) findViewById(R.id.seller);
        modelView = (CheckedTextView) findViewById(R.id.model);
        phoneNumberView = (EditText) findViewById(R.id.user_name);
        backView = (TextView) findViewById(R.id.back);
        nextStepView = (TextView) findViewById(R.id.next_step);
        backView.setOnClickListener(this);
        sellerView.setOnClickListener(this);
        modelView.setOnClickListener(this);
        nextStepView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.seller:
                modelView.setChecked(false);
                sellerView.setChecked(true);
                checkType = 1;
                break;
            case R.id.model:
                modelView.setChecked(true);
                sellerView.setChecked(false);
                checkType = 2;
                break;
            case R.id.next_step:
                String phoneNumber = phoneNumberView.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkType < 1) {
                    Toast.makeText(this, "请选择帐号类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent it = new Intent(this, Reg2Activity.class);
                it.putExtra("checkType", checkType);
                it.putExtra("phoneNumber", phoneNumber);
                startActivity(it);
                finish();
                break;
        }

    }
}
