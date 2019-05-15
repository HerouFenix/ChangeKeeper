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

public class LoanScreenLendFragment extends Fragment {

    private static final String TAG = "LoanScreenLendFragment";
    ViewGroup thisView;
    private ArrayList<String> loans = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = (ViewGroup) inflater.inflate(
                R.layout.fragment_info_table, container, false);

        Log.i(TAG,"FDS WTF LOOOOOOL");
        loadLoans();

        if(this.loans!=null && this.loans.size()!=0){
            LinearLayout ll;
            ll = (LinearLayout) thisView.findViewById(R.id.noInfoLayout);
            ll.removeAllViewsInLayout();
        }else{
            TextView text = (TextView) thisView.findViewById(R.id.noAllowance1);
            text.setText("You haven't lent any money");
            text = (TextView) thisView.findViewById(R.id.noAllowance2);
            text.setText("^.^");
        }

        drawTable();

        return thisView;
    }


    private ArrayList<String> loadLoans() {
        try{
            FileInputStream fileInputStream = getActivity().openFileInput("UserLends.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            //Format: WALLET/CARD - Amount - Register Date - Person (LOANS) - Category(EXPENSES) - FrequencyType - Frequency - Weekdays - Description - PayDate(LOANS) - PAID/NOT PAID (LOANS)
            while((line = bufferedReader.readLine()) != null){
                line =  line.split(" - ")[0] + " - " +
                        line.split(" - ")[1] + " - " +  //Make the value negative
                        line.split(" - ")[2] + " - " +
                        line.split(" - ")[3] + " - " +
                        line.split(" - ")[4] + " - " +
                        line.split(" - ")[5] + " - " +
                        line.split(" - ")[6] + " - " +
                        line.split(" - ")[7] + " - " +
                        line.split(" - ")[8] + " - " +
                        line.split(" - ")[9] + " - " +
                        line.split(" - ")[10];
                this.loans.add(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return null;
        } catch (FileNotFoundException e) {
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
