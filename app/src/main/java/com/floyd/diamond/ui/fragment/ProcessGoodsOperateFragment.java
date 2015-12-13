package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProcessGoodsOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessGoodsOperateFragment extends Fragment {

    private static final String TASK_PROCESS_VO = "TASK_PROCESS_VO";

    private TaskProcessVO taskProcessVO;

    public static ProcessGoodsOperateFragment newInstance(TaskProcessVO taskProcessVO) {
        ProcessGoodsOperateFragment fragment = new ProcessGoodsOperateFragment();
        Bundle args = new Bundle();
        args.putSerializable(TASK_PROCESS_VO, taskProcessVO);
        fragment.setArguments(args);
        return fragment;
    }

    public ProcessGoodsOperateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskProcessVO = (TaskProcessVO) getArguments().getSerializable(TASK_PROCESS_VO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_process_goods_operate, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}