package com.arrg.android.app.ugalleryvault.presenter;

import android.content.pm.PackageManager;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryPresenter;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.view.activity.GalleryActivity;

import java.util.ArrayList;
import java.util.List;

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionResults = new ArrayList<>();

        switch (requestCode) {
            case GalleryActivity.CAMERA_PERMISSION_RC:
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
                    galleryView.launchCamera();
                }

                break;
        }

        permissionResults.clear();
    }
}
