package com.app.testSample.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.adapter.CategoriesAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CateGoryModel;
import com.app.testSample.offer.OfferDetailActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.Util;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for top categories
 */

public class TopCategoriesFragment extends BaseFragment {

    private ListView mListViewCategories;
    private List<CateGoryModel> mListCategoryModels;
    private TextView textViewError;
    private ProgressBar mProgressBar;
    private LinearLayout layoutProgress;

    private static TopCategoriesFragment topCategoriesFragment;

    /**
     * Create new instance of fragment
     * @return
     */
    public static TopCategoriesFragment newInstance(){
        if(topCategoriesFragment == null){
            topCategoriesFragment = new TopCategoriesFragment();
        }
        return topCategoriesFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.top_categories_fragment, container, false);
        mListViewCategories = (ListView)viewLayout.findViewById(R.id.list_view_categories);
        textViewError = (TextView) viewLayout.findViewById(R.id.text_view_error);
        mProgressBar = (ProgressBar)viewLayout.findViewById(R.id.progress_bar);
        layoutProgress = (LinearLayout) viewLayout.findViewById(R.id.layout_progress_bar);
        updateListView();
        mListViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                CateGoryModel model = mListCategoryModels.get(position);
                String [] data = new String[2];
                data[0] = model.getTermId();
                data[1] = model.getTermTaxonomyId();
                intent.putExtra("TermTaxId", data);
                intent.putExtra("screen_from", Constants.SCREEN_FROM_CATEGORY);
                startActivity(intent);
            }
        });
        return viewLayout;
    }

    /**
     * Method to update list-view
     */
    public void updateListView(){
        if(ConnectivityUtils.isNetworkEnabled(getActivity())){
            mProgressBar.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
            mListViewCategories.setVisibility(View.GONE);
            new ApiRequestController(getActivity(), this).getData(Constants.REQUEST_GET_TOP_20_CATEGORY, null);
        } else {
            mListViewCategories.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_TOP_20_CATEGORY:{
                    mListCategoryModels = (ArrayList<CateGoryModel>)response.getResponseObject();
                    if(mListCategoryModels.size() > 0) {
                        layoutProgress.setVisibility(View.GONE);
                        mListViewCategories.setVisibility(View.VISIBLE);
                        CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
                        adapter.setData(mListCategoryModels);
                        adapter.notifyDataSetChanged();
                        mListViewCategories.setAdapter(adapter);
                        ViewCompat.setNestedScrollingEnabled(mListViewCategories, true);
//                        Util.setListViewHeightBasedOnChildren(mListViewCategories);
                    } else {
                        mListViewCategories.setVisibility(View.GONE);
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(mListCategoryModels.get(0).getMessage());
                    }
                    break;
                }
            }
        }
        mProgressBar.setVisibility(View.GONE);
//        removeProgressDialog();
    }


}
