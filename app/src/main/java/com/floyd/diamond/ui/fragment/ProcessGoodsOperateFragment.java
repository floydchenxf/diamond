package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.process.ProcessStatus;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.floyd.zxing.MipcaActivityCapture;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProcessGoodsOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessGoodsOperateFragment extends Fragment implements View.OnClickListener {

    private static final String TASK_PROCESS_VO = "TASK_PROCESS_VO";

    private static final int SCANNIN_GREQUEST_CODE = 100;

    private TaskProcessVO taskProcessVO;

    private View goodsOperateLayout;
    private CheckedTextView selfBuyView;
    private CheckedTextView returnBackView;
    private TextView selfBuyResultLayout;

    private TextView operateTimeView;

    private Dialog dataLoadingDialog;

    private View returnGoodsLayout;


    private TextView shopNameView;
    private TextView phoneNumView;
    private TextView addressView;

    private TextView expressCompanyView;
    private TextView expressNoView;
    private TextView confirmExpressNoView;

    private View expressInfoSettingLayout;
    private GridLayout expressCompanyLayout;
    private int expressCompanyId = -1;
    private String expressNO;

    private String[] expressCompanies = new String[]{"圆通", "申通", "天天", "EMS", "顺丰", "韵达", "中通", "汇通", "国通", "全峰", "京东", "优速", "速尔", "佳吉"};

    private TextView layoutExpressNoEditView;
    private ImageView layoutSaoView;
    private TextView layoutExpressNoConfirmView;


    public static ProcessGoodsOperateFragment newInstance(TaskProcessVO taskProcessVO) {
        ProcessGoodsOperateFragment fragment = new ProcessGoodsOperateFragment();
        Bundle args = new Bundle();
        args.putSerializable(TASK_PROCESS_VO, taskProcessVO);
        fragment.setArguments(args);
        return fragment;
    }

    public ProcessGoodsOperateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this.getActivity());
        taskProcessVO = (TaskProcessVO) getArguments().getSerializable(TASK_PROCESS_VO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_process_goods_operate, container, false);
        operateTimeView = (TextView) v.findViewById(R.id.operate_time);
        initGoodsOperateLayout(v);
        initSelfBuyView(v);
        initReturnGoodsView(v);
        initExpressInfo(v);
        expressInfoSettingLayout = v.findViewById(R.id.express_info_setting_layout);
        expressInfoSettingLayout.setVisibility(View.GONE);
        expressCompanyLayout = (GridLayout) v.findViewById(R.id.express_company_layout);

        fillData(v);
        return v;
    }

    private void fillData(View v) {
        int status = taskProcessVO.moteTask.status;
        if (status == ProcessStatus.UPLOAD_PICS) {
            operateTimeView.setText(DateUtil.getDateStr(System.currentTimeMillis()));
            goodsOperateLayout.setVisibility(View.VISIBLE);
            returnGoodsLayout.setVisibility(View.GONE);
            selfBuyResultLayout.setVisibility(View.GONE);
        } else if (status == ProcessStatus.SELF_BUY_GOODS) {
            goodsOperateLayout.setVisibility(View.GONE);
            returnGoodsLayout.setVisibility(View.GONE);
            selfBuyResultLayout.setVisibility(View.VISIBLE);
            fillSlefBuyData();
        } else if (status == ProcessStatus.RETURN_GOODS) {
            goodsOperateLayout.setVisibility(View.GONE);
            returnGoodsLayout.setVisibility(View.VISIBLE);
            selfBuyResultLayout.setVisibility(View.GONE);
            fillReturnGoodsData(true);
            String companyId = taskProcessVO.moteTask.expressCompanyId;
            String expressNo = taskProcessVO.moteTask.expressNo;
            showEditExpressInfo(companyId, expressNo);
        } else if (status > ProcessStatus.RETURN_GOODS) {
            long selfBuyTime = taskProcessVO.moteTask.selfBuyTime;
            long returnItemTime = taskProcessVO.moteTask.returnItemTime;
            boolean isSelfBuy = selfBuyTime > 0;
            boolean isReturnItem = returnItemTime > 0;
            if (isSelfBuy) {
                fillSlefBuyData();
            }

            if (isReturnItem) {
                fillReturnGoodsData(false);
                String companyId = taskProcessVO.moteTask.expressCompanyId;
                String expressNo = taskProcessVO.moteTask.expressNo;
                showExpressInfo(companyId, expressNo);
            }
        }
    }

    public void fillSlefBuyData() {
        long time = taskProcessVO.moteTask.selfBuyTime;
        operateTimeView.setText(DateUtil.getDateStr(time));
    }

    public void fillReturnGoodsData(boolean canEdit) {
        long time = taskProcessVO.moteTask.returnItemTime;
        operateTimeView.setText(DateUtil.getDateStr(time));
        String address = taskProcessVO.seller.address;
        String phoneNumber = taskProcessVO.seller.phoneNumber;
        String shopName = taskProcessVO.seller.shopName;
        addressView.setText(address);
        phoneNumView.setText(phoneNumber);
        shopNameView.setText(shopName);
    }

    private void initExpressInfo(View v) {
        expressCompanyView = (TextView) v.findViewById(R.id.express_company_id);
        expressNoView = (TextView) v.findViewById(R.id.express_no);
        confirmExpressNoView = (TextView) v.findViewById(R.id.confirm_express);
        hiddenExpressInfo();
    }

    private void hiddenExpressInfo() {
        expressCompanyView.setVisibility(View.GONE);
        expressNoView.setVisibility(View.GONE);
        confirmExpressNoView.setVisibility(View.GONE);
    }

    private void showExpressInfo(String companyId, String expressNo) {
        expressCompanyView.setVisibility(View.VISIBLE);
        expressNoView.setVisibility(View.VISIBLE);
        expressCompanyView.setText(companyId);
        expressNoView.setText(expressNo);

    }

    private void showEditExpressInfo(String companyId, String expressNO) {
        confirmExpressNoView.setVisibility(View.VISIBLE);
        expressCompanyView.setVisibility(View.VISIBLE);
        expressNoView.setVisibility(View.VISIBLE);
        confirmExpressNoView.setOnClickListener(this);
        expressCompanyView.setText(companyId);
        expressNoView.setText(expressNO);
    }

    private void initExpressGridLayout() {
        expressInfoSettingLayout.setVisibility(View.VISIBLE);
        expressCompanyLayout.removeAllViews();
        int width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        float ondp = this.getActivity().getResources().getDimension(R.dimen.one_dp);
        final float eachWidth = (width - 24 * ondp) / 4;
        int idx = 0;
        for (String s : expressCompanies) {
            View item = View.inflate(this.getActivity(), R.layout.process_express_company_item, null);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = (int) eachWidth;
            lp.height = (int) (60 * ondp);
            item.setLayoutParams(lp);
            CheckedTextView companyView = (CheckedTextView) item.findViewById(R.id.express_company_item);
            companyView.setText(s);
            companyView.setTextColor(Color.WHITE);
            companyView.setTag(idx++);
            companyView.setOnClickListener(this);
            expressCompanyLayout.addView(item);
        }

        layoutExpressNoEditView = (EditText) expressInfoSettingLayout.findViewById(R.id.express_no_edit_view);
        layoutExpressNoConfirmView = (TextView) expressInfoSettingLayout.findViewById(R.id.express_no_confirm_button);
        layoutExpressNoConfirmView.setOnClickListener(this);
        layoutSaoView = (ImageView) expressInfoSettingLayout.findViewById(R.id.express_sao);
        layoutSaoView.setOnClickListener(this);

    }

    private void initReturnGoodsView(View v) {
        returnGoodsLayout = v.findViewById(R.id.return_goods_layout);
        shopNameView = (TextView) v.findViewById(R.id.shop_name);
        phoneNumView = (TextView) v.findViewById(R.id.phone_num);
        addressView = (TextView) v.findViewById(R.id.goods_address);
    }

    private void initGoodsOperateLayout(View v) {
        goodsOperateLayout = v.findViewById(R.id.goods_operate_layout);
        returnBackView = (CheckedTextView) v.findViewById(R.id.return_back_view);
        selfBuyView = (CheckedTextView) v.findViewById(R.id.self_buy_view);
        returnBackView.setOnClickListener(this);
        selfBuyView.setOnClickListener(this);
    }

    private void initSelfBuyView(View v) {
        selfBuyResultLayout = (TextView) v.findViewById(R.id.self_buy_desc);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_back_view:
                goodsOperateLayout.setVisibility(View.GONE);
                returnGoodsLayout.setVisibility(View.VISIBLE);
                selfBuyResultLayout.setVisibility(View.GONE);
                fillReturnGoodsData(true);
                initExpressGridLayout();
                break;
            case R.id.self_buy_view:
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this.getActivity());
                builder.setMessage("您确认自购商品？")
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dataLoadingDialog.show();
                                        LoginVO vo = LoginManager.getLoginInfo(ProcessGoodsOperateFragment.this.getActivity());
                                        MoteManager.selfBuy(taskProcessVO.moteTask.id, vo.token).startUI(new ApiCallback<Boolean>() {
                                            @Override
                                            public void onError(int code, String errorInfo) {
                                                if (!ProcessGoodsOperateFragment.this.getActivity().isFinishing()) {
                                                    dataLoadingDialog.dismiss();
                                                    Toast.makeText(ProcessGoodsOperateFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                if (!ProcessGoodsOperateFragment.this.getActivity().isFinishing()) {
                                                    dataLoadingDialog.dismiss();
                                                    goodsOperateLayout.setVisibility(View.GONE);
                                                    selfBuyResultLayout.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onProgress(int progress) {

                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.express_company_item:
                if (expressCompanyId > -1) {
                    CheckedTextView textView = (CheckedTextView) expressCompanyLayout.findViewWithTag(expressCompanyId);
                    textView.setTextColor(Color.WHITE);
                    textView.setChecked(true);
                }

                CheckedTextView view = (CheckedTextView) v;
                expressCompanyId = (int) v.getTag();
                view.setChecked(false);
                view.setTextColor(Color.BLACK);
                break;
            case R.id.express_no_confirm_button:
                if (expressCompanyId == -1) {
                    Toast.makeText(this.getActivity(), "请选择快递公司", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String expressCompany = expressCompanies[expressCompanyId];
                final String expressNo = layoutExpressNoEditView.getText().toString();
                if (TextUtils.isEmpty(expressNo)) {
                    Toast.makeText(this.getActivity(), "请输入快递单号", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataLoadingDialog.show();
                LoginVO vo = LoginManager.getLoginInfo(this.getActivity());
                MoteManager.returnGoods(taskProcessVO.moteTask.id, vo.token, expressCompany, expressNo).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        if (!ProcessGoodsOperateFragment.this.getActivity().isFinishing()) {
                            dataLoadingDialog.dismiss();
                            Toast.makeText(ProcessGoodsOperateFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!ProcessGoodsOperateFragment.this.getActivity().isFinishing()) {
                            dataLoadingDialog.dismiss();
                            expressInfoSettingLayout.setVisibility(View.GONE);
                            showEditExpressInfo(expressCompany, expressNo);
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.confirm_express:
                expressInfoSettingLayout.setVisibility(View.VISIBLE);
                initExpressGridLayout();
                hiddenExpressInfo();
                break;
            case R.id.express_sao:
                Intent intent = new Intent(ProcessGoodsOperateFragment.this.getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    layoutExpressNoEditView.setText(bundle.getString("result"));
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}