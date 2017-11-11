package com.jxyedu.lib.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jxyedu.lib.filepicker.activity.FPickerActivity;

/**
 * FPickerBuilder
 * Created by renwoxing on 2017/11/9.
 */
public class FPickerBuilder {

    private final Bundle mPickerOptionsBundle;

    public FPickerBuilder() {
        mPickerOptionsBundle = new Bundle();
    }

    public static FPickerBuilder getInstance() {
        return new FPickerBuilder();
    }

    public FPickerBuilder setMaxCount(int maxCount) {
        FPickerManager.INSTANCE.setMaxCount(maxCount);
        return this;
    }


    public void pickPhoto(Activity context) {
        mPickerOptionsBundle.putInt(FPickerConstants.EXTRA_PICKER_TYPE, FPickerConstants.MEDIA_PICKER);
        start(context, FPickerConstants.MEDIA_PICKER);
    }

    public void pickPhoto(Fragment context) {
        mPickerOptionsBundle.putInt(FPickerConstants.EXTRA_PICKER_TYPE, FPickerConstants.MEDIA_PICKER);
        start(context, FPickerConstants.MEDIA_PICKER);
    }


    private void start(Activity context, int pickerType) {
        FPickerManager.INSTANCE.setProviderAuthorities(context.getApplicationContext().getPackageName() + ".droidninja.filepicker.provider");

        Intent intent = new Intent(context, FPickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);

        if (pickerType == FPickerConstants.MEDIA_PICKER)
            context.startActivityForResult(intent, FPickerConstants.REQUEST_CODE_PHOTO);
        else
            context.startActivityForResult(intent, FPickerConstants.REQUEST_CODE_DOC);
    }

    private void start(Fragment fragment, int pickerType) {
        FPickerManager.INSTANCE.setProviderAuthorities(fragment.getContext().getApplicationContext().getPackageName() + ".droidninja.filepicker.provider");

        Intent intent = new Intent(fragment.getActivity(), FPickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        if (pickerType == FPickerConstants.MEDIA_PICKER)
            fragment.startActivityForResult(intent, FPickerConstants.REQUEST_CODE_PHOTO);
        else
            fragment.startActivityForResult(intent, FPickerConstants.REQUEST_CODE_DOC);
    }


}
