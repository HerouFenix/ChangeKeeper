package com.example.changekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        };

        //Show Money Values
        readFile();

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
