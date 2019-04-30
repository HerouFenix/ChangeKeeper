package com.example.changekeeper;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements TransferDialogue.TransferListener {
    /* TO DO:
        1.Implement bottom navbar
        2.Implement Carroussel ( HOW THE HECK!?)
        3.Implement TransferToX
        4.Implement RegisterIncome
        5.Change TopBar
     */

    public static final String EXTRA_MESSAGE = "com.example.MainActivity.MESSAGE";
    private static final String TAG = "MainAct";

    private ActionBar toolbar;

    private ViewPager mPager;
    private PagerAdapter pageAdapter;
    private static final int NUM_PAGES = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.layout_actionbar);
        ((TextView)toolbar.getCustomView().findViewById(R.id.ourTitle)).setText("ChangeKeeper");

        this.mPager = (ViewPager) findViewById(R.id.typeSelector);
        this.pageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(pageAdapter);

        this.mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if (position == 0 ){
                    TextView view = (TextView) findViewById(R.id.transferText);
                    view.setText("Transfer to Card");
                }else{
                    TextView view = (TextView) findViewById(R.id.transferText);
                    view.setText("Transfer to Wallet");
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_subscriptions:
                    startActivity(new Intent(this, SubscriptionScreen.class));
                    return true;
                case R.id.navigation_allowances:
                    startActivity(new Intent(this, AllowanceScreen.class));
                    return true;
                case R.id.navigation_home:
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
        private Fragment walletFrag;
        private Fragment cardFrag;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.walletFrag = new MainWalletFragment();
            this.cardFrag = new MainCardFragment();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return this.walletFrag;
            else
                return this.cardFrag;
        }

        public void updateWallet(){
            ((MainWalletFragment) this.walletFrag).updateAmount();
        }

        public void updateCard(){
            ((MainCardFragment) this.cardFrag).updateAmount();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }


    public void goToExpense(View view){
        if (mPager.getCurrentItem() == 0) {
            Intent intent = new Intent(this, RegExpenseScreen.class);
            String message = "EXPENSE-WALLET";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, RegExpenseScreen.class);
            String message = "EXPENSE-CARD";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }


    public void goToIncome(View view){
        if (mPager.getCurrentItem() == 0) {
            Intent intent = new Intent(this, RegIncomeScreen.class);
            String message = "INCOME-WALLET";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, RegIncomeScreen.class);
            String message = "INCOME-CARD";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }


    public void openTransferDialogue(View view){
        TransferDialogue transferDialogue = new TransferDialogue();
        transferDialogue.show(getSupportFragmentManager(), "Transfer");
    }

    @Override
    public void updateTransfer(String amount) {
        try {
            FileInputStream fileInputStream = openFileInput("UserMoney.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Double walletAmount = Double.parseDouble(bufferedReader.readLine());
            Double cardAmount = Double.parseDouble(bufferedReader.readLine());



            inputStreamReader.close();
            fileInputStream.close();

            switch(((TextView) findViewById(R.id.transferText)).getText().toString()){
                case "Transfer to Card":
                    walletAmount = walletAmount - Double.parseDouble(amount);
                    cardAmount = cardAmount + Double.parseDouble(amount);
                    break;
                case "Transfer to Wallet":
                    walletAmount = walletAmount + Double.parseDouble(amount);
                    cardAmount = cardAmount - Double.parseDouble(amount);
                    break;
            }

            FileOutputStream fileOutputStream = openFileOutput("UserMoney.txt", MODE_PRIVATE);
            fileOutputStream.write((walletAmount+"\n").getBytes());
            fileOutputStream.write((cardAmount+"\n").getBytes());
            fileOutputStream.close();

            ((ScreenSlidePagerAdapter) this.pageAdapter).updateWallet();
            ((ScreenSlidePagerAdapter) this.pageAdapter).updateCard();
            int current = this.mPager.getCurrentItem();
            this.mPager.setAdapter(pageAdapter);
            this.mPager.setCurrentItem(current);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
