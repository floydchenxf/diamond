package com.floyd.diamond.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.JobFactory;
import com.floyd.diamond.bean.SpacesItemDecoration;
import com.floyd.diamond.ui.activity.GuideActivity;
import com.floyd.diamond.ui.activity.HomeChooseActivity;
import com.floyd.diamond.ui.adapter.Adapter_WelcomeFragment;
import com.floyd.diamond.ui.adapter.HomeSecondAdapter;
import com.floyd.diamond.ui.adapter.SimpleAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PullToRefreshListView pullToRefreshListView;
    private TextView textView;

    private ViewPager pager;//首页的广告条
    private ImageView tips1, tips2, tips3, tips4, tips5;
    private TextView choose;//筛选模特
    private TextView guide;//操作指引
    private int count=0;
    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};

    private List<String> mListItems = new LinkedList<String>();

    private int[]imgId={R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4};
    private String[]counts={"1","3","2","6"};

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    //private int[]imgId={R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4};

    private Context context;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pager.setCurrentItem(count,false);
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IndexFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_index, container, false);

//        textView = (TextView) view.findViewById(R.id.aaaa);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.setBackgroundColor(Color.WHITE);
//            }
//        });
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pic_list);

        //初始化
        init(view);

        //给首页的Recycle填充数据
        setRecycleData();

        //给广告条填充数据
        addPagerData();

        mListItems.addAll(Arrays.asList(mStrings));
        final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), mListItems);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JobFactory.createJob("add after....").startUI(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {

                    }

                    @Override
                    public void onSuccess(String o) {
                        adapter.add(o);
                        pullToRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

            }
        });

        pullToRefreshListView.setAdapter(adapter);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                count = pager.getCurrentItem();
                count++;
                if (count == 5) {
                    count = 0;
                }
                handler.sendEmptyMessage(2);

            }
        }, 2000, 2000);
        return view;
    }

    //初始化操作
    public void init(View view) {
        pager = ((ViewPager) view.findViewById(R.id.index_pager));
        recyclerView= ((RecyclerView) view.findViewById(R.id.recycler_home));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        tips1 = (ImageView) view.findViewById(R.id.img_home_pager_tips1);
        tips2 = (ImageView) view.findViewById(R.id.img_home_pager_tips2);
        tips3 = (ImageView) view.findViewById(R.id.img_home_pager_tips3);
        tips4 = (ImageView) view.findViewById(R.id.img_home_pager_tips4);
        tips5 = (ImageView) view.findViewById(R.id.img_home_pager_tips5);
        choose= ((TextView) view.findViewById(R.id.right));
        guide= ((TextView) view.findViewById(R.id.guide));
        //点击进入筛选模特界面
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeChooseActivity.class);
                startActivity(intent);
            }
        });

        //点击进入操作指引界面
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GuideActivity.class));
            }
        });
    }
    //给首页的Recycle填充数据
    public void setRecycleData(){
        HomeSecondAdapter adapter=new HomeSecondAdapter(context,imgId,counts);
        recyclerView.setAdapter(adapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);

    }

    //给首页的广告条添加数据
    public void addPagerData(){
        ArrayList<IndexFragmentPager> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            IndexFragmentPager fragment = new IndexFragmentPager();
            Bundle bundle = new Bundle();
            bundle.putInt("imageid", (i + 1));
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        Adapter_WelcomeFragment adapter = new Adapter_WelcomeFragment(
                getFragmentManager(), list);
        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                count = arg0;
                tips1.setImageResource(R.color.tips1);
                tips2.setImageResource(R.color.tips1);
                tips3.setImageResource(R.color.tips1);
                tips4.setImageResource(R.color.tips1);
                tips5.setImageResource(R.color.tips1);
                switch (count % 5) {
                    case 0:
                        tips1.setImageResource(R.color.tips2);
                        break;
                    case 1:
                        tips2.setImageResource(R.color.tips2);
                        break;
                    case 2:
                        tips3.setImageResource(R.color.tips2);
                        break;
                    case 3:
                        tips4.setImageResource(R.color.tips2);
                        break;
                    case 4:
                        tips5.setImageResource(R.color.tips2);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
