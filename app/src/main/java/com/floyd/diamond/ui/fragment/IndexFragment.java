package com.floyd.diamond.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.JobFactory;
import com.floyd.diamond.ui.adapter.SimpleAdapter;
import com.floyd.diamond.ui.pageindicator.CircleLoopPageIndicator;
import com.floyd.diamond.ui.pojo.banner.DataList;
import com.floyd.diamond.ui.view.LoopViewPager;
import com.floyd.diamond.utils.CommonUtil;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends BackHandledFragment implements AbsListView.OnScrollListener {

    private static  int BANNER_HEIGHT_IN_DP = 150;
    public static final int CHANGE_BANNER_HANDLER_MSG_WHAT = 51;

    private PullToRefreshListView mPullToRefreshListView;
    private View mTitleLayout;
    private ListView mListView;

    private View mHeaderView;
    private View mViewPagerContainer;//整个广告
    private LoopViewPager mHeaderViewPager;//广告
    private CircleLoopPageIndicator mHeaderViewIndicator;//广告条索引
    private LinearLayout mFakeNavigationContainer; //固定顶端的导航栏
    private LinearLayout mNavigationContainer;//导航栏
    private List<DataList> mTopBannerList;//最上部分左右循环广告条

    private BannerImageAdapter mBannerImageAdapter;

    private boolean isShowFakeNavigationTips;

    private boolean isShowBanner;

    private int mLastShowFakeNavigationItem = 0;

    private boolean isScrollToUp = false; //ListView滚动的方向

    private Handler mChangeViewPagerHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHANGE_BANNER_HANDLER_MSG_WHAT:
                    if(mTopBannerList !=null) {
                        if (mTopBannerList != null && mTopBannerList.size() > 0 && mHeaderViewPager != null) {
                            int totalcount = mTopBannerList.size();//autoChangeViewPager.getChildCount();
                            int currentItem = mHeaderViewPager.getCurrentItem();
                            int toItem = currentItem + 1 == totalcount ? 0 : currentItem + 1;
                            mHeaderViewPager.setCurrentItem(toItem, true);
                            //每5秒钟发送一个message，用于切换viewPager中的图片
                            this.sendEmptyMessageDelayed(CHANGE_BANNER_HANDLER_MSG_WHAT, 5000);
                        }
                    }
            }
        }
    };


    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    public IndexFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBannerList = new ArrayList<DataList>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_index, container, false);
        mTitleLayout = view.findViewById(R.id.title);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pic_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setOnScrollListener(this);
        mFakeNavigationContainer = (LinearLayout)view.findViewById(R.id.fake_navigation_container);
        initListViewHeader();
        mListView.addHeaderView(mHeaderView);
        JobFactory.createJob("http://img.taopic.com/uploads/allimg/130501/240451-13050106450911.jpg").startUI(new ApiCallback<String>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(String s) {
                DataList dataList = new DataList();
                dataList.setContentUrl(s);
                DataList dataList2 = new DataList();
                dataList2.setContentUrl(s);
                mTopBannerList.add(dataList);
                mTopBannerList.add(dataList2);
                mBannerImageAdapter.addItems(mTopBannerList);

                if (mTopBannerList != null && mTopBannerList.size() > 0) {
                    isShowBanner = true;
                    int count = mBannerImageAdapter.getCount();
                    mHeaderViewIndicator.setTotal(count);
                    mHeaderViewIndicator.setIndex(0);
                    mHeaderViewPager.setAdapter(mBannerImageAdapter);
                    mBannerImageAdapter.notifyDataSetChanged();
                    if (mTopBannerList.size() == 1) {
                        mHeaderViewIndicator.setVisibility(View.GONE);
                    } else {
                        mHeaderViewIndicator.setVisibility(View.VISIBLE);
                        stopBannerAutoLoop();
                        startBannerAutoLoop();
                    }
//                    gotoTop();
                } else {
                    isShowBanner = false;
                    mHeaderView.findViewById(R.id.pager_layout).setVisibility(View.GONE);

                }

                mNavigationContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(int progress) {

            }
        });

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this.getActivity(), Arrays.asList(new String[]{"wuliao", "nihao"}));
        mListView.setAdapter(simpleAdapter);

        mListView.setOnTouchListener(new View.OnTouchListener() {

            float y1, y2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        y2 = event.getRawY();
                        if (y2 - y1 > 30) {
                            isScrollToUp = true;
                        } else if (y2 - y1 < -60) {
                            isScrollToUp = false;
                        }

                        break;
                }
                return false;
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                simpleAdapter.add("1111");
                mPullToRefreshListView.onRefreshComplete(true, true);
            }

            @Override
            public void onPullUpToRefresh() {
                simpleAdapter.add("2222");
                mPullToRefreshListView.onRefreshComplete(true, true);
            }
        });

        mPullToRefreshListView.setOnTouchListener(new View.OnTouchListener() {

            float y1=0, y2=0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if( y1 == 0){
                            y1 = event.getRawY();
                        }
                        y2 = event.getRawY();

                        if (mPullToRefreshListView.isBeingDragged()) {
                            if (mFakeNavigationContainer != null && mNavigationContainer != null) {
                                mFakeNavigationContainer.setVisibility(View.GONE);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        return view;
    }

    private void initListViewHeader() {
        mHeaderView = LayoutInflater.from(this.getActivity()).inflate(R.layout.new_head, mListView, false);
        mViewPagerContainer = mHeaderView.findViewById(R.id.pager_layout);
        ViewGroup.LayoutParams mViewPagerContainerLayoutParams = mViewPagerContainer.getLayoutParams();
        mViewPagerContainerLayoutParams.height = CommonUtil.dip2px(this.getActivity(), BANNER_HEIGHT_IN_DP);
        mHeaderViewPager = (LoopViewPager) mHeaderView.findViewById(R.id.loopViewPager);
        mHeaderViewIndicator = (CircleLoopPageIndicator) mHeaderView.findViewById(R.id.indicator);
        mNavigationContainer = (LinearLayout) mHeaderView.findViewById(R.id.navigation_container);
        mBannerImageAdapter = new BannerImageAdapter(this.getActivity().getSupportFragmentManager(), null);
        mHeaderViewPager.setAdapter(mBannerImageAdapter);
        mHeaderViewPager.setOnPageChangeListener(new LoopViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                mHeaderViewIndicator.setIndex(index);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mHeaderViewPager.setDispatchTouchEventListener(new LoopViewPager.DispatchTouchEventListener() {
            @Override
            public void dispatchTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    stopBannerAutoLoop();

                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_OUTSIDE
                        || event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerAutoLoop();
                }
            }
        });


    }

    public void stopBannerAutoLoop(){
        if(mChangeViewPagerHandler!=null){
            mChangeViewPagerHandler.removeCallbacksAndMessages(null);
        }
    }
    public void startBannerAutoLoop(){
        if(mChangeViewPagerHandler!=null && !mChangeViewPagerHandler.hasMessages(CHANGE_BANNER_HANDLER_MSG_WHAT)){
            mChangeViewPagerHandler.sendEmptyMessageDelayed(CHANGE_BANNER_HANDLER_MSG_WHAT, 5000);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isShowBanner && firstVisibleItem >= mListView.getHeaderViewsCount()) {
            stopBannerAutoLoop();
        } else {
            startBannerAutoLoop();
        }

        if(mNavigationContainer!=null && mTitleLayout!=null && mFakeNavigationContainer!=null){

            int[] navigationLocation = new int[2];
            mNavigationContainer.getLocationOnScreen(navigationLocation);
            int[] titleLocation = new int[2];
            mTitleLayout.getLocationOnScreen(titleLocation);

            if ((navigationLocation[1] <= (titleLocation[1] + mTitleLayout.getHeight())) || (firstVisibleItem >= mListView.getHeaderViewsCount())) {
                //快速滑动的时候第一个判断条件会失效,
                isShowFakeNavigationTips = true;
            } else {
                isShowFakeNavigationTips = false;
            }

            if((firstVisibleItem+visibleItemCount )== totalItemCount) { //滑到底
                mListView.setSelection(totalItemCount - 1);
            }

            if (isShowFakeNavigationTips) {
                mFakeNavigationContainer.setVisibility(View.VISIBLE);
            } else {
                mFakeNavigationContainer.setVisibility(View.GONE);
            }
        }

    }


    public class BannerImageAdapter extends FragmentPagerAdapter {

        List<DataList> dataLists = new ArrayList<DataList>();

        public BannerImageAdapter(FragmentManager fm, List<DataList> dataList) {
            super(fm);
            if (dataList!=null && !dataList.isEmpty()) {
                this.dataLists.addAll(dataList);
            }
        }

        public void addItems(List<DataList> dataList) {
            this.dataLists.addAll(dataList);
            this.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("test", "BannerImageAdapter getItem");
            Bundle args = new Bundle();
            args.putParcelable(BannerFragment.Banner, dataLists.get(position));
            args.putInt(BannerFragment.Position, position);
            args.putInt(BannerFragment.Height, BANNER_HEIGHT_IN_DP);
            return BannerFragment.newInstance(args);
        }
        @Override
        public int getCount() {
            if (dataLists != null) {
                return dataLists.size();
            }
            return 0;
        }

        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        public long getItemId(int position) {
            if (position >= 0 && position < dataLists.size()) {
                DataList dataList = dataLists.get(position);
                if (dataList != null) {
                    String id=dataList.getId();
                    if(!TextUtils.isEmpty(id)){
                        return Long.parseLong(id);
                    }
                }
            }
            return (long)position;
        }
    }
}
