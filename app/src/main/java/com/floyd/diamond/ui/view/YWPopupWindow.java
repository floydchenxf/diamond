package com.floyd.diamond.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.PopupWindow;

import com.floyd.diamond.R;

import java.lang.reflect.Field;

public class YWPopupWindow {

    private PopupWindow menuBg;
    private PopupWindow menu;
    private Activity context;
    private View v;
    private boolean isShow;
    private int screanHeight;

    public YWPopupWindow(Activity context) {
        this.context = context;
    }

    public void initView(final View v, int layout, int height, ViewInit viewInit) {
        this.v = v;
        screanHeight = context.getWindowManager().getDefaultDisplay().getHeight();
        if (menuBg == null) {
            View view = View.inflate(context, R.layout.common_popup_bg, null);
            menuBg = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    screanHeight - dip2px(context, 50) - getStatusBarHeight(context)); //50dp是v的height
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    hidePopUpWindow();
                }
            });
        }

        if (menu == null) {
            View view = View.inflate(v.getContext(), layout, null);
            if (viewInit != null) {
                viewInit.initView(view);
            }
            int h = (int) v.getContext().getResources().getDimension(height);
            menu = new PopupWindow(view, LayoutParams.MATCH_PARENT, h);
        }
    }

    public void showPopUpWindow() {
        if (menu == null) {
            throw new RuntimeException("please init view first!");
        }

        menuBg.setAnimationStyle(R.style.common_popup_bg_animation);
        menuBg.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0, v.getHeight());
        menu.setAnimationStyle(R.style.messageactivity_menu_animation);
        menu.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0, v.getHeight());
        isShow = true;
    }

    public boolean hidePopUpWindow() {
        if (!context.isFinishing()) {
            if (menu != null) {
                menu.dismiss();
            }
            if (menuBg != null) {
                menuBg.dismiss();
            }
            isShow = false;
            return true;
        }
        return false;
    }

    public boolean isShowing() {
        return isShow;
    }

    public interface ViewInit {
        void initView(View v);
    }

    public int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
