package com.jxyedu.lib.filepicker.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 修复图片在ViewPager控件中缩放报错
 * ViewPagerViewPhoto
 * Created by renwoxing on 2017/11/13.
 */
public class ViewPagerViewPhoto extends ViewPager {

    public ViewPagerViewPhoto(Context context) {
        super(context);
    }

    public ViewPagerViewPhoto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
