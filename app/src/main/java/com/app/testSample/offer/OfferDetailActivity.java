package com.app.testSample.offer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.app.testSample.DialogDescription;
import com.app.testSample.R;
import com.app.testSample.adapter.CouponOfferAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.model.GetCouponByCategoryModel;
import com.app.testSample.model.GetCouponsByStoresResultModel;
import com.app.testSample.model.GetStoreDetails;
import com.app.testSample.utility.BitmapLruCache;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.ToastUtils;
import com.app.testSample.utility.Util;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OfferDetailActivity extends BaseActivity {

    private TextView mTextViewTitle, mTextViewDescription, mTextViewOfferCount, mTextViewReadMore;
    private NetworkImageView mImageViewOffer;
    private List<GetCouponsByStoresResultModel> getCouponsByStoresResultModelList;
    private List<GetCouponByCategoryModel> mCouponsByCategoryModelList;
    private List<CouponsByCategoriesView> mCouponsByCategoriesViewList;
    private List<GetStoreDetails> mGetStoreDetailsList;
    private ImageLoader mImageLoader;
    private ListView mListViewOfferDetails;
    private int mScreenFrom;
    //    private ScrollView mScrollView;
    public static boolean isViewCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_screen);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        configureToolBar(Constants.SCREEN_TYPE_OFFER_DETAIL);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerView = inflater.inflate(R.layout.offer_list_header_view, null);

        mTextViewTitle = (TextView) headerView.findViewById(R.id.text_view_store_name);
        mTextViewDescription = (TextView) headerView.findViewById(R.id.text_view_store_description);
//        mTextViewExpiresOn = (TextView) findViewById(R.id.text_view_expiry_date);
        mImageViewOffer = (NetworkImageView) headerView.findViewById(R.id.image_view_offer_detail);
        mTextViewOfferCount = (TextView) headerView.findViewById(R.id.text_view_offer_count);
        mTextViewReadMore = (TextView) headerView.findViewById(R.id.text_view_read_more);
        mListViewOfferDetails = (ListView) findViewById(R.id.list_view_coupons);
