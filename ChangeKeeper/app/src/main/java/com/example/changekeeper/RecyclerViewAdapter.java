package com.example.changekeeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> infoList;
    private Context mContext;

    public static final String EXTRA_MESSAGE = "com.example.RecyclerViewAdapter.MESSAGE";


    public RecyclerViewAdapter(ArrayList<String> infoList, Context context){
        this.infoList = infoList;
        this.mContext = context;
        Log.i(TAG,"BOOOOOOI" + this.mContext.getClass().toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_info_table_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.i(TAG,"onBindViewHolder: called");

        //Format: WALLET/CARD - Amount - Date - Person(NULL UNLESS LOAN) - Category (NULL UNLESS EXPENSE)- FrequencyType (NULL IF LOAN) - Frequency (NULL IF LOAN) - Weekdays (NULL IF LOAN) - Description
        Log.i(TAG,"ELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + infoList.get(position));
        viewHolder.description.setText(infoList.get(position).split(" - ")[8]);
        viewHolder.date.setText(infoList.get(position).split(" - ")[2]);

        String amount = infoList.get(position).split(" - ")[1];
        viewHolder.amount.setText(amount);

        if(amount.contains("-")){
            viewHolder.amount.setTextColor(Color.parseColor("#e74c3c"));
        }else{
            viewHolder.amount.setTextColor(Color.parseColor("#2ecc71"));
        }

        viewHolder.type.setText(infoList.get(position).split(" - ")[0]);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,infoList.get(position));

                if(!infoList.get(position).split(" - ")[3].equals("NULL")){
                    //Loan
                    Log.i(TAG,"GOING TO LOAN DETAILS");
                    Intent intent;
                    if(mContext.getClass().equals(GraphsScreen.class))
                        intent  = new Intent(mContext, GraphsLoanMoreInfoScreen.class);
                    else
                        intent = new Intent(mContext, LoansLoanMoreInfoScreen.class);

                    String message = infoList.get(position);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    mContext.startActivity(intent);
                }
                else if(!infoList.get(position).split(" - ")[4].equals("NULL")){
                    //Expense
                    Log.i(TAG,"GOING TO EXPENSE DETAILS");
                    Intent intent;
                    if(mContext.getClass().equals(GraphsScreen.class))
                        intent  = new Intent(mContext, GraphsSubscriptionMoreInfoScreen.class);
                    else
                        intent = new Intent(mContext, SubscriptionMoreInfoScreen.class);

                    String message = infoList.get(position);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    mContext.startActivity(intent);
                }
                else{
                    //Income
                    Log.i(TAG,"GOING TO INCOME DETAILS");
                    Intent intent;
                    if(mContext.getClass().equals(GraphsScreen.class))
                       intent  = new Intent(mContext, GraphsAllowanceMoreInfoScreen.class);
                    else
                        intent = new Intent(mContext, AllowanceMoreInfoScreen.class);
                    String message = infoList.get(position);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    mContext.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.infoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        TextView date;
        TextView amount;
        TextView type;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.regText);
            date = itemView.findViewById(R.id.regDate);
            amount = itemView.findViewById(R.id.amount);
            type = itemView.findViewById(R.id.type);
            parentLayout = itemView.findViewById(R.id.infoTableItem);

        }
    }
}
