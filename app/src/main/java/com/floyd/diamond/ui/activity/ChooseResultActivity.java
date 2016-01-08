package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.ChoiceCondition;
import com.floyd.diamond.bean.ChooseCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.Model;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.ui.adapter.MasonryAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/18.
 */
public class ChooseResultActivity extends Activity {
    private RecyclerView recyclerView;
    private LinearLayout back;//返回按钮
    private LinearLayout find;//查找模特
    private RequestQueue queue;
    private List<Model.DataEntity> modelsList;
    private List<Model.DataEntity>allModel;//大集合
    private int pageNo=1;//当前页数
    private PullToRefreshListView mPullToRefreshListView;
    private boolean needClear;
    private GridLayoutManager mLayoutManager;
    private MasonryAdapter adapter;
    private com.floyd.diamond.bean.SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

               adapter.notifyDataSetChanged();
            }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();

        setData();

        //设置adapter
        adapter = new MasonryAdapter(allModel, ChooseResultActivity.this, new MasonryAdapter.ChangeText() {
            @Override
            public void setText(String tag, boolean isChecked) {
                CheckBox cb = (CheckBox) recyclerView.findViewWithTag(tag);
                if (cb != null) {
                    if (isChecked) {
                        cb.setText((Integer.parseInt(cb.getText().toString()) + 1) + "");
                    } else {
                        cb.setText((Integer.parseInt(cb.getText().toString()) - 1) + "");
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);

        //点击跳转到模特界面
        adapter.setMyOnItemClickListener(new MasonryAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(ChooseResultActivity.this, MoteDetailActivity.class);
                intent.putExtra("moteId", allModel.get(postion).getId());
                if (GlobalParams.isDebug) {
                    Log.e("TAG_moteId", allModel.get(postion).getId() + "");
                }
                startActivity(intent);
            }
        });

    }

    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        back = ((LinearLayout) findViewById(R.id.left));
        find = ((LinearLayout) findViewById(R.id.right));
        queue= Volley.newRequestQueue(ChooseResultActivity.this);
        modelsList=new ArrayList<>();//用于存放每一页的模特
        allModel=new ArrayList<>();//用于存储所有的模特
        swipeRefreshLayout = ((com.floyd.diamond.bean.SwipeRefreshLayout) findViewById(R.id.swip));
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setBottomColor(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setTopColor(android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new com.floyd.diamond.bean.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                allModel.clear();
                setData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setOnLoadListener(new com.floyd.diamond.bean.SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNo++;
                setData();
                swipeRefreshLayout.setLoading(false);
            }
        });
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
                startActivity(new Intent(ChooseResultActivity.this, ChooseActivity1.class));
            }
        });

        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(6);
        recyclerView.addItemDecoration(decoration);

        //如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

//        //设置无动画显示
//        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }

    public void setData(){

        final ChoiceCondition.DataEntity dataEntity= (ChoiceCondition.DataEntity) getIntent().getSerializableExtra("chooseCondition");

        if (GlobalParams.isDebug){
            Log.e("TAG_shapes",dataEntity.getShapes().toString().substring(1,dataEntity.getShapes().toString().length()-1).replace(" ",""));
            Log.e("TAG_areaids",dataEntity.getAreaids().toString().substring(1,dataEntity.getAreaids().toString().length()-1).replace(" ",""));
        }

        String url= APIConstants.HOST+APIConstants.CHOOSEMOTE;
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (GlobalParams.isDebug){
                    Log.e("TAG",response);
                }
                Gson gson=new Gson();
                Model model=gson.fromJson(response,Model.class);
                modelsList=model.getData();

                allModel.addAll(modelsList);

                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChooseResultActivity.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("gender",dataEntity.getGender()+"");
                params.put("ageMin",dataEntity.getAgeMin()+"");
                params.put("ageMax",dataEntity.getAgeMax()+"");
                params.put("heightMin",dataEntity.getHeightMin()+"");
                params.put("heightMax",dataEntity.getHeightMax()+"");
                params.put("creditMin",dataEntity.getCreditMin()+"");
                params.put("creditMax",dataEntity.getCreditMax()+"");
                params.put("shapes",dataEntity.getShapes().toString().substring(1,dataEntity.getShapes().toString().length()-1).replace(" ","")+"");
                params.put("areaids",dataEntity.getAreaids().toString().substring(1,dataEntity.getAreaids().toString().length()-1).replace(" ","")+"");
                params.put("pageNo",pageNo+"");
                params.put("pageSize",10+"");
                return params;
            }
        };
        queue.add(request);

    }

}
