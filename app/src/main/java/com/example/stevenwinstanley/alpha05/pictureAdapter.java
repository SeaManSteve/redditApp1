package com.example.stevenwinstanley.alpha05;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_APPEND;

public class pictureAdapter extends RecyclerView.Adapter<pictureAdapter.MyViewHolder> {

    static final String FILE_NAME = "invested.txt";
    private ArrayList<post> postsList;
    private ArrayList<Investment> investmentArrayList;
    boolean isRun = true;

    public pictureAdapter(ArrayList<post> postss, ArrayList<Investment> investments) {
        this.postsList = postss;
        this.investmentArrayList = investments;

    }

    @NonNull
    @Override
    public pictureAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.postdisplay, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final pictureAdapter.MyViewHolder myViewHolder, int i) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(myViewHolder.imgView.getContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        myViewHolder.mTxtpostUp.setText("" + Integer.toString(postsList.get(i).upVotes));
        myViewHolder.mTxtRate.setText(("" + postsList.get(i).ratio));
        for (Investment investment : investmentArrayList) {
            if (investment.getSelectedPost().contains(postsList.get(i).id)) {
                postsList.get(i).isInvested = true;
            }
        }

        Picasso.get().load(postsList.get(i).url)
                .into(myViewHolder.imgView);
        if (postsList.get(i).isInvested) {
            myViewHolder.stickySwitch.setDirection(StickySwitch.Direction.RIGHT);

        } else {
            myViewHolder.stickySwitch.setDirection(StickySwitch.Direction.LEFT);
        }
        if (myViewHolder.stickySwitch.getDirection() == StickySwitch.Direction.RIGHT) {
            myViewHolder.stickySwitch.setEnabled(false);
        } else {
            myViewHolder.stickySwitch.setEnabled(true);
        }
        myViewHolder.stickySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                View newView = LayoutInflater.from(view.getContext()).inflate(R.layout.invest_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(newView);
                final EditText userIn = newView.findViewById(R.id.investAmount);
                final boolean[] intrest = {false};
                builder.setCancelable(true)
                        .setPositiveButton("Invest!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                float temp = sharedPreferences.getFloat("balence", 1000);
                                if (temp < Float.valueOf(userIn.getText().toString().trim())) {
                                    Toast.makeText(myViewHolder.imgView.getContext(), "Not enough balence", Toast.LENGTH_LONG).show();
                                    myViewHolder.stickySwitch.setDirection(StickySwitch.Direction.LEFT);
                                } else {
                                    intrest[0] = true;
                                    postsList.get(myViewHolder.getAdapterPosition()).isInvested = true;
                                    myViewHolder.stickySwitch.setEnabled(false);
                                    Investment tempInvest = new Investment(postsList.get(myViewHolder.getAdapterPosition()).id, Float.parseFloat(userIn.getText().toString().trim()));
                                    temp = temp - tempInvest.getInvestmentAmount();
                                    editor.putFloat("balence", temp);
                                    editor.apply();
                                    if (temp == 0) {
                                        Toast.makeText(myViewHolder.imgView.getContext(), "G O I N G  A L L  I N!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(myViewHolder.imgView.getContext(), "Balence of account is " + String.valueOf(temp), Toast.LENGTH_LONG).show();
                                    }
                                    createTime(myViewHolder.imgView.getContext(), postsList.get(myViewHolder.getAdapterPosition()).id);
                                    writeInvestToFile(tempInvest, myViewHolder.imgView.getContext());
                                }
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                myViewHolder.stickySwitch.setDirection(StickySwitch.Direction.LEFT);
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void createTime(Context context, String id) {

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("myKey", id);
        ComponentName name = new ComponentName(context.getApplicationContext(), JobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(id.hashCode(), name)
                .setMinimumLatency(5000)
                .setExtras(bundle);

        JobInfo myJob = builder.build();
        android.app.job.JobScheduler scheduler = (android.app.job.JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

        scheduler.schedule(myJob);


    }



    private void writeInvestToFile(Investment tempInvest, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fos);
        printWriter.println(tempInvest.toString());
        printWriter.close();
    }
    @Override
    public int getItemCount() {
        return postsList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtpostUp;
        private TextView mTxtRate;
        private ImageView imgView;
        private StickySwitch stickySwitch;

        public MyViewHolder(LinearLayout layout) {
            super(layout);
            mTxtpostUp = layout.findViewById(R.id.updoot);
            mTxtRate = layout.findViewById(R.id.rate);
            imgView = layout.findViewById(R.id.imageView);
            stickySwitch = layout.findViewById(R.id.sticky_switch);
        }
    }


}

