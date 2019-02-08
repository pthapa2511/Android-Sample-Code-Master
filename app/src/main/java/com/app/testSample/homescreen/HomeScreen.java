package com.app.testSample.homescreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.testSample.R;
import com.app.testSample.category.AllCategoryActivity;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.BaseModel;
import com.app.testSample.model.SliderUrl;
import com.app.testSample.offer.BankOffer;
import com.app.testSample.search.SearchCoupons;
import com.app.testSample.store.GetAllStoreActivity;
import com.app.testSample.support.HelpAndSupportActivity;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.ToastUtils;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;
import com.app.testSample.utility.ui.CommonEventHandler;
import com.app.testSample.utility.ui.CouponsAutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeScreen
 */
public class HomeScreen extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, CommonEventHandler {

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<SliderUrl> mSliderList;
    private CouponsAutoScrollViewPager mAutoScrollViewPager;
    private LinearLayout viewPagerDots;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mLayoutWithoutPager;
    private int color = Color.BLACK;
    private boolean layoutCollapsed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAutoScrollViewPager = (CouponsAutoScrollViewPager) findViewById(R.id.view_pager_top_layout_home_screen);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPagerDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mLayoutWithoutPager = (RelativeLayout) findViewById(R.id.layout_without_pager);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTablayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (ConnectivityUtils.isNetworkEnabled(this)) {
//            showProgressDialog(getString(R.string.progress_loading));
            new ApiRequestController(this, this).getData(Constants.REQUEST_GET_IMPORTANT_LINKS, null);
            new ApiRequestController(this, this).getData(Constants.REQUEST_SLIDER_URL, null);
            mLayoutWithoutPager.setVisibility(View.VISIBLE);
        } else {
            ToastUtils.showToast(this, getString(R.string.error_internet_availability));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);
        mAppBarLayout.addOnOffsetChangedListener(listener);
    }

    AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
        int scrollRange = -1;
        int collapsCount = 0;
        int expandCount = 0;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            } else if (scrollRange + verticalOffset == 0) {
                if (collapsCount == 0) {
                    mTablayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    mTablayout.setTabTextColors(Color.WHITE, Color.WHITE);
                    mTablayout.setSelectedTabIndicatorColor(Color.WHITE);
                    color = Color.WHITE;
                    collapsCount++;
                    expandCount = 0;
                }
            } else if (scrollRange > verticalOffset) {
                if (expandCount == 0) {
                    collapsCount = 0;
                    mTablayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_white));
                    mTablayout.setTabTextColors(Color.BLACK, Color.BLACK);
                    mTablayout.setSelectedTabIndicatorColor(Color.BLACK);
                    color = Color.BLACK;
                    expandCount++;
                }
            }
        }
    };

    private int dotsCount;
    private ImageView[] dots;

    private void setUiPageViewController() {
        if (autoScrollPagerAdapter != null) {
            dotsCount = autoScrollPagerAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                viewPagerDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selection_dot));
        }
    }

    private AutoScrollPagerAdapter autoScrollPagerAdapter;

    @Override
    protected void updateUi(Response response) {
        if (response.isSuccess()) {
            switch (response.getDataType()) {
                case Constants.REQUEST_NEWSLETTER_SUBSCRIPTION: {
                    BaseModel baseModel = (BaseModel) response.getResponseObject();
                    ToastUtils.showToast(this, baseModel.getMessage());
                    removeProgressDialog();
                    break;
                }

                case Constants.REQUEST_SLIDER_URL: {
                    mLayoutWithoutPager.setVisibility(View.GONE);
                    viewPagerDots.setVisibility(View.VISIBLE);
                    mAutoScrollViewPager.setVisibility(View.VISIBLE);
                    mSliderList = (ArrayList<SliderUrl>) response.getResponseObject();
                    autoScrollPagerAdapter = new AutoScrollPagerAdapter(this, mSliderList);
                    mAutoScrollViewPager.setAdapter(autoScrollPagerAdapter);
//                    mAutoScrollViewPager.setAutoScrollDurationFactor(6.0);
                    mAutoScrollViewPager.startAutoScroll();
                    mAutoScrollViewPager.setInterval(6000);
                    autoScrollPagerAdapter.notifyDataSetChanged();
                    setUiPageViewController();

                    mAutoScrollViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotsCount; i++) {
                                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dot));
                            }

                            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selection_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    break;
                }

            }
        }
//        removeProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchCoupons.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Intent intent = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
//                fragment = new BestOfferFragment();
                break;
            case R.id.nav_store:
                intent = new Intent(this, GetAllStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_bank_offer:
                intent = new Intent(this, BankOffer.class);
                startActivity(intent);
                break;
            case R.id.nav_categories:
                intent = new Intent(this, AllCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_help_and_support:
                intent = new Intent(this, HelpAndSupportActivity.class);
                startActivity(intent);
                break;
        }

        //replacing the fragment
        /*if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public void onEvent(int eventType, String data) {
        showProgressDialog(getString(R.string.progress_loading));
        new ApiRequestController(this, this).getData(Constants.REQUEST_NEWSLETTER_SUBSCRIPTION, data);
    }
}
