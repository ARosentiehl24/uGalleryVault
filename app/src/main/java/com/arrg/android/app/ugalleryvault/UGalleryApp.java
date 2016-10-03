package com.arrg.android.app.ugalleryvault;

import android.app.Application;
import android.support.v4.content.ContextCompat;

import com.example.jackmiras.placeholderj.library.PlaceHolderManager;

public class UGalleryApp extends Application {

    private static PlaceHolderManager placeHolderManager;

    public static Integer DURATIONS_OF_ANIMATIONS = 250;

    @Override
    public void onCreate() {
        super.onCreate();

        placeHolderManager = new PlaceHolderManager.Configurator()
                .emptyBackground(R.color.colorPrimary)
                .emptyButton(R.string.try_again, 0)
                .emptyText(R.string.empty_content_message, 0, R.color.colorAccent)
                .errorBackground(R.color.colorPrimary)
                .errorButton(R.string.try_again, 0)
                .errorText(R.string.error_occurred_message, 0, R.color.colorAccent)
                .loadingBackground(R.color.colorPrimary)
                .loadingText(R.string.loading_content_message, 0, R.color.colorAccent)
                .progressBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .config();
    }

    public static PlaceHolderManager getPlaceHolderManager() {
        return placeHolderManager;
    }
}
