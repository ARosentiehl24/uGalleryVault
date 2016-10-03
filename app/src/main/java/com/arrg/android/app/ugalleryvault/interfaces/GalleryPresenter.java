package com.arrg.android.app.ugalleryvault.interfaces;

import com.mukesh.permissions.AppPermissions;

public interface GalleryPresenter {

    void onCreate();

    void onCentreButtonClick(AppPermissions appPermissions);

    void onItemClick(int itemIndex, String itemName);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

}
