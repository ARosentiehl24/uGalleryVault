package com.arrg.android.app.ugalleryvault.interfaces;

import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;

import java.util.ArrayList;

public interface GalleryFragmentPresenter {

    void onCreateView(ViewGroup container);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    ArrayList<PhoneAlbum> getAlbums(FragmentActivity fragmentContext);

    FragmentActivity getContext();
}
