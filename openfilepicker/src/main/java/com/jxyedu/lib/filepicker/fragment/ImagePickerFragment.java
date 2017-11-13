package com.jxyedu.lib.filepicker.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.adapter.BaseFileItemClickListener;
import com.jxyedu.lib.filepicker.adapter.ImageDirectoryAdapter;
import com.jxyedu.lib.filepicker.adapter.ImageRecyclerAdapter;
import com.jxyedu.lib.filepicker.data.FileDataRepository;
import com.jxyedu.lib.filepicker.data.FileResultCallback;
import com.jxyedu.lib.filepicker.model.Photo;
import com.jxyedu.lib.filepicker.model.PhotoDirectory;
import com.jxyedu.lib.filepicker.utils.Utils;
import com.jxyedu.lib.filepicker.view.DirectoryPopUpWindow;
import com.jxyedu.lib.filepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择
 */
public class ImagePickerFragment extends BaseFragment implements BaseFileItemClickListener<Photo>, View.OnClickListener {

    public static final String TAG = ImagePickerFragment.class.getSimpleName();

    private Button mBtnOk;       //确定按钮
    private TextView mBtnPre;      //预览按钮
    private View mFooterBar;     //底部栏

    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter mRecyclerAdapter;
    private List<PhotoDirectory> mPhotoDirectories;   //所有的图片文件夹

    private TextView mtvDir; //显示当前文件夹
    private View mllDir; //文件夹切换按钮
    private ImageDirectoryAdapter mImageDirectoryAdapter;    //图片文件夹的适配器
    private DirectoryPopUpWindow mDirectoryPopUpWindow;  //ImageSet的PopupWindow

    //private FilePickerFragmentListener mListener;

    private ArrayList<Photo> mCurrentDirPhotos;


    public PhotoPreviewFragmentListener mPhotoPreviewListener;

    public interface PhotoPreviewFragmentListener {
        void onItemSelectedCompletedAndBack();

        void onAllImagePreview(String path);

        void onSelectImagePreview(String path);
        // 返回事件
        void onBackExit();
    }


    /**
     * 保存 fragment data 到 Bundle
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("data", mCurrentDirPhotos);
    }

    /**
     * 获取保存在 fragment 中的 Bundle 值
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mCurrentDirPhotos = (ArrayList<Photo>) savedInstanceState.getSerializable("data");
    }


    /**
     * 初始化 mListener 回调
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "------ onAttach ");
        try {
            mPhotoPreviewListener = (PhotoPreviewFragmentListener) context;
            Log.d(TAG, "--PhotoPreviewFragmentListener");
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    + " must implements interface MyListener");
        }

    }

    /**
     * 注销回调
     */
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        mPhotoPreviewListener = null;
    }


    public ImagePickerFragment() {
        // Required empty public constructor
    }

    /**
     * 接收确切的参数，
     * 返回一个 Fragment 实例，
     * 避免了在创建 Fragment 的时候无法在类外部知道所需参数的问题
     *
     * @return
     */
    public static ImagePickerFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putString(cityName,"cityName");
