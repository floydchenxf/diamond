package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.Care;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.bean.SwipeRefreshLayout;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.adapter.CareAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/19.
 */
public class CareActivity extends Activity {
    private RecyclerView recyclerView;
    private LinearLayout back;//返回按钮
    private LinearLayout find;//查找模特
    private RequestQueue queue;
    private List<Care.DataEntity.DataListEntity> modelsList;
    private List<Care.DataEntity.DataListEntity> allModel;//大集合
    private int pageNo = 1;//当前页数
    private TextView care;
    private PullToRefreshListView mPullToRefreshListView;
    private TextView edit;
    private boolean needClear;
    private ProgressBar progressBar;
    private LoginVO loginVO;
    private String editOrdelete = "编辑";
    private boolean isFirst=true;
    private ArrayList<String>deleteModel;//取消关注的模特
    private com.floyd.diamond.bean.SwipeRefreshLayout swipeRefreshLayout;
    private CareAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressBar.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();

            if (editOrdelete.equals("编辑")) {
                //点击跳转到模特界面
                adapter.setMyOnItemClickListener(new CareAdapter.MyOnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CareActivity.this, MoteDetailActivity.class);
                        intent.putExtra("moteId", allModel.get(position).getMoteId());
                        if (GlobalParams.isDebug) {
                            Log.e("TAG_moteId", allModel.get(position).getMoteId() + "");
                        }
                        startActivity(intent);
                    }
                });

            } else if (editOrdelete.equals("删除")) {
                //点击要删除的图片
                adapter.setMyOnItemClickListener(new CareAdapter.MyOnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {

                        TextView bg_recycle=(TextView)recyclerView.findViewWithTag("bg" + postion);

                        bg_recycle.setLayoutParams(new RelativeLayout.LayoutParams(view.getWidth(),view.getHeight()));

                        // Toast.makeText(CareActivity.this, "要删除了哦~~", Toast.LENGTH_SHORT).show();

                        deleteModel.add(allModel.get(postion).getMoteId()+"");

                    }
                });
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homechoose_layout);

        init();

        setData();

        //设置adapter
        adapter = new CareAdapter(allModel, CareActivity.this,deleteModel);
        recyclerView.setAdapter(adapter);

    }

    public void init() {
        loginVO = LoginManager.getLoginInfo(this);
        progressBar= ((ProgressBar) findViewById(R.id.progress));
        care = ((TextView) findViewById(R.id.center));
        care.setText("我的关注");
        care.setTextColor(Color.WHITE);
        care.setTextSize(16);
        edit = ((TextView) findViewById(R.id.edit));
        edit.setBackgroundColor(Color.parseColor("#d4377e"));
        edit.setText("编辑");
        edit.setTextColor(Color.WHITE);
        edit.setTextSize(10);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        back = ((LinearLayout) findViewById(R.id.left));
        find = ((LinearLayout) findViewById(R.id.right));
        queue = Volley.newRequestQueue(CareActivity.this);
        modelsList = new ArrayList<>();//用于存放每一页的模特
        allModel = new ArrayList<>();//用于存储所有的模特
        deleteModel=new ArrayList<>();//取消关注的模特
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip);
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
                editOrdelete = (String) edit.getText();
                if (editOrdelete.equals("编辑")) {
                    edit.setText("删除");
                    editOrdelete = "删除";

                    handler.sendEmptyMessage(2);
                } else if (editOrdelete.equals("删除")) {
                    if (deleteModel.size()==0){
                        Toast.makeText(CareActivity.this,"~~请选择取消关注的模特~~",Toast.LENGTH_SHORT).show();
                    }else{
                        if(GlobalParams.isDebug){
                            Log.e("deleteModel",deleteModel.toString());
                        }

                            Intent intent=new Intent(CareActivity.this, CareDialogActivity.class);
                            intent.putStringArrayListExtra("deleteList", deleteModel);
                            startActivity(intent);

                        deleteModel.clear();

                        edit.setText("编辑");
                        editOrdelete = "编辑";

                        handler.sendEmptyMessage(3);

                    }

                }
            }
        });

        //设置layoutManager
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(CareActivity.this,2));


        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(6);
        recyclerView.addItemDecoration(decoration);

        //如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

//        //设置无动画显示
//        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void setData() {

        MoteManager.fetchMyCareInfos(loginVO.token, pageNo, 10).startUI(new ApiCallback<Care.DataEntity>() {
            @Override
            public void onError(int code, String errorInfo) {
                Toast.makeText(CareActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Care.DataEntity dataEntity) {
                modelsList = dataEntity.getDataList();
                allModel.addAll(modelsList);
                if (allModel.size() == 0) {
                    Toast.makeText(CareActivity.this, "~你还没有关注模特，赶快去关注吧~", Toast.LENGTH_LONG).show();
                }

                handler.sendEmptyMessage(1);
            }

            @Override
            public void onProgress(int progress) {

            }
        });

//            String url = APIConstants.HOST + APIConstants.MYCARE;
//            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    if (GlobalParams.isDebug) {
//                        Log.e("TAG_care", response);
//                    }
//                    Gson gson = new Gson();
//                    Care care = gson.fromJson(response, Care.class);
//                    modelsList = care.getData().getDataList();
//
//                    allModel.addAll(modelsList);
//
//                    if (allModel.size()==0){
//                        Toast.makeText(CareActivity.this,"~你还没有关注模特，赶快去关注吧~",Toast.LENGTH_LONG).show();
//                    }
//
//                    handler.sendEmptyMessage(1);
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(CareActivity.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    //在这里设置需要post的参数
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("pageNo", pageNo + "");
//                    params.put("pageSize", 10 + "");
//                    params.put("token", loginVO.token);
//                    return params;
//                }
//            };
//            queue.add(request);

    }


}
