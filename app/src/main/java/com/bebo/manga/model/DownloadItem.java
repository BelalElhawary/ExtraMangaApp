package com.bebo.manga.model;

public class DownloadItem {
    public Comic DComic;
    public Chapter DChapter;
    public int max;
    public int progress;

    public DownloadItem(Comic DComic, Chapter DChapter, int max, int progress){
        this.DComic = DComic;
        this.DChapter = DChapter;
        this.max = max;
        this.progress = progress;
    }
}