//        ImagePickerFragment fragment = new ImagePickerFragment();
//        fragment.setArguments(args);
//        return fragment;
        ImagePickerFragment imagePickerFragment = new ImagePickerFragment();
        return imagePickerFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mllDir = view.findViewById(R.id.ll_dir);
        mllDir.setOnClickListener(this);

        mtvDir = view.findViewById(R.id.tv_dir);

        mRecyclerView = view.findViewById(R.id.recycler);
        //图片点击大图浏览
        //mRecyclerAdapter.setOnImageItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(getContext(), 2), false));

        mFooterBar = view.findViewById(R.id.footer_bar);
        mBtnOk = view.findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        mBtnPre = view.findViewById(R.id.btn_preview);
        mBtnPre.setOnClickListener(this);

        mBtnOk.setEnabled(false);
        mBtnPre.setEnabled(false);

        mBtnOk.setText(getString(R.string.fp_select_complete, FPickerManager.INSTANCE.getCurrentCount(), FPickerManager.INSTANCE.getMaxCount()));

        view.findViewById(R.id.btn_back).setOnClickListener(this);


        mImageDirectoryAdapter = new ImageDirectoryAdapter(getActivity(), null);

        //回退键
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //不一定是要触发返回栈，可以做一些其他的事情
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });

        getDataFromImage();
    }

    private void getDataFromImage() {
        final Bundle mediaStoreArgs = new Bundle();
        FileDataRepository.getPhotoDirs(getActivity(), mediaStoreArgs, new FileResultCallback<PhotoDirectory>() {
            @Override
            public void onResultCallback(List<PhotoDirectory> paths) {
                updateList(paths);
            }
        });

    }


    /**
     * 点击文件夹选择查看
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            //需要回传到 activity 并
            //setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
            //mListener.onItemSelectedCompletedAndBack();
            mPhotoPreviewListener.onItemSelectedCompletedAndBack();
        } else if (id == R.id.ll_dir) {
            if (mPhotoDirectories == null) {
                Log.i(this.getClass().getSimpleName(), "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            mImageDirectoryAdapter.refreshData(mPhotoDirectories);  //刷新数据
            if (mDirectoryPopUpWindow.isShowing()) {
                mDirectoryPopUpWindow.dismiss();
            } else {
                mDirectoryPopUpWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mImageDirectoryAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mDirectoryPopUpWindow.setSelection(index);
            }
        } else if (id == R.id.btn_preview) {
            Log.d(TAG, " 预览 点击后的事件！");
            mPhotoPreviewListener.onSelectImagePreview(null);
        } else if (id == R.id.btn_back) {
            //点击返回按钮
            //finish();
            mPhotoPreviewListener.onBackExit();
        }
    }


    private void createPopupFolderList() {
        mDirectoryPopUpWindow = new DirectoryPopUpWindow(getActivity(), mImageDirectoryAdapter);
        mDirectoryPopUpWindow.setOnItemClickListener(new DirectoryPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageDirectoryAdapter.setSelectIndex(position);
                //imagePicker.setCurrentImageFolderPosition(position);
                mDirectoryPopUpWindow.dismiss();
                PhotoDirectory imageFolder = (PhotoDirectory) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    mRecyclerAdapter.refreshData(imageFolder.photos);
                    mtvDir.setText(imageFolder.name);
                }
            }
        });
        mDirectoryPopUpWindow.setMargin(mFooterBar.getHeight());
    }


    private void updateList(List<PhotoDirectory> paths) {
        Log.d("Tag", "path size:" + paths.size());
        mPhotoDirectories = paths;
        mImageDirectoryAdapter.refreshData(mPhotoDirectories);
        if (null == mCurrentDirPhotos) {
            mCurrentDirPhotos = paths.get(0).photos;
        }
        mRecyclerAdapter = new ImageRecyclerAdapter(getActivity(), mCurrentDirPhotos,
                FPickerManager.INSTANCE.getSelectedPhotos(), this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }


    /**
     * 选中图片回调
     *
     * @param view
     * @param image
     * @param isSelected
     */
    @SuppressLint("StringFormatMatches")
    @Override
    public void onItemSelected(View view, Photo image, boolean isSelected) {
        Log.d(getClass().getSimpleName(), "onItemSelected: " + FPickerManager.INSTANCE.getCurrentCount());
        if (FPickerManager.INSTANCE.getMaxCount() > 0) {
            mBtnOk.setText(getString(R.string.fp_select_complete, FPickerManager.INSTANCE.getCurrentCount(), FPickerManager.INSTANCE.getMaxCount()));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
            mBtnPre.setText(getResources().getString(R.string.fp_preview_count, FPickerManager.INSTANCE.getCurrentCount()));
            mBtnPre.setTextColor(ContextCompat.getColor(getContext(), R.color.fp_text_primary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.fp_text_primary_inverted));
        } else {
            mBtnOk.setText(getString(R.string.fp_complete));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
            mBtnPre.setText(getResources().getString(R.string.fp_preview));
            mBtnPre.setTextColor(ContextCompat.getColor(getContext(), R.color.fp_text_secondary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.fp_text_secondary_inverted));
        }
        //for (int i = imagePicker.isShowCamera() ? 1 : 0; i < mRecyclerAdapter.getItemCount(); i++) {
        for (int i = 0; i < mRecyclerAdapter.getItemCount(); i++) {
            if (mRecyclerAdapter.getItem(i).path != null && mRecyclerAdapter.getItem(i).path.equals(image.path)) {
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    /**
     * 大图点击后回调 -> 预览
     *
     * @param view
     * @param image
     * @param position
     */
    @Override
    public void onItemClick(View view, Photo image, int position) {
        Log.d(this.getClass().getSimpleName(), image.name + " | " + image.path);
        //previewPhotoBack(image.path);
        mPhotoPreviewListener.onAllImagePreview(image.path);
    }


    private void previewPhotoBack(String path) {
        mPhotoPreviewListener.onSelectImagePreview(path);
    }


}
