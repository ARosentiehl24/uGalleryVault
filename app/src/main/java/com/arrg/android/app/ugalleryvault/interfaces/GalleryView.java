package com.arrg.android.app.ugalleryvault.interfaces;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public interface GalleryView {

    void configViews();

    void switchSearchView();

    void showSearchView();

    void hideSearchView();

    void showKeyboard(View view, Context context);

    void hideKeyboard(View view, Context context);

    void showMessage(Integer message);

    void showMessage(String message);

    void launchCamera();

    void launchPackage(String packageName);

    Activity getContext();
}
