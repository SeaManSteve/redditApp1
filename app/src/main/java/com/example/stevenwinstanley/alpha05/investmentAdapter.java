package com.example.stevenwinstanley.alpha05;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class investmentAdapter extends RecyclerView.Adapter<investmentAdapter.MyViewHolder>{
    ArrayList<Investment> investmentArrayList;
    static final String FILE_NAME = "invested.txt";

    public investmentAdapter(ArrayList<Investment> invest){
        this.investmentArrayList = invest;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.invest_screen, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.investView.setText(investmentArrayList.get(i).toString());

    }

    @Override
    public int getItemCount() {
        return investmentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView investView;
        public MyViewHolder(TextView itemView) {
            super(itemView);
            investView = itemView;
        }
    }
}





