package com.example.changekeeper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegExpenseScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CategoryDialogue.CategoryDialogListener,ConfirmDialogue.ConfirmDialogListener, FrequencyDialogue.FrequencyDialogueListener {

    private static final String TAG = "RegExpense";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int typeFlag;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> frequencyAdapter;

    //To save:
    //Amount
    private double amount;

    //Category
    private String category;

    //Date

    private String date;

    //Frequency
    private String frequency;
    private ArrayList<String> weekdays;
    private String frequencyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        ActionBar toolbar = getSupportActionBar();

        setContentView(R.layout.activity_reg_expense_screen);

        switch(message){
            case "EXPENSE-WALLET":
                this.typeFlag = 0;
                toolbar.setTitle("Register Expense - Wallet");

                break;

            case "EXPENSE-CARD":
                this.typeFlag = 1;
                toolbar.setTitle("Register Expense - Card");

                break;

            default:
                Log.v(TAG,"wtf erro :D");
        }

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
                        RegExpenseScreen.this,
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
                month = month+1; //We do this cus by default January = 0
                String date = day+"/"+month+"/"+year;
                mDisplayDate.setText(date);
            }
        };

        //Category Dropwdown
        buildCategorySpinner();

        //Frequency Dropdown
        buildFrequencySpinner();
    }

    private void buildCategorySpinner(){
        String[] items;
        try {
            FileInputStream fileInputStream = openFileInput("UserCategories.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> tempList = new ArrayList<>();
            String line = "";
            while((line = bufferedReader.readLine()) != null && line != "\n"){
                tempList.add(line);
            }


            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();


            items = new String[getResources().getStringArray(R.array.categories).length + tempList.size()];

            for(int i = 0; i < getResources().getStringArray(R.array.categories).length-1; i++){
                items[i] = getResources().getStringArray(R.array.categories)[i];
            }

            int j = 0;
            for(int i = getResources().getStringArray(R.array.categories).length-1; i < items.length-1; i++){
                items[i] = tempList.get(j);
                j++;
            }

            items[items.length-1] = getResources().getStringArray(R.array.categories)[getResources().getStringArray(R.array.categories).length-1];

        }catch (Exception e){
            items = getResources().getStringArray(R.array.categories);

        }
        Spinner spinner = findViewById(R.id.expenseCategory);
        spinner.setOnItemSelectedListener(this);

        this.categoryAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(this.categoryAdapter);
    }

    @Override
    public void updateCategories(String newCategory) {
        boolean found = false;
        for(String i : fileList()){
            Log.v(TAG,i+" ------------------------");
            if(i.equals("UserCategories.txt")){
                found = true;
                break;
            }
        }

        try{
            FileOutputStream fileOutputStream;
            if(!found) {
                fileOutputStream = openFileOutput("UserCategories.txt", MODE_PRIVATE);
            }else{
                boolean alreadyExistsFlag = false;
                FileInputStream fileInputStream = openFileInput("UserCategories.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                ArrayList<String> tempList = new ArrayList<>();
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    if(line.equals(newCategory))
                        alreadyExistsFlag = true;
                }

                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();

                if(alreadyExistsFlag){
                    return;
                }
                fileOutputStream = openFileOutput("UserCategories.txt", MODE_APPEND);
            }

            fileOutputStream.write((newCategory+"\n").getBytes());
            fileOutputStream.close();


            //Update Spinner
            Spinner spinner = findViewById(R.id.expenseCategory);
            spinner.setOnItemSelectedListener(this);
            String[] items = new String[this.categoryAdapter.getCount()+1];

            for(int i = 0; i < this.categoryAdapter.getCount()+1; i++){
                if (i == this.categoryAdapter.getCount()-1)
                    items[i] = newCategory;
                else if(i==this.categoryAdapter.getCount())
                    items[i] = (String)this.categoryAdapter.getItem(i-1);
                else
                    items[i] = (String)this.categoryAdapter.getItem(i);
            }

            this.categoryAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(this.categoryAdapter);
            spinner.setSelection(this.categoryAdapter.getCount()-2);

            this.category = newCategory;

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void buildFrequencySpinner(){
        String[] items;
        try {
            FileInputStream fileInputStream = openFileInput("UserFrequencies.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> tempList = new ArrayList<>();
            String line = "";
            while((line = bufferedReader.readLine()) != null && line != "\n"){
                tempList.add(line);
            }


            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();


            items = new String[getResources().getStringArray(R.array.frequencies).length + tempList.size()];

            for(int i = 0; i < getResources().getStringArray(R.array.frequencies).length-1; i++){
                items[i] = getResources().getStringArray(R.array.frequencies)[i];
            }

            int j = 0;
            for(int i = getResources().getStringArray(R.array.frequencies).length-1; i < items.length-1; i++){
                items[i] = tempList.get(j);
                j++;
            }

            items[items.length-1] = getResources().getStringArray(R.array.frequencies)[getResources().getStringArray(R.array.frequencies).length-1];

        }catch (Exception e){
            items = getResources().getStringArray(R.array.frequencies);

        }
        Spinner spinner = findViewById(R.id.frequencyPicker);
        spinner.setOnItemSelectedListener(this);

        this.frequencyAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(this.frequencyAdapter);
    }

    @Override
    public void updateFrequencies(String frequency, String type, ArrayList<String> weekdays) {
        this.frequency = frequency;
        this.weekdays = weekdays;
        this.frequencyType = type;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos).toString().equals("Add a new category...") && parent.getId() == R.id.expenseCategory){
                 CategoryDialogue categoryDialogue = new CategoryDialogue();
                categoryDialogue.show(getSupportFragmentManager(), "Category Dialogue");
            }

            else if (parent.getItemAtPosition(pos).toString().equals("Custom...") && parent.getId() == R.id.frequencyPicker){
                FrequencyDialogue frequencyDialogue = new FrequencyDialogue();
                frequencyDialogue.show(getSupportFragmentManager(), "Frequency Dialogue");
            }

            else if(parent.getId() == R.id.expenseCategory){
                this.category = parent.getSelectedItem().toString();
            }
            else if(parent.getId() == R.id.frequencyPicker){
                switch(parent.getSelectedItem().toString()){
                    case ("Every day"):
                        this.frequency = "1";
                        this.frequencyType = "Day";
                        break;
                    case ("Every month"):
                        this.frequency = "1";
                        this.frequencyType = "Month";
                        break;
                    case ("Every week"):
                        this.frequency = "1";
                        this.frequencyType = "Week";
                        break;

                    case ("Does not repeat"):
                        this.frequency = "0";
                        this.frequencyType = "NULL";
                        break;

                }
                this.weekdays = new ArrayList<>();
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void errorCheck(View view){
        boolean valid = true;
        //Check amount

        if (((TextView)findViewById(R.id.editAmount)).getText().length() == 0){
            valid = false;
        }

        //Check date
        if (((TextView)findViewById(R.id.datePicker)).getText().length() == 0){
            valid = false;
        }


        if (valid == false){
            /*TO DO: ADD MESSAGE TELLING THE USER TO FILL ALL FIELDS*/
        }else{
            callConfirm();
        }

    }

    private void callConfirm(){
        Bundle args = new Bundle();
        args.putString("amount",((TextView)findViewById(R.id.editAmount)).getText().toString());
        args.putString("regDate",((TextView)findViewById(R.id.datePicker)).getText().toString());
        args.putString("type","EXPENSE");

        if(this.typeFlag==0){
            args.putString("dest","WALLET");
        }else{
            args.putString("dest","CARD");
        }
        ConfirmDialogue confirmDialogue = new ConfirmDialogue();
        confirmDialogue.setArguments(args);
        confirmDialogue.show(getSupportFragmentManager(), "Confirm Dialogue");
    }

    @Override
    public void confirm() {
        registerExpense();
    }

    private void registerExpense(){
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.editAmount);
        this.amount = Double.parseDouble(editText.getText().toString());
        this.date = ((TextView)findViewById(R.id.datePicker)).getText().toString();

        //Register
        writeFile();
        Calendar cal = Calendar.getInstance();

        if(cal.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(this.date.split("/")[0]) && (cal.get(Calendar.MONTH)+1) == Integer.parseInt(this.date.split("/")[1]) && cal.get(Calendar.YEAR) == Integer.parseInt(this.date.split("/")[2]))
            updateWallet();

        Toast toast = Toast.makeText(this,"Expense Regitered Successfully", Toast.LENGTH_SHORT);

        toast.show();
        startActivity(intent);
    }

    private void writeFile(){
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

            String type;


            if(this.typeFlag==0){
                type = "WALLET";
            }else{
                type = "CARD";
            }

            //Format: WALLET/CARD - Amount - Register Date - Person (LOANS) - Category(EXPENSES) - FrequencyType - Frequency - Weekdays - Description - PayDate(LOANS) - PAID/NOT PAID (LOANS)

            StringBuilder register = new StringBuilder();
            register.append(type);
            register.append(" - ");
            register.append("-"+amount+""); //Make amount negative
            register.append(" - ");
            register.append(this.date);
            register.append(" - ");
            register.append("NULL");
            register.append(" - ");
            register.append(this.category);
            register.append(" - ");
            register.append(this.frequencyType);
            register.append(" - ");
            register.append(this.frequency);
            register.append(" - ");
            register.append(this.weekdays);
            register.append(" - ");
            register.append(" Do Description ");
            register.append(" - ");
            register.append("NULL");
            register.append(" - ");
            register.append("NULL"+"\n");


            fileOutputStream.write(register.toString().getBytes());
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updateWallet() {
        try {
            FileInputStream fileInputStream = openFileInput("UserMoney.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Double walletAmount = Double.parseDouble(bufferedReader.readLine());
            Double cardAmount = Double.parseDouble(bufferedReader.readLine());

            if(this.typeFlag == 0){
                walletAmount = walletAmount - this.amount;
            }else{
                cardAmount = cardAmount - this.amount;
            }


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
