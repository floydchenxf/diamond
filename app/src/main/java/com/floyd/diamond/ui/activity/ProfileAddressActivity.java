package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.AreaDetailVO;
import com.floyd.diamond.biz.vo.AreaVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.adapter.AreaAdapter;

import java.util.List;

public class ProfileAddressActivity extends Activity implements View.OnClickListener {

    public static final String AREA_DETAIL_INFO = "AREA_DETAIL_INFO";

    private ListView addressListView;
    private TextView addressShowView;
    private EditText addressDetailView;
    private TextView confirmView;

    private Dialog dataLoadingDialog;
    private AreaAdapter areaAdapter;

    private int step = 0;//当前步骤
    private long provineId; //省份
    private long cityId; //城市
    private long districtId;

//    private long pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_address);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);

        initView();
        fillData(0l, new AreaCompleteCallback() {
            @Override
            public void onCallback(List<AreaVO> vos) {
                step = 1;
            }
        });
    }

    private void initView() {

        addressShowView = (TextView) findViewById(R.id.profile_address);
        addressListView = (ListView) findViewById(R.id.profile_address_listview);
        confirmView = (TextView) findViewById(R.id.confirm_view);
        addressDetailView = (EditText) findViewById(R.id.address_detail_view);

        confirmView.setOnClickListener(this);

        findViewById(R.id.left).setOnClickListener(this);
        areaAdapter = new AreaAdapter(this);
        addressListView.setAdapter(areaAdapter);
        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final List<AreaVO> areaList = areaAdapter.getAreaList();
                final AreaVO vo = areaList.get(position);
                final long cpid = vo.id;
                fillData(cpid, new AreaCompleteCallback() {
                    @Override
                    public void onCallback(List<AreaVO> vos) {
                        if (step == 1) {
                            provineId = cpid;
                            addressShowView.setText(vo.name);
                            step = 2;
                        } else if (step == 2) {
                            cityId = cpid;
                            String addressName = addressShowView.getText().toString();
                            addressShowView.setText(addressName + " " + vo.name);
                            if (vos == null || vos.isEmpty()) {
                                hiddenAreaChoose();
                                return;
                            }
                            step = 3;
                        } else if (step == 3) {
                            districtId = cpid;
                            String addressName = addressShowView.getText().toString();
                            addressShowView.setText(addressName + " " + vo.name);
                            if (vos == null || vos.isEmpty()) {
                                hiddenAreaChoose();
                                return;
                            }
                        }
                    }
                });

            }
        });

        showAreaChoose();
    }

    private void hiddenAreaChoose() {
        addressListView.setVisibility(View.GONE);
        addressDetailView.setVisibility(View.VISIBLE);
        confirmView.setVisibility(View.VISIBLE);
    }

    private void showAreaChoose() {
        addressListView.setVisibility(View.VISIBLE);
        addressDetailView.setVisibility(View.GONE);
        confirmView.setVisibility(View.GONE);
    }

    private void fillData(long pid, final AreaCompleteCallback callback) {
        dataLoadingDialog.show();
        MoteManager.getAreaListByPid(pid).startUI(new ApiCallback<List<AreaVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!ProfileAddressActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                    Toast.makeText(ProfileAddressActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(List<AreaVO> areaList) {
                if (!ProfileAddressActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                    areaAdapter.addAll(areaList, true);
                    if (callback != null) {
                        callback.onCallback(areaList);
                    }
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
            case R.id.left:
//                if (step == 0) {
//                    this.finish();
//                } else if (step == 1) {
//                    fillData(0l, new AreaCompleteCallback() {
//                        @Override
//                        public void onCallback(List<AreaVO> vos) {
//                            step = 0;
//                        }
//                    });
//                } else if (step == 2) {
//                    fillData(provineId, new AreaCompleteCallback() {
//                        @Override
//                        public void onCallback(List<AreaVO> vos) {
//                            step = 1;
//                        }
//                    });
//                } else if (step == 3) {
//                    fillData(cityId, new AreaCompleteCallback() {
//                        @Override
//                        public void onCallback(List<AreaVO> vos) {
//                            step = 2;
//                        }
//                    });
//                }
                this.finish();
                break;
            case R.id.confirm_view:
                String addressShowInfo = addressShowView.getText().toString();
                String addressDetailInfo = addressDetailView.getText().toString();
                if (TextUtils.isEmpty(addressDetailInfo)) {
                    Toast.makeText(this, "请输入地址信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(addressShowInfo)) {
                    Toast.makeText(this, "请输入城市信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                AreaDetailVO areaDetailVO = new AreaDetailVO(Parcel.obtain());
                areaDetailVO.provideId = provineId;
                areaDetailVO.cityId = cityId;
                areaDetailVO.districtId = districtId;
                areaDetailVO.addressSummary = addressShowInfo;
                areaDetailVO.addressDetail = addressDetailInfo;

                Intent it = new Intent();
                it.putExtra(AREA_DETAIL_INFO, areaDetailVO);
                setResult(RESULT_OK, it);
                finish();
                break;
        }
    }
}


interface AreaCompleteCallback {
    void onCallback(List<AreaVO> vos);
}