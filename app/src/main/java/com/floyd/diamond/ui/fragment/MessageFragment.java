package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.Message;
import com.floyd.diamond.ui.URl;
import com.floyd.diamond.ui.activity.MessageItemActivity;
import com.floyd.diamond.ui.adapter.MessageAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RequestQueue mQueue;
    private ListView mListView;
    private int currentpage=1;

    private PullToRefreshListView mPullToRefreshListView;
    private boolean needClear;
    private MessageAdapter adapter;


    private List<Message.DataEntity>messageList;//通告项目列表
    private List<Message.DataEntity>allMessage; //所有的通告
   // private int[]imgId={R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5};
    private Handler handler=new Handler(){
       @Override
       public void handleMessage(android.os.Message msg) {
           super.handleMessage(msg);

           adapter=new MessageAdapter(getActivity(),allMessage,R.layout.messagelistviewitem_layout);
           ListView listView = mPullToRefreshListView.getRefreshableView();
           listView.setAdapter(adapter);//适配器适配

          // swipeRefreshLayout.setRefreshing(false);

           //点击跳入通告详细信息界面
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent=new Intent(getActivity(), MessageItemActivity.class);
                   intent.putExtra("imgUrl",allMessage.get(position).getImgUrl());
                   startActivity(intent);
               }
           });

       }
   };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mQueue = Volley.newRequestQueue(getActivity());
        messageList=new ArrayList<>();
        allMessage=new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        //listView = ((ListView) view.findViewById(R.id.listview));
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
       // mListView = mPullToRefreshListView.getRefreshableView();
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//        //设置刷新时动画的颜色，可以设置4个
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//        //设置监听，刷新界面
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                setData();
//                //数据重新获取之后隐藏进度条
//                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(),"刷新完成",Toast.LENGTH_SHORT).show();
//
//            }
//        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                needClear = false;
                currentpage=1;
                setData();
                mPullToRefreshListView.onRefreshComplete(true, true);
                allMessage.clear();
                //messageList.clear();
                //handler.sendEmptyMessage(1);
            }

            @Override
            public void onPullUpToRefresh() {
                needClear = false;
                mPullToRefreshListView.onRefreshComplete(true, true);
                currentpage++;
                setData();
                //adapter.notifyDataSetChanged();
               // handler.sendEmptyMessage(1);
            }
        });

        mPullToRefreshListView.setOnTouchListener(new View.OnTouchListener() {

            float y1 = 0, y2 = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (y1 == 0) {
                            y1 = event.getRawY();
                        }
                        y2 = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        setData();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //获取数据
    public void setData() {
        String messageUrl= URl.BASEURL+ URl.TGLIST;
        StringRequest request = new StringRequest(Request.Method.POST,messageUrl, new Response.Listener<String>() {

            //数据请求成功的回调
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug){
                    Log.e("TAG",response);
                }

                Gson gson=new Gson();
                Message message=gson.fromJson(response,Message.class);

                messageList=message.getData();

                allMessage.addAll(messageList);

                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {

            //数据请求错误的回调
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "请检查网络连接..." + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("pageNo",currentpage+"");
                params.put("pageSize","10");
                return params;
            }
        };

        mQueue.add(request);
    }
}
