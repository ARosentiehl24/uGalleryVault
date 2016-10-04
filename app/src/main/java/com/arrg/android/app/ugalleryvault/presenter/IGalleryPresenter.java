package com.arrg.android.app.ugalleryvault.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryPresenter;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.arrg.android.app.ugalleryvault.view.activity.GalleryActivity;
import com.drivemode.media.image.ImageFacade;
import com.mukesh.permissions.AppPermissions;

import java.util.ArrayList;
import java.util.List;

import static com.arrg.android.app.ugalleryvault.view.activity.GalleryActivity.CAMERA_PERMISSION_RC;

public class IGalleryPresenter implements GalleryPresenter {

    private GalleryView galleryView;

    public IGalleryPresenter(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    @Override
    public void onCreate() {
        galleryView.configViews();
    }

    @Override
    public void onCentreButtonClick(AppPermissions appPermissions) {
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
            case GalleryActivity.STORAGE_PERMISSION_RC:
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }

                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    galleryView.showEmptyView();
                } else {
                    galleryView.showEmptyView();
                    //galleryView.launchCamera();
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
