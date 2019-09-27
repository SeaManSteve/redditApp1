package com.example.stevenwinstanley.alpha05;

import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final String FILE_NAME = "invested.txt";
    RecyclerView mRecyView;
    String[] ary;
    ArrayList<post> postList;
    CircularProgressButton cir;
    ArrayList<Investment> investmentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StickySwitch invest = findViewById(R.id.sticky_switch);
        mRecyView = findViewById(R.id.reView);
        postList = new ArrayList<post>();
        investmentArrayList = new ArrayList<Investment>();
        cir = findViewById(R.id.but2);
        cir.setIndeterminateProgressMode(true);
        cir.setIdleText("Meme");
        cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new doIt().execute();
                new readInvest().execute();
                cir.setProgress(30);

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the camera action
        if (id == R.id.view_post) {
            Intent intent = new Intent(getApplicationContext(), InvestmentActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class doIt extends AsyncTask<Void, Void, Void> {
        String webText;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("http://seamansteve.pythonanywhere.com/").get();
                webText = doc.text();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ary = webText.split(" ");
            for (int i = 0; i < 10; i++) {
                String[] postInfo = ary[i].split(",");
                postList.add(new post(postInfo[0], Integer.parseInt(postInfo[1]), postInfo[2], Double.parseDouble(postInfo[3])));
            }
            cir.setProgress(100);
            cir.setCompleteText("Done!");
            loadPics();
            cir.setVisibility(View.GONE);
        }
    }

    private void loadPics() {

        mRecyView.setHasFixedSize(true);
        mRecyView.addItemDecoration(new DividerItemDecoration(mRecyView.getContext()
                , DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mRecyView.getContext()
                , LinearLayoutManager.VERTICAL, false);
        mRecyView.setLayoutManager(manager);
        pictureAdapter adapter = new pictureAdapter(postList, investmentArrayList);
        mRecyView.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mRecyView.getContext());
        Toast.makeText(this, "Your balence is " + prefs.getFloat("balence", 1000), Toast.LENGTH_LONG).show();
    }


    private class readInvest extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
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
            return null;
        }
    }

}
