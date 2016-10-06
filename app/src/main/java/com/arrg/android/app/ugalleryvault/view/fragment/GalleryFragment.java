package com.arrg.android.app.ugalleryvault.view.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.UGalleryApp;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryFragmentView;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.arrg.android.app.ugalleryvault.presenter.IGalleryFragmentPresenter;
import com.arrg.android.app.ugalleryvault.view.adapter.GalleryAdapter;
import com.example.jackmiras.placeholderj.library.PlaceHolderJ;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryFragmentView {

    @BindView(R.id.gallery)
    DragSelectRecyclerView gallery;

    private GalleryAdapter galleryAdapter;
    private IGalleryFragmentPresenter iGalleryFragmentPresenter;
    private PlaceHolderJ placeHolderJ;
    private boolean isLongClickPressed = false;

    public GalleryFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, container);

        iGalleryFragmentPresenter = new IGalleryFragmentPresenter(this);
        iGalleryFragmentPresenter.onCreateView(container);

        return container;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        iGalleryFragmentPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void initializeViews(ViewGroup container) {
        placeHolderJ = new PlaceHolderJ(container, R.id.gallery, UGalleryApp.getPlaceHolderManager());
        placeHolderJ.init(R.id.view_loading, R.id.view_empty, R.id.view_error);

        ((Button) placeHolderJ.viewEmpty.getRootView().findViewById(R.id.button_empty_try_again)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        ((Button) placeHolderJ.viewError.getRootView().findViewById(R.id.button_error_try_again)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    public void configViews() {
        new LoadAlbumTask().execute();
    }

    @Override
    public void showEmpty() {
        placeHolderJ.showEmpty(null);
    }

    @Override
    public void showMessage(Integer message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rqPermission(String[] permissions, int rc) {
        requestPermissions(permissions, rc);
    }

    @Override
    public FragmentActivity getFragmentContext() {
        return getActivity();
    }

    @Override
    public void selectAll() {
        isLongClickPressed = true;

        for (int i = 0; i < galleryAdapter.getItemCount(); i++) {
            galleryAdapter.setSelected(i, true);
        }

        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void unSelectAll() {
        isLongClickPressed = false;

        galleryAdapter.clearSelected();

        for (int i = 0; i < galleryAdapter.getItemCount(); i++) {
            galleryAdapter.setChecked(i, false);
        }

        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFavorite() {

    }

    class LoadAlbumTask extends AsyncTask<Void, Void, ArrayList<PhoneAlbum>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            placeHolderJ.showLoading();
        }

        @Override
        protected ArrayList<PhoneAlbum> doInBackground(Void... params) {
            return iGalleryFragmentPresenter.getAlbums(getFragmentContext());
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneAlbum> albumArrayList) {
            super.onPostExecute(albumArrayList);

            placeHolderJ.hideLoading();

            if (albumArrayList == null || albumArrayList.size() == 0) {
                placeHolderJ.showEmpty(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoadAlbumTask().execute();
                    }
                });
            } else {
                galleryAdapter = new GalleryAdapter(getActivity(), albumArrayList);
                galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(GalleryAdapter.ViewHolder viewHolder, View itemView, int position) {
                        if (isLongClickPressed) {
                            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.cbIsSelected);
                            checkBox.setChecked(!checkBox.isChecked());

                            galleryAdapter.setChecked(position, checkBox.isChecked());
                        } else {
                            showMessage("Open Album");
                        }
                    }

                    @Override
                    public void onLongItemClick(GalleryAdapter.ViewHolder viewHolder, View itemView, int position) {
                        if (isLongClickPressed) {
                            unSelectAll();
                        } else {
                            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.cbIsSelected);
                            checkBox.setChecked(!checkBox.isChecked());

                            galleryAdapter.setChecked(position, checkBox.isChecked());

                            selectAll();
                        }
                    }
                });
                gallery.setAdapter(galleryAdapter);
                gallery.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_count_gallery)));
            }
        }
    }
}
