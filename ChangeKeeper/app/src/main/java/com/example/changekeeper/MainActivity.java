package com.example.changekeeper;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    /* TO DO:
        1.Implement bottom navbar
        2.Implement Carroussel ( HOW THE HECK!?)
        3.Implement TransferToX
        4.Implement RegisterIncome
        5.Change TopBar
     */

    public static final String EXTRA_MESSAGE = "com.example.MainActivity.MESSAGE";
    private static final String TAG = "MainAct";

    private String walletAmount;
    private String cardAmount;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_subscriptions:
                    toolbar.setTitle("My Subscriptions");
                    return true;
                case R.id.navigation_allowances:
                    toolbar.setTitle("My Allowances");
                    goToExpenseWallet(this.findViewById(android.R.id.content)); // alterar isto!!! mas bascially ja ta
                    return true;
                case R.id.navigation_home:
                    toolbar.setTitle("ChangeKeeper");
                    return true;
                case R.id.navigation_loans:
                    toolbar.setTitle("My Loans");
                    return true;
                case R.id.navigation_info:
                    toolbar.setTitle("Info");
                    return true;
            }
            return false;
        });

        Log.v(TAG,"HELLOOOOOOOOOOOOOOOOOOOOOOOO :D");

        //In case the file doesn't exist yet
        boolean found = false;
        for(String i : fileList()){
            Log.v(TAG,i+" ------------------------");
            if(i.equals("UserMoney.txt")){
                found = true;
                break;
            }
        }

        if(!found){
            writeFile();
        }

        //Show Money Values
        readFile();

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToExpenseWallet(View view){
        Intent intent = new Intent(this, RegExpenseScreen.class);
        String message = "EXPENSE-WALLET";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToExpenseCard(View view){
        Intent intent = new Intent(this, RegExpenseScreen.class);
        String message = "EXPENSE-CARD";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

   /* public void goToIncomeWallet(View view){
        Intent intent = new Intent(this, RegIncomeScreen.class);
        String message = "INCOME-WALLET";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToIncomeCard(View view){
        Intent intent = new Intent(this, RegIncomeScreen.class);
        String message = "INCOME-WALLET";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    private void writeFile(){
        try{
            FileOutputStream fileOutputStream = openFileOutput("UserMoney.txt",MODE_PRIVATE);
            fileOutputStream.write("0.00\n".getBytes());
            fileOutputStream.write("0.00\n".getBytes());
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void readFile() {
        try {
            FileInputStream fileInputStream = openFileInput("UserMoney.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            walletAmount = bufferedReader.readLine();
            cardAmount = bufferedReader.readLine();

            TextView walletMoney = findViewById(R.id.walletMoney);
            walletMoney.setText(walletAmount+"€");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
