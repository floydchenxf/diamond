package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.process.ProcessPicVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.multiimage.MultiPickGalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessUploadImageFragment extends Fragment implements View.OnClickListener {
    private static final String TASK_PROCESS_VO = "TASK_PROCESS_VO";
    public static final int MULIT_PIC_CHOOSE_WITH_DATA = 10;

    private TaskProcessVO taskProcessVO;
    private ImageLoader mImageLoader;
    private GridLayout uploadPicLayout;
    private TextView deletePicButton;
    private TextView confirmPicButton;
    private TextView uploadPicTimeView;

    private List<ProcessPicVO> picList = new ArrayList<ProcessPicVO>();

    private FinishCallback callback;

    public static ProcessUploadImageFragment newInstance(TaskProcessVO taskProcessVO, FinishCallback callback) {
        ProcessUploadImageFragment fragment = new ProcessUploadImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(TASK_PROCESS_VO, taskProcessVO);
        fragment.setArguments(args);
        fragment.callback = callback;
        return fragment;
    }

    public ProcessUploadImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskProcessVO = (TaskProcessVO) getArguments().getSerializable(TASK_PROCESS_VO);
        if (taskProcessVO != null && taskProcessVO.picList != null) {
            this.picList.addAll(taskProcessVO.picList);
        }
        mImageLoader = ImageLoaderFactory.createImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_process_upload_image, container, false);
        uploadPicLayout = (GridLayout) v.findViewById(R.id.upload_pic_layout);
        deletePicButton = (TextView) v.findViewById(R.id.delete_pic_button);
        confirmPicButton = (TextView) v.findViewById(R.id.confirm_pic_button);
        uploadPicTimeView = (TextView) v.findViewById(R.id.upload_pic_time);
        deletePicButton.setOnClickListener(this);
        confirmPicButton.setOnClickListener(this);

        int status = taskProcessVO.moteTask.status;
        if (this.picList.isEmpty()) {
            deletePicButton.setVisibility(View.GONE);
            confirmPicButton.setVisibility(View.GONE);
            if (status == 2) {
                //填写订单，但是未上传图片．
                String dateStr = DateUtil.getDateStr(System.currentTimeMillis());
                uploadPicTimeView.setText(dateStr);
                drawPicLayout(this.picList, false, true);
            } else if (status > 4) {
                //FIXME 已经上传图片了．不能修改
                long time = this.taskProcessVO.moteTask.uploadPicTime;
                uploadPicTimeView.setText(DateUtil.getDateStr(time));
                drawPicLayout(this.picList, false, true);
            }
        } else {
            long time = this.taskProcessVO.moteTask.uploadPicTime;
            uploadPicTimeView.setText(DateUtil.getDateStr(time));
            if (status > 4) {
                //已经上传图片了．不能修改
                drawPicLayout(this.picList, false, false);
            } else if (status == 4){
                drawPicLayout(this.picList, false, true);
                deletePicButton.setVisibility(View.VISIBLE);
                confirmPicButton.setVisibility(View.GONE);
            }
        }


        return v;
    }

    private void drawPicLayout(List<ProcessPicVO> pics, boolean showEdit, boolean canAdd) {
        uploadPicLayout.removeAllViews();
        int width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        float ondp = this.getActivity().getResources().getDimension(R.dimen.one_dp);
        final float eachWidth = (width - 32 * ondp) / 3;
        if (pics != null) {
            for (ProcessPicVO vo : pics) {
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(0, (int) (4 * ondp), (int) (4 * ondp), 0);
                lp.width = (int)eachWidth;
                lp.height = (int)eachWidth;
                View picItemView = View.inflate(this.getActivity(), R.layout.process_upload_pic_item, null);
                picItemView.setLayoutParams(lp);

                NetworkImageView networkImage = (NetworkImageView)picItemView.findViewById(R.id.task_pic_item);
                networkImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                networkImage.setImageUrl(vo.getPreviewUrl(), mImageLoader, new BitmapProcessor() {
                    @Override
                    public Bitmap processBitmpa(Bitmap bitmap) {
                        return ImageUtils.getRoundBitmap(bitmap, (int) eachWidth, 20);
                    }
                });

                View markLayout = picItemView.findViewById(R.id.mark_layout);
                ImageView deleteButton = (ImageView)picItemView.findViewById(R.id.delete_button);
                if (showEdit) {
                    markLayout.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setTag(vo);
                    deleteButton.setOnClickListener(this);
                } else {
                    markLayout.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    deleteButton.setTag(vo);
                    deleteButton.setOnClickListener(null);
                }

                uploadPicLayout.addView(picItemView);
            }
        }

        if (canAdd) {
            View addView = View.inflate(this.getActivity(), R.layout.process_add_pic, null);
            addView.setOnClickListener(this);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(0, (int) (4 * ondp), (int) (4 * ondp), 0);
            lp.width = (int)eachWidth;
            lp.height = (int)eachWidth;
            addView.setLayoutParams(lp);
            uploadPicLayout.addView(addView);
        }
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
            case R.id.delete_button:
                final ProcessPicVO vo = (ProcessPicVO)v.getTag();
                LoginVO loginVO = LoginManager.getLoginInfo(this.getActivity());
                MoteManager.reomveImageUrl(Arrays.asList(new Long[]{vo.id}), loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(ProcessUploadImageFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        ProcessUploadImageFragment.this.picList.remove(vo);
                        drawPicLayout(ProcessUploadImageFragment.this.picList, true, true);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.delete_pic_button:
                drawPicLayout(this.picList, true, true);
                deletePicButton.setVisibility(View.GONE);
                confirmPicButton.setVisibility(View.VISIBLE);
                break;
            case R.id.confirm_pic_button:
                drawPicLayout(this.picList, false, true);
                deletePicButton.setVisibility(View.VISIBLE);
                confirmPicButton.setVisibility(View.GONE);
                if (this.callback != null) {
                    this.callback.doFinish();
                }
                break;
            case R.id.add_pic_layout:
                Intent picIntent = new Intent(this.getActivity(), MultiPickGalleryActivity.class);
                picIntent.putExtra(MultiPickGalleryActivity.MAX_COUNT, 6);
                picIntent.putExtra(MultiPickGalleryActivity.MAX_TOAST, "最多选择6张图片");
                this.startActivityForResult(picIntent, MULIT_PIC_CHOOSE_WITH_DATA);
                break;
            //TODO
//            if (this.callback != null) {
//                this.callback.finishUpload("finish");
//            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MULIT_PIC_CHOOSE_WITH_DATA && resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pics = data.getStringArrayListExtra(MultiPickGalleryActivity.RESULT_LIST);
            if (pics == null||pics.isEmpty()) {
                return;
            }

            List<File> files = new ArrayList<File>();
            for (String pic:pics) {
                files.add(new File(pic));
            }

            LoginVO vo = LoginManager.getLoginInfo(this.getActivity());
            MoteManager.uploadPics(taskProcessVO.moteTask.id, files, vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    for(String pic:pics) {
//                        ProcessPicVO processPicVO = new ProcessPicVO();
//                        processPicVO.id = 111;
//                        processPicVO.
                    }


                }

                @Override
                public void onProgress(int progress) {

                }
            });

        }

    }
}
