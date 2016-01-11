package com.floyd.diamond.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.ui.fragment.GuideFragmentListener;
import com.floyd.diamond.ui.fragment.GuideFragmentNew;
import com.floyd.diamond.ui.pageindicator.CirclePageIndicator;
import com.floyd.diamond.ui.pageindicator.CustomViewPager;

/**
 * 新手指导
 * 
 * @author wb-jiangxiang
 * 
 *         modify by kevin
 */
public class IndexGuideActivity extends FragmentActivity implements
		CustomViewPager.OnPageChangeListener, OnGuideClick {
	private static final String TAG = GuideActivity.class.getSimpleName();

	public static final String POSITION = "position";
	public static final String EXTRA_BUNDLE ="extra_bundle";
	
	private CustomViewPager mViewPager;
	private GuideAdapter mAdapter;
	private CirclePageIndicator mCirclePageIndicator;

	public static final int PAGE_SIZE = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		init();
		Log.d("IndexGuideActivity", "oncreate IndexGuideActivity");
	}
	/**
	 * 
	 */
	private void init() {
		mViewPager = (CustomViewPager) findViewById(R.id.container);
		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		
		mAdapter = new GuideAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(PAGE_SIZE);
		mAdapter.notifyDataSetChanged();

		mCirclePageIndicator.setViewPager(mViewPager);
		mCirclePageIndicator.setOnPageChangeListener(this);

	}

	private final class GuideAdapter extends FragmentStatePagerAdapter {

		public GuideAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			final Bundle bundle = new Bundle();
			bundle.putInt(POSITION, position);
			Fragment fragment = GuideFragmentNew.newInstance(bundle);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return PAGE_SIZE;
		}
	}

	/**
	 * 启动主页面
	 */
	private void enterMainPage() {
		Intent intent = getIntent();
		if (intent == null) {
			intent = new Intent();
		}
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		LoginManager.saveGuided(this, true);
		this.finish();
	}

	@Override
	public void onBackPressed() {
//		enterMainPage();
		return;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("GuideActivity", "onDestroy");
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.d("test", "onPageScrollStateChanged");

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.d("test", "onPageScrolled");

		Fragment fragment = findFragmentByPosition(0);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageScroll();
		}		
		fragment = findFragmentByPosition(1);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageScroll();
		}		
		fragment = findFragmentByPosition(2);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageScroll();
		}
		fragment = findFragmentByPosition(3);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageScroll();
		}
		fragment = findFragmentByPosition(4);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageScroll();
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		Fragment fragment = findFragmentByPosition(0);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageSelected(arg0);
		}		
		fragment = findFragmentByPosition(1);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageSelected(arg0);
		}		
		fragment = findFragmentByPosition(2);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageSelected(arg0);
		}	
		fragment = findFragmentByPosition(3);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageSelected(arg0);
		}	
		fragment = findFragmentByPosition(4);
		if (fragment != null
				&& fragment instanceof GuideFragmentListener) {
			((GuideFragmentListener) fragment).onPageSelected(arg0);
		}	
	}

	public Fragment findFragmentByPosition(int position) {
		Object fragmentObject = mAdapter.instantiateItem(mViewPager, position);
		if (fragmentObject instanceof Fragment) {
			return (Fragment) fragmentObject;
		} else {
			return null;
		}

	}



	@Override
	public void onGuideClick(int position) {
		if (position == PAGE_SIZE - 1) {
			enterMainPage();
		}	
	}
	
	public void onMediaError(){
		Log.d("test", "onMediaError");
		enterMainPage();
	}
	
}
