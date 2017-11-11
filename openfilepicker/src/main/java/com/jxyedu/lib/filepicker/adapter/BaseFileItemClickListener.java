package com.jxyedu.lib.filepicker.adapter;

import android.view.View;

/**
 * BaseFileItemClickListener
 * Created by renwoxing on 2017/11/10.
 */
public interface BaseFileItemClickListener<T> {

    /**
     * 选项选中回调
     */
    void onItemSelected(View view, T image, boolean isSelected);
    /**
     * 选项点击回调方法
     */
    void onItemClick(View view, T image, int position);
}
