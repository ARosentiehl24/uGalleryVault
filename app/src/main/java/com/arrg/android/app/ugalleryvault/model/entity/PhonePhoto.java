package com.arrg.android.app.ugalleryvault.model.entity;

public class PhonePhoto {

    private Integer id;
    private String albumName;
    private String photoPath;

    public PhonePhoto(Integer id, String albumName, String photoPath) {
        this.id = id;
        this.albumName = albumName;
        this.photoPath = photoPath;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
