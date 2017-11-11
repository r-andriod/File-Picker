package com.jxyedu.lib.filepicker.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jxyedu.lib.filepicker.FPickerConstants;
import com.jxyedu.lib.filepicker.model.PhotoDirectory;

/**
 * FileDataRepository
 * Created by renwoxing on 2017/11/10.
 */
public class FileDataRepository {

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
        if (activity.getSupportLoaderManager().getLoader(FPickerConstants.MEDIA_TYPE_IMAGE) != null)
            activity.getSupportLoaderManager()
                    .restartLoader(FPickerConstants.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallback(activity, resultCallback));
        else
            activity.getSupportLoaderManager()
                    .initLoader(FPickerConstants.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallback(activity, resultCallback));
    }
}
