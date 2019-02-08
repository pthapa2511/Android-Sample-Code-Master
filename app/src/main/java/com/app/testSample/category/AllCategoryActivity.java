package com.app.testSample.category;

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
import com.app.testSample.adapter.CategoriesAdapter;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.CateGoryModel;
import com.app.testSample.model.StoreModel;
import com.app.testSample.offer.OfferDetailActivity;
import com.app.testSample.store.GetAllStoreActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Activity for All categories
 */

public class AllCategoryActivity extends BaseActivity {
    private ListView mListViewAllCategories;
    private List<CateGoryModel> mCateGoryModelList;
    private List<CateGoryModel> mTempList;
    private TextView mTextViewNoData;
    private CategoriesAdapter categoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_categories_activity);
        mListViewAllCategories = (ListView) findViewById(R.id.list_view_categories);
        mTextViewNoData = (TextView)findViewById(R.id.text_view_error);

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        configureToolBar(Constants.SCREEN_TYPE_ALL_CATEGORIES);

        if(ConnectivityUtils.isNetworkEnabled(this)){
            showProgressDialog(getString(R.string.progress_loading));
            new ApiRequestController(this, this).getData(Constants.REQUEST_GET_ALL_CATEGORY, null);
        }


        mListViewAllCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllCategoryActivity.this, OfferDetailActivity.class);
                CateGoryModel model = (CateGoryModel) mListViewAllCategories.getAdapter().getItem(position);//mCateGoryModelList.get(position);
                String [] data = new String[2];
                data[0] = model.getTermId();
                data[1] = model.getTermTaxonomyId();
                intent.putExtra("TermTaxId", data);
                intent.putExtra("screen_from", Constants.SCREEN_FROM_CATEGORY);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_GET_ALL_CATEGORY:{
                    mCateGoryModelList = (ArrayList<CateGoryModel>)response.getResponseObject();
                    if(mCateGoryModelList.size() > 0) {
                        Collections.sort(mCateGoryModelList, CateGoryModel.COMPARE_BY_CATEGORY_NAME);
                        categoriesAdapter = new CategoriesAdapter(this);
                        categoriesAdapter.setData(mCateGoryModelList);
                        mListViewAllCategories.setAdapter(categoriesAdapter);
                        findViewById(R.id.layout_error).setVisibility(View.GONE);
                        mTextViewNoData.setVisibility(View.GONE);
                        mListViewAllCategories.setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                        mTextViewNoData.setVisibility(View.VISIBLE);
                        mTextViewNoData.setText(getString(R.string.error_no_data));
                        mListViewAllCategories.setVisibility(View.GONE);
                    }
                    break;
                }
            }
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
                } else {
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
        if(mCateGoryModelList != null){
            if(mTextViewNoData.getVisibility() == View.VISIBLE){
                findViewById(R.id.layout_error).setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.GONE);
                mListViewAllCategories.setVisibility(View.VISIBLE);
            }
            if(mCateGoryModelList.size() > 0){
                categoriesAdapter.setData(mCateGoryModelList);
                categoriesAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Method to perform searh
     * @param query
     */
    private void performSearch(String query){
        boolean isSearchSuccess = false;
        List<CateGoryModel> tempStoreList = new ArrayList<>();
        if(mCateGoryModelList != null){
            if(mCateGoryModelList.size() > 0){
                for(CateGoryModel model : mCateGoryModelList){
                    if((model.getCategoryName().toLowerCase()).contains(query.toLowerCase())){
                        isSearchSuccess = true;
                        tempStoreList.add(model);
                    }
                }
                if(isSearchSuccess) {
                    categoriesAdapter.setData(tempStoreList);
                    categoriesAdapter.notifyDataSetChanged();
                    mListViewAllCategories.setVisibility(View.VISIBLE);
                    mTextViewNoData.setVisibility(View.GONE);
                    findViewById(R.id.layout_error).setVisibility(View.GONE);
                } else {
                    mListViewAllCategories.setVisibility(View.GONE);
                    mTextViewNoData.setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                    mTextViewNoData.setText("Currently there are no items related to your search.");
                }
            }
        }
    }
}
