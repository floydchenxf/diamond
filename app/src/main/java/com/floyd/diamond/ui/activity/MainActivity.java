package com.floyd.diamond.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.event.LoginEvent;
import com.floyd.diamond.ui.activity.LoginActivity;
import com.floyd.diamond.ui.fragment.BackHandledFragment;
import com.floyd.diamond.ui.fragment.FragmentTabAdapter;
import com.floyd.diamond.ui.fragment.IndexFragment;
import com.floyd.diamond.ui.fragment.MessageFragment;
import com.floyd.diamond.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class MainActivity extends FragmentActivity implements View.OnClickListener, BackHandledInterface {

    private static final String TAG = "MainActivity";

    private FragmentTransaction fragmentTransaction;

    private BackHandledFragment mBackHandedFragment;

    private RadioGroup rgs;

    private RadioButton myButton;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.umeng.socialize.utils.Log.LOG = true;

        fragments.add(new IndexFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());

        myButton = (RadioButton)findViewById(R.id.tab_my);
        myButton.setOnClickListener(this);

        rgs = (RadioGroup) findViewById(R.id.id_ly_bottombar);

        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.id_content, rgs);
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

        EventBus.getDefault().register(this);

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

    public void onBackPressed() {
        if(mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()){
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        Log.i(TAG, "-----------login event fire");
        tabAdapter.onCheckedChanged(rgs, R.id.tab_my);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
