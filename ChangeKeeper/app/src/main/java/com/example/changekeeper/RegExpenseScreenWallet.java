package com.example.changekeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLockInterruptionException;
import java.util.Calendar;

public class RegExpenseScreenWallet extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /* TO DO:
        1.Implement "Add Category"
        2.Make it so when nothing is selected on dropwdown, a Hint is show instead
        3.Make it so "Confirm" checks if everything is properly filled
        4.Make it so on "Confirm" a new modal is shown with the current balance, expense ammount, and total after expense (and another confirm button)
        5.Change Category Drop Down Appearance
     */

    private static final String TAG = "RegExpWallet";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_income_screen);
        Log.v(TAG,"HELLOOOOOOOOOOOOOOOOOOOOOOOO2 :D");

        //Date
        mDisplayDate = (TextView) findViewById(R.id.datePicker);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegExpenseScreenWallet.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month++; //We do this cus by default January = 0
                String date = day+" / "+month+" / "+year;
                mDisplayDate.setText(date);
            }
        };

        //Category Dropwdown
        Spinner spinner = findViewById(R.id.expenseCategory);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Income Dropdown

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void registerExpense(View view){
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.editAmount);
        Double amount = Double.parseDouble(editText.getText().toString());

        //Register
        writeFile(amount);
        updateWallet(amount);
        startActivity(intent);
    }

    private void writeFile(double amount){
        boolean found = false;
        for(String i : fileList()){
            Log.v(TAG,i+" ------------------------");
            if(i.equals("UserExpenses.txt")){
                found = true;
                break;
            }
        }


        try{
            FileOutputStream fileOutputStream;
            if(!found) {
                fileOutputStream = openFileOutput("UserExpenses.txt", MODE_PRIVATE);
            }else{
                fileOutputStream = openFileOutput("UserExpenses.txt", MODE_APPEND);
            }

            fileOutputStream.write(("WALLET - "+ amount+"\n").getBytes());
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updateWallet(double amount) {
        try {
            FileInputStream fileInputStream = openFileInput("UserMoney.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Double walletAmount = Double.parseDouble(bufferedReader.readLine());
            String cardAmount = bufferedReader.readLine();

            walletAmount = walletAmount-amount;

            Log.v(TAG,walletAmount+"-------------------------------------------");
            FileOutputStream fileOutputStream = openFileOutput("UserMoney.txt", MODE_PRIVATE);
            fileOutputStream.write((walletAmount+"\n").getBytes());
            fileOutputStream.write((cardAmount+"\n").getBytes());

            fileOutputStream.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
