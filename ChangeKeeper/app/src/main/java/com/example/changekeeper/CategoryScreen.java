package com.example.changekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CategoryScreen extends AppCompatActivity {
    private static final String TAG = ":(";
    public static final String EXTRA_MESSAGE = "com.example.CategoryScreen.MESSAGE";
    public static String[] info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar toolbar = getSupportActionBar();
        toolbar = getSupportActionBar();
        toolbar.setTitle("Pick a category...");

        Intent intent = getIntent();
        info = intent.getStringArrayExtra(RegExpenseScreen.EXTRA_MESSAGE);
        setContentView(R.layout.activity_category_screen);


    }

    public String[] getInfo(){
        return this.info;
    }

}
