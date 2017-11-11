package com.jxyedu.lib.filepicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jxyedu.lib.filepicker.FPickerConstants;
import com.jxyedu.lib.filepicker.FPickerManager;
import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.model.Photo;
import com.jxyedu.lib.filepicker.utils.Utils;
import com.jxyedu.lib.filepicker.view.SuperCheckBox;

import java.io.File;
import java.util.ArrayList;

/**
 * 加载相册图片的RecyclerView适配器
 */
public class ImageRecyclerAdapter extends BaseSelectAdapter<RecyclerView.ViewHolder, Photo> {


    private static final int ITEM_TYPE_CAMERA = 0;  //第一个条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //第一个条目不是相机
    //private ImagePicker imagePicker;
    private Activity mActivity;
    private ArrayList<Photo> images;       //当前需要显示的所有的图片数据
    //private ArrayList<String> mSelectedImages; //全局保存的已经选中的图片数据
    private boolean isShowCamera = false;         //是否显示拍照按钮
    private int mImageSize;               //每个条目的大小
    private LayoutInflater mInflater;
    private BaseFileItemClickListener listener;   //图片被点击的监听



    public void refreshData(ArrayList<Photo> images) {
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = images;
        notifyDataSetChanged();
    }

    /**
     * 构造方法
     */
    public ImageRecyclerAdapter(Activity activity,
                                ArrayList<Photo> images,
                                ArrayList<String> selectedImages,
                                BaseFileItemClickListener baseFileItemClickListener) {
        super(images, selectedImages);
        this.mActivity = activity;
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = images;

        listener = baseFileItemClickListener;
        mImageSize = Utils.getImageItemWidth(mActivity);
        //mSelectedImages = selectedImages;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(mInflater.inflate(R.layout.adapter_camera_item, parent, false));
        }
        return new ImageViewHolder(mInflater.inflate(R.layout.adapter_image_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).bindCamera();
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera) return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        return ITEM_TYPE_NORMAL;
    }



    @Override
    public int getItemCount() {
        return isShowCamera ? images.size() + 1 : images.size();
    }

    public Photo getItem(int position) {
        if (isShowCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    private class ImageViewHolder extends ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        View checkView;
        SuperCheckBox cbCheck;


        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb =  itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            checkView = itemView.findViewById(R.id.checkView);
            cbCheck =  itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        void bind(final int position) {
            final Photo imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemClick(rootView, imageItem, position);
                }
            });
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCheck.setChecked(!cbCheck.isChecked());
                    int selectLimit = FPickerManager.INSTANCE.getMaxCount();
                    if (cbCheck.isChecked() && getSelectedPaths().size() >= selectLimit) {
                        Toast.makeText(mActivity.getApplicationContext(), mActivity.getString(R.string.fp_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                        cbCheck.setChecked(false);
                        mask.setVisibility(View.GONE);
                    } else {
                        FPickerManager.INSTANCE.add(imageItem.path, FPickerConstants.FILE_TYPE_MEDIA);
                        toggleSelection(imageItem);  //表示选中
                        mask.setVisibility(View.VISIBLE);
                    }
                    // 先存储值再回调
                    if (null!=listener){
                        listener.onItemSelected(rootView,imageItem,cbCheck.isChecked());
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
//            if (imagePicker.isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                boolean checked = isSelected(imageItem);
                if (checked) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
//            } else {
//                cbCheck.setVisibility(View.GONE);
//            }

            Glide.with(mActivity)
                    .load(new File(images.get(position).path))
                    .apply(RequestOptions
                            .centerCropTransform()
                            .override(mImageSize, mImageSize)
                            .placeholder(R.drawable.ic_default_image))
                    .thumbnail(0.5f)
                    .into(ivThumb);
        }

    }

    private class CameraViewHolder extends ViewHolder {

        View mItemView;

        CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        void bindCamera() {
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            mItemView.setTag(null);
//            mItemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!((ImageBaseActivity) mActivity).checkPermission(Manifest.permission.CAMERA)) {
//                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, ImageGridActivity.REQUEST_PERMISSION_CAMERA);
//                    } else {
//                        imagePicker.takePicture(mActivity, ImagePicker.REQUEST_CODE_TAKE);
//                    }
//                }
//            });
        }
    }


}
