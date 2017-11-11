package com.jxyedu.lib.filepicker.data;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.jxyedu.lib.filepicker.FPickerConstants;

/**
 * PhotoDirectoryLoader
 * Created by renwoxing on 2017/11/10.
 */
public class PhotoDirectoryLoader extends CursorLoader {

    final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.TITLE
    };

    public PhotoDirectoryLoader(Context context, Bundle args) {
        super(context);

        String bucketId = args.getString(FPickerConstants.EXTRA_BUCKET_ID, null);
        int mediaType = args.getInt(FPickerConstants.EXTRA_FILE_TYPE, FPickerConstants.MEDIA_TYPE_IMAGE);

        setProjection(null);

        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Images.Media._ID + " DESC");

        StringBuilder selection = new StringBuilder("");

//      String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE);
        selection.append("=");
        selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

        if (mediaType == FPickerConstants.MEDIA_TYPE_VIDEO) {
            selection.setLength(0);
            selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE);
            selection.append("=");
            selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
//          selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        }

        if (bucketId != null) {
            //selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'";
            selection.append(" AND ");
            selection.append(MediaStore.Images.Media.BUCKET_ID);
            selection.append("='");
            selection.append(bucketId);
            selection.append("'");
        }

        setSelection(selection.toString());
    }

    public PhotoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
