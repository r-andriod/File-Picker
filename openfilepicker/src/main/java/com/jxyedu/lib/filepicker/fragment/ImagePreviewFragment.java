package com.jxyedu.lib.filepicker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxyedu.lib.filepicker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePreviewFragment extends BaseFragment {

    //TAG
    public static final String TAG = ImagePreviewFragment.class.getSimpleName();

    public ImagePreviewFragment() {
        // Required empty public constructor
    }

    /**
     * 保存 fragment data 到 Bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("data", mPhotoDirectories);
    }

    /**
     * 获取保存在 fragment 中的 Bundle 值
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mPhotoDirectories = savedInstanceState.getString("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false);
    }

}
