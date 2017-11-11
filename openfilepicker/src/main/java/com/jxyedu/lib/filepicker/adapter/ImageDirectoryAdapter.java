package com.jxyedu.lib.filepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.model.PhotoDirectory;
import com.jxyedu.lib.filepicker.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹适配器
 * ImageDirectoryAdapter
 * Created by renwoxing on 2017/11/10.
 */
public class ImageDirectoryAdapter extends BaseAdapter {

    //private ImagePicker imagePicker;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<PhotoDirectory> photoDirectories;
    private int lastSelected = 0;


    public ImageDirectoryAdapter(Activity activity, List<PhotoDirectory> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0) photoDirectories = folders;
        else photoDirectories = new ArrayList<>();

        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void refreshData(List<PhotoDirectory> folders) {
        if (folders != null && folders.size() > 0) photoDirectories = folders;
        else photoDirectories.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photoDirectories.size();
    }

    @Override
    public PhotoDirectory getItem(int i) {
        return photoDirectories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoDirectory folder = getItem(position);
        holder.folderName.setText(folder.name);
        holder.imageCount.setText(mActivity.getString(R.string.fp_folder_image_count, folder.photos.size()));

        Glide.with(mActivity)
                .load(new File(folder.cover.path))
                .apply(RequestOptions
                        .centerCropTransform()
                        .override(mImageSize,mImageSize)
                        .placeholder(R.drawable.ic_default_image))
                .thumbnail(0.5f)
                .into(holder.cover);

        if (lastSelected == position) {
            holder.folderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.folderCheck.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover = view.findViewById(R.id.iv_cover);
            folderName =  view.findViewById(R.id.tv_folder_name);
            imageCount = view.findViewById(R.id.tv_image_count);
            folderCheck = view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}
