package com.app.testSample.offer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.app.testSample.R;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.model.GetCouponsDetails;
import com.app.testSample.utility.BitmapLruCache;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.CouponDialog;
import com.app.testSample.utility.StringUtils;
import com.app.testSample.utility.ToastUtils;
import com.app.testSample.utility.Util;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GetCouponDetailsActivity extends BaseActivity {

    private NetworkImageView mImageViewOffer;
    private TextView mTextViewTitle, mTextViewDescription, mTextViewExpiryDate, mTextViewError, mTextViewCouponCode, mTextViewcouponDisclaimer;
    private TextView mTextViewLabelCouponCode;
    private List<GetCouponsDetails> mCouponsDetailsList;
    private CouponsByCategoriesView mCouponsByCategoryModel;
    private ImageLoader mImageLoader;
    private Button redeemOfferButton, mButtonCopyCode, mButtonStartShopping;
    private LinearLayout layoutShopVia;
    private LinearLayout layoutCouponCode;
    private RadioButton mRadioButtonShopViaWeb, mRadioButtonShopViaApp;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_detail_activity);
        mCouponsByCategoryModel = getIntent().getParcelableExtra("coupons_by_category_view_model");
        isChecked = false;
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        configureToolBar(Constants.SCREEN_TYPE_COUPON_DETAIL);

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);

        mImageViewOffer = (NetworkImageView)findViewById(R.id.image_view_offer);
        mTextViewTitle = (TextView)findViewById(R.id.text_view_title);
        mTextViewExpiryDate = (TextView) findViewById(R.id.text_view_expiry_date);
        mTextViewDescription = (TextView) findViewById(R.id.text_view_offer_description);
        mTextViewError = (TextView) findViewById(R.id.text_view_error);
        redeemOfferButton = (Button)findViewById(R.id.redeem_offer_button);
        mButtonStartShopping = (Button) findViewById(R.id.start_shopping_button);
        layoutCouponCode = (LinearLayout)findViewById(R.id.layout_coupon_code);
        layoutShopVia = (LinearLayout)findViewById(R.id.layout_shop_via);
        mTextViewCouponCode = (TextView) findViewById(R.id.text_view_dialog_coupon_code);
        mButtonCopyCode = (Button) findViewById(R.id.button_dialog_copy_code);
        mRadioButtonShopViaApp = (RadioButton) findViewById(R.id.radio_button_mobile_app);
        mRadioButtonShopViaWeb = (RadioButton) findViewById(R.id.radio_button_mobile_web);
        mTextViewcouponDisclaimer = (TextView) findViewById(R.id.coupon_disclaimer);
        mTextViewLabelCouponCode = (TextView) findViewById(R.id.text_view_label_coupon_code);

        String objectID = mCouponsByCategoryModel.getObjectID();
        if(ConnectivityUtils.isNetworkEnabled(this)){
            showProgressDialog(getString(R.string.progress_loading));
            new ApiRequestController(this,this).getData(Constants.REQUEST_GET_COUPONS_DETAILS, objectID);
        } else {
            ToastUtils.showToast(this, getString(R.string.error_internet_availability));
        }

        findViewById(R.id.image_view_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mCouponsByCategoryModel.getTitle() + "\n\n" + mCouponsByCategoryModel.getSocialURL()/*model.getProduct_url()*/);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        redeemOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCouponsDetailsList != null){
                    if(mCouponsDetailsList.size() > 0){
                        GetCouponsDetails model = mCouponsDetailsList.get(0);
                        layoutCouponCode.setVisibility(View.VISIBLE);
                        layoutShopVia.setVisibility(View.VISIBLE);
                        mButtonStartShopping.setVisibility(View.VISIBLE);
                        redeemOfferButton.setVisibility(View.GONE);
                        if(StringUtils.isNullOrEmpty(model.getCouponCode())) {
                            mTextViewCouponCode.setBackgroundResource(R.color.color_white);
                            mTextViewCouponCode.setText("Deal Activated");
                            mTextViewCouponCode.setTextColor(getResources().getColor(R.color.best_offer_title_color));
                            mTextViewcouponDisclaimer.setVisibility(View.GONE);
                            mButtonCopyCode.setVisibility(View.GONE);
                            mTextViewLabelCouponCode.setText("No Coupon Code Required");
                        } else {
                            mTextViewCouponCode.setText(model.getCouponCode());
                            mTextViewcouponDisclaimer.setVisibility(View.VISIBLE);
                        }

                          /*  Uri uri = Uri.parse(model.getCouponAffiliateUrl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);*/
//                        } else {
                            /*CouponDialog dialog = new CouponDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString("coupon_code", model.getCouponCode());
                            bundle.putString("title", mCouponsByCategoryModel.getTitle());
                            bundle.putString("affiliate_url", model.getCouponAffiliateUrl());
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(), CouponDialog.TAG);*/
//                        }
                        /*if(!mRadioButtonShopViaWeb.isChecked() && !mRadioButtonShopViaApp.isChecked()){
                            ToastUtils.showToast(GetCouponDetailsActivity.this, "Please select an option to shop");
                        }*/

                    }
                }
            }
        });

        mButtonStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCouponsDetails model = mCouponsDetailsList.get(0);
                if(!isChecked){
                    ToastUtils.showToast(GetCouponDetailsActivity.this, "Please select an option to shop");
                } else {
                    Uri uri = Uri.parse(model.getCouponAffiliateUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        mButtonCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipBoard();
            }
        });

        mRadioButtonShopViaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioButtonShopViaApp.setChecked(true);
                mRadioButtonShopViaWeb.setChecked(false);
                isChecked = true;
            }
        });

        mRadioButtonShopViaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioButtonShopViaWeb.setChecked(true);
                mRadioButtonShopViaApp.setChecked(false);
                isChecked = true;
            }
        });

    }


    @SuppressLint("NewApi")
    private void copyToClipBoard(){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(mTextViewCouponCode.getText().toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext()
                    .getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("", mTextViewCouponCode.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
        ToastUtils.showToast(GetCouponDetailsActivity.this, "Code copied to clipboard", Toast.LENGTH_LONG);
    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_COUPONS_DETAILS:{
                    mCouponsDetailsList = (ArrayList<GetCouponsDetails>)response.getResponseObject();
                    if(mCouponsDetailsList.size() > 0){
                        findViewById(R.id.layout_title).setVisibility(View.VISIBLE);
                        findViewById(R.id.layout_description).setVisibility(View.VISIBLE);
                        mTextViewError.setVisibility(View.GONE);
                        mTextViewTitle.setText(mCouponsByCategoryModel.getTitle());
                        mTextViewCouponCode.setText(mCouponsDetailsList.get(0).getCouponCode());
                        mTextViewDescription.setText(Html.fromHtml(mCouponsByCategoryModel.getDescription()));
                        mImageViewOffer.setImageUrl(mCouponsByCategoryModel.getImage_URL(), mImageLoader);
                        try {
                            long expiryDate = Util.getDateDifference(mCouponsDetailsList.get(0).getCouponExpiryDate());
                            if(expiryDate > 1){
                                mTextViewExpiryDate.setText("Expires in " + expiryDate + " days");
                            } else {
                                mTextViewExpiryDate.setText("Expires in " + expiryDate + " day");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(StringUtils.isNullOrEmpty(mCouponsDetailsList.get(0).getCouponCode())){
                            redeemOfferButton.setText("REDEEM OFFER");
                        } else {
                            redeemOfferButton.setText("SHOW COUPON");
                        }

                    } else {
                        mTextViewError.setVisibility(View.VISIBLE);
                        mTextViewError.setText(mCouponsDetailsList.get(0).getMessage());
                        findViewById(R.id.layout_title).setVisibility(View.GONE);
                        findViewById(R.id.layout_description).setVisibility(View.GONE);
                        layoutCouponCode.setVisibility(View.GONE);
                        layoutShopVia.setVisibility(View.GONE);
                    }
                    break;
                }
            }
        }
        removeProgressDialog();
    }
}
