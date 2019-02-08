package com.app.testSample.homescreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.app.testSample.R;
import com.app.testSample.model.SliderUrl;
import com.app.testSample.utility.BitmapLruCache;
import com.app.testSample.utility.ui.CouponsAutoScrollViewPager;

import java.util.List;


/**
 * AutoPageScroller
 */

public class AutoScrollPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int [] resource;// = new int[]{R.drawable.babycare, R.drawable.dettol};
    private LayoutInflater inflater;
    private List<SliderUrl> mSliderUIrlList;
    private ImageLoader mImageLoader;

    public AutoScrollPagerAdapter(Context context, List<SliderUrl> pList){
        this.mContext = context;
        mSliderUIrlList = pList;
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
    }


    @Override
    public int getCount() {
        return mSliderUIrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewItem = inflater.inflate(R.layout.slider_fragment, null);
        NetworkImageView imageView = (NetworkImageView) viewItem.findViewById(R.id.image_view_slider);
        imageView.setImageUrl(mSliderUIrlList.get(position).getImageUrl(), mImageLoader);
//        imageView.setImageResource(mSliderUIrlList.get(position).getImageUrl());
        container.addView(viewItem);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mSliderUIrlList.get(position).getLinkUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        return viewItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((CouponsAutoScrollViewPager) container).removeView((LinearLayout) object);
    }
}
