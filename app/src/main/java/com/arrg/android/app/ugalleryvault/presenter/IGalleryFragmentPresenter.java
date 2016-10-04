package com.arrg.android.app.ugalleryvault.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.arrg.android.app.ugalleryvault.interfaces.GalleryFragmentPresenter;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryFragmentView;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneMedia;
import com.mukesh.permissions.AppPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class IGalleryFragmentPresenter implements GalleryFragmentPresenter {

    private static final int STORAGE_PERMISSION_RC = 101;
    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private GalleryFragmentView galleryFragmentView;

    public IGalleryFragmentPresenter(GalleryFragmentView galleryFragmentView) {
        this.galleryFragmentView = galleryFragmentView;
    }

    @Override
    public void onCreateView(ViewGroup container) {
        galleryFragmentView.initializeViews(container);

        AppPermissions appPermissions = new AppPermissions(getContext());

        if (appPermissions.hasPermission(STORAGE_PERMISSIONS)) {
            galleryFragmentView.configViews();
        } else {
            galleryFragmentView.rqPermission(STORAGE_PERMISSIONS, STORAGE_PERMISSION_RC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionResults = new ArrayList<>();

        switch (requestCode) {
            case STORAGE_PERMISSION_RC:
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }

                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    galleryFragmentView.showEmpty();
                } else {
                    galleryFragmentView.configViews();
                }

                break;
        }

        permissionResults.clear();
    }

    @Override
    public ArrayList<PhoneAlbum> getAlbums(FragmentActivity fragmentContext) {
        ArrayList<PhoneAlbum> albumArrayList = new ArrayList<>();
        ArrayList<String> albumNames = new ArrayList<>();

        /*String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };*/

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        //Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Uri externalContentUri = MediaStore.Files.getContentUri("external");

        /*Cursor cursor = fragmentContext.getContentResolver().query(externalContentUri,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN + " DESC"        // Ordering
        );*/

        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

        Cursor cursor = fragmentContext.getContentResolver().query(externalContentUri, projection, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            Log.i("DeviceImageManager", " query count=" + cursor.getCount());

            if (cursor.moveToFirst()) {
                Integer id;
                Integer folderId;
                String data;
                Long dateAdded;
                String mediaType;
                String mimeType;
                String title;

                int idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                int folderIdColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.PARENT);
                int dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int dateAddedColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED);
                int mediaTypeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
                int mimeTypeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE);
                int titleColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE);

                while (cursor.moveToNext()) {
                    id = cursor.getInt(idColumn);
                    folderId = cursor.getInt(folderIdColumn);
                    data = cursor.getString(dataColumn);
                    dateAdded = cursor.getLong(dateAddedColumn);
                    mediaType = cursor.getString(mediaTypeColumn);
                    mimeType = cursor.getString(mimeTypeColumn);
                    title = cursor.getString(titleColumn);

                    File file = new File(data);
                    String albumName = file.getParentFile().getName();

                    SimpleDateFormat dateTimeInstance = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
                    String date = dateTimeInstance.format(new Date(dateAdded * 1000L));

                    PhoneMedia media = new PhoneMedia(id, albumName, data, date, mediaType, mimeType, title);

                    if (albumNames.contains(albumName)) {
                        for (PhoneAlbum album : albumArrayList) {
                            if (album.getAlbumName().equals(albumName)) {
                                album.getPhoneMedias().add(media);

                                //Log.i("DeviceImageManager", "A photo was added to album => " + albumName);

                                break;
                            }
                        }
                    } else {
                        //Log.i("DeviceImageManager", "A new album was created => " + albumName);

                        PhoneAlbum album = new PhoneAlbum(folderId, albumName, media.getMediaPath());
                        album.getPhoneMedias().add(media);

                        //Log.i("DeviceImageManager", "A photo was added to album => " + albumName);

                        albumArrayList.add(album);
                        albumNames.add(albumName);
                    }

                    Log.e(getClass().getSimpleName(), id + " - " + albumName + " - " + data + " - " + date + " - " + mediaType + " - " + mimeType + " - " + title);
                }
            }
        }

        assert cursor != null;
        cursor.close();

        Collections.sort(albumArrayList, new Comparator<PhoneAlbum>() {
            @Override
            public int compare(PhoneAlbum o1, PhoneAlbum o2) {
                return o1.getAlbumName().compareTo(o2.getAlbumName());
            }
        });

        return albumArrayList;
    }

    @Override
    public FragmentActivity getContext() {
        return galleryFragmentView.getFragmentContext();
    }
}
