package com.jxyedu.lib.filepicker.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import java.util.List;

public class FPickerActivity extends AppCompatActivity implements BaseFileItemClickListener<Photo>, View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        mllDir = findViewById(R.id.ll_dir);
        mllDir.setOnClickListener(this);

        mtvDir = findViewById(R.id.tv_dir);

        mRecyclerView = findViewById(R.id.recycler);
        //图片点击大图浏览
        //mRecyclerAdapter.setOnImageItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 2), false));

        mFooterBar = findViewById(R.id.footer_bar);
        mBtnOk = findViewById(R.id.btn_ok);
        mBtnPre = findViewById(R.id.btn_preview);

        mImageDirectoryAdapter = new ImageDirectoryAdapter(this, null);

        getDataFromImage();

    }

    private void getDataFromImage() {
        final Bundle mediaStoreArgs = new Bundle();
        //mediaStoreArgs.putBoolean(FPickerConstants.EXTRA_SHOW_GIF, PickerManager.getInstance().isShowGif());
        //mediaStoreArgs.putInt(FPickerConstants.EXTRA_FILE_TYPE, fileType);

        FileDataRepository.getPhotoDirs(this, mediaStoreArgs, new FileResultCallback<PhotoDirectory>() {
            @Override
            public void onResultCallback(List<PhotoDirectory> paths) {
                updateList(paths);
            }
        });

    }


    private void updateList(List<PhotoDirectory> paths) {
        Log.d("Tag", "path size:" + paths.size());
        mPhotoDirectories = paths;
        mImageDirectoryAdapter.refreshData(mPhotoDirectories);
        mRecyclerAdapter = new ImageRecyclerAdapter(FPickerActivity.this, paths.get(0).photos,
                FPickerManager.INSTANCE.getSelectedPhotos(), this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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
            Intent intent = new Intent();
            //intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            // setResult(ImagePicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
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
//            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
//            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
//            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getSelectedImages());
//            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
//            intent.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
//            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else if (id == R.id.btn_back) {
            //点击返回按钮
            finish();
        }
    }


    /**
     * 选择文件夹 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mDirectoryPopUpWindow = new DirectoryPopUpWindow(this, mImageDirectoryAdapter);
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
            mBtnPre.setTextColor(ContextCompat.getColor(this, R.color.fp_text_primary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(this, R.color.fp_text_primary_inverted));
        } else {
            mBtnOk.setText(getString(R.string.fp_complete));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
            mBtnPre.setText(getResources().getString(R.string.fp_preview));
            mBtnPre.setTextColor(ContextCompat.getColor(this, R.color.fp_text_secondary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(this, R.color.fp_text_secondary_inverted));
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
     * 大图查看
     *
     * @param view
     * @param image
     * @param position
     */
    @Override
    public void onItemClick(View view, Photo image, int position) {
        Log.d(this.getClass().getSimpleName(), image.name + " | " + image.path);
        //根据是否有相机按钮确定位置
        //position = imagePicker.isShowCamera() ? position - 1 : position;

        //Intent intent = new Intent(FPickerActivity.this, ImagePreviewActivity.class);
        //intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);

        /**
         * 2017-03-20
         *
         * 依然采用弱引用进行解决，采用单例加锁方式处理
         */

        // 据说这样会导致大量图片的时候崩溃
//            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getCurrentImageFolderItems());

        // 但采用弱引用会导致预览弱引用直接返回空指针
        //DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, imagePicker.getCurrentImageFolderItems());
        //intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
        //startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面

    }
}
