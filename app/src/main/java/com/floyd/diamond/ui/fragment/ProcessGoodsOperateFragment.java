package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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
import com.floyd.diamond.ui.view.UIAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProcessGoodsOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessGoodsOperateFragment extends Fragment implements View.OnClickListener {

    private static final String TASK_PROCESS_VO = "TASK_PROCESS_VO";

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
        dataLoadingDialog = new Dialog(this.getActivity(), R.style.data_load_dialog);
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

        fillData();
        return v;
    }

    private void fillData() {
        int status = taskProcessVO.moteTask.status;
        if (status == ProcessStatus.UPLOAD_PICS) {
            operateTimeView.setText(DateUtil.getDateStr(System.currentTimeMillis()));
            goodsOperateLayout.setVisibility(View.VISIBLE);
            returnGoodsLayout.setVisibility(View.GONE);
            selfBuyResultLayout.setVisibility(View.GONE);
        } else if (status > ProcessStatus.UPLOAD_PICS) {
            if (status == ProcessStatus.SELF_BUY_GOODS) {
                goodsOperateLayout.setVisibility(View.GONE);
                returnGoodsLayout.setVisibility(View.GONE);
                selfBuyResultLayout.setVisibility(View.VISIBLE);
                fillSlefBuyData();
            } else if (status == ProcessStatus.CONFIRM_RETURN_GOODS) {
                goodsOperateLayout.setVisibility(View.GONE);
                returnGoodsLayout.setVisibility(View.VISIBLE);
                selfBuyResultLayout.setVisibility(View.GONE);
                fillReturnGoodsData();
            } else {
                long selfBuyTime = taskProcessVO.moteTask.selfBuyTime;
                long returnItemTime = taskProcessVO.moteTask.returnItemTime;
                boolean isSelfBuy = selfBuyTime > 0;
                boolean isReturnItem = returnItemTime > 0;
                if (isSelfBuy) {
                    fillSlefBuyData();
                }

                if (isReturnItem) {
                    fillReturnGoodsData();
                }
            }
        }
    }

    public void fillSlefBuyData() {
        long time = taskProcessVO.moteTask.selfBuyTime;
        operateTimeView.setText(DateUtil.getDateStr(time));
    }

    public void fillReturnGoodsData() {
        long time = taskProcessVO.moteTask.returnItemTime;
        operateTimeView.setText(DateUtil.getDateStr(time));
        String address = taskProcessVO.seller.address;
        String phoneNumber = taskProcessVO.seller.phoneNumber;
        String shopName = taskProcessVO.seller.shopName;
        addressView.setText(address);
        phoneNumView.setText(phoneNumber);
        shopNameView.setText(shopName);
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
                fillReturnGoodsData();
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
        }

    }
}