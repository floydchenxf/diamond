package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.ChoiceCondition;
import com.floyd.diamond.bean.DLCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModelInfo;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.adapter.MasonryAdapter;
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
    private List<ModelInfo.DataEntity> modelsList;
    private List<ModelInfo.DataEntity>allModel;//大集合
    private int pageNo=1;//当前页数
    private PullToRefreshListView mPullToRefreshListView;
    private boolean needClear;
    private GridLayoutManager mLayoutManager;
    private MasonryAdapter adapter;
    private LoginVO vo;
    private ProgressBar progressBar;
    private com.floyd.diamond.bean.SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            progressBar.setVisibility(View.INVISIBLE);

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
            public void setText(String tag, boolean isChecked,final int position) {
               final CheckBox cb = (CheckBox) recyclerView.findViewWithTag(tag);
                if (cb != null) {
                    if (isChecked) {

                        MoteManager.addFollow(allModel.get(position).getId(), vo.token).startUI(new ApiCallback<Integer>() {
                            @Override
                            public void onError(int code, String errorInfo) {
                                Toast.makeText(ChooseResultActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Integer aBoolean) {
                                Toast.makeText(ChooseResultActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                cb.setText((Integer.parseInt(cb.getText().toString()) + 1) + "");
                                allModel.get(position).setIsFollow(true);

                            }

                            @Override
                            public void onProgress(int progress) {

                            }
                        });

                    } else {
                        MoteManager.cancelOneFollow(allModel.get(position).getId(), vo.token).startUI(new ApiCallback<Boolean>() {
                            @Override
                            public void onError(int code, String errorInfo) {
                                Toast.makeText(ChooseResultActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Boolean num) {
                                Toast.makeText(ChooseResultActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                cb.setText((Integer.parseInt(cb.getText().toString()) - 1) + "");
                                allModel.get(position).setIsFollow(false);
                            }

                            @Override
                            public void onProgress(int progress) {

                            }
                        });

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
        progressBar= ((ProgressBar) findViewById(R.id.progress));
        vo = LoginManager.getLoginInfo(ChooseResultActivity.this);
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
                finish();
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

        final DLCondition.DataEntity dataEntity= (DLCondition.DataEntity) getIntent().getSerializableExtra("chooseCondition");

        String url= APIConstants.HOST+APIConstants.CHOOSEMOTE;
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (GlobalParams.isDebug){
                    Log.e("TAG_result",response);
                }
                Gson gson=new Gson();
                ModelInfo model=gson.fromJson(response,ModelInfo.class);
                modelsList=model.getData();

                allModel.addAll(modelsList);

                if (allModel.size()==0){
//                    startActivity(new Intent(ChooseResultActivity.this, ChooseResultNullActivity.class));
//                    finish();
                    setContentView(R.layout.activity_chooseresultnull);
                }else{
                    handler.sendEmptyMessage(1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChooseResultActivity.this, "请检查网络连接...", Toast.LENGTH_LONG).show();
                finish();
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
                params.put("shapes",dataEntity.getShapes()+"");
                params.put("areaids",dataEntity.getAreaids()+"");
                params.put("pageNo",pageNo+"");
                params.put("pageSize",10+"");
                if (vo!=null){
                    params.put("token",vo.token);
                }
                return params;
            }
        };
        queue.add(request);

    }

}
