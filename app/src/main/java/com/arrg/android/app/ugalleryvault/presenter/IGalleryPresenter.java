package com.arrg.android.app.ugalleryvault.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryPresenter;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.drivemode.media.image.ImageFacade;
import com.mukesh.permissions.AppPermissions;

import java.util.ArrayList;
import java.util.List;

public class IGalleryPresenter implements GalleryPresenter {

    private static final int CAMERA_PERMISSION_RC = 100;

    private AppPermissions appPermissions;
    private GalleryView galleryView;

    public IGalleryPresenter(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    @Override
    public void onCreate() {
        appPermissions = new AppPermissions(getContext());

        galleryView.configViews();
    }

    @Override
    public Activity getContext() {
        return galleryView.getContext();
    }

    @Override
    public void onCentreButtonClick() {
        if (!appPermissions.hasPermission(Manifest.permission.CAMERA)) {
            appPermissions.requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_RC);
        } else {
            galleryView.launchCamera();
        }
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        switch (itemIndex) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                galleryView.switchSearchView();
                break;
            case 3:
                
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionResults = new ArrayList<>();

        switch (requestCode) {
            case CAMERA_PERMISSION_RC:
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }

                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    galleryView.showMessage(R.string.camera_permission_denied_message);
                } else {
                    galleryView.launchCamera();
                }

                break;
        }

        permissionResults.clear();
    }

    @Override
    public ArrayList<PhoneAlbum> getPhoneAlbums(ImageFacade imageFacade) {


        return null;
    }


}
