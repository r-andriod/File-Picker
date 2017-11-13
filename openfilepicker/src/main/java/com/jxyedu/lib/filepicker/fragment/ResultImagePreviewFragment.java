package com.jxyedu.lib.filepicker.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxyedu.lib.filepicker.FPickerConstants;
import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.adapter.ImagePreviewPageAdapter;
import com.jxyedu.lib.filepicker.utils.Utils;
import com.jxyedu.lib.filepicker.view.SystemBarTintManager;
import com.jxyedu.lib.filepicker.view.ViewPagerViewPhoto;

import java.util.ArrayList;

import static com.jxyedu.lib.filepicker.FPickerConstants.PHOTO_PREVIEW_PATH_KEY;

/**
 * 预览已经选择的图片，并可以删除
 * A simple {@link Fragment} subclass.
 */
public class ResultImagePreviewFragment extends BaseFragment implements View.OnClickListener {


    protected View topBar;
    protected ViewPagerViewPhoto mViewPager;
    protected ImagePreviewPageAdapter mAdapter;
    protected int mCurrentPosition = 0;
    protected SystemBarTintManager tintManager;
    protected TextView mTitleCount;
    protected ArrayList<String> paths;

    public static ResultImagePreviewFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(PHOTO_PREVIEW_PATH_KEY, path);
        ResultImagePreviewFragment resultImagePreviewFragment = new ResultImagePreviewFragment();
        resultImagePreviewFragment.setArguments(args);
        return resultImagePreviewFragment;
    }

    /**
     * 保存 fragment data 到 Bundle
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("data", mPhotoDirectories);
    }

    /**
     * 获取保存在 fragment 中的 Bundle 值
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mPhotoDirectories = savedInstanceState.getString("data");
    }

    public ResultImagePreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        paths = FPickerManager.INSTANCE.getSelectedPhotos();
        tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.fp_color_primary_dark);  //设置上方状态栏的颜色


        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
        topBar = view.findViewById(R.id.top_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
            params.topMargin = Utils.getStatusHeight(getContext());
            topBar.setLayoutParams(params);
        }
        topBar.findViewById(R.id.btn_ok).setVisibility(View.GONE);
        topBar.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTitleCount = view.findViewById(R.id.tv_des);
        mTitleCount.setText(getString(R.string.fp_preview_image_count, mCurrentPosition + 1, paths.size()));

        ImageView mBtnDel = view.findViewById(R.id.btn_del);
        mBtnDel.setOnClickListener(this);
        mBtnDel.setVisibility(View.VISIBLE);
        topBar.findViewById(R.id.btn_back).setOnClickListener(this);


        mViewPager = view.findViewById(R.id.viewpager);
        mAdapter = new ImagePreviewPageAdapter(getActivity(), FPickerManager.INSTANCE.getSelectedPhotos());
        mAdapter.setPhotoViewClickListener(new ImagePreviewPageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);


        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTitleCount.setText(getString(R.string.fp_preview_image_count, mCurrentPosition + 1, FPickerManager.INSTANCE.getCurrentCount()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_del) {
            showDeleteDialog();
        } else if (id == R.id.btn_back) {
            popBackStack();
        }
    }


    /**
     * 单击时，隐藏头和尾
     */
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.top_out));
            topBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.top_in));
            topBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(R.color.fp_color_primary_dark);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 是否删除此张图片
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage("要删除这张照片吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //移除当前图片刷新界面
                Log.d("del", "path size:" + paths.size() + "path:" + paths.get(mCurrentPosition) + "| mCurrentPosition:" + mCurrentPosition);
                FPickerManager.INSTANCE.remove(paths.get(mCurrentPosition), FPickerConstants.FILE_TYPE_MEDIA);
                //paths.remove(mCurrentPosition);
                Log.d("del", "path del size:" + paths.size());
                if (paths.size() > 0) {
                    mAdapter.setData(paths);
                    mAdapter.notifyDataSetChanged();
                    mTitleCount.setText(getString(R.string.fp_preview_image_count, mCurrentPosition + 1, paths.size()));
                } else {
                    popBackStack();
                }
            }
        });
        builder.show();
    }

    private void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


}
