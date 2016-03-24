package com.floyd.diamond.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.BadgeUtil;
import com.floyd.diamond.bean.Code;
import com.floyd.diamond.bean.UpdateManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.BackHandledInterface;
import com.floyd.diamond.ui.fragment.BackHandledFragment;
import com.floyd.diamond.ui.fragment.FragmentTabAdapter;
import com.floyd.diamond.ui.fragment.IndexFragment;
import com.floyd.diamond.ui.fragment.MessageFragment;
import com.floyd.diamond.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, BackHandledInterface {

    private static final String TAG = "MainActivity";

    private FragmentTransaction fragmentTransaction;

    private BackHandledFragment mBackHandedFragment;

    private RadioGroup rgs;

    private RadioButton myButton;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentTabAdapter tabAdapter;

    private long exitTime = 0;

    private String serverVersion;

    private UpdateManager mUpdateManager;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //这里来检测版本是否需要更新
            Log.e("version",serverVersion);
            mUpdateManager = new UpdateManager(MainActivity.this,serverVersion);
            mUpdateManager.showNoticeDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadCode();

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

    }


    public void loadCode(){
        MoteManager.getNewCode().startUI(new ApiCallback<Code>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(Code code) {
                serverVersion=code.getVersion();

                handler.sendEmptyMessage(1);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_my:
                boolean isLogin = LoginManager.isLogin(this);
                if (!isLogin) {
                    int currentTab = tabAdapter.getCurrentTab();
                    if (currentTab == 0) {
                        rgs.check(R.id.tab_index_page);
                    } else if (currentTab == 1) {
                        rgs.check(R.id.tab_message);
                    } else {
                        rgs.check(R.id.tab_my);
                    }
                }

                break;
            default:
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    getSupportFragmentManager().popBackStack();
                }
            }
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            View toastRoot = getLayoutInflater().inflate(R.layout.toast_layout, null);
            Toast toast=new Toast(MainActivity.this);
            toast.setView(toastRoot);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
