package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;

public class EditBudgetActivity extends AppCompatActivity {

    private static final String TAG = "SubCategoryActivity";
    private UserInteraction ui;
    private final String CSV_FILENAME = "csvdata.txt", BACKUP_BUDGET_FILENAME = "budgetcategories.txt", USER_BUDGET_FILENAME = "userbudget.txt";
    private ListView lv;
    private ToggleButton tb;
    private EditText amountET;
    ToggleButton simpleToggleButton;
    String[] expenseAmountData;
    String[] expenseInfoData;
    String catName, scatName;
    int Cspot, sCspot;
    int timeframe;
    RadioGroup rg;
    TextView expenseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);
        // get extras
        catName = getIntent().getStringExtra("categoryName");
        scatName = getIntent().getStringExtra("subcategoryName");
        Cspot = getIntent().getIntExtra("Cspot", 0);// 0 os the default value
        sCspot = getIntent().getIntExtra("sCspot", 0);
        timeframe = 1;

        TextView tv = findViewById(R.id.TitleTV);
        String title = catName + " - " + scatName;
        tv.setText(title);

        expenseTV = findViewById(R.id.expenseTV);

        rg = findViewById(R.id.timeSwithcRG);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rg.getChildAt(0).getId() == i) {
                    timeframe = 1;
                } else if (rg.getChildAt(1).getId() == i) {
                    timeframe = 2;
                } else {
                    timeframe = 3;
                }
                updateListView();
                Log.e(TAG, "Item Selected: " + rg.getChildAt(2).getId()+ " " + i);
            }
        });


        SetUp();

        // inputs

        amountET = findViewById(R.id.budgetAmountET);
        amountET.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountET.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amountET.setText(ProcessMoneyTEXT(ui.budgetCategories.get(Cspot).getSubCategories().get(sCspot).getBudgetAmount(1).toString()));

        //Log.e(TAG, "AMOUNT " + ProcessMoneyTEXT(ui.budgetCategories.get(Cspot).getSubCategories().get(sCspot).getBudgetAmount(1).toString()));
        //----


        updateListView();
        findViewById(R.id.updateBudgetB).setOnClickListener(view -> UpdateBudget());
    }

    public void updateListView() {
        if (ui.dBackend.getDataTimeframe(ui.expences, catName, scatName, timeframe) != null) {
            expenseAmountData = MakeExpenseAmountData();
            expenseInfoData = MakeExpenseInfoData();

            lv = findViewById(R.id.listViewUserB);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), expenseAmountData, expenseInfoData);
            lv.setAdapter(customAdapter);
        }
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

    public String[] MakeExpenseAmountData() {
        ArrayList<Expence> data = ui.dBackend.getDataTimeframe(ui.expences, catName, scatName, timeframe);
        Money pT = ui.dBackend.getPurchaceTotal(data);
        expenseTV.setText("Expenses: " + pT.toString());
        String[] out = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            out[i] = data.get(i).getMoney().toString();
        }
        return out;
    }

    public void UpdateBudget() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
        ui.budgetCategories.get(Cspot).getSubCategories().get(sCspot).setBudgetAmount(new Money(ProcessMoneyTEXT(amountET.getText().toString())), timeframe);
        try {
            ui.updateFiles();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR: unable to update file",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage() + " back press error");
        }
    }

    public String[] MakeExpenseInfoData() {
        ArrayList<Expence> data = ui.dBackend.getDataTimeframe(ui.expences, catName, scatName, timeframe);
        String[] out = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            String d = data.get(i).getDate().toString();
            if (!data.get(i).getDescription().equals("")) {
                //d += "\nNote: " + data.get(i).getDescription();
                d += "  -  " + data.get(i).getDescription();
            }
            out[i] = d;
        }
        return out;
    }

    public String ProcessMoneyTEXT(String in) {
        if(in.contains("$")) {
            return in.substring(1);
        }

        if (in.indexOf(".") == in.length() - 1) {
            return "0.0";
        } else if (in.length() <= in.indexOf('.')+1) {
            return in + "0";
        } else if (in.charAt(0) == '.') {
            return "0" + in;
        } else if (!in.contains(".")) {
            return in + ".0";
        }else {
            return in;
        }
    }

    @Override
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
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG," restart error");
        }
        super.onRestart();
    }
}
