package com.example.stevenwinstanley.alpha05;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class JobScheduler extends JobService {
    String message;
    static final String FILE_NAME = "invested.txt";


    @Override
    public boolean onStartJob(JobParameters params) {
        message = params.getExtras().getString("myKey");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        new writeToFile().execute();
        return false;


    }

    private class writeToFile extends AsyncTask<String, Void, Void>{
        ArrayList<Investment> investmentArrayList = new ArrayList<Investment>();


        @Override
        protected Void doInBackground(String... voids) {


            FileInputStream fis;
            fis = null;
            String fileText[];

            try {
                fis = openFileInput(FILE_NAME);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Scanner scanner;
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                fileText = scanner.nextLine().split(" ");
                investmentArrayList.add(new Investment(fileText[0], Float.parseFloat(fileText[1])));
            }
            FileOutputStream fos = null;
            for (int i = 0;i < investmentArrayList.size(); i++){
                if (investmentArrayList.get(i).getSelectedPost().contains(message)){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    float temp = prefs.getFloat("balence", 1000);
                    temp += investmentArrayList.get(i).getInvestmentAmount()* 1.5;
                    editor.putFloat("balence", temp);
                    editor.apply();
                    investmentArrayList.remove(i);
                }
            }
            try {
                fos = getApplicationContext().openFileOutput(FILE_NAME, MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.write("");
            for (int i = 0; i < investmentArrayList.size(); i++) {
                printWriter.println(investmentArrayList.get(i).toString());
            }
            printWriter.close();
            return null;
        }




        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(getApplicationContext(), InvestmentActivity.class);
            PendingIntent contentIntent =
                    TaskStackBuilder.create(getApplicationContext())
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(InvestmentActivity.NOTIFICATION_TAPPED, PendingIntent.FLAG_UPDATE_CURRENT);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentText("New Return brings your balence to " + prefs.getFloat("balence", 1000))
                    .setContentTitle("WE'RE IN THE MONEY")
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.fatbils_24dp)
                    .setAutoCancel(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = builder.build();
            manager.notify(message.hashCode() , notification);

            super.onPostExecute(aVoid);

        }


    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
