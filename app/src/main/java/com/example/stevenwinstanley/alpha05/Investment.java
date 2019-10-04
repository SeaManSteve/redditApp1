package com.example.stevenwinstanley.alpha05;

import android.widget.EditText;

//class for user investment
public class Investment {
    //selected postID
    private String selectedPost;
    // chosen Amount of points
    private float investmentAmount;
    //generated return
    private float returnAmount;
    //ctor 
    public Investment(String POST, float INVESTMENTAMOUNT){
        this.selectedPost = POST;
        this.investmentAmount = INVESTMENTAMOUNT;
    }

    public float getInvestmentAmount() {
        return investmentAmount;
    }

    public String getSelectedPost() {
        return selectedPost;
    }

    public float getReturnAmount() {
        return returnAmount;
    }

    @Override
    public String toString() {
        return selectedPost + " " + Float.toString(investmentAmount);
    }
}
