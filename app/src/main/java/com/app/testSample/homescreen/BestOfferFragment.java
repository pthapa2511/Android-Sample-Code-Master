package com.app.testSample.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.adapter.BestOfferAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.offer.GetCouponDetailsActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.Util;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment fot Best Offers
 */

public class BestOfferFragment extends BaseFragment {

    private static BestOfferFragment bestOfferFragment;
    private List<CouponsByCategoriesView> mBestOfferModelList;
    private ListView mListViewBestOffers;
    private LinearLayout mLinearLayoutProgressBar;
    private ProgressBar mProgressBar;
    private TextView textViewError;
    private RecyclerView mRecyclerView;

    /**
     * Create new instance of fragment
     * @return
     */
    public static BestOfferFragment newInstance(){
        if(bestOfferFragment == null){
            bestOfferFragment = new BestOfferFragment();
        }
        return bestOfferFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewLayout = inflater.inflate(R.layout.best_offer_fragment, null);
        mListViewBestOffers = (ListView)viewLayout.findViewById(R.id.list_view_best_offer);
        textViewError = (TextView) viewLayout.findViewById(R.id.text_view_error);
        mProgressBar = (ProgressBar)viewLayout.findViewById(R.id.progress_bar);
        mLinearLayoutProgressBar = (LinearLayout) viewLayout.findViewById(R.id.layout_progress_bar);
//        mRecyclerView = (RecyclerView) viewLayout.findViewById(R.id.recycler_view);
        updateListView();

        mListViewBestOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mBestOfferModelList != null){
                    if(mBestOfferModelList.size() > 0){
                        CouponsByCategoriesView model = mBestOfferModelList.get(position);
                        Intent intent = new Intent(getActivity(), GetCouponDetailsActivity.class);
                        intent.putExtra("coupons_by_category_view_model", model);
                        startActivity(intent);
                    }
                }
            }
        });

        return viewLayout;
    }


    private void updateListView() {
        if(ConnectivityUtils.isNetworkEnabled(getActivity())){
            mProgressBar.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
            mListViewBestOffers.setVisibility(View.GONE);
            new ApiRequestController(getActivity(), this).getData(Constants.REQUEST_GET_BEST_OFFER, null);
        } else {
            mListViewBestOffers.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_BEST_OFFER:{
                    mBestOfferModelList = (ArrayList<CouponsByCategoriesView>)response.getResponseObject();
                    if(mBestOfferModelList.size() > 0) {
                        mLinearLayoutProgressBar.setVisibility(View.GONE);
                        mListViewBestOffers.setVisibility(View.VISIBLE);
                        BestOfferAdapter adapter = new BestOfferAdapter(getActivity());
                        adapter.setData(mBestOfferModelList);
                        adapter.notifyDataSetChanged();
                        mListViewBestOffers.setAdapter(adapter);
//                        Util.setListViewHeightBasedOnChildren(mListViewBestOffers);
                        ViewCompat.setNestedScrollingEnabled(mListViewBestOffers, true);
                    } else {
                        mListViewBestOffers.setVisibility(View.GONE);
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(getString(R.string.error_no_data));
                    }
                    break;
                }
            }
        } else {
            mListViewBestOffers.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(getString(R.string.error_no_data));
        }
        mProgressBar.setVisibility(View.GONE);
    }
}
