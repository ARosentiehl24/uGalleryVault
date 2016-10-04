package com.arrg.android.app.ugalleryvault.interfaces;

import android.app.Activity;

import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.drivemode.media.image.ImageFacade;

import java.util.ArrayList;

public interface GalleryPresenter {

    void onCreate();

    Activity getContext();

    void onCentreButtonClick();

    void onItemClick(int itemIndex, String itemName);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    ArrayList<PhoneAlbum> getPhoneAlbums(ImageFacade imageFacade);
}
