package com.app.testSample.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.app.testSample.R;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.utility.BitmapLruCache;
import com.app.testSample.utility.Util;

import java.util.List;

/**
 * Adapter for Coupons offer
 */

public class CouponOfferAdapter extends BaseAdapter {
    private Context mContext;
    private List<CouponsByCategoriesView> couponsByCategoriesViewList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public CouponOfferAdapter(Activity pActivity) {
        this.mContext = pActivity;
        mInflater = (LayoutInflater) pActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(pActivity), imageCache);
    }

    public void setData(List<CouponsByCategoriesView> pData) {
        this.couponsByCategoriesViewList = pData;
    }

    @Override
    public int getCount() {
        return couponsByCategoriesViewList == null ? 0 : couponsByCategoriesViewList.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return couponsByCategoriesViewList.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_offer_detail, null);
            convertView.setTag(holder);
            holder.mTextViewOfferTitle = (TextView) convertView.findViewById(R.id.text_view_title);
            holder.mTextExpiryDate = (TextView) convertView.findViewById(R.id.text_view_expiry_date);
            holder.mTextViewOfferDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            holder.imageViewOffer = (NetworkImageView) convertView.findViewById(R.id.image_view_offer);
//            holder.imageViewShare = (ImageView) convertView.findViewById(R.id.image_view_share);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            final CouponsByCategoriesView model = couponsByCategoriesViewList.get(position);
            holder.imageViewOffer.setImageUrl(model.getImage_URL(), mImageLoader);
            String title = "";
            if (model.getTitle().contains("&amp;")) {
                title = model.getTitle().replace("&amp;", "&");
            } else {
                title = model.getTitle();
            }
            holder.mTextViewOfferTitle.setText(title);
            String description = model.getDescription();
            if (description.contains("<div")) {
                description = description.substring(0, description.indexOf("<"));
            }
            holder.mTextViewOfferDescription.setText(description);
            long dateDifference = Util.getDateDifference(model.getExpiryDate());
            if (dateDifference > 1) {
                holder.mTextExpiryDate.setText(dateDifference + " days remaining");
            } else {
                holder.mTextExpiryDate.setText(dateDifference + " day remaining");
            }

            /*holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, model.get);
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }
            });*/

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
//        ImageView imageViewShare;
    }


}
