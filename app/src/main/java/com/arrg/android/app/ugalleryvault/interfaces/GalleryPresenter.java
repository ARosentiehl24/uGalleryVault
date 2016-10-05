package com.arrg.android.app.ugalleryvault.interfaces;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public interface GalleryPresenter {

    void onCreate();

    void onBackPressed();

    void onCentreButtonClick();

    void onItemClick(int itemIndex, String itemName);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    AppCompatActivity getContext();

    Fragment getFragment(Class fragmentClass);

    Boolean isNotNull(Object o);
}
