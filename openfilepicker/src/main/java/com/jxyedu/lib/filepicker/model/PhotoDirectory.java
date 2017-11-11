package com.jxyedu.lib.filepicker.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * PhotoDirectory
 * Created by renwoxing on 2017/11/9.
 */
public class PhotoDirectory extends BaseFileBean implements Serializable {

    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public Photo cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<Photo> photos;  //当前文件夹下所有图片的集合


    public PhotoDirectory(int id, String name, String path, int mediaType) {
        super(name, path);
    }

    public PhotoDirectory() {
        super(null,null);
    }

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            PhotoDirectory other = (PhotoDirectory) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
