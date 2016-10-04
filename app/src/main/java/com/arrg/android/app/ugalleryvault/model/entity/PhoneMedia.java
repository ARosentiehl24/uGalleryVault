package com.arrg.android.app.ugalleryvault.model.entity;

public class PhoneMedia {

    private Integer id;
    private String albumName;
    private String mediaPath;
    private String dateAdded;
    private String mediaType;
    private String mimeType;
    private String title;

    public PhoneMedia(Integer id, String albumName, String mediaPath, String dateAdded, String mediaType, String mimeType, String title) {
        this.id = id;
        this.albumName = albumName;
        this.mediaPath = mediaPath;
        this.dateAdded = dateAdded;
        this.mediaType = mediaType;
        this.mimeType = mimeType;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
