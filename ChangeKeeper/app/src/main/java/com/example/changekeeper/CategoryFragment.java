package com.example.changekeeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class CategoryFragment extends Fragment {

    ViewGroup thisView;

    private ArrayList<String> all = new ArrayList<>();
    public CategoryScreen catScreen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = (ViewGroup) inflater.inflate(
                R.layout.fragment_category_table, container, false);

        getCategories();
        try {
            drawTable();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return thisView;
    }



    private void drawTable() throws NoSuchFieldException {
        RecyclerView recyclerView = thisView.findViewById(R.id.infoTable);
        CategoryViewAdapter adapter = new CategoryViewAdapter(this.all, CategoryScreen.info,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getCategories() {
        try {

            FileInputStream fileInputStream = getActivity().openFileInput("UserCategories.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            for (int i = 0; i < getResources().getStringArray(R.array.categories).length; i++) {
                all.add(getResources().getStringArray(R.array.categories)[i]);
                Log.i(TAG,"Oi colega " + all.get(i));
            }

            ArrayList<String> tempList = new ArrayList<>();
            String line = "";
            while ((line = bufferedReader.readLine()) != null && line != "\n") {
                all.add(line);
                Log.i(TAG,"Oi colega " + line);
            }


            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();


            this.all.add("Add a new category...");


        } catch (Exception e) {
            for (int i = 0; i < getResources().getStringArray(R.array.categories).length; i++) {
                all.add(getResources().getStringArray(R.array.categories)[i]);
            }
            this.all.add("Add a new category...");

            Log.i(TAG,"Oi colega :)" + this.all.size());

        }
    }

}
