package com.jxyedu.lib.filepicker;

import android.util.Log;

import java.util.ArrayList;

/**
 * FPickerManager
 * Created by renwoxing on 2017/11/9.
 */
public enum FPickerManager {

    INSTANCE;

    private int maxCount = FPickerConstants.DEFAULT_MAX_COUNT;
    private boolean showImages = true;
    //private int cameraDrawable = R.drawable.ic_camera;

    /**
     * 选中的值
     */
    private ArrayList<String> mediaFiles;
    /**
     *
     */
    private ArrayList<String> docFiles;

    //private LinkedHashSet<FileType> fileTypes;

    //private int theme = R.style.LibAppTheme;

    private boolean showVideos;

    private boolean showGif;

    private boolean showSelectAll;

    private boolean docSupport = true;

    private boolean enableCamera = true;

    //private Orientation orientation = Orientation.UNSPECIFIED;

    private boolean showFolderView = true;

    private String providerAuthorities;

    private FPickerManager() {
        mediaFiles = new ArrayList<>();
        docFiles = new ArrayList<>();
        //fileTypes = new LinkedHashSet<>();
    }




    public void setMaxCount(int count) {
        reset();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCurrentCount() {
        return mediaFiles.size() + docFiles.size();
    }

    public void add(String path, int type) {
        Log.d("----->", "add: ----- path:"+path +" | "+shouldAdd()+"|"+(type == FPickerConstants.FILE_TYPE_MEDIA));
        if (path != null && shouldAdd()) {
            if (!mediaFiles.contains(path) && type == FPickerConstants.FILE_TYPE_MEDIA)
                mediaFiles.add(path);
            else if (!docFiles.contains(path) && type == FPickerConstants.FILE_TYPE_DOCUMENT)
                docFiles.add(path);
            else
                return;
        }
    }

    public void add(ArrayList<String> paths, int type) {
        for (int index = 0; index < paths.size(); index++) {
            add(paths.get(index), type);
        }
    }

    public void remove(String path, int type) {
        if ((type == FPickerConstants.FILE_TYPE_MEDIA) && mediaFiles.contains(path)) {
            mediaFiles.remove(path);

        } else if (type == FPickerConstants.FILE_TYPE_DOCUMENT) {
            docFiles.remove(path);
        }
    }

    public boolean shouldAdd() {
        if (maxCount == -1)
            return true;
        return getCurrentCount() < maxCount;
    }

    public ArrayList<String> getSelectedPhotos() {
        return mediaFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

//    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
//        ArrayList<String> paths = new ArrayList<>();
//        for (int index = 0; index < files.size(); index++) {
//            paths.add(files.get(index).getPath());
//        }
//        return paths;
//    }

    public void reset() {
        docFiles.clear();
        mediaFiles.clear();
        //fileTypes.clear();
        maxCount = -1;
    }

    public void clearSelections() {
        mediaFiles.clear();
        docFiles.clear();
    }

    public void deleteMedia(ArrayList<String> paths) {
        mediaFiles.removeAll(paths);
    }

//    public int getTheme() {
//        return theme;
//    }
//
//    public void setTheme(int theme) {
//        this.theme = theme;
//    }

    public boolean showVideo() {
        return showVideos;
    }

    public void setShowVideos(boolean showVideos) {
        this.showVideos = showVideos;
    }

    public boolean showImages() {
        return showImages;
    }

    public void setShowImages(boolean showImages) {
        this.showImages = showImages;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

//    public void addFileType(FileType fileType)
//    {
//        fileTypes.add(fileType);
//    }
//
//    public void addDocTypes()
//    {
//        String[] pdfs = {"pdf"};
//        fileTypes.add(new FileType(FilePickerConst.PDF,pdfs,R.drawable.ic_pdf));
//
//        String[] docs = {"doc","docx", "dot","dotx"};
//        fileTypes.add(new FileType(FilePickerConst.DOC,docs,R.drawable.ic_word));
//
//        String[] ppts = {"ppt","pptx"};
//        fileTypes.add(new FileType(FilePickerConst.PPT,ppts,R.drawable.ic_ppt));
//
//        String[] xlss = {"xls","xlsx"};
//        fileTypes.add(new FileType(FilePickerConst.XLS,xlss,R.drawable.ic_excel));
//
//        String[] txts = {"txt"};
//        fileTypes.add(new FileType(FilePickerConst.TXT,txts,R.drawable.ic_txt));
//    }
//
//    public ArrayList<FileType> getFileTypes()
//    {
//        return new ArrayList<>(fileTypes);
//    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }

    public boolean isEnableCamera() {
        return enableCamera;
    }

    public void setEnableCamera(boolean enableCamera) {
        this.enableCamera = enableCamera;
    }

//    public Orientation getOrientation() {
//        return orientation;
//    }
//
//    public void setOrientation(Orientation orientation) {
//        this.orientation = orientation;
//    }

    public String getProviderAuthorities() {
        return providerAuthorities;
    }

    public void setProviderAuthorities(String providerAuthorities) {
        this.providerAuthorities = providerAuthorities;
    }

//    public void setCameraDrawable(int drawable) {
//        this.cameraDrawable = drawable;
//    }
//
//    public int getCameraDrawable()
//    {
//        return cameraDrawable;
//    }

    public boolean hasSelectAll() {
        return maxCount == -1 && showSelectAll;
    }

    public void enableSelectAll(boolean showSelectAll) {
        this.showSelectAll = showSelectAll;
    }
}
