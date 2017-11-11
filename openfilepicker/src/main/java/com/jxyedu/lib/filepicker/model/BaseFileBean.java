package com.jxyedu.lib.filepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BaseFileBean
 * Created by renwoxing on 2017/11/10.
 */
public class BaseFileBean implements Parcelable {

    protected String name;
    protected String path;

    public BaseFileBean() {

    }

    public BaseFileBean(String name, String path) {
        this.name = name;
        this.path = path;
    }

    protected BaseFileBean(Parcel in) {
        name = in.readString();
        path = in.readString();
    }

    public static final Creator<BaseFileBean> CREATOR = new Creator<BaseFileBean>() {
        @Override public BaseFileBean createFromParcel(Parcel in) {
            return new BaseFileBean(in);
        }

        @Override public BaseFileBean[] newArray(int size) {
            return new BaseFileBean[size];
        }
    };



    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseFileBean)) return false;

        BaseFileBean baseFile = (BaseFileBean) o;
        if(path!=null && ((BaseFileBean) o).path!=null)
            //return id == baseFile.id && path.equals(baseFile.path);
            return path.equals(baseFile.path);
        else
            return false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(path);
    }
}
