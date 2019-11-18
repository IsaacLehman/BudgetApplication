package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText inET;
    private TextView outTV;
    private UserInteraction ui;
    private final String CSV_FILENAME = "csvdata.txt"
            , BACKUP_BUDGET_FILENAME = "budgetcategories.txt"
            , USER_BUDGET_FILENAME = "userbudget.txt";
    private final String[] FILE_NAMES = {CSV_FILENAME, BACKUP_BUDGET_FILENAME, USER_BUDGET_FILENAME};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        SetUp();

        outTV = findViewById(R.id.outputTV);
        inET = findViewById(R.id.inputET);
        inET.setFilters(new InputFilter[] {new CurrencyFormatInputFilter()});
        inET.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Set the onClick listeners for the button bar.
        findViewById(R.id.expensesB).setOnClickListener(view -> GotoExpenses());
        findViewById(R.id.loadB).setOnClickListener(view -> printExpenses());
        findViewById(R.id.userbudgetB).setOnClickListener(view -> GotoUserBudgetCategories());
    }



    public void SetUp() {
        try {
            ui = new UserInteraction(CSV_FILENAME, BACKUP_BUDGET_FILENAME, USER_BUDGET_FILENAME, getBaseContext());
            ui.FillBudget_w_Expenses();
            ui.sortExpences();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG,"file not made + error");
        }
    }

    public void GotoExpenses() {
        try {
            ui.updateFiles();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage() + "hi");
        }
        Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
        //intent.putExtra("filenames", FILE_NAMES);
        startActivity(intent);
    }

    public void GotoUserBudgetCategories() {
        try {
            ui.updateFiles();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage() + "hi");
        }
        Intent intent = new Intent(MainActivity.this, UserBudgetActivity.class);
        //intent.putExtra("filenames", FILE_NAMES);
        startActivity(intent);
    }


    public void printExpenses() {
        try {
            //ui.dBackend.resetUserBudgetFile();
           // ui.dBackend.csvFile.clearFile();
            ui.sortExpences();
            outTV.setText(ui.dBackend.getPurchaceTotal(ui.expences).toString() + "\n" + ui.dBackend.csvFile.fileTools.getText().toString());
            //outTV.setText(ui.dBackend.getPurchaceTotal(ui.expences).toString() + "\n" + ui.expences);
            //outTV.setText(ui.dBackend.getPurchaceTotal(ui.expences).toString() + "\n" + ui.dBackend.getData(ui.expences,"Income","Salary"));

        } catch (Exception e) {
            Log.e(TAG,e.getMessage() + "hi");
        }
       // outTV.setText(ui.dBackend.getPurchaceTotal(ui.expences).toString());
        //outTV.setText(ProcessMoneyTEXT(inET.getText().toString()));
    }

//    @Override
//    public void onDestroy() {
//        try {
//            ui.updateFiles();
//        } catch (IOException e) {
//            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
//                    Toast.LENGTH_LONG).show();
//            Log.e(TAG,e.getMessage());
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        try {
//            ui.updateFiles();
//        } catch (IOException e) {
//            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
//                    Toast.LENGTH_LONG).show();
//            Log.e(TAG,e.getMessage());
//        }
//    }

    @Override
    public void onRestart() {
        try {
            ui = new UserInteraction(CSV_FILENAME, BACKUP_BUDGET_FILENAME, USER_BUDGET_FILENAME, getBaseContext());
            ui.FillBudget_w_Expenses();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG," restart error");
        }
        super.onRestart();
    }

}
