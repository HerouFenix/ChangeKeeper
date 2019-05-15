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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoanScreen extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.MainActivity.MESSAGE";
    private static final String TAG = "MainAct";

    private ActionBar toolbar;

    private ViewPager mPager;
    private PagerAdapter pageAdapter;
    private static final int NUM_PAGES = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);

        toolbar = getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.layout_actionbar);
        ((TextView)toolbar.getCustomView().findViewById(R.id.ourTitle)).setText("My Loans");

        this.mPager = (ViewPager) findViewById(R.id.typeSelector);
        this.pageAdapter = new LoanScreen.ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(pageAdapter);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_loans);
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
                    return true;
                case R.id.navigation_info:
                    startActivity(new Intent(this, GraphsScreen.class));
                    return true;
            }
            return false;
        });


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

    public void goToBorrow(View view) {
        Intent intent = new Intent(this, RegLoanScreen.class);
        String message = "BORROW";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToLend(View view) {
        Intent intent = new Intent(this, RegLoanScreen.class);
        String message = "LEND";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Fragment borrowFrag;
        private Fragment lendFrag;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.borrowFrag = new LoanScreenBorrowFragment();
            this.lendFrag = new LoanScreenLendFragment();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return this.borrowFrag;
            else
                return this.lendFrag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}



