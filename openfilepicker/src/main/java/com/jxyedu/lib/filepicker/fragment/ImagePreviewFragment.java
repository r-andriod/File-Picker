package com.jxyedu.lib.filepicker.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.adapter.ImagePreviewPageAdapter;
import com.jxyedu.lib.filepicker.utils.Utils;
import com.jxyedu.lib.filepicker.view.SuperCheckBox;
import com.jxyedu.lib.filepicker.view.ViewPagerViewPhoto;

import static com.jxyedu.lib.filepicker.FPickerConstants.EXTRA_PHOTO_PREVIEW_PATH;

/**
 * 一个对所有图片进行查看的 view
 */
public class ImagePreviewFragment extends BaseFragment {

    //TAG
    public static final String TAG = ImagePreviewFragment.class.getSimpleName();

    public static final String ISORIGIN = "isOrigin";

    private boolean isOrigin;                      //是否选中原图
    private SuperCheckBox mCbCheck;                //是否选中当前图片的CheckBox
    private SuperCheckBox mCbOrigin;               //原图
    private Button mBtnOk;                         //确认图片的选择
    private View bottomBar;
    private View marginView;
    protected View topBar;
    protected ViewPagerViewPhoto mViewPager;
    protected ImagePreviewPageAdapter mAdapter;

    protected int mCurrentPosition = 0;

    public ImagePreviewFragment() {
        // Required empty public constructor
    }
    public static ImagePreviewFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(EXTRA_PHOTO_PREVIEW_PATH,path);
        ImagePreviewFragment imagePreviewFragment = new ImagePreviewFragment();
        imagePreviewFragment.setArguments(args);
        return  imagePreviewFragment;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){

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
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        //isOrigin = getIntent().getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
        //imagePicker.addOnImageSelectedListener(this);
        mBtnOk = view.findViewById(R.id.btn_ok);
        mBtnOk.setVisibility(View.VISIBLE);
        //mBtnOk.setOnClickListener(this);

        bottomBar = view.findViewById(R.id.bottom_bar);
        bottomBar.setVisibility(View.VISIBLE);

        mCbCheck = view.findViewById(R.id.cb_check);
        mCbOrigin = view.findViewById(R.id.cb_origin);
        marginView = view.findViewById(R.id.margin_bottom);
        mCbOrigin.setText(getString(R.string.fp_origin));
        //mCbOrigin.setOnCheckedChangeListener(this);
        mCbOrigin.setChecked(isOrigin);

        //getDataFromImage();

        mViewPager = view.findViewById(R.id.viewpager);
        mAdapter = new ImagePreviewPageAdapter(getActivity(), FPickerManager.INSTANCE.getSelectedPhotos());
        mAdapter.setPhotoViewClickListener(new ImagePreviewPageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                //onImageSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);
    }
}
