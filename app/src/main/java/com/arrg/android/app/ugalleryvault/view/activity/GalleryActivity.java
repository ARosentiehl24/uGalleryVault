package com.arrg.android.app.ugalleryvault.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.interfaces.GalleryView;
import com.arrg.android.app.ugalleryvault.model.service.UriObserver;
import com.arrg.android.app.ugalleryvault.presenter.IGalleryPresenter;
import com.arrg.android.app.ugalleryvault.view.fragment.GalleryFragment;
import com.jaouan.revealator.Revealator;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arrg.android.app.ugalleryvault.UGalleryApp.DURATIONS_OF_ANIMATIONS;

public class GalleryActivity extends AppCompatActivity implements GalleryView, SpaceOnClickListener {

    private Boolean isModePrivateEnabled = false;
    private Handler handler;
    private IGalleryPresenter iGalleryPresenter;
    private List<ResolveInfo> infoList;
    private UriObserver uriObserver;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    AppCompatEditText searchView;
    @BindView(R.id.revealView)
    LinearLayout revealView;
    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        handler = new Handler();
        uriObserver = new UriObserver(handler, this);

        this.getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, uriObserver);

        iGalleryPresenter = new IGalleryPresenter(this);
        iGalleryPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.getContentResolver().unregisterContentObserver(uriObserver);
    }

    @Override
    public void onBackPressed() {
        iGalleryPresenter.onBackPressed();
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
        Navigator.with(this)
                .build()
                .goTo(new GalleryFragment(), R.id.container)
                .tag(GalleryFragment.class.getSimpleName())
                .replace()
                .commit();

        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_home), R.drawable.ic_home_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_favorite), R.drawable.ic_favorite_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_search), R.drawable.ic_search_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_edit), R.drawable.ic_edit_black_24dp));

        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_photo_camera_black_24dp);
        spaceNavigationView.setCentreButtonIconColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.black75PercentColor));
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setSpaceOnClickListener(this);
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
    public void launchCamera() {
        String defaultCamera = PreferencesManager.getString(getString(R.string.default_camera));

        if (defaultCamera.length() != 0) {
            launchPackage(defaultCamera);
        } else {
            BottomSheet.Builder cameraSelector = new BottomSheet.Builder(this);

            Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Integer id = 0;

            infoList = getPackageManager().queryIntentActivities(imageCapture, 0);

            for (ResolveInfo resolveInfo : infoList) {
                CharSequence name = resolveInfo.loadLabel(getPackageManager());
                Drawable icon = resolveInfo.loadIcon(getPackageManager());

                cameraSelector.addMenuItem(new BottomSheetMenuItem(this, id, name, icon));

                id++;
            }

            cameraSelector.setTitle(R.string.select_camera_title);

            if (infoList.size() > getResources().getInteger(R.integer.default_bottom_sheet_grid)) {
                cameraSelector.setColumnCountResource(R.integer.default_bottom_sheet_grid);
            } else {
                cameraSelector.setColumnCount(infoList.size());
            }

            cameraSelector.grid().setListener(new BottomSheetListener() {
                @Override
                public void onSheetShown(@NonNull BottomSheet bottomSheet) {

                }

                @Override
                public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                    ResolveInfo resolveInfo = infoList.get(menuItem.getItemId());

                    launchPackage(resolveInfo.activityInfo.packageName);

                    PreferencesManager.putString(getString(R.string.default_camera), resolveInfo.activityInfo.packageName);
                }

                @Override
                public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

                }
            }).create();

            if (infoList.size() == 1) {
                launchPackage(infoList.get(0).activityInfo.packageName);
            } else {
                cameraSelector.show();
            }
        }
    }

    @Override
    public void launchPackage(String packageName) {
        Intent launchPackage = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(launchPackage);
    }

    @Override
    public AppCompatActivity getContext() {
        return this;
    }

    @Override
    public void onCentreButtonClick() {
        iGalleryPresenter.onCentreButtonClick();
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        iGalleryPresenter.onItemClick(itemIndex, itemName);
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        iGalleryPresenter.onItemClick(itemIndex, itemName);
    }
}
