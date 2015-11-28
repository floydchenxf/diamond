package com.floyd.diamond.ui.adapter;

import java.util.ArrayList;

import com.floyd.diamond.ui.fragment.IndexFragment;
import com.floyd.diamond.ui.fragment.IndexFragmentPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter_WelcomeFragment extends FragmentPagerAdapter {

	private ArrayList<IndexFragmentPager> list;

	public Adapter_WelcomeFragment(FragmentManager fragmentManager,
			ArrayList<IndexFragmentPager> list) {
		super(fragmentManager);
		this.list = list;

	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
