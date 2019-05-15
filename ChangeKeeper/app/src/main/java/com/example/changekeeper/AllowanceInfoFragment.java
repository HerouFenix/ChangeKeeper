package com.example.changekeeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class AllowanceInfoFragment extends Fragment {

    ViewGroup thisView;
    private ArrayList<String> incomes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = (ViewGroup) inflater.inflate(
                R.layout.fragment_info_table, container, false);

        loadIncomes();

        if(this.incomes!=null && this.incomes.size()!=0){
            LinearLayout ll;
            ll = (LinearLayout) thisView.findViewById(R.id.noInfoLayout);
            ll.removeAllViewsInLayout();
        }else{
            TextView text = (TextView) thisView.findViewById(R.id.noAllowance1);
            text.setText("You don't have any allowances");
            text = (TextView) thisView.findViewById(R.id.noAllowance2);
            text.setText(":(");
        }

        drawTable();

        return thisView;
    }

    private ArrayList<String> loadIncomes() {
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("UserIncomes.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> ourAllowances = new ArrayList<>();
            String line;

            //Format: WALLET/CARD - Amount - Date - Person(NULL UNLESS LOAN) - Category (NULL UNLESS EXPENSE)- FrequencyType (NULL IF LOAN) - Frequency (NULL IF LOAN) - Weekdays (NULL IF LOAN) - Description
            while((line = bufferedReader.readLine()) != null){
                if(line.split(" - ")[5].equals("NULL")){
                    continue;
                }
                ourAllowances.add(line);
                this.incomes.add(line);
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


    private void drawTable(){
        RecyclerView recyclerView = thisView.findViewById(R.id.infoTable);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.incomes,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
