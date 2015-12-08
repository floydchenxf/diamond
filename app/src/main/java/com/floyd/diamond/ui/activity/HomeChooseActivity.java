package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.Model;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.ui.URl;
import com.floyd.diamond.ui.adapter.MasonryAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/11/22.
 */
public class HomeChooseActivity extends Activity {
    private RecyclerView recyclerView;
    private List<Model> productList;
    private TextView back;//返回按钮
    private TextView find;//查找模特
    private boolean isChoosed;//收藏按钮是否被选中

    private int[] imgId = {R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5, R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5, R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5};
    private int[] counts = {1024, 521, 354, 16, 256, 690, 32, 154, 2356, 154, 222, 1024, 521, 354, 555};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();

    }

    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        back = ((TextView) findViewById(R.id.left));
        find = ((TextView) findViewById(R.id.right));

        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击跳转到筛选模特界面
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeChooseActivity.this, ChooseActivity.class));
            }
        });

        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        MasonryAdapter adapter = new MasonryAdapter(imgId, HomeChooseActivity.this, new MasonryAdapter.ChangeText() {
            @Override
            public void setText(String tag, boolean isChecked) {
                CheckBox cb = (CheckBox) recyclerView.findViewWithTag(tag);
                if (cb != null) {
                    if (isChecked) {
                        cb.setText((Integer.parseInt(cb.getText().toString()) + 1) + "");
                        isChoosed=true;
                        if (GlobalParams.isDebug){
                            Log.e("checked-t",isChoosed+"");
                        }
                    } else {
                        cb.setText((Integer.parseInt(cb.getText().toString()) - 1) + "");
                        isChoosed=false;
                        if (GlobalParams.isDebug){
                            Log.e("checked-f",isChoosed+"");
                        }
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(6);
        recyclerView.addItemDecoration(decoration);

        //点击跳转到模特界面
        adapter.setMyOnItemClickListener(new MasonryAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent=new Intent(HomeChooseActivity.this, ModelInfoActivity.class);
                intent.putExtra("isChecked",isChoosed);//需改进
                startActivity(intent);
            }
        });

    }



}
