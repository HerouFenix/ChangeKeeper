package com.example.changekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

public class SettingsScreen extends AppCompatActivity{
    private static final String TAG = ":(";
    public static final String EXTRA_MESSAGE = "com.example.CategoryScreen.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar toolbar = getSupportActionBar();
        toolbar = getSupportActionBar();
        toolbar.setTitle("Settings");


        Intent intent = getIntent();
        setContentView(R.layout.activity_settings);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }


}
