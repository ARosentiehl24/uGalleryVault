package com.arrg.android.app.ugalleryvault.interfaces;

import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.drivemode.media.image.ImageFacade;
import com.mukesh.permissions.AppPermissions;

import java.util.ArrayList;

public interface GalleryPresenter {

    void onCreate();

    void onCentreButtonClick(AppPermissions appPermissions);

    void onItemClick(int itemIndex, String itemName);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    ArrayList<PhoneAlbum> getPhoneAlbums(ImageFacade imageFacade);
}
