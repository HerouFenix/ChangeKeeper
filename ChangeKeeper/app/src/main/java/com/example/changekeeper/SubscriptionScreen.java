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
import java.util.Calendar;

public class SubscriptionScreen extends AppCompatActivity implements MoreDialogue.MoreDialogueListener {
    private static final String TAG = "Subscriptions Screen";
    private ArrayList<String> ourAllowances;
    private TableLayout mTableLayout;
    private ArrayList<TableRow> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("My Subscriptions");

        Intent intent = getIntent();

        setContentView(R.layout.activity_subscriptions);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_subscriptions);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_subscriptions:
                    return true;
                case R.id.navigation_allowances:
                    startActivity( new Intent(this, AllowanceScreen.class));
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

        this.mTableLayout = findViewById(R.id.allowanceTable);
        updateTable();

    }

    private void openMore(String[] row,String next){
        MoreDialogue moreDialogue = new MoreDialogue(row,next);
        moreDialogue.show(getSupportFragmentManager(), "More Dialogue");
    }

    private String calcNextDate(String dateOfReg,String frequency, String type){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateOfReg.split("/")[0]));
        cal.set(Calendar.MONTH,Integer.parseInt(dateOfReg.split("/")[1])-1);
        cal.set(Calendar.YEAR,Integer.parseInt(dateOfReg.split("/")[2]));

        Calendar current = Calendar.getInstance();


        switch(type){
            case "Day":
                do {
                    cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(frequency));
                }while(current.after(cal));
                break;
            case "Week":
                do {
                    cal.add(Calendar.WEEK_OF_MONTH, Integer.parseInt(frequency));
                }while(current.after(cal));
                break;
            case "Month":
                do {
                    cal.add(Calendar.MONTH, Integer.parseInt(frequency));
                }while(current.after(cal));
                break;
            case "Year":
                do {
                    cal.add(Calendar.YEAR, Integer.parseInt(frequency));
                }while(current.after(cal));
                break;
        }

        //Add support for weekdays
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String date = day+"/"+month+"/"+year;

        return date;
    }

    private ArrayList<String> readFile() {
        try {
            FileInputStream fileInputStream = openFileInput("UserExpenses.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> ourAllowances = new ArrayList<>();
            int i = 0;
            String line;

            //Format: ID - WALLET/CARD - Amount - Category - Date - FrequencyType - Frequency - Weekdays
            while((line = bufferedReader.readLine()) != null){
                if(line.split("-")[3].equals("NONE")){
                    continue;
                }
                line = i+"-"+line;
                ourAllowances.add(line);
                i++;
            }

            bufferedReader.close();

            return ourAllowances;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateTable(){
        this.ourAllowances = readFile();

        if(this.ourAllowances != null)
            for(int i = 0 ; i < this.ourAllowances.size() ; i++){

                TableRow row = new TableRow(this);
                row.setLayoutParams(findViewById(R.id.firstRow).getLayoutParams());

                String[] thisRow = this.ourAllowances.get(i).split("-");


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

                //Add Next Date
                currentText = new TextView(this);
                String next = calcNextDate(thisRow[4],thisRow[6],thisRow[5]);
                currentText.setText(next);
                currentText.setLayoutParams(findViewById(R.id.nextView).getLayoutParams());
                row.addView(currentText);


                //Add Frequency
                currentText = new TextView(this);
                currentText.setText(thisRow[6] + " " + thisRow[5]);
                currentText.setLayoutParams(findViewById(R.id.freqView).getLayoutParams());
                row.addView(currentText);

                //Add More button
                ImageButton imButton = new ImageButton(this);
                imButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openMore(thisRow,next);
                    };
                });
                row.addView(imButton);

                this.rows.add(row);

                this.mTableLayout.addView(row);
            }
    }

    private void updateAllowances(String toRemove){
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();

        try {
            FileInputStream fileInputStream = openFileInput("UserExpenses.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int i = 0;
            int j = 0;
            String line;

            //Format: ID - WALLET/CARD - Amount - Date - FrequencyType - Frequency - Weekdays
            while((line = bufferedReader.readLine()) != null){
                if(line.split("-")[3].equals("NONE")){
                    line = j + "-" + line;
                    temp.add(line);
                }else {
                    line = i + "-" + line;
                    temp.add(line);
                    i++;
                }
            }

            bufferedReader.close();
            temp.remove(toRemove);

            for(int m = 0 ; m < temp.size() ; m++){
                String[] toAdd = temp.get(m).split("-");
                String newLine = toAdd[1];
                for(int k = 2 ; k < toAdd.length ; k++){
                    newLine = newLine+"-"+toAdd[k];
                }
                temp2.add(newLine);
            }

            FileOutputStream fileOutputStream = openFileOutput("UserExpenses.txt", MODE_PRIVATE);

            for(int k = 0 ; k < temp2.size() ; k++){
                fileOutputStream.write((temp2.get(k)+"\n").getBytes());
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
    public void removeAllowance(String id) {
        updateAllowances(this.ourAllowances.get(Integer.parseInt(id)));
        this.ourAllowances.remove(Integer.parseInt(id));
        this.mTableLayout.removeView(rows.get(Integer.parseInt(id)));
        this.rows.remove(Integer.parseInt(id));

    }
}
