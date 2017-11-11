package com.jxyedu.lib.filepicker.data;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.jxyedu.lib.filepicker.R;
import com.jxyedu.lib.filepicker.model.Photo;
import com.jxyedu.lib.filepicker.model.PhotoDirectory;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * PhotoDirLoaderCallback
 * Created by renwoxing on 2017/11/10.
 */
public class PhotoDirLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED      //图片被添加的时间，long型  1450518608
    };

    private WeakReference<Context> contextWeakReference;
    private FileResultCallback<PhotoDirectory> resultCallback;


    public PhotoDirLoaderCallback(Context context, FileResultCallback<PhotoDirectory> resultCallback) {
        this.contextWeakReference = new WeakReference<>(context);
        this.resultCallback = resultCallback;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new PhotoDirectoryLoader(contextWeakReference.get(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;

        ArrayList<PhotoDirectory> photoDirectories = new ArrayList<>();
        ArrayList<Photo> allPhotos = new ArrayList<>();

        while (data.moveToNext()) {

            //查询数据
            String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

            File file = new File(imagePath);
            if (!file.exists() || file.length() <= 0) {
                continue;
            }

            long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
            int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
            int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
            String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
            long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
            //封装实体
            Photo photo = new Photo();
            photo.name = imageName;
            photo.path = imagePath;
            photo.size = imageSize;
            photo.width = imageWidth;
            photo.height = imageHeight;
            photo.mimeType = imageMimeType;
            photo.addTime = imageAddTime;
            allPhotos.add(photo);
            //根据父路径分类存放图片
            File imageFile = new File(imagePath);
            File imageParentFile = imageFile.getParentFile();
            PhotoDirectory photoDir = new PhotoDirectory();
            photoDir.name = imageParentFile.getName();
            photoDir.path = imageParentFile.getAbsolutePath();

            if (!photoDirectories.contains(photoDir)) {
                ArrayList<Photo> newPhotos = new ArrayList<>();
                newPhotos.add(photo);
                photoDir.cover = photo;
                photoDir.photos = newPhotos;
                photoDirectories.add(photoDir);
            } else {
                photoDirectories.get(photoDirectories.indexOf(photoDir)).photos.add(photo);
            }
        }

        //没有图片时
        if (data.getCount() > 0 && allPhotos.size()>0) {
            //构造所有图片的集合
            PhotoDirectory photoDirectory = new PhotoDirectory();
            photoDirectory.name = contextWeakReference.get().getString(R.string.fp_all_images);
            photoDirectory.path = "/";
            photoDirectory.cover = allPhotos.get(0);
            photoDirectory.photos = allPhotos;
            photoDirectories.add(0, photoDirectory);  //确保第一条是所有图片
        }

        if (resultCallback != null) {
            resultCallback.onResultCallback(photoDirectories);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
