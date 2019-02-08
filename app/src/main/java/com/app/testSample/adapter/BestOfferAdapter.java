package com.app.testSample.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.app.testSample.model.BestOfferModel;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.utility.BitmapLruCache;
import com.app.testSample.utility.Util;

import java.util.List;

/**
 * Adapter for best offers
 */

public class BestOfferAdapter extends BaseAdapter {
    private Context mContext;
    private List<CouponsByCategoriesView> bestOfferModelList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public BestOfferAdapter(Activity pActivity) {
        this.mContext = pActivity;
        mInflater = (LayoutInflater) pActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(pActivity), imageCache);
    }

    public void setData(List<CouponsByCategoriesView> pData) {
        this.bestOfferModelList = pData;
    }

    @Override
    public int getCount() {
        return bestOfferModelList == null ? 0 : bestOfferModelList.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return bestOfferModelList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_best_offer, null);
            convertView.setTag(holder);
            holder.mTextViewOfferTitle = (TextView) convertView.findViewById(R.id.text_view_offer_title);
            holder.mTextExpiryDate = (TextView) convertView.findViewById(R.id.text_view_expiry_date);
            holder.mTextViewOfferDescription = (TextView) convertView.findViewById(R.id.text_view_offer_description);
            holder.imageViewOffer = (NetworkImageView) convertView.findViewById(R.id.image_view_best_offer);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            CouponsByCategoriesView model = bestOfferModelList.get(position);
            holder.imageViewOffer.setImageUrl(model.getImage_URL(), mImageLoader);
            String title = "";
            if (model.getTitle().contains("&amp;")) {
                title = model.getTitle().replace("&amp;", "&");
            } else {
                title = model.getTitle();
            }
            holder.mTextViewOfferTitle.setText(title);
            holder.mTextViewOfferDescription.setText(model.getDescription());
            long dateDifference = Util.getDateDifference(model.getExpiryDate());
            if (dateDifference > 1) {
                holder.mTextExpiryDate.setText("Expires in " + dateDifference + " days");
            } else {
                holder.mTextExpiryDate.setText("Expires in " + dateDifference + " day");
            }
        } catch (Exception ex) {
            Log.e("BestOfferAdapter", "getView() " + ex);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTextExpiryDate;
        TextView mTextViewOfferTitle;
        TextView mTextViewOfferDescription;
        NetworkImageView imageViewOffer;
    }


}
