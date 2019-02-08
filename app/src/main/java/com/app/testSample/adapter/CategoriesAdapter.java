package com.app.testSample.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.model.CateGoryModel;

import java.util.List;

/**
 * Adapter for categories
 */

public class CategoriesAdapter extends BaseAdapter {
    private Context mContext;
    private List<CateGoryModel> mCateGoryModelList;
    private LayoutInflater mInflater;

    public CategoriesAdapter(Activity pActivity){
        this.mContext = pActivity;
        mInflater = (LayoutInflater) pActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CateGoryModel> pData){
        this.mCateGoryModelList = pData;
    }

    @Override
    public int getCount() {
        return mCateGoryModelList == null ? 0 : mCateGoryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mCateGoryModelList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_categories, null);
            convertView.setTag(holder);
            holder.mTextViewCategoryName = (TextView) convertView.findViewById(R.id.text_view_category_name);
            holder.mTextViewCount = (TextView) convertView.findViewById(R.id.text_view_category_count);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            CateGoryModel model = mCateGoryModelList.get(position);
            String categoryName = "";
            if(model.getCategoryName().contains("&amp;")){
                categoryName = model.getCategoryName().replace("&amp;", "&");
            }else {
                categoryName = model.getCategoryName();
            }

            if(TextUtils.isEmpty(model.getCouponCount())){
                holder.mTextViewCount.setText("0 Offer");
            } else if(model.getCouponCount().equals("1") ){
                holder.mTextViewCount.setText("1 Offer");
            } else {
                holder.mTextViewCount.setText(model.getCouponCount() + " Offers");
            }

            holder.mTextViewCategoryName.setText(categoryName);

        } catch (Exception ex){
            Log.e("CategoriesAdapter",  "getView() " + ex);
        }

        return convertView;
    }

    class ViewHolder{
        TextView mTextViewCount;
        TextView mTextViewCategoryName;
    }
}
