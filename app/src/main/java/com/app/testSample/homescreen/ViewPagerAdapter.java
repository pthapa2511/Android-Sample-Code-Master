package com.app.testSample.homescreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.app.testSample.category.TopCategoriesFragment;
import com.app.testSample.store.TopStoresFragment;

/**
 * Created by anoop.singh on 08-Mar-17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int TOTAL_TABS = 3;
    private String[] tabTitles = new String[]{"Best Offers", "Categories", "Top Stores"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return BestOfferFragment.newInstance();
            case 1:
                return TopCategoriesFragment.newInstance();
            case 2:
                return TopStoresFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }

}
