package com.jxyedu.lib.filepicker.data;

import java.util.List;

/**
 * file path callback
 * FileResultCallback
 * Created by renwoxing on 2017/11/10.
 */
public interface FileResultCallback<T> {
    /**
     * path 回调
     * @param paths
     */
    void onResultCallback(List<T> paths);
}
