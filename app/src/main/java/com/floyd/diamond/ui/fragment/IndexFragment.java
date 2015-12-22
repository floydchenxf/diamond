package com.floyd.diamond.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.IndexManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.IndexItemVO;
import com.floyd.diamond.biz.vo.IndexVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.GuideActivity;
import com.floyd.diamond.ui.activity.HomeChooseActivity;
import com.floyd.diamond.ui.activity.MoteTaskTypeActivity;
import com.floyd.diamond.ui.adapter.IndexMoteAdapter;
import com.floyd.diamond.ui.anim.LsLoadingView;
import com.floyd.diamond.ui.pageindicator.CircleLoopPageIndicator;
import com.floyd.diamond.ui.view.LoopViewPager;
import com.floyd.diamond.utils.CommonUtil;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends BackHandledFragment implements AbsListView.OnScrollListener, View.OnClickListener {

    private static final String TAG = "IndexFragment";

    private static int BANNER_HEIGHT_IN_DP = 300;
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
    private List<AdvVO> mTopBannerList;//最上部分左右循环广告条

    private BannerImageAdapter mBannerImageAdapter;

    private boolean isShowFakeNavigationTips;

    private boolean isShowBanner;

    private int mLastShowFakeNavigationItem = 0;

    private boolean isScrollToUp = false; //ListView滚动的方向

    private TextView mActLsFailTv;

    private View mActLsFailLayoutView;

    private FrameLayout mActLsloading;

    private LsLoadingView mLsLoadingView;

    private View mLoading_container;

    private IndexMoteAdapter indexMoteAdapter;

    private Dialog loadDialog;

    private int moteType = 1;
    private int pageNo = 1;
    private int PAGE_SIZE  = 18;

    private boolean needClear;

    private CheckedTextView femaleView1, femaleview2;
    private CheckedTextView maleView1, maleView2;
    private CheckedTextView babyView1, babyView2;

    private ScrollView productTypeLayout;
    private GestureDetector mGestureDetector;

    //品类红点
    private ImageView redHot1;
    private ImageView redHot2;
    private ImageView redHot3;
    private ImageView redHot4;

    private NetworkImageView femaleProduct, maleProduct, babyProduct, multiPriduct;

    private TextView shuaixuan;//筛选模特
    private LinearLayout guide;//操作指引

    private ImageLoader mImageLoader;


    private Handler mChangeViewPagerHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHANGE_BANNER_HANDLER_MSG_WHAT:
                    if (mTopBannerList != null) {
                        if (mTopBannerList != null && mTopBannerList.size() > 0 && mHeaderViewPager != null) {
                            int totalcount = mTopBannerList.size();//autoChangeViewPager.getChildCount();
                            int currentItem = mHeaderViewPager.getCurrentItem();
                            int toItem = currentItem + 1 == totalcount ? 0 : currentItem + 1;
                            mHeaderViewPager.setCurrentItem(toItem, true);
                            //每5秒钟发送一个message，用于切换viewPager中的图片
                            this.sendEmptyMessageDelayed(CHANGE_BANNER_HANDLER_MSG_WHAT, 3000);
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
        mImageLoader = ImageLoaderFactory.createImageLoader();
        mTopBannerList = new ArrayList<AdvVO>();
        loadDialog = new Dialog(this.getActivity(), R.style.data_load_dialog);
        mGestureDetector = new GestureDetector(this.getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i(TAG, "-----onScroll----x:"+distanceX+"--------y:"+distanceY);
                if (Math.abs(distanceX) > 30) {
                    Toast.makeText(IndexFragment.this.getActivity(), "移动了", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i(TAG, "----onFling-----x:"+velocityX+"--------y:"+velocityY);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_index, container, false);


        if (mActLsloading == null) {
            mActLsloading = (FrameLayout) view.findViewById(R.id.act_lsloading);
        }
        //一些错误和空页面
        if (mActLsFailLayoutView == null) {
            mActLsFailLayoutView = view.findViewById(R.id.act_ls_fail_layout);
            mActLsFailLayoutView.setOnClickListener(this);
            mActLsFailLayoutView.setVisibility(View.GONE);
        }

        if (mActLsFailTv == null) {
            mActLsFailTv = (TextView) view.findViewById(R.id.act_ls_fail_tv);
            mActLsFailTv.setText(Html.fromHtml("页面太调皮，跑丢了...<br>请<font color='#1fb4fc'>刷新</font>再试试吧^^"));
        }


        mLsLoadingView = (LsLoadingView) view.findViewById(R.id.ls_loading_image);
        mLoading_container = view.findViewById(R.id.loading_container);
        if (mLoading_container != null) {
            mLoading_container.setVisibility(View.GONE);
        }

        mTitleLayout = view.findViewById(R.id.title);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pic_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setOnScrollListener(this);
        mFakeNavigationContainer = (LinearLayout) view.findViewById(R.id.fake_navigation_container);


        initListViewHeader();

        initButton();

        startLoading();

        loadData();

        init(view);

        mListView.addHeaderView(mHeaderView);

        indexMoteAdapter = new IndexMoteAdapter(this.getActivity(), new ArrayList<MoteInfoVO>());
        mListView.setAdapter(indexMoteAdapter);

        mListView.setOnTouchListener(new View.OnTouchListener() {

            float y1, y2;
            float x1, x2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getRawY();
                        x1 = event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        y2 = event.getRawY();
                        x2 = event.getRawX();
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
                needClear = false;
                loadMoteInfo();
                mPullToRefreshListView.onRefreshComplete(false, true);
            }

            @Override
            public void onPullUpToRefresh() {
                needClear = false;
                loadMoteInfo();
                mPullToRefreshListView.onRefreshComplete(false, true);
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

    public void  init(View view){
        guide= ((LinearLayout) view.findViewById(R.id.guide));
        shuaixuan= ((TextView) view.findViewById(R.id.right));
        //跳转到操作指引界面
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GuideActivity.class));
            }
        });

        //跳转到筛选模特界面
        shuaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeChooseActivity.class));
            }
        });
    }

    private void initButton() {

        femaleView1 = (CheckedTextView) mFakeNavigationContainer.findViewById(R.id.female_colther_new);
        femaleview2 = (CheckedTextView) mNavigationContainer.findViewById(R.id.female_colther);
        maleView1 = (CheckedTextView) mFakeNavigationContainer.findViewById(R.id.male_colther_new);
        maleView2 = (CheckedTextView) mNavigationContainer.findViewById(R.id.male_colther);
        babyView1 = (CheckedTextView) mFakeNavigationContainer.findViewById(R.id.baby_colther_new);
        babyView2 = (CheckedTextView) mNavigationContainer.findViewById(R.id.baby_colther);

        femaleView1.setOnClickListener(this);
        femaleview2.setOnClickListener(this);
        maleView1.setOnClickListener(this);
        maleView2.setOnClickListener(this);
        babyView1.setOnClickListener(this);
        babyView2.setOnClickListener(this);

        femaleView1.setChecked(true);
        femaleview2.setChecked(true);
    }

    public void loadMoteInfo() {
        IndexManager.fetchMoteList(moteType, pageNo, PAGE_SIZE).startUI(new ApiCallback<List<MoteInfoVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                loadFail();
                loadDialog.dismiss();
            }

            @Override
            public void onSuccess(List<MoteInfoVO> moteInfoVOs) {
                loadDialog.dismiss();
                ++pageNo;
                indexMoteAdapter.addAll(moteInfoVOs, needClear);
                loadSuccess();
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }

    private void loadData() {
        IndexManager.getIndexInfoJob().startUI(new ApiCallback<IndexVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                loadFail();
                loadDialog.dismiss();
            }

            @Override
            public void onSuccess(IndexVO indexVO) {
                mViewPagerContainer.setVisibility(View.VISIBLE);
                mTopBannerList.clear();
                List<AdvVO> advVOs = indexVO.advertList;
                mTopBannerList.addAll(advVOs);
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
                    if (needClear) {
                        gotoTop();
                    }
                } else {
                    isShowBanner = false;
                    mHeaderView.findViewById(R.id.pager_layout).setVisibility(View.GONE);

                }

                mNavigationContainer.setVisibility(View.VISIBLE);

                List<IndexItemVO> categoryPics = indexVO.categoryPics;
                if (categoryPics == null||categoryPics.isEmpty()) {
                    productTypeLayout.setVisibility(View.GONE);
                } else {
                    productTypeLayout.setVisibility(View.VISIBLE);
                    for (IndexItemVO itemVO:categoryPics) {
                        int type = itemVO.type;
                        String url = itemVO.picUrl;
                        if (type == 1) {
                            femaleProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int)IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 20);
                                }
                            });
                        } else if (type==2) {
                            maleProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int)IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 10);
                                }
                            });
                        } else if (type == 3) {
                            babyProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int)IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 10);
                                }
                            });
                        } else if (type == 4) {
                            multiPriduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int)IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 10);
                                }
                            });
                        }
                    }
                }


                List<MoteInfoVO> moteInfoVOs = indexVO.moteVOs;
                loadDialog.dismiss();
                ++pageNo;
                indexMoteAdapter.addAll(moteInfoVOs, needClear);
                loadSuccess();
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    private void initListViewHeader() {
        mHeaderView = LayoutInflater.from(this.getActivity()).inflate(R.layout.new_head, mListView, false);
        mViewPagerContainer = mHeaderView.findViewById(R.id.pager_layout);
        mViewPagerContainer.setVisibility(View.GONE);
        ViewGroup.LayoutParams mViewPagerContainerLayoutParams = mViewPagerContainer.getLayoutParams();
        mViewPagerContainerLayoutParams.height = CommonUtil.dip2px(this.getActivity(), BANNER_HEIGHT_IN_DP);
        mHeaderViewPager = (LoopViewPager) mHeaderView.findViewById(R.id.loopViewPager);
        mHeaderViewIndicator = (CircleLoopPageIndicator) mHeaderView.findViewById(R.id.indicator);
        productTypeLayout = (ScrollView) mHeaderView.findViewById(R.id.product_type_layout);
        productTypeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
;                return mGestureDetector.onTouchEvent(event);
            }
        });

        initProductType();


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

    private void initProductType() {

        redHot1 = (ImageView) productTypeLayout.findViewById(R.id.red_hot_1);
        redHot2 = (ImageView) productTypeLayout.findViewById(R.id.red_hot_2);
        redHot3 = (ImageView) productTypeLayout.findViewById(R.id.red_hot_3);
        redHot4 = (ImageView) productTypeLayout.findViewById(R.id.red_hot_4);
        femaleProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.female_type);
        maleProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.male_type);
        babyProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.baby_type);
        multiPriduct = (NetworkImageView) productTypeLayout.findViewById(R.id.multi_type);

        femaleProduct.setOnClickListener(this);
        maleProduct.setOnClickListener(this);
        babyProduct.setOnClickListener(this);
        multiPriduct.setOnClickListener(this);
    }

    /**
     * 数据加载成功
     */
    private void loadSuccess() {
        mActLsloading.setVisibility(View.GONE);
        mActLsFailLayoutView.setVisibility(View.GONE);
        stopLoading();
    }

    /**
     * 数据加载失败
     */
    private void loadFail() {
        mActLsloading.setVisibility(View.VISIBLE);
        mActLsFailLayoutView.setVisibility(View.VISIBLE);
        stopLoading();
    }

    /**
     * 开始显示加载动画
     */
    private void startLoading() {
        if (mActLsloading != null) {
            mActLsloading.setVisibility(View.VISIBLE);
            mLoading_container.setVisibility(View.VISIBLE);
            mLsLoadingView.startAnimation();
            mActLsFailLayoutView.setVisibility(View.GONE);
        }
    }

    /**
     * 结束显示加载动画
     */
    private void stopLoading() {
        if (mActLsloading != null) {
            mLoading_container.setVisibility(View.GONE);
            mLsLoadingView.stopAnimation();
        }
    }


    public void stopBannerAutoLoop() {
        if (mChangeViewPagerHandler != null) {
            mChangeViewPagerHandler.removeCallbacksAndMessages(null);
        }
    }

    public void startBannerAutoLoop() {
        if (mChangeViewPagerHandler != null && !mChangeViewPagerHandler.hasMessages(CHANGE_BANNER_HANDLER_MSG_WHAT)) {
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

        if (mNavigationContainer != null && mTitleLayout != null && mFakeNavigationContainer != null) {

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

//            if ((firstVisibleItem + visibleItemCount) == totalItemCount) { //滑到底
//                mListView.setSelection(totalItemCount - 1);
//            }

            if (isShowFakeNavigationTips) {
                mFakeNavigationContainer.setVisibility(View.VISIBLE);
            } else {
                mFakeNavigationContainer.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.female_colther:
            case R.id.female_colther_new:
                loadDialog.show();
                femaleView1.setChecked(true);
                femaleview2.setChecked(true);
                maleView2.setChecked(false);
                maleView1.setChecked(false);
                babyView1.setChecked(false);
                babyView2.setChecked(false);
                moteType = 1;
                pageNo = 1;
                needClear = true;
                loadMoteInfo();
                break;

            case R.id.male_colther:
            case R.id.male_colther_new:
                loadDialog.show();
                femaleView1.setChecked(false);
                femaleview2.setChecked(false);
                maleView2.setChecked(true);
                maleView1.setChecked(true);
                babyView1.setChecked(false);
                babyView2.setChecked(false);
                moteType = 2;
                pageNo = 1;
                needClear = true;
                loadMoteInfo();
                break;

            case R.id.baby_colther:
            case R.id.baby_colther_new:
                loadDialog.show();
                femaleView1.setChecked(false);
                femaleview2.setChecked(false);
                maleView2.setChecked(false);
                maleView1.setChecked(false);
                babyView1.setChecked(true);
                babyView2.setChecked(true);
                moteType = 3;
                pageNo = 1;
                needClear = true;
                loadMoteInfo();
                break;
            case R.id.act_ls_fail_layout:
                moteType = 1;
                pageNo = 1;
                needClear = true;
                loadData();
                break;
            case R.id.female_type:
                Intent it = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 1);
                startActivity(it);
                break;
            case R.id.male_type:
                Intent it2 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it2.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 2);
                startActivity(it2);

                break;
            case R.id.baby_type:
                Intent it3 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it3.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 3);
                startActivity(it3);
                break;
            case R.id.multi_type:
                Intent it4 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it4.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 4);
                startActivity(it4);
                break;
        }

    }


    public class BannerImageAdapter extends FragmentPagerAdapter {

        List<AdvVO> dataLists = new ArrayList<AdvVO>();

        public BannerImageAdapter(FragmentManager fm, List<AdvVO> dataList) {
            super(fm);
            if (dataList != null && !dataList.isEmpty()) {
                this.dataLists.addAll(dataList);
            }
        }

        public void addItems(List<AdvVO> dataList) {
            this.dataLists.clear();
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
                AdvVO dataList = dataLists.get(position);
                if (dataList != null) {
                    long id = dataList.id;
                    return id;
                }
            }
            return (long) position;
        }
    }

    /**
     *跳转到ListView的最上方
     */
    public void gotoTop(){
        if(mListView!=null){
            mListView.setSelection(0);
        }
    }
}
