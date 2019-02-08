package com.app.testSample.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.adapter.CouponOfferAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.offer.GetCouponDetailsActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.ToastUtils;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchCoupons extends BaseActivity {

    private ListView mListViewSearchCoupons;
    private TextView mTextViewError;
    private LinearLayout layout_error;
    private EditText editText;
    private List<CouponsByCategoriesView> mSearchResultList;
    private ImageView mImageViewSearch;
    private boolean isTextAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_coupons);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        configureToolBar(Constants.SCREEN_TYPE_SEARCH_COUPONS);

        mListViewSearchCoupons = (ListView) findViewById(R.id.list_view_search_coupons);
        mTextViewError = (TextView) findViewById(R.id.text_view_error);
        editText = (EditText) findViewById(R.id.edit_text_search_coupon);
        mImageViewSearch = (ImageView) findViewById(R.id.image_view_search);
        layout_error = (LinearLayout) findViewById(R.id.layout_error);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = editText.getEditableText().toString();
                    if(query.length() > 0) {
                        performSearch(query);
                        mImageViewSearch.setImageResource(R.mipmap.ic_clear);
                        isTextAvailable = true;
                    } else {
                        isTextAvailable = false;
                        mImageViewSearch.setImageResource(R.mipmap.ic_search);
                        ToastUtils.showToast(SearchCoupons.this, "Please enter a keyword to search");
                    }
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText.getEditableText().toString();
                if(query.length() > 0) {
                    performSearch(query);
                    mImageViewSearch.setImageResource(R.mipmap.ic_clear);
                    isTextAvailable = true;
                } else {
                    isTextAvailable = false;
                    mImageViewSearch.setImageResource(R.mipmap.ic_search);
                    ToastUtils.showToast(SearchCoupons.this, "Please enter a keyword to search");
                }
            }
        });

        editText.addTextChangedListener(textWatcher);

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTextAvailable){
                    editText.setText("");
                }
            }
        });

        mListViewSearchCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mSearchResultList != null){
                    if(mSearchResultList.size() > 0){
                        CouponsByCategoriesView model = mSearchResultList.get(position);
                        Intent intent = new Intent(SearchCoupons.this, GetCouponDetailsActivity.class);
                        intent.putExtra("coupons_by_category_view_model", model);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    /**
     * Method to perform search
     * @param s
     */
    private void performSearch(String s) {
        if(s.length() > 0){
            if(ConnectivityUtils.isNetworkEnabled(this)){
                //Enable below to show progressbar
                showProgressDialog(getString(R.string.progress_loading));
                new ApiRequestController(this,this).getData(Constants.REQUEST_GET_SEARCH_COUPONS, s);
            } else {
                ToastUtils.showToast(this, getString(R.string.error_internet_availability));
            }
        }
    }


    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(s.length() > 0){
                isTextAvailable = true;
                mImageViewSearch.setImageResource(R.mipmap.ic_clear);
            }else{
                isTextAvailable = false;
                mImageViewSearch.setImageResource(R.mipmap.ic_search);
            }
        }
    };


    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_SEARCH_COUPONS:{
                    mSearchResultList = (ArrayList<CouponsByCategoriesView>)response.getResponseObject();
                    if(mSearchResultList.size() > 0) {
                        CouponOfferAdapter adapter = new CouponOfferAdapter(this);
                        adapter.setData(mSearchResultList);
                        mListViewSearchCoupons.setAdapter(adapter);
                        mTextViewError.setVisibility(View.GONE);
                        mListViewSearchCoupons.setVisibility(View.VISIBLE);
                        layout_error.setVisibility(View.GONE);
                        hideVirtualKeyboard();
                    } else {
                        layout_error.setVisibility(View.VISIBLE);
                        mTextViewError.setVisibility(View.VISIBLE);
                        mListViewSearchCoupons.setVisibility(View.GONE);
                        mTextViewError.setText("Currently there are no coupons related yo your search.");
                    }
                    break;
                }
            }
        } else {
            mTextViewError.setVisibility(View.VISIBLE);
            mListViewSearchCoupons.setVisibility(View.GONE);
            mTextViewError.setText(mSearchResultList.get(0).getMessage());
        }
        removeProgressDialog();
    }
}
