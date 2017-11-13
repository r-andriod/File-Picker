package com.jxyedu.lib.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jxyedu.lib.filepicker.FPickerConstants;
import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.fragment.FilePickerFragmentListener;
import com.jxyedu.lib.filepicker.fragment.ImagePickerFragment;
import com.jxyedu.lib.filepicker.fragment.ResultImagePreviewFragment;
import com.jxyedu.lib.filepicker.utils.FragmentUtil;

import java.util.ArrayList;

public class FPickerActivity extends AppCompatActivity implements
        FilePickerFragmentListener,
        ImagePickerFragment.PhotoPreviewFragmentListener,
        ResultImagePreviewFragment.PhotoPreviewAndResultFragmentListener {

    public static final String TAG = FPickerActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        Log.d(TAG, " fp activity onCreate: ------>");


        if (isPreviewExtra()){
            openPreviewPhotoFragment("");
        }else {
            //fragment
            openSpecificFragment(savedInstanceState);
        }
    }

    /**
     * 回退键时注销对象
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FPickerManager.INSTANCE.reset();
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * 打开对应的 Fragment
     *
     * @param savedInstanceState
     */
    private void openSpecificFragment(Bundle savedInstanceState) {
        ImagePickerFragment imagePickerFragment = null;
        if (savedInstanceState != null) {
            imagePickerFragment = (ImagePickerFragment) FragmentUtil.getFragmentByTag(this, ImagePickerFragment.TAG.toString());
        }
        if (imagePickerFragment == null) {
            imagePickerFragment = ImagePickerFragment.newInstance();
        }
        FragmentUtil.addFragment(this, R.id.container, imagePickerFragment);

    }


    /**
     * 返回选择文件数据结果给需要的页面
     */
    private void returnData() {
        ArrayList<String> paths = new ArrayList<>();
        paths = FPickerManager.INSTANCE.getSelectedPhotos();
        Log.d(TAG, "path size:" + paths.size() + " 返回！");
        Intent intent = new Intent();
        intent.putStringArrayListExtra(FPickerConstants.KEY_SELECTED_MEDIA, paths);
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * 文件选择后确定的回调
     */
    @Override
    public void onItemSelectedCompletedAndBack() {
        returnData();
    }

    @Override
    public void onAllImagePreview(String path) {
        Log.d(TAG, "onAllImagePreview path -> " + path);
        //ImagePreviewFragment imagePreviewFragment = ImagePreviewFragment.newInstance(path);
        //FragmentUtil.addFragment(this, R.id.container, imagePreviewFragment);
    }

    @Override
    public void onSelectImagePreview(String path) {
        Log.d(TAG, "onSelectImagePreview path -> " + path);
        openPreviewPhotoFragment(path);
    }


    @Override
    public void onBackExit() {
        finish();
    }


    @Override
    public void onPhotoPreviewAndResult() {
        Log.d(TAG, "onPhotoPreviewAndResult: ----");
        returnData();
    }


    /**
     *
     */
    private boolean isPreviewExtra() {
        ArrayList<String> lists = (ArrayList<String>) getIntent()
                .getSerializableExtra(FPickerConstants.EXTRA_PHOTO_PREVIEW_PATH);
        if (null != lists && lists.size() > 0) {
            Log.d(TAG, "onCreate: list size:" + lists.size());
            FPickerManager.INSTANCE.add(lists,FPickerConstants.FILE_TYPE_MEDIA);
            return true;
        }
        return false;
    }


    private void openPreviewPhotoFragment(String path) {
        ResultImagePreviewFragment resultImagePreviewFragment = ResultImagePreviewFragment.newInstance(path);
        FragmentUtil.replaceFragment(this, R.id.container, resultImagePreviewFragment);
    }
}
