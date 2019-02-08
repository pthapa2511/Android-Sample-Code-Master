package com.app.testSample.utility.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.app.testSample.R;
import com.app.testSample.constants.Constants;
import com.app.testSample.utility.network.Response;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is used as base-class for application-base-activity.
 */
public abstract class BaseActivity extends AppCompatActivity implements IScreen {
    public int _ScreenHeight;
    public int _ScreenWidth;
    public Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenHeightAndWidth();

        Log.i(getClass().getSimpleName(), "onCreate()");
    }

    /**
     * Method is used to calculate
     * screen width and height of device
     */
    private void getScreenHeightAndWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        _ScreenHeight = dm.heightPixels;
        _ScreenWidth = dm.widthPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(getClass().getSimpleName(), "onResume()");
        // Logs 'install' and 'app activate' App Events.

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(getClass().getSimpleName(), "onNewIntent()");
    }

    /**
     * this method should be called only from UI thread.
     *
     * @param response
     */
    @Override
    public final void handleUiUpdate(final Response response) {
        if (isFinishing()) {
            return;
        }
        try {
            updateUi(response);
        } catch (Exception e) {
            Log.i(getClass().getSimpleName(), "updateUi()", e);
        }
    }

    /**
     * Subclass should over-ride this method to update the UI with response
     *
     * @param response
     */
    protected abstract void updateUi(Response response);

    // ////////////////////////////// show and hide ProgressDialog

    public ProgressDialog mProgressDialog;

    /**
     * Method to show progress dialog
     *
     * @param bodyText
     */
    public void showProgressDialog(String bodyText) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_bar));
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true; //
                    }
                    return false;
                }
            });
        }

        mProgressDialog.setMessage(bodyText);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Method to remove progress dialog
     */
    public void removeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // ////////////////////////////// show and hide key-board

    protected void showVirturalKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 100);
    }

    public void hideVirtualKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method common for all to configure tool bar
     *
     * @param screenType
     */
    public void configureToolBar(int screenType) {
        setSupportActionBar(mToolBar);
        switch (screenType) {
            case Constants.SCREEN_TYPE_ALL_CATEGORIES: {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_category));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }
            case Constants.SCREEN_TYPE_ALL_STORES: {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_stores));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }
            case Constants.SCREEN_TYPE_COUPON_DETAIL:{
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_offer_details));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }
            case Constants.SCREEN_TYPE_OFFER_DETAIL:
            case Constants.SCREEN_TYPE_GET_COUPONS_BY_CATEGORY: {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_offers));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }

            case Constants.SCREEN_TYPE_SEARCH_COUPONS:{
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_search_coupons));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }

            case Constants.SCREEN_TYPE_BANK_OFFER:{
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_bank_offer));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }

            case Constants.REQUEST_HELP_SUPPORT:{
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.label_help_and_support));
                mToolBar.setNavigationIcon(R.mipmap.ic_back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }
        }
    }
}