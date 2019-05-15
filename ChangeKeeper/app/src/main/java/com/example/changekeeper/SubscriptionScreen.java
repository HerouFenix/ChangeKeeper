package com.example.changekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class SubscriptionScreen extends AppCompatActivity {
    private static final String TAG = "SubscriptionScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar toolbar = getSupportActionBar();
        toolbar = getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.layout_actionbar);
        ((TextView)toolbar.getCustomView().findViewById(R.id.ourTitle)).setText("My Subscriptions");

        Intent intent = getIntent();

        setContentView(R.layout.activity_subscriptions);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_subscriptions);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_subscriptions:
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
                    startActivity(new Intent(this, GraphsScreen.class));
                    return true;
            }
            return false;
        });

        ArrayList<String> temp = loadExpenses();
        if(temp!=null && temp.size()!=0){
            LinearLayout ll;
            ll = (LinearLayout) findViewById(R.id.noInfoLayout);
            ll.removeAllViewsInLayout();
        }
    }

    private ArrayList<String> loadExpenses() {
        try {
            FileInputStream fileInputStream = openFileInput("UserExpenses.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> ourAllowances = new ArrayList<>();
            String line;

            //Format: WALLET/CARD - Amount - Register Date - Person (LOANS) - Category(EXPENSES) - FrequencyType - Frequency - Weekdays - Description - PayDate(LOANS) - PAID/NOT PAID (LOANS)
            while((line = bufferedReader.readLine()) != null){
                if(line.split(" - ")[5].equals("NONE")){
                    continue;
                }
                line =  line.split(" - ")[0] + " - " +
                        line.split(" - ")[1] + " - " +
                        line.split(" - ")[2] + " - " +
                        line.split(" - ")[3] + " - " +
                        line.split(" - ")[4] + " - " +
                        line.split(" - ")[5] + " - " +
                        line.split(" - ")[6] + " - " +
                        line.split(" - ")[7] + " - " +
                        line.split(" - ")[8] + " - " +
                        line.split(" - ")[9] + " - " +
                        line.split(" - ")[10];

                ourAllowances.add(line);
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


}
