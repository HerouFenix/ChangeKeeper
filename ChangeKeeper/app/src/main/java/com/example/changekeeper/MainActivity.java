package com.example.changekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    /* TO DO:
        1.Implement bottom navbar
        2.Implement Carroussel ( HOW THE HECK!?)
        3.Implement TransferToX
        4.Implement RegisterIncome
        5.Change TopBar
     */
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

    public void goToExpense(View view){
        Intent intent = new Intent(this, RegExpenseScreenWallet.class);
        startActivity(intent);
    }

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
