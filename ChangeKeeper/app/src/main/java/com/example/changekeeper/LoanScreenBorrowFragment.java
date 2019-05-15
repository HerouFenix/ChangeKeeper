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

import static android.support.constraint.Constraints.TAG;

public class LoanScreenBorrowFragment extends Fragment {

    ViewGroup thisView;
    private ArrayList<String> loans = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = (ViewGroup) inflater.inflate(
                R.layout.fragment_info_table, container, false);

        loadLoans();

        if(this.loans!=null && this.loans.size()!=0){
            LinearLayout ll;
            ll = (LinearLayout) thisView.findViewById(R.id.noInfoLayout);
            ll.removeAllViewsInLayout();
        }else{
            TextView text = (TextView) thisView.findViewById(R.id.noAllowance1);
            text.setText("You haven't borrowed any money");
            text = (TextView) thisView.findViewById(R.id.noAllowance2);
            text.setText(":D");
        }

        drawTable();

        return thisView;
    }


    private ArrayList<String> loadLoans() {
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("UserBorrows.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> ourRegs = new ArrayList<>();
            String line;

            //Format: ID - WALLET/CARD - Amount - Person - Date - Description
            while ((line = bufferedReader.readLine()) != null) {
                ourRegs.add(line);
                this.loans.add(line);
            }


        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void drawTable(){
        RecyclerView recyclerView = thisView.findViewById(R.id.infoTable);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.loans,getActivity());

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
