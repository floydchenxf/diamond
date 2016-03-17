package com.floyd.diamond.bean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.floyd.diamond.R;

/**
 * Created by hy on 2016/2/16.
 */
public class GoTopScrollView extends ScrollView implements View.OnClickListener
{
    private ImageView goTopBtn;

    private int screenHeight;

    public GoTopScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setScrollListener(ImageView goTopBtn)
    {
        this.goTopBtn = goTopBtn;
        this.goTopBtn.setOnClickListener(this);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 滑动距离超过500px,出现向上按钮,可以做为自定义属性
         */
        if (t >= 500)
        {
            goTopBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            goTopBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.jiantou_up)
        {
            this.smoothScrollTo(0, 0);
        }
    }
}
