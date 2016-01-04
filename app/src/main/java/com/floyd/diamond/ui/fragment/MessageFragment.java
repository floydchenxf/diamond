package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.manager.IndexManager;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.MessageItemActivity;
import com.floyd.diamond.ui.adapter.MessageAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {
    private ListView mListView;
//    private int currentpage = 1;

    //    private PullToRefreshListView mPullToRefreshListView;
    private boolean needClear = true;
    private MessageAdapter adapter;
    private List<AdvVO> advList;

    private ImageLoader mImageLoader;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = ImageLoaderFactory.createImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(view, this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this.getActivity());
//        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageItemActivity.class);
                if (GlobalParams.isDebug){
                    Log.e("TAG_idd",advList.get(position).id+"");
                }
                intent.putExtra("id",advList.get(position).id);
                startActivity(intent);
            }
        });
//        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
//        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
//            @Override
//            public void onPullDownToRefresh() {
//                needClear = false;
//                currentpage = 1;
//                setData(false);
//                mPullToRefreshListView.onRefreshComplete(true, true);
//            }
//
//            @Override
//            public void onPullUpToRefresh() {
//                needClear = false;
//                mPullToRefreshListView.onRefreshComplete(true, true);
//                currentpage++;
//                setData(false);
//            }
//        });

        adapter = new MessageAdapter(this.getActivity(), mImageLoader);
        mListView.setAdapter(adapter);
        setData(true);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //获取数据
    public void setData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        IndexManager.fetchAdvLists(200, 2).startUI(new ApiCallback<List<AdvVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (isFirst) {
                    dataLoadingView.loadFail();
                } else {
                    dataLoadingDialog.dismiss();
                }
                Toast.makeText(MessageFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<AdvVO> advVOs) {

                if (isFirst) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDialog.dismiss();
                }
                advList = advVOs;
                adapter.addAll(advVOs, needClear);
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_ls_fail_layout:
                setData(true);
                break;
        }
    }
}
