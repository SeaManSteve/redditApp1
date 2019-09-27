package com.example.stevenwinstanley.alpha05;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InvestmentActivity extends Activity {
    ArrayList<Investment> investmentArrayList;
    public static final int NOTIFICATION_TAPPED = 1;
    public static final int NOTIFICATION_DISMISSED = 2;
    RecyclerView investments;
    static final String FILE_NAME = "invested.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        investments = findViewById(R.id.invest_view);
        investmentArrayList = new ArrayList<Investment>();
        Intent intent = getIntent();

        if (intent != null) {
            String data = intent.getStringExtra("data");
            ((TextView)findViewById(R.id.txt_display)).setText(data);
        }
        loadInvestments();
        setAdapter();
    }

    private void setAdapter() {
        investments.setHasFixedSize(true);
        investments.addItemDecoration(new DividerItemDecoration(investments.getContext()
                , DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(investments.getContext()
                , LinearLayoutManager.VERTICAL, false);
        investments.setLayoutManager(manager);
        investmentAdapter adapter = new investmentAdapter(investmentArrayList);
        investments.setAdapter(adapter);
    }

    private void loadInvestments() {
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


    }
}
