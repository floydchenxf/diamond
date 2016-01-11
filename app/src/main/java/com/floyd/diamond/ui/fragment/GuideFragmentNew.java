package com.floyd.diamond.ui.fragment;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.floyd.diamond.R;
import com.floyd.diamond.ui.activity.IndexGuideActivity;
import com.floyd.diamond.ui.activity.OnGuideClick;

public class GuideFragmentNew extends Fragment implements
        GuideFragmentListener, OnClickListener, OnErrorListener {

    private int mPostion;

    public static GuideFragmentNew newInstance(Bundle args) {
        final GuideFragmentNew f = new GuideFragmentNew();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Bundle argsBundle = getArguments();
        mPostion = argsBundle.getInt(IndexGuideActivity.POSITION);
        View view = null;
        if (mPostion == 0) {
            view = inflater.inflate(R.layout.guide_1, container, false);
        } else if (mPostion == 1) {
            view = inflater.inflate(R.layout.guide_2, container, false);
        } else if (mPostion == 2) {
            view = inflater.inflate(R.layout.guide_3, container, false);
        } else if (mPostion == 3) {
            view = inflater.inflate(R.layout.guide_4, container, false);
            view.findViewById(R.id.enter_pager).setOnClickListener(this);
        } else {
            view = inflater.inflate(R.layout.guide_4, container, false);
            view.findViewById(R.id.enter_pager).setOnClickListener(this);
        }

        return view;
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_pager:
                FragmentActivity fragmentActivity = getActivity();
                if (fragmentActivity instanceof OnGuideClick) {
                    OnGuideClick onGuideClick = (OnGuideClick) fragmentActivity;
                    onGuideClick.onGuideClick(mPostion);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mPostion) {
        } else {
        }
    }

    @Override
    public void onPageScroll() {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onPrepare() {

    }

}
