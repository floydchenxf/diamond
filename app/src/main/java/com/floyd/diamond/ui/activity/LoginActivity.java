package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import com.floyd.diamond.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    private CheckedTextView loginButton;
    private CheckedTextView regButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (CheckedTextView) findViewById(R.id.login);
        regButton = (CheckedTextView) findViewById(R.id.regest);
        loginButton.setOnClickListener(this);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent phoneIntent = new Intent(this, PhoneLoginActivity.class);
                this.startActivity(phoneIntent);
                finish();
                break;
            case R.id.regest:
                Intent it = new Intent(this, RegActivity.class);
                this.startActivity(it);
                finish();
                break;
            default:
        }

    }
}