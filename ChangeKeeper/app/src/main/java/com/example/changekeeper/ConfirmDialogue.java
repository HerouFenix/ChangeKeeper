package com.example.changekeeper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class ConfirmDialogue extends AppCompatDialogFragment{
    //Class used to confirm
    private String walletAmount;
    private String cardAmount;
    private String current;
    private String amount;
    private ConfirmDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_dialogue,null);

        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.confirm();
                    }
                });

        readFile();
        Bundle args = getArguments();

        switch(args.getString("dest")){
            case "WALLET":
                ((TextView)view.findViewById(R.id.currentText)).setText("Current Money in wallet:");
                this.current = this.walletAmount;
                break;
            case "CARD":
                ((TextView)view.findViewById(R.id.currentText)).setText("Current Money in card:");
                this.current = this.cardAmount;
                break;
        }

        this.amount = args.getString("amount");

        ((TextView)view.findViewById(R.id.current)).setText(current + "€");
        ((TextView)view.findViewById(R.id.registered)).setText(amount + "€");
        double finalAmount = 0;
        switch(args.getString("type")){
            case "EXPENSE":
                finalAmount = Double.parseDouble(this.current) - Double.parseDouble(this.amount);

                break;
            case "INCOME":
                finalAmount = Double.parseDouble(this.current) + Double.parseDouble(this.amount);
                this.current = this.cardAmount;
                break;
        }

        ((TextView)view.findViewById(R.id.moneyAfter)).setText(finalAmount + "€");

        try{
            String date = args.getString("regDate");
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String currentDate = day+"/"+month+"/"+year;

            if(!date.equals(currentDate)){
                TextView warning = new TextView(this.getContext());
                warning.setText("Money will only be altered in " + date);
                ((LinearLayout)view.findViewById(R.id.layout)).addView(warning);
            }
        }catch(Exception e){}
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((context.toString() + "Did not implement CategoryDialogueListener"));
        }
    }

    public interface ConfirmDialogListener{
        void confirm();
    }

    private void readFile() {
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("UserMoney.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            this.walletAmount = bufferedReader.readLine();
            this.cardAmount = bufferedReader.readLine();

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


