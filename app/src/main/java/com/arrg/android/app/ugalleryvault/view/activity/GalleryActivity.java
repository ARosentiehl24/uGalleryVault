package com.arrg.android.app.ugalleryvault.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.UGalleryApp;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.presenter.IGalleryPresenter;
import com.example.jackmiras.placeholderj.library.PlaceHolderJ;
import com.jaouan.revealator.Revealator;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mukesh.permissions.AppPermissions;

import org.fingerlinks.mobile.android.navigator.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arrg.android.app.ugalleryvault.UGalleryApp.*;

public class GalleryActivity extends AppCompatActivity implements GalleryView, SpaceOnClickListener {

    public static final int CAMERA_PERMISSION_RC = 100;
    public static final int STORAGE_PERMISSION_RC = 101;

    public static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private AppPermissions appPermissions;
    private Boolean isSearchViewDisplayed = false;
    private IGalleryPresenter iGalleryPresenter;
    private PlaceHolderJ placeHolderJ;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    AppCompatEditText searchView;
    @BindView(R.id.revealView)
    LinearLayout revealView;
    @BindView(R.id.gallery)
    RecyclerView gallery;
    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        appPermissions = new AppPermissions(this);

        placeHolderJ = new PlaceHolderJ(this, R.id.gallery, UGalleryApp.getPlaceHolderManager());
        placeHolderJ.init(R.id.view_loading, R.id.view_empty, R.id.view_error);
        placeHolderJ.showLoading();

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        iGalleryPresenter = new IGalleryPresenter(this);
        iGalleryPresenter.onCreate();
    }

    @Override
    public void onBackPressed() {
        if (isSearchViewDisplayed) {
            hideSearchView();
        } else {
            Navigator.with(this).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        iGalleryPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @OnClick(R.id.btnBack)
    public void onClick() {
        hideSearchView();
    }

    @Override
    public void configViews() {
        ((Button) placeHolderJ.viewEmpty.getRootView().findViewById(R.id.button_empty_try_again)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((Button) placeHolderJ.viewError.getRootView().findViewById(R.id.button_error_try_again)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_home), R.drawable.ic_home_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_favorite), R.drawable.ic_favorite_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_search), R.drawable.ic_search_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_settings), R.drawable.ic_settings_black_24dp));

        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_photo_camera_black_24dp);
        spaceNavigationView.setCentreButtonIconColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.black75PercentColor));
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setSpaceOnClickListener(this);

        if (appPermissions.hasPermission(STORAGE_PERMISSIONS)) {
            showEmptyView();
        } else {
            appPermissions.requestPermission(STORAGE_PERMISSIONS, STORAGE_PERMISSION_RC);
        }
    }

    @Override
    public void showSearchView() {
        Revealator.reveal(revealView)
                .withRevealDuration(DURATIONS_OF_ANIMATIONS)
                .withChildAnimationDuration(DURATIONS_OF_ANIMATIONS)
                .withTranslateDuration(DURATIONS_OF_ANIMATIONS)
                .withChildsAnimation()
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isSearchViewDisplayed = true;

                        searchView.requestFocus();

                        showKeyboard(searchView, getApplicationContext());
                    }
                })
                .start();
    }

    @Override
    public void hideSearchView() {
        Revealator.unreveal(revealView)
                .withUnrevealDuration(DURATIONS_OF_ANIMATIONS)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isSearchViewDisplayed = false;

                        searchView.getText().clear();

                        hideKeyboard(searchView, getApplicationContext());
                    }
                })
                .start();
    }

    @Override
    public void showKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showMessage(Integer message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        placeHolderJ.hideLoading();
        placeHolderJ.showEmpty(null);
    }

    @Override
    public void launchCamera() {
        Intent imageCapture = new Intent(Intent.ACTION_CAMERA_BUTTON);
        startActivity(imageCapture);
    }

    @Override
    public void onCentreButtonClick() {
        if (!appPermissions.hasPermission(Manifest.permission.CAMERA)) {
            appPermissions.requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_RC);
        } else {
            launchCamera();
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
                if (isSearchViewDisplayed) {
                    hideSearchView();
                } else {
                    showSearchView();
                }
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        switch (itemIndex) {
            case 0:

                break;
            case 1:

                break;
            case 2:
                if (isSearchViewDisplayed) {
                    hideSearchView();
                } else {
                    showSearchView();
                }
                break;
            case 3:

                break;
        }
    }
}
