package com.floyd.diamond.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.ui.view.YWPopupWindow;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyFragment extends Fragment {

    private YWPopupWindow ywPopupWindow;

    private TextView editHeadButton;
    private TextView editProfileButton;
    private TextView cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);

        ywPopupWindow = new YWPopupWindow(this.getActivity());
        ywPopupWindow.initView(view, R.layout.popup_edit_head, R.dimen.edit_head_bar_heigh, new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                editHeadButton = (TextView) v.findViewById(R.id.edit_head);
                editProfileButton = (TextView) v.findViewById(R.id.edit_profile);
                cancelButton = (TextView) v.findViewById(R.id.cancel_button);
            }
        });
        return view;
    }
}
