package com.example.changekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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

public class LoanScreen extends AppCompatActivity implements MoreDialogueLoans.MoreDialogueListener{
    private TableLayout loansTable;
    private ArrayList<TableRow> loansRows = new ArrayList<>();

    private TableLayout lentsTable;
    private ArrayList<TableRow> lentsRows = new ArrayList<>();

    private ArrayList<String> ourLoans;
    private ArrayList<String> ourLents;

    public static final String EXTRA_MESSAGE = "com.example.MainActivity.MESSAGE";
    private static final String TAG = "Loans";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Loans");

        Intent intent = getIntent();

        setContentView(R.layout.activity_loans);

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
                    return true;
            }
            return false;
        });

        this.loansTable = findViewById(R.id.borrowTable);
        updateTable(loansTable);
        this.lentsTable = findViewById(R.id.lendTable);
        updateTable(lentsTable);
    }



    public void goToBorrow(View view){
        Intent intent = new Intent(this, RegLoanScreen.class);
        String message = "BORROW";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void goToLend(View view){
        Intent intent = new Intent(this, RegLoanScreen.class);
        String message = "LEND";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void updateTable(TableLayout table){
        if(table.equals(this.loansTable)){
            this.ourLoans = readFile("UserBorrows");
            if(this.ourLoans != null)
                for(int i = 0 ; i < this.ourLoans.size() ; i++){

                    TableRow row = new TableRow(this);
                    row.setLayoutParams(findViewById(R.id.firstRow).getLayoutParams());

                    String[] thisRow = this.ourLoans.get(i).split("-");
                    //Format: ID - WALLET/CARD - Amount - Person - Date - Description

                    //Add amount
                    TextView currentText = new TextView(this);
                    currentText.setText(thisRow[2]);
                    currentText.setLayoutParams(findViewById(R.id.amountView).getLayoutParams());
                    row.addView(currentText);

                    //Add Destination
                    currentText = new TextView(this);
                    currentText.setText(thisRow[1]);
                    currentText.setLayoutParams(findViewById(R.id.destView).getLayoutParams());
                    row.addView(currentText);

                    //Add Destination
                    currentText = new TextView(this);
                    currentText.setText(thisRow[3]);
                    currentText.setLayoutParams(findViewById(R.id.personView).getLayoutParams());
                    row.addView(currentText);

                    //Add Pay Date
                    currentText = new TextView(this);
                    currentText.setText(thisRow[4]);
                    currentText.setLayoutParams(findViewById(R.id.payByView).getLayoutParams());
                    row.addView(currentText);

                    //Add More button
                    ImageButton imButton = new ImageButton(this);
                    imButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openMore(thisRow,"borrows");
                        };
                    });
                    row.addView(imButton);

                    this.loansRows.add(row);

                    this.loansTable.addView(row);
                }
        }else{
            this.ourLents = readFile("UserLends");
            if(this.ourLents != null)
                for(int i = 0 ; i < this.ourLents.size() ; i++){

                    TableRow row = new TableRow(this);
                    row.setLayoutParams(findViewById(R.id.firstRow).getLayoutParams());

                    String[] thisRow = this.ourLents.get(i).split("-");


                    //Add amount
                    TextView currentText = new TextView(this);
                    currentText.setText(thisRow[2]);
                    currentText.setLayoutParams(findViewById(R.id.amountView).getLayoutParams());
                    row.addView(currentText);

                    //Add Destination
                    currentText = new TextView(this);
                    currentText.setText(thisRow[1]);
                    currentText.setLayoutParams(findViewById(R.id.destView).getLayoutParams());
                    row.addView(currentText);

                    //Add Destination
                    currentText = new TextView(this);
                    currentText.setText(thisRow[1]);
                    currentText.setLayoutParams(findViewById(R.id.personView).getLayoutParams());
                    row.addView(currentText);

                    //Add Pay Date
                    currentText = new TextView(this);
                    currentText.setText(thisRow[1]);
                    currentText.setLayoutParams(findViewById(R.id.payByView).getLayoutParams());
                    row.addView(currentText);

                    //Add More button
                    ImageButton imButton = new ImageButton(this);
                    imButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openMore(thisRow,"lents");
                        };
                    });
                    row.addView(imButton);

                    this.lentsRows.add(row);

                    this.lentsTable.addView(row);
                }
        }
    }

    private ArrayList<String> readFile(String fileName) {
        try {
            FileInputStream fileInputStream = openFileInput(fileName+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> ourRegs = new ArrayList<>();
            int i = 0;
            String line;

            //Format: ID - WALLET/CARD - Amount - Person - Date - Description
            while((line = bufferedReader.readLine()) != null){
                line = i+"-"+line;
                ourRegs.add(line);
                i++;
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return ourRegs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void openMore(String[] row,String type){
        MoreDialogueLoans moreDialogueLoans = new MoreDialogueLoans(row,type);
        moreDialogueLoans.show(getSupportFragmentManager(), "More Dialogue");
    }

    private void updateBorrows(String toRemove){
        ArrayList<String> temp = new ArrayList<>();

        try {
            FileInputStream fileInputStream = openFileInput("UserBorrows.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int i = 0;
            String line;

            //Format: ID - WALLET/CARD - Amount - Date - FrequencyType - Frequency - Weekdays
            while((line = bufferedReader.readLine()) != null){
                line = i + "-" + line;
                if(line.equals(toRemove)){
                    continue;
                }
                temp.add(line);
                i++;

            }

            bufferedReader.close();

            FileOutputStream fileOutputStream = openFileOutput("UserBorrows.txt", MODE_PRIVATE);

            for(int k = 0 ; k < temp.size() ; k++){
                fileOutputStream.write((temp.get(k)+"\n").getBytes());
            }

            fileOutputStream.close();
            inputStreamReader.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLents(String toRemove){
        ArrayList<String> temp = new ArrayList<>();

        try {
            FileInputStream fileInputStream = openFileInput("UserLends.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int i = 0;
            String line;

            //Format: ID - WALLET/CARD - Amount - Date - FrequencyType - Frequency - Weekdays
            while((line = bufferedReader.readLine()) != null){
                line = i + "-" + line;
                if(line == toRemove){
                    continue;
                }
                temp.add(line);
                i++;

            }

            bufferedReader.close();

            FileOutputStream fileOutputStream = openFileOutput("UserLends.txt", MODE_PRIVATE);

            for(int k = 0 ; k < temp.size() ; k++){
                fileOutputStream.write((temp.get(k)+"\n").getBytes());
            }

            fileOutputStream.close();
            inputStreamReader.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeLoan(String id, String type) {
        if(type == "borrows"){
            updateBorrows(this.ourLoans.get(Integer.parseInt(id)));
            this.ourLoans = new ArrayList<>();

            for(int i = 0 ; i < this.loansRows.size() ; i++){
                this.loansTable.removeView(this.loansRows.get(i));
            }

            this.loansRows.remove(Integer.parseInt(id));

            updateTable(this.loansTable);
        }else{
            updateLents(this.ourLents.get(Integer.parseInt(id)));
            this.ourLents = new ArrayList<>();

            for(int i = 0 ; i < this.lentsRows.size() ; i++){
                this.lentsTable.removeView(lentsRows.get(i));
            }

            this.lentsRows.remove(Integer.parseInt(id));

            updateTable(this.lentsTable);
        }
    }
}
