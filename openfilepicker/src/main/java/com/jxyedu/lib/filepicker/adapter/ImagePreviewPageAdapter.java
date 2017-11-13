package com.jxyedu.lib.filepicker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.utils.Utils;
import com.jxyedu.lib.filepicker.view.GestureImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * ImagePreviewPageAdapter
 * Created by renwoxing on 2017/11/13.
 */
public class ImagePreviewPageAdapter extends PagerAdapter {

    private int screenWidth;
    private int screenHeight;
    private FPickerManager imagePicker;
    private ArrayList<String> images = new ArrayList<>();
    private Activity mActivity;
    public PhotoViewClickListener listener;

    public ImagePreviewPageAdapter(Activity activity, ArrayList<String> images) {
        this.mActivity = activity;
        this.images = images;

        DisplayMetrics dm = Utils.getScreenPix(activity);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        imagePicker = FPickerManager.INSTANCE;
    }

    public void setData(ArrayList<String> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GestureImageView photoView = new GestureImageView(mActivity);
        String imagePath = images.get(position);
        //photoView.setImageURI(Uri.fromFile(new File(imagePath)));
        Glide.with(mActivity)
                .load(new File(imagePath))
                .apply(RequestOptions
                        .centerInsideTransform()
                        .override(screenWidth, screenHeight)
                        .placeholder(R.drawable.ic_default_image))
                //.thumbnail(0.5f)
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }


//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return false;
//    }
}
