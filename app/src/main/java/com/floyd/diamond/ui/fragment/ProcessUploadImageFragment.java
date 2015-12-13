package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.process.ProcessPicVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.ui.ImageLoaderFactory;

import java.util.List;

public class ProcessUploadImageFragment extends Fragment {
    private static final String TASK_PROCESS_VO = "TASK_PROCESS_VO";

    private TaskProcessVO taskProcessVO;
    private ImageLoader mImageLoader;
    private GridLayout uploadPicLayout;

    public static ProcessUploadImageFragment newInstance(TaskProcessVO taskProcessVO) {
        ProcessUploadImageFragment fragment = new ProcessUploadImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(TASK_PROCESS_VO, taskProcessVO);
        fragment.setArguments(args);
        return fragment;
    }

    public ProcessUploadImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskProcessVO = (TaskProcessVO) getArguments().getSerializable(TASK_PROCESS_VO);
        mImageLoader = ImageLoaderFactory.createImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_process_upload_image, container, false);
        uploadPicLayout = (GridLayout) v.findViewById(R.id.upload_pic_layout);
        int status = taskProcessVO.moteTask.status;
        if (status == 2) {
            //填写订单，但是未上传图片．

        } else if (status > 4) {
            //已经上传图片了．不能修改
            List<ProcessPicVO> pics = taskProcessVO.picList;
            if (pics !=null) {
                for (ProcessPicVO vo: pics) {
                    NetworkImageView networkImage = new NetworkImageView(this.getActivity());
                    networkImage.setImageUrl(vo.getPreviewUrl(), mImageLoader);
                    uploadPicLayout.addView(networkImage);
                }
            }
        }
        return v;
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
