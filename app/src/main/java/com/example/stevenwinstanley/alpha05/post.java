package com.example.stevenwinstanley.alpha05;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class post {
    public String url;
    public int upVotes;
    public String id;
    public boolean isInvested;
    public double ratio;


    public post(String URL, int UPVOTES, String ID, double RATIO) {
        this.url = URL;
        this.upVotes = UPVOTES;
        this.id = ID;
        this.ratio = RATIO;
    }
}