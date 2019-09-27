package com.example.stevenwinstanley.alpha05;

import android.widget.EditText;

public class Investment {
    private String selectedPost;
    private float investmentAmount;
    private float returnAmount;

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
