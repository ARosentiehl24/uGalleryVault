package com.arrg.android.app.ugalleryvault.interfaces;

import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

public interface GalleryFragmentView {

    void initializeViews(ViewGroup container);

    void configViews();

    void showEmpty();

    void showMessage(Integer message);

    void showMessage(String message);

    void rqPermission(String[] permissions, int rc);

    FragmentActivity getFragmentContext();

    void selectAll();

    void unSelectAll();

    void showFavorite();
}
