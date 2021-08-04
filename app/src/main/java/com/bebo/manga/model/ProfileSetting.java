package com.bebo.manga.model;

public class ProfileSetting {

    public int ImageCompress = 100;
    public boolean autoRefresh = false;

    public ProfileSetting(int ImageCompress, boolean autoRefresh){
        this.ImageCompress = ImageCompress;
        this.autoRefresh = autoRefresh;
    }
}
