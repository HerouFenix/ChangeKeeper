package com.example.changekeeper;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class MoreDialogue extends AppCompatDialogFragment{

    private MoreDialogueListener listener;
    private String[] info;
    private String next;

    MoreDialogue(String[] row,String next){
        this.info = row;
        this.next = next;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_more_dialogue,null);

        builder.setView(view).setTitle("More details");

        TextView text = view.findViewById(R.id.amountText);
        text.setText(this.info[2] + "€");

        text = view.findViewById(R.id.destText);
        text.setText(this.info[1]);

        text = view.findViewById(R.id.regText);
        text.setText(this.info[3]);

        text = view.findViewById(R.id.nextText);
        text.setText(this.next);

        text = view.findViewById(R.id.freqText);
        text.setText(this.info[5] + " " + this.info[4] + "s");

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteIncome();
                dismiss();
            }
        });

        return builder.create();
    }

    public void deleteIncome(){
        listener.removeAllowance(this.info[0]);
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
        void removeAllowance(String id);
    }

}


