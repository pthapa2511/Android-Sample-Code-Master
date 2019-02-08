package com.app.testSample.store;

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
import com.app.testSample.adapter.StoreAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.StoreModel;
import com.app.testSample.offer.OfferDetailActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.Util;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class TopStoresFragment extends BaseFragment {

    private static TopStoresFragment topStoresFragment;

    private ListView mListViewAllStores;
    private List<StoreModel> mListAllStores;
    private TextView textViewError;
    private ProgressBar mProgressBar;
    private LinearLayout layoutProgress;
    private int itemCount = 0;

    /**
     * Create new instance of fragment
     * @return
     */
    public static TopStoresFragment newInstance(){
        if(topStoresFragment == null){
            topStoresFragment = new TopStoresFragment();
        }
        return topStoresFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO change layout here
        View viewLayout = inflater.inflate(R.layout.top_stores_fragment, null);
        mListViewAllStores = (ListView)viewLayout.findViewById(R.id.list_view_stores);
        textViewError = (TextView) viewLayout.findViewById(R.id.text_view_error);
        mProgressBar = (ProgressBar)viewLayout.findViewById(R.id.progress_bar);
        layoutProgress = (LinearLayout) viewLayout.findViewById(R.id.layout_progress_bar);
        updateListView();
        mListViewAllStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                StoreModel model = mListAllStores.get(position);
                String [] data = new String[2];
                data[0] = model.getTermID();
                data[1] = model.getTermTaxonomyID();
                intent.putExtra("TermTaxId", data);
                intent.putExtra("screen_from", Constants.SCREEN_FROM_STORE);
                startActivity(intent);
            }
        });

       /* mListViewAllStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCount += 1;
                if(itemCount > 1){
                    Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                    StoreModel model = mListAllStores.get(position);
                    String [] data = new String[2];
                    data[0] = model.getTermID();
                    data[1] = model.getTermTaxonomyID();
                    intent.putExtra("TermTaxId", data);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        return viewLayout;
    }


    private void updateListView() {
        if(ConnectivityUtils.isNetworkEnabled(getActivity())){
            mProgressBar.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
            mListViewAllStores.setVisibility(View.GONE);
            new ApiRequestController(getActivity(), this).getData(Constants.REQUEST_GET_TOP_20_STORE, null);
        } else {
            mListViewAllStores.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_TOP_20_STORE:{
                    mListAllStores = (ArrayList<StoreModel>)response.getResponseObject();
                    if(mListAllStores.size() > 0){
                        mListViewAllStores.setVisibility(View.VISIBLE);
                        textViewError.setVisibility(View.GONE);
                        StoreAdapter adapter = new StoreAdapter(getActivity());
                        adapter.setData(mListAllStores);
                        mListViewAllStores.setAdapter(adapter);
//                        Util.setListViewHeightBasedOnChildren(mListViewAllStores);
                        ViewCompat.setNestedScrollingEnabled(mListViewAllStores, true);
                    } else {
                        mListViewAllStores.setVisibility(View.GONE);
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(mListAllStores.get(0).getMessage());
                    }
                    break;
                }
            }
        } else {
            mListViewAllStores.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }
        layoutProgress.setVisibility(View.GONE);
    }
}
