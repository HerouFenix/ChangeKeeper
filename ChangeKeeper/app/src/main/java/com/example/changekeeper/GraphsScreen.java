package com.example.changekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class GraphsScreen extends AppCompatActivity  {

    public static final String EXTRA_MESSAGE = "com.example.MainActivity.MESSAGE";
    private static final String TAG = "GRAPHS";

    private ActionBar toolbar;

    private ViewPager mPager;
    private PagerAdapter pageAdapter;
    public static int currentPage = 0;
    private static final int NUM_PAGES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        toolbar = getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.layout_actionbar);
        ((TextView)toolbar.getCustomView().findViewById(R.id.ourTitle)).setText("Graphs & Info");

        this.mPager = (ViewPager) findViewById(R.id.typeSelector);
        this.pageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(pageAdapter);

        this.mPager.setCurrentItem(this.currentPage);
        if (this.currentPage == 0){
            ((View)findViewById(R.id.graphsButton)).setBackgroundResource(R.drawable.ic_graphs);
            ((View)findViewById(R.id.infoButton)).setBackgroundResource(R.drawable.ic_infonotselected);
        }else{
            ((View)findViewById(R.id.graphsButton)).setBackgroundResource(R.drawable.ic_graphsnotselected);
            ((View)findViewById(R.id.infoButton)).setBackgroundResource(R.drawable.ic_info);
        }
        this.mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if (position == 0 ){
                    ((View)findViewById(R.id.graphsButton)).setBackgroundResource(R.drawable.ic_graphs);
                    ((View)findViewById(R.id.infoButton)).setBackgroundResource(R.drawable.ic_infonotselected);
                }else{
                    ((View)findViewById(R.id.graphsButton)).setBackgroundResource(R.drawable.ic_graphsnotselected);
                    ((View)findViewById(R.id.infoButton)).setBackgroundResource(R.drawable.ic_info);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_info);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_subscriptions:
                    startActivity(new Intent(this, SubscriptionScreen.class));
                    return true;
                case R.id.navigation_allowances:
                    startActivity(new Intent(this, AllowanceScreen.class));
                    return true;
                case R.id.navigation_home:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                case R.id.navigation_loans:
                    startActivity(new Intent(this, LoanScreen.class));
                    return true;
                case R.id.navigation_info:
                    return true;
            }
            return false;
        });

        Log.v(TAG,"HELLOOOOOOOOOOOOOOOOOOOOOOOO :D");

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Fragment graphsFrag;
        private Fragment infoFrag;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.graphsFrag = new GraphsGraphsFragment();
            this.infoFrag = new AllInfoFragment();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return this.graphsFrag;
            else
                return this.infoFrag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

    public void changeToGraphs(View view){
        this.mPager.setCurrentItem(0);
        this.currentPage = 0;

    }

    public void changeToInfo(View view){
        this.mPager.setCurrentItem(1);
        this.currentPage = 1;


    }

}
