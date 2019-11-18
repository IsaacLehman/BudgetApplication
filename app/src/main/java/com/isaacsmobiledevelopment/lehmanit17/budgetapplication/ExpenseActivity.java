package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.nfc.FormatException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseActivity extends AppCompatActivity{

    private static final String TAG = "ExpenseActivity";
    private EditText amountET, noteET, dateET;
    private Spinner categoryS, subcategoryS;

    List<String> categorynames;
    List<String> subCategorynames;
    String currentCategory, currentSubcategory;

    private ArrayAdapter aa2;

    private UserInteraction ui;
    private final String CSV_FILENAME = "csvdata.txt", BACKUP_BUDGET_FILENAME = "budgetcategories.txt", USER_BUDGET_FILENAME = "userbudget.txt";

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        //String[] savedExtra = getIntent().getStringArrayExtra("filenames");
        SetUp();
        categorynames = new ArrayList<>(Arrays.asList(ui.getCategoryNames()));
        subCategorynames = new ArrayList<>();

        amountET = findViewById(R.id.moneyET);
        amountET.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountET.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        noteET = findViewById(R.id.noteET);
        dateET = findViewById(R.id.dateET);

        categoryS = findViewById(R.id.categorySP);
        subcategoryS = findViewById(R.id.subcategorySP);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,categorynames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        categoryS.setAdapter(aa);


        //Creating the ArrayAdapter instance having the bank name list
        aa2 = new ArrayAdapter(this, R.layout.spinner_item, subCategorynames);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        subcategoryS.setAdapter(aa2);

        categoryS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentCategory = parentView.getItemAtPosition(position).toString();

                subCategorynames = Arrays.asList(ui.getSubcategoryNames(currentCategory));
                //Log.e(TAG, "This is an error - "+currentCategory + " " + Arrays.toString(subCategorynames));
                try {
                    aa2.clear();
                    aa2.addAll(subCategorynames);
                    aa2.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "This is an error");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        subcategoryS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSubcategory = ui.getSubcategoryNames(currentCategory)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });



        findViewById(R.id.addexpenseB).setOnClickListener(view -> addExpense());

        // make calender popup for date picking
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExpenseActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    public void addExpense() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
        try {
            String money = ProcessMoneyTEXT(amountET.getText().toString());
            String date;
            if (dateET.getText() == null || dateET.getText().toString().equals("")) {
                Date d = new Date();
                date = d.toString();
            } else {
                date = dateET.getText().toString();
            }

            String category = currentCategory;
            String subcategory = currentSubcategory;
            String note = noteET.getText().toString();
            note = note.replace("\n", " ");
            note = note.replace(";", ", ");
            Expence expence = new Expence(date, category, subcategory, note, money);
            if (expence.getMoney().get() == 0) {
                throw new FormatException("zero Dollars");
            }
            ui.addExpence(expence);
            ui.updateFiles();

            amountET.setText(null);
            dateET.setText(null);
            noteET.setText(null);
        } catch (FormatException n) {
            Toast.makeText(getBaseContext(), "Enter Expense Amount",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Invalid Input",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage() + "hi");
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

    private void updateLabel() {
        String myFormat = "MM/dd/YYYY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }

    public static String ProcessMoneyTEXT(String in) {
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
}
