package com.floyd.diamond.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.ui.activity.Setting_personInfoActivity;


/**
 * Created by Administrator on 2015/11/25.
 */
public class MyFragment extends Fragment {
    private Context context;
    private TextView set, volley, pictrue, task, care;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        //初始化操作
        init(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public void init(View view) {
        set = ((TextView) view.findViewById(R.id.set));//设置
        volley = ((TextView) view.findViewById(R.id.volley));//我的钱包
        pictrue = ((TextView) view.findViewById(R.id.pictrue));//我的图库
        task = ((TextView) view.findViewById(R.id.task));//我的任务
        care= ((TextView) view.findViewById(R.id.care));//我的关注

        //点击跳转到设置详情界面
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Setting_personInfoActivity.class));
            }
        });

        //点击跳转到我的钱包界面
        volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击跳转到我的图库界面
        pictrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击跳转到我的任务界面
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击跳转到我的关注界面
        care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
