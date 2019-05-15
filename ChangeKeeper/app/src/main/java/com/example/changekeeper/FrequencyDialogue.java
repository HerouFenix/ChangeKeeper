package com.example.changekeeper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class FrequencyDialogue extends AppCompatDialogFragment{
    //Class used to create a new category for incomes/expenses
    private FrequencyDialogueListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_frequencydialogue,null);

        Spinner spinner = (Spinner) view.findViewById(R.id.freqPick);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                    R.array.frequencies2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view).setTitle("Create a custom frequency...")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String frequency = ((TextView)view.findViewById(R.id.regText)).getText().toString();
                        String type = spinner.getSelectedItem().toString();
                        ArrayList<String> days = new ArrayList<>();
                        if(view.findViewById(R.id.mondayButton).isSelected())
                            days.add("Monday");
                        if(view.findViewById(R.id.tuesdayButton).isSelected())
                            days.add("Tuesday");
                        if(view.findViewById(R.id.wednesdayButton).isSelected())
                            days.add("Wednesday");
                        if(view.findViewById(R.id.thursdayButton).isSelected())
                            days.add("Thursday");
                        if(view.findViewById(R.id.fridayButton).isSelected())
                            days.add("Friday");
                        if(view.findViewById(R.id.saturdayButton).isSelected())
                            days.add("Saturday");
                        if(view.findViewById(R.id.sundayButton).isSelected())
                            days.add("Sunday");

                        listener.updateFrequencies(frequency,type,days);
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FrequencyDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((context.toString() + "Did not implement FrequencyDialogueListener"));
        }
    }

    public interface FrequencyDialogueListener{
        void updateFrequencies(String frequency,String type, ArrayList<String> weekdays);
    }

}


