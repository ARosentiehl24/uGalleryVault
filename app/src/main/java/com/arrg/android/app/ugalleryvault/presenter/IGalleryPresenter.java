package com.arrg.android.app.ugalleryvault.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryPresenter;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.view.fragment.GalleryFragment;
import com.mukesh.permissions.AppPermissions;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

public class IGalleryPresenter implements GalleryPresenter {

    private static final int CAMERA_PERMISSION_RC = 100;

    private AppPermissions appPermissions;
    private Boolean isAllSelected = false;
    private Boolean isSearchViewDisplayed = false;
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
    public void onBackPressed() {
        if (isSearchViewDisplayed) {
            isSearchViewDisplayed = false;

            galleryView.hideSearchView();
        } else {
            Navigator.with(getContext()).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
        }
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
        GalleryFragment galleryFragment = (GalleryFragment) getFragment(GalleryFragment.class);

        switch (itemIndex) {
            case 0:
                Navigator.with(getContext())
                        .build()
                        .goTo(new GalleryFragment(), R.id.container)
                        .tag(GalleryFragment.class.getSimpleName())
                        .animation(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace()
                        .commit();
                break;
            case 1:
                galleryFragment.showFavorite();
                break;
            case 2:
                if (isSearchViewDisplayed) {
                    isSearchViewDisplayed = false;

                    galleryView.hideSearchView();
                } else {
                    isSearchViewDisplayed = true;

                    galleryView.showSearchView();
                }
                break;
            case 3:
                if (isNotNull(galleryFragment)) {
                    if (isAllSelected) {
                        isAllSelected = false;

                        galleryFragment.unSelectAll();
                    } else {
                        isAllSelected = true;

                        galleryFragment.selectAll();
                    }
                }
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
    public AppCompatActivity getContext() {
        return galleryView.getContext();
    }

    @Override
    public Fragment getFragment(Class fragmentClass) {
        return getContext().getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
    }
    @Override
    public Boolean isNotNull(Object o) {
        return o != null;
    }
}
