package com.floyd.diamond.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
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
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.IndexItemVO;
import com.floyd.diamond.biz.vo.IndexVO;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteInfoVO;
import com.floyd.diamond.biz.vo.mote.UnReadMsgVO;
import com.floyd.diamond.event.LoginEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.GuideActivity;
import com.floyd.diamond.ui.activity.HomeChooseActivity;
import com.floyd.diamond.ui.activity.MoteTaskTypeActivity;
import com.floyd.diamond.ui.adapter.IndexMoteAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.pageindicator.CircleLoopPageIndicator;
import com.floyd.diamond.ui.view.LoopViewPager;
import com.floyd.diamond.utils.CommonUtil;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends BackHandledFragment implements AbsListView.OnScrollListener, View.OnClickListener {

    private static final String TAG = "IndexFragment";
    public static final int MIN_JULI = 600;
    public static final int MIN_VELOCITY_X = 130;

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

    private DataLoadingView dataLoadingView;

    private IndexMoteAdapter indexMoteAdapter;

    private Dialog loadDialog;

    private int moteType = 1;
    private int pageNo = 1;
    private int PAGE_SIZE = 18;

    private boolean needClear;

    private CheckedTextView femaleView1, femaleview2;
    private CheckedTextView maleView1, maleView2;
    private CheckedTextView babyView1, babyView2;

    private ScrollView productTypeLayout;
    private GestureDetector mGestureDetector;

    private GestureDetector listViewGestureDetector;

    //品类红点
    private ImageView redHot1;
    private ImageView redHot2;
    private ImageView redHot3;
    private ImageView redHot4;

    private TextView womenView;
    private TextView menView;
    private TextView babyView;
    private TextView otherView;

    private View redHot1Layout;
    private View redHot2Layout;
    private View redHot3Layout;
    private View redHot4Layout;

    Map<Integer, Boolean> showRedHotMap = new HashMap<Integer, Boolean>();

    private NetworkImageView femaleProduct, maleProduct, babyProduct, multiPriduct;

    private TextView shuaixuan;//筛选模特
    private LinearLayout guide;//操作指引

    private ImageLoader mImageLoader;

    private float keydownX1;


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
                            //每3秒钟发送一个message，用于切换viewPager中的图片
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
        showRedHotMap.put(1, Boolean.FALSE);
        showRedHotMap.put(2, Boolean.FALSE);
        showRedHotMap.put(3, Boolean.FALSE);
        showRedHotMap.put(4, Boolean.FALSE);

        EventBus.getDefault().register(this);
        mTopBannerList = new ArrayList<AdvVO>();
        loadDialog = DialogCreator.createDataLoadingDialog(this.getActivity());
        listViewGestureDetector = new GestureDetector(this.getActivity(), new GestureDetector.OnGestureListener() {
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
                Log.i(TAG, "onScroll------------vx:" + distanceX + "----vy:" + distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x1 = keydownX1;
                Log.i(TAG, "-------x1:" + x1 + "-------x2:" + e2.getX() + "---vx:" + velocityX);
                if (x1 - e2.getX() > MIN_JULI && Math.abs(velocityX) > MIN_VELOCITY_X) {
                    pageNo = 1;
                    needClear = true;
                    int k = (++moteType - 1) % 3 + 1;
                    checkMoteType(k);
                    loadMoteInfo(true);
                } else if (e2.getX() - x1 > MIN_JULI && Math.abs(velocityX) > MIN_VELOCITY_X) {

                    int k = 1;
                    if (moteType > 1) {
                        k = moteType - 1;
                    } else {
                        k = 4-moteType;
                    }

                    pageNo = 1;
                    needClear = true;
                    checkMoteType(k);
                    loadMoteInfo(true);
                }
                return false;
            }
        });
    }

    private void checkMoteType(int moteType) {
        if (moteType == 1) {
            femaleView1.setChecked(true);
            femaleview2.setChecked(true);
            maleView2.setChecked(false);
            maleView1.setChecked(false);
            babyView1.setChecked(false);
            babyView2.setChecked(false);
            this.moteType = moteType;
            this.pageNo = 1;
        } else if (moteType == 2) {
            femaleView1.setChecked(false);
            femaleview2.setChecked(false);
            maleView2.setChecked(true);
            maleView1.setChecked(true);
            babyView1.setChecked(false);
            babyView2.setChecked(false);
            this.moteType = moteType;
            this.pageNo = 1;
        } else if (moteType == 3) {
            femaleView1.setChecked(false);
            femaleview2.setChecked(false);
            maleView2.setChecked(false);
            maleView1.setChecked(false);
            babyView1.setChecked(true);
            babyView2.setChecked(true);
            this.moteType = moteType;
            this.pageNo = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_index, container, false);

        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(view, this);

        mTitleLayout = view.findViewById(R.id.title);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pic_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setOnScrollListener(this);
        mFakeNavigationContainer = (LinearLayout) view.findViewById(R.id.fake_navigation_container);


        initListViewHeader();

        initButton();

        loadData(true);

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
                        Log.i(TAG, "===========x1:" + event.getX());
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

                listViewGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                needClear = false;
                loadMoteInfo(false);
                mPullToRefreshListView.onRefreshComplete(false, true);
            }

            @Override
            public void onPullUpToRefresh() {
                needClear = false;
                loadMoteInfo(false);
                mPullToRefreshListView.onRefreshComplete(false, true);
            }
        });

        mPullToRefreshListView.setOnTouchListener(new View.OnTouchListener() {

            float y1 = 0, y2 = 0;
            float x1 = 0, x2 = 0;

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

        mPullToRefreshListView.setKeydownCallback(new PullToRefreshListView.KeydownCallback() {
            @Override
            public void keydown(MotionEvent event) {
                keydownX1 = event.getX();
            }
        });
        return view;
    }

    public void init(View view) {
        guide = ((LinearLayout) view.findViewById(R.id.guide));
        shuaixuan = ((TextView) view.findViewById(R.id.right));
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

    public void loadMoteInfo(final boolean needDialog) {
        if (needDialog) {
            loadDialog.show();
        }
        IndexManager.fetchMoteList(moteType, pageNo, PAGE_SIZE).startUI(new ApiCallback<List<MoteInfoVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (needDialog) {
                    loadDialog.dismiss();
                }
                Toast.makeText(IndexFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<MoteInfoVO> moteInfoVOs) {
                if (needDialog) {
                    loadDialog.dismiss();
                }
                ++pageNo;
                indexMoteAdapter.addAll(moteInfoVOs, needClear);
                dataLoadingView.loadSuccess();
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }

    private void loadData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
            loadUnReadMsgs(true);
        } else {
            loadDialog.show();
            loadUnReadMsgs(false);
        }
        IndexManager.getIndexInfoJob().startUI(new ApiCallback<IndexVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (isFirst) {
                    dataLoadingView.loadFail();
                } else {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(IndexVO indexVO) {
                if (isFirst) {
                    dataLoadingView.loadSuccess();
                } else {
                    loadDialog.dismiss();
                }
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
                if (categoryPics == null || categoryPics.isEmpty()) {
                    productTypeLayout.setVisibility(View.GONE);
                } else {
                    productTypeLayout.setVisibility(View.VISIBLE);
                    for (IndexItemVO itemVO : categoryPics) {
                        int type = itemVO.type;
                        String url = itemVO.picUrl;
                        if (type == 1) {
                            femaleProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int) IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 40);
                                }
                            });
                        } else if (type == 2) {
                            maleProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int) IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 40);
                                }
                            });
                        } else if (type == 3) {
                            babyProduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int) IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 40);
                                }
                            });
                        } else if (type == 4) {
                            multiPriduct.setImageUrl(url, mImageLoader, new BitmapProcessor() {
                                @Override
                                public Bitmap processBitmpa(Bitmap bitmap) {
                                    return ImageUtils.getRoundBitmap(bitmap, (int) IndexFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size), 40);
                                }
                            });
                        }
                    }
                }


                List<MoteInfoVO> moteInfoVOs = indexVO.moteVOs;
                ++pageNo;
                indexMoteAdapter.addAll(moteInfoVOs, needClear);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    public void loadUnReadMsgs(boolean atOnce) {
        if (!atOnce) {
            long saveTime = LoginManager.getMsgReadTimes(this.getActivity());
            if ((System.currentTimeMillis() - saveTime) < 30*1000*60 ) {
                return;
            }
        }

        MoteManager.fetchUnReadMsgs().startUI(new ApiCallback<UnReadMsgVO>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(UnReadMsgVO unReadMsgVO) {
                showRedHotMap.put(1, unReadMsgVO.women > 0);
                showRedHotMap.put(2, unReadMsgVO.men > 0);
                showRedHotMap.put(3, unReadMsgVO.boy > 0);
                showRedHotMap.put(4, unReadMsgVO.other > 0);

                if (showRedHotMap.get(1)) {
                    String womenNum = unReadMsgVO.women > 99?"99+":unReadMsgVO.women+"";
                    womenView.setText(womenNum);
                    redHot1Layout.setVisibility(View.VISIBLE);
                } else {
                    redHot1Layout.setVisibility(View.GONE);
                }

                if (showRedHotMap.get(2)) {
                    String menNum = unReadMsgVO.men > 99?"99+":unReadMsgVO.men+"";
                    menView.setText(menNum);
                    redHot2Layout.setVisibility(View.VISIBLE);
                } else {
                    redHot2Layout.setVisibility(View.GONE);
                }

                if (showRedHotMap.get(3)) {
                    String boyNum = unReadMsgVO.boy > 99?"99+":unReadMsgVO.boy+"";
                    babyView.setText(boyNum);
                    redHot3Layout.setVisibility(View.VISIBLE);
                } else {
                    redHot3Layout.setVisibility(View.GONE);
                }

                if (showRedHotMap.get(4)) {
                    String otherNum = unReadMsgVO.other > 99?"99+":unReadMsgVO.other+"";
                    otherView.setText(otherNum);
                    redHot4Layout.setVisibility(View.VISIBLE);
                } else {
                    redHot4Layout.setVisibility(View.GONE);
                }

                LoginManager.setMsgReadTimes(IndexFragment.this.getActivity(), System.currentTimeMillis());
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

        womenView = (TextView) productTypeLayout.findViewById(R.id.women_num_view);
        menView = (TextView) productTypeLayout.findViewById(R.id.men_num_view);
        babyView = (TextView) productTypeLayout.findViewById(R.id.baby_num_view);
        otherView = (TextView) productTypeLayout.findViewById(R.id.other_num_view);

        redHot1Layout = productTypeLayout.findViewById(R.id.redHot1_layout);
        redHot2Layout = productTypeLayout.findViewById(R.id.redHot2_layout);
        redHot3Layout = productTypeLayout.findViewById(R.id.redHot3_layout);
        redHot4Layout = productTypeLayout.findViewById(R.id.redHot4_layout);

        femaleProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.female_type);
        maleProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.male_type);
        babyProduct = (NetworkImageView) productTypeLayout.findViewById(R.id.baby_type);
        multiPriduct = (NetworkImageView) productTypeLayout.findViewById(R.id.multi_type);

        femaleProduct.setOnClickListener(this);
        maleProduct.setOnClickListener(this);
        babyProduct.setOnClickListener(this);
        multiPriduct.setOnClickListener(this);
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

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        if (!IndexFragment.this.getActivity().isFinishing()) {
            Log.i(TAG, "unReadMsg when login");
            loadUnReadMsgs(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                femaleView1.setChecked(true);
                femaleview2.setChecked(true);
                maleView2.setChecked(false);
                maleView1.setChecked(false);
                babyView1.setChecked(false);
                babyView2.setChecked(false);
                moteType = 1;
                pageNo = 1;
                needClear = true;
                loadMoteInfo(true);
                break;

            case R.id.male_colther:
            case R.id.male_colther_new:
                femaleView1.setChecked(false);
                femaleview2.setChecked(false);
                maleView2.setChecked(true);
                maleView1.setChecked(true);
                babyView1.setChecked(false);
                babyView2.setChecked(false);
                moteType = 2;
                pageNo = 1;
                needClear = true;
                loadMoteInfo(true);
                break;

            case R.id.baby_colther:
            case R.id.baby_colther_new:
                femaleView1.setChecked(false);
                femaleview2.setChecked(false);
                maleView2.setChecked(false);
                maleView1.setChecked(false);
                babyView1.setChecked(true);
                babyView2.setChecked(true);
                moteType = 3;
                pageNo = 1;
                needClear = true;
                loadMoteInfo(true);
                break;
            case R.id.act_ls_fail_layout:
                moteType = 1;
                pageNo = 1;
                needClear = true;
                loadData(true);
                break;
            case R.id.female_type:
                processRedHot(1, redHot1Layout);
                Intent it = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 1);
                startActivity(it);
                break;
            case R.id.male_type:
                processRedHot(2, redHot2Layout);
                Intent it2 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it2.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 2);
                startActivity(it2);
                break;
            case R.id.baby_type:
                processRedHot(3, redHot3Layout);
                Intent it3 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it3.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 3);
                startActivity(it3);
                break;
            case R.id.multi_type:
                processRedHot(4, redHot4Layout);
                Intent it4 = new Intent(this.getActivity(), MoteTaskTypeActivity.class);
                it4.putExtra(MoteTaskTypeActivity.PRODUCT_TYPE_KEY, 4);
                startActivity(it4);
                break;
        }

    }

    private void processRedHot(final int type, final View v) {

        Boolean show = showRedHotMap.get(type);
        if (!show) {
            return;
        }
        LoginVO loginVO = LoginManager.getLoginInfo(this.getActivity());
        if (loginVO != null) {
            MoteManager.hasReadMsg(loginVO.token, type).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (!IndexFragment.this.getActivity().isFinishing()) {
                        v.setVisibility(View.GONE);
                        showRedHotMap.put(type, Boolean.FALSE);
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
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
     * 跳转到ListView的最上方
     */
    public void gotoTop() {
        if (mListView != null) {
            mListView.setSelection(0);
        }
    }
}
