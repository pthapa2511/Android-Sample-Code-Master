package com.app.testSample.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.adapter.StoreAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CateGoryModel;
import com.app.testSample.model.StoreModel;
import com.app.testSample.offer.OfferDetailActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllStoreActivity extends BaseActivity {

    private ListView mListViewAllStores;
    private List<StoreModel> mListAllStores;
    private TextView textViewError;
    private int itemCount = 0;
    private StoreAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_store_activity);
        mListViewAllStores = (ListView)findViewById(R.id.list_view_stores);
        textViewError = (TextView)findViewById(R.id.text_view_error);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        configureToolBar(Constants.SCREEN_TYPE_ALL_STORES);

        if(ConnectivityUtils.isNetworkEnabled(this)){
            showProgressDialog(getString(R.string.progress_loading));
            new ApiRequestController(this,this).getData(Constants.REQUEST_GET_ALL_STORE, null);
        } else {
            findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.VISIBLE);
            mListViewAllStores.setVisibility(View.GONE);
        }

        mListViewAllStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GetAllStoreActivity.this, OfferDetailActivity.class);
                StoreModel model = (StoreModel) mListViewAllStores.getAdapter().getItem(position);//mListAllStores.get(position);
                String [] data = new String[2];
                data[0] = model.getTermID();
                data[1] = model.getTermTaxonomyID();
                intent.putExtra("TermTaxId", data);
                intent.putExtra("screen_from", Constants.SCREEN_FROM_STORE);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_ALL_STORE:{
                    mListAllStores = (ArrayList<StoreModel>)response.getResponseObject();
                    if(mListAllStores.size() > 0){
                        Collections.sort(mListAllStores, StoreModel.COMPARE_BY_STORE_NAME);
                        mListViewAllStores.setVisibility(View.VISIBLE);
                        textViewError.setVisibility(View.GONE);
                        storeAdapter = new StoreAdapter(this);
                        storeAdapter.setData(mListAllStores);
                        mListViewAllStores.setAdapter(storeAdapter);
                        findViewById(R.id.layout_error).setVisibility(View.GONE);
                    } else {
                        mListViewAllStores.setVisibility(View.GONE);
                        textViewError.setVisibility(View.VISIBLE);
                        findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }
        } else {
            mListViewAllStores.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
        }
        removeProgressDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SearchView searchView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchViewMenuItem.getActionView();
        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        v.setImageResource(R.mipmap.ic_search); //Changing the image

        EditText editText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.WHITE);

       /* if (!searchFor.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(searchFor, false);
        }*/

        searchView.setQueryHint("Search Store");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    searchView.clearFocus();
                    clearSearch();
                } else{
                  performSearch(newText);
                }
                return false;
            }
        });
        return true;
    }

    /**
     * Method to clear all search
     */
    private void clearSearch() {
        if(mListAllStores != null){
            if(textViewError.getVisibility() == View.VISIBLE){
                textViewError.setVisibility(View.GONE);
                mListViewAllStores.setVisibility(View.VISIBLE);
                findViewById(R.id.layout_error).setVisibility(View.GONE);
            }
            if(mListAllStores.size() > 0){
                storeAdapter.setData(mListAllStores);
                storeAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Method to perform searh
     * @param query
     */
    private void performSearch(String query){
        boolean isSearchSuccess = false;
        List<StoreModel> tempStoreList = new ArrayList<>();
        if(mListAllStores != null){
            if(mListAllStores.size() > 0){
                for(StoreModel model : mListAllStores){
                    if((model.getStorename().toLowerCase()).contains(query.toLowerCase())){
                        isSearchSuccess = true;
                        tempStoreList.add(model);
                    }
                }
                if(isSearchSuccess) {
                    storeAdapter.setData(tempStoreList);
                    storeAdapter.notifyDataSetChanged();
                    mListViewAllStores.setVisibility(View.VISIBLE);
                    textViewError.setVisibility(View.GONE);
                    findViewById(R.id.layout_error).setVisibility(View.GONE);
                } else {
                    mListViewAllStores.setVisibility(View.GONE);
                    textViewError.setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                    textViewError.setText("Currently there are no items related to your search.");
                }
            }
        }
    }
}