//        mScrollView = (ScrollView)findViewById(R.id.scrollView);
        mListViewOfferDetails.addHeaderView(headerView, null, false);
        mScreenFrom = getIntent().getIntExtra("screen_from", 0);
        String[] data = getIntent().getStringArrayExtra("TermTaxId");

        if (ConnectivityUtils.isNetworkEnabled(this)) {
            showProgressDialog(getString(R.string.progress_loading));
            if (mScreenFrom == Constants.SCREEN_FROM_STORE) {
                new ApiRequestController(this, this).getData(Constants.REQUEST_GET_COUPONS_BY_STORES, data);
            } else if (mScreenFrom == Constants.SCREEN_FROM_CATEGORY) {
                new ApiRequestController(this, this).getData(Constants.REQUEST_GET_COUPONS_BY_CATEGORIES, data);
            }
        } else {
            ToastUtils.showToast(this, getString(R.string.error_internet_availability));
        }

        mListViewOfferDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCouponsByCategoriesViewList != null) {
                    if (mCouponsByCategoriesViewList.size() > 0) {
                        CouponsByCategoriesView model = mCouponsByCategoriesViewList.get(position - 1);
                        Intent intent = new Intent(OfferDetailActivity.this, GetCouponDetailsActivity.class);
                        intent.putExtra("coupons_by_category_view_model", model);
                        startActivity(intent);
                    }
                }
            }
        });


    }

    @Override
    protected void updateUi(Response response) {

        if (response.isSuccess()) {
            switch (response.getDataType()) {
                case Constants.REQUEST_GET_COUPONS_BY_STORES: {

                    getCouponsByStoresResultModelList = (ArrayList<GetCouponsByStoresResultModel>) response.getResponseObject();
                    mCouponsByCategoriesViewList = getCouponsByStoresResultModelList.get(0).getCouponsByCategoriesViewList();
                    mGetStoreDetailsList = getCouponsByStoresResultModelList.get(0).getGetStoreDetailsList();
                    if (mGetStoreDetailsList.size() > 0) {

                        final String description = mGetStoreDetailsList.get(0).getDescription();
                        CouponOfferAdapter adapter = new CouponOfferAdapter(this);
                        adapter.setData(mCouponsByCategoriesViewList);
                        mListViewOfferDetails.setAdapter(adapter);
                        if (mCouponsByCategoriesViewList.size() == 0) {
                            LayoutInflater inflater = LayoutInflater.from(this);
                            View footerView = inflater.inflate(R.layout.error_layout, null);
                            TextView errorText = (TextView) footerView.findViewById(R.id.text_view_error);
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("We are finding the best offer in this category, Visit us back!");
                            mListViewOfferDetails.addFooterView(footerView, null, false);
                        }
                        mListViewOfferDetails.setVisibility(View.VISIBLE);
                        mTextViewDescription.setText(Html.fromHtml(description)/*description*/);
                        String title = mGetStoreDetailsList.get(0).getName();
                        if (title.contains("&amp;")) {
                            title = title.replace("&amp;", "&");
                        }
                        mTextViewTitle.setText(title);
                        mImageViewOffer.setImageUrl(mGetStoreDetailsList.get(0).getImage_URL(), mImageLoader);
                        mTextViewOfferCount.setText("Offers " + mGetStoreDetailsList.get(0).getCouponCount());
                        mTextViewReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogDescription dialogDescription = new DialogDescription();
                                Bundle bundle = new Bundle();
                                bundle.putString("description", description);
                                dialogDescription.setArguments(bundle);
                                dialogDescription.show(getSupportFragmentManager(), DialogDescription.TAG);
                            }
                        });
                    } else {
                        findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                        mListViewOfferDetails.setVisibility(View.GONE);
                        findViewById(R.id.text_view_error).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.text_view_error)).setText(getCouponsByStoresResultModelList.get(0).getMessage());
                    }
                    break;
                }

                case Constants.REQUEST_GET_COUPONS_BY_CATEGORIES: {
                    mCouponsByCategoryModelList = (ArrayList<GetCouponByCategoryModel>) response.getResponseObject();
                    mCouponsByCategoriesViewList = mCouponsByCategoryModelList.get(0).getCouponsByCategoriesViewList();
                    if (mCouponsByCategoryModelList.size() > 0) {
                        String name = mCouponsByCategoryModelList.get(0).getCategoriesName();
                        if (name.contains("&amp;")) {
                            name = name.replace("&amp;", "&");
                        }
                        mTextViewTitle.setText(name);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.BELOW, R.id.text_view_store_name);
                        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.text_view_store_name);
                        mTextViewDescription.setLayoutParams(layoutParams);
                        mTextViewDescription.setText(Html.fromHtml(mCouponsByCategoryModelList.get(0).getCategoriesDescription()));
                        mTextViewReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogDescription dialogDescription = new DialogDescription();
                                Bundle bundle = new Bundle();
                                bundle.putString("description", mCouponsByCategoryModelList.get(0).getCategoriesDescription());
                                dialogDescription.setArguments(bundle);
                                dialogDescription.show(getSupportFragmentManager(), DialogDescription.TAG);
                            }
                        });
                        mTextViewOfferCount.setText("Offers " + mCouponsByCategoryModelList.get(0).getCouponsCount());
                        mImageViewOffer.setVisibility(View.GONE);
                        CouponOfferAdapter adapter = new CouponOfferAdapter(this);
                        adapter.setData(mCouponsByCategoriesViewList);
                        mListViewOfferDetails.setAdapter(adapter);
                        if (mCouponsByCategoriesViewList.size() == 0) {
                            LayoutInflater inflater = LayoutInflater.from(this);
                            View footerView = inflater.inflate(R.layout.error_layout, null);
                            TextView errorText = (TextView) footerView.findViewById(R.id.text_view_error);
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("We are finding the best offer in this category, Visit us back!");
                            mListViewOfferDetails.addFooterView(footerView, null, false);
                        }
                    } else {
                        findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                        findViewById(R.id.text_view_error).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.text_view_error)).setText(getCouponsByStoresResultModelList.get(0).getMessage());
                    }

                    break;
                }


            }

        }

        removeProgressDialog();
    }
}
