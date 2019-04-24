package com.example.changekeeper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class MoreDialogueLoans extends AppCompatDialogFragment{
    private static final String TAG = "MoreDialogue";

    private MoreDialogueListener listener;
    private String[] info;
    private String type;

    MoreDialogueLoans(String[] row,String type){
        this.type = type;
        this.info = row;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_more_dialogue_loans,null);

        builder.setView(view).setTitle("More details");
        //Format: ID - WALLET/CARD - Amount - Person - Date - Description
        TextView text = view.findViewById(R.id.amountText);
        text.setText(this.info[2] + "€");

        text = view.findViewById(R.id.destText);
        text.setText(this.info[1]);

        text = view.findViewById(R.id.personText);
        text.setText(this.info[3]);
        Log.i(TAG,"HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOe");

        text = view.findViewById(R.id.paydayText);
        text.setText(this.info[4]);

        text = view.findViewById(R.id.descriptionText);
        text.setText(this.info[5]);

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLoan();
                dismiss();
            }
        });

        return builder.create();
    }

    public void deleteLoan(){
        listener.removeLoan(this.info[0],this.type);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MoreDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((context.toString() + "Did not implement CategoryDialogueListener"));
        }
    }

    public interface MoreDialogueListener{
        void removeLoan(String id,String type);
    }

}


