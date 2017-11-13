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
import com.jxyedu.lib.filepicker.utils.FragmentUtil;

import java.util.ArrayList;

public class FPickerActivity extends AppCompatActivity implements
        FilePickerFragmentListener {

    public static final String TAG = FPickerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        //fragment 
        openSpecificFragment(savedInstanceState);
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
     * 点击
     * @param v
     */
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.btn_back) {
//            //点击返回按钮
//            finish();
//        }
//    }


    /**
     * 返回选择文件数据结果给需要的页面
     *
     * @param paths
     */
    private void returnData(ArrayList<String> paths) {
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
    public void onItemSelected() {
        ArrayList<String> paths = new ArrayList<>();
        paths = FPickerManager.INSTANCE.getSelectedPhotos();
        returnData(paths);
    }
}
