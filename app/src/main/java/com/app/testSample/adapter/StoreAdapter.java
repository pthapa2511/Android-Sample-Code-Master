package com.app.testSample.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.app.testSample.R;
import com.app.testSample.model.StoreModel;
import com.app.testSample.utility.BitmapLruCache;

import java.util.List;

/**
 * Adapter for Stores
 */

public class StoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<StoreModel> mStoreModelList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public StoreAdapter(Activity pActivity) {
        this.mContext = pActivity;
        mInflater = (LayoutInflater) pActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(pActivity), imageCache);
    }

    public void setData(List<StoreModel> pData) {
        this.mStoreModelList = pData;
    }

    @Override
    public int getCount() {
        return mStoreModelList == null ? 0 : mStoreModelList.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mStoreModelList.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_stores, null);
            convertView.setTag(holder);
            holder.mTextViewStoreName = (TextView) convertView.findViewById(R.id.text_view_store_name);
            holder.mTextViewCount = (TextView) convertView.findViewById(R.id.text_view_category_count);
            holder.imageViewStore = (NetworkImageView) convertView.findViewById(R.id.image_view_store);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            StoreModel model = mStoreModelList.get(position);
            holder.imageViewStore.setImageUrl(model.getIMAGE_URL(), mImageLoader);
            String categoryName = "";
            if (model.getStorename().contains("&amp;")) {
                categoryName = model.getStorename().replace("&amp;", "&");
            } else {
                categoryName = model.getStorename();
            }

            if (TextUtils.isEmpty(model.getCouponCount())) {
                holder.mTextViewCount.setText("0 Offer");
            } else if(model.getCouponCount().equals("1")){
                holder.mTextViewCount.setText("1 Offer");
            } else {
                holder.mTextViewCount.setText(model.getCouponCount() + " Offers");
            }

            holder.mTextViewStoreName.setText(categoryName);

        } catch (Exception ex) {
            Log.e("StoreAdapter", "getView() " + ex);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTextViewCount;
        TextView mTextViewStoreName;
        NetworkImageView imageViewStore;
    }
}
