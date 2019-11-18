package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubCategoryActivivty extends AppCompatActivity {

    private static final String TAG = "SubCategoryActivity";
    private UserInteraction ui;
    private final String CSV_FILENAME = "csvdata.txt", BACKUP_BUDGET_FILENAME = "budgetcategories.txt", USER_BUDGET_FILENAME = "userbudget.txt";
    private ListView lv;
    private ToggleButton tb;
    ToggleButton simpleToggleButton;
    String[] categorynames;
    String[] subCategorynames;
    String catName;
    int spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_activivty);

        catName = getIntent().getStringExtra("categoryName");
        spot = getIntent().getIntExtra("spot", 0);// 0 os the default value

        TextView tvTitle = findViewById(R.id.budgetCategoryTitleTV);
        tvTitle.setText(catName);

        simpleToggleButton = findViewById(R.id.month_yearT); // initiate a toggle button
        //Boolean ToggleButtonState = simpleToggleButton.isChecked(); // check current state of a toggle button (true or false).
        SetUp();

        updateListView();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String subcategoryName = categorynames[position];
                Log.e(TAG, "POSITION: " + position);
                Intent myIntent = new Intent(getBaseContext(),   EditBudgetActivity.class);
                myIntent.putExtra("categoryName", catName);
                myIntent.putExtra("subcategoryName", subcategoryName);
                myIntent.putExtra("Cspot", spot);
                myIntent.putExtra("sCspot", position);
                startActivity(myIntent);
            }

        });

        simpleToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setAdapter(null);
                subCategorynames = MakeSubCategoryData();
                categorynames = MakeCategoryData();
                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), categorynames, subCategorynames);
                lv.setAdapter(customAdapter);
            }
        });
    }

    public void SetUp() {
        try {
            ui = new UserInteraction(CSV_FILENAME, BACKUP_BUDGET_FILENAME, USER_BUDGET_FILENAME, getBaseContext());
            ui.sortExpences();
            ui.FillBudget_w_Expenses();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "file not made + error");
        }
    }

    public String[] MakeCategoryData() {
        int tf;
        if (simpleToggleButton.isChecked()) {
            tf = 1; // month
        } else {
            tf = 2; // year
        }
        String[] outData = new String[ui.budgetCategories.get(spot).getSubCategories().size()];

        for (int i = 0; i < ui.budgetCategories.get(spot).getSubCategories().size(); i++) {

            outData[i] = ui.budgetCategories.get(spot).getSubCategories().get(i).getSubcategory();
        }
        return outData;
    }

    public String[] MakeSubCategoryData() {
        int tf;
        if (simpleToggleButton.isChecked()) {
            tf = 1; // month
        } else {
            tf = 2; // year
        }
        List<String> outData = new ArrayList<>();

        for (int i = 0; i < ui.budgetCategories.get(spot).getSubCategories().size(); i++) {
            Money ba = ui.budgetCategories.get(spot).getSubCategories().get(i).getBudgetAmount(tf);
            Money sa = ui.budgetCategories.get(spot).getSubCategories().get(i).getTotalSpentCurrent(tf);
            Money da = ui.difference(ba, sa);
            String makeD = "";
            makeD +=   padRight("budget:".toUpperCase(), 20) + ba;
            makeD +=  "\n" + padRight("Spent:".toUpperCase(),22) + sa;
            makeD +=  "\n" + padRight("Difference:".toUpperCase(),16) + da;
            outData.add(makeD);
        }
        return outData.toArray(new String[0]);
    }
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }


    public  void updateListView() {
        subCategorynames = MakeSubCategoryData();
        categorynames = MakeCategoryData();

        lv = findViewById(R.id.listViewUserB);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), categorynames, subCategorynames);
        lv.setAdapter(customAdapter);
    }

    public void onDestroy() {
        try {
            ui.updateFiles();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage() + " destroy press");
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            ui.updateFiles();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage() + " back press error");
        }
        finish();
    }

    @Override
    public void onRestart() {
        try {
            ui = new UserInteraction(CSV_FILENAME, BACKUP_BUDGET_FILENAME, USER_BUDGET_FILENAME, getBaseContext());
            ui.FillBudget_w_Expenses();
            updateListView();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG," restart error");
        }
        super.onRestart();
    }
}
