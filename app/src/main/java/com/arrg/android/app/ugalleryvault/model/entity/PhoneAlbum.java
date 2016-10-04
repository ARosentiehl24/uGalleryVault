package com.arrg.android.app.ugalleryvault.model.entity;

import java.util.ArrayList;

public class PhoneAlbum {

    private Integer id;
    private String albumName;
    private String coverPhoto;
    private ArrayList<PhonePhoto> phonePhotos;

    public PhoneAlbum(Integer id, String albumName, String coverPhoto, ArrayList<PhonePhoto> phonePhotos) {
        this.id = id;
        this.albumName = albumName;
        this.coverPhoto = coverPhoto;
        this.phonePhotos = phonePhotos;
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

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public ArrayList<PhonePhoto> getPhonePhotos() {
        return phonePhotos;
    }

    public void setPhonePhotos(ArrayList<PhonePhoto> phonePhotos) {
        this.phonePhotos = phonePhotos;
    }
}
