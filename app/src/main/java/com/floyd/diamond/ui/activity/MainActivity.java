package com.floyd.diamond.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.fragment.FragmentTabAdapter;
import com.floyd.diamond.ui.fragment.IndexFragment;
import com.floyd.diamond.ui.fragment.MessageFragment;
import com.floyd.diamond.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentTransaction fragmentTransaction;


    private RadioGroup rgs;

    private RadioButton myButton;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments.add(new IndexFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());

        myButton = (RadioButton)findViewById(R.id.tab_my);
        myButton.setOnClickListener(this);

        rgs = (RadioGroup) findViewById(R.id.id_ly_bottombar);

        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.id_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
            }
        });

        tabAdapter.setOnLoginCheck(new FragmentTabAdapter.OnLoginCheck() {
            @Override
            public boolean needLogin(RadioGroup radioGroup, int preCheckedId, int idx) {
                if (idx == 2) {
                    LoginVO loginVO = LoginManager.getLoginInfo(MainActivity.this);
                    if (loginVO == null) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_my:
                LoginVO loginVO = LoginManager.getLoginInfo(MainActivity.this);
                if (loginVO == null) {
                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(it);
                }

                break;
            default:
        }

    }
}
