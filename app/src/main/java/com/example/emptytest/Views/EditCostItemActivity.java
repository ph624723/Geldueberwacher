package com.example.emptytest.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emptytest.R;
import com.example.emptytest.datamanagement.Categories;
import com.example.emptytest.datamanagement.CostItem;
import com.example.emptytest.datamanagement.DatabaseHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditCostItemActivity extends AppCompatActivity {
    public static final String uidKey = "uid";
    public static final String categoryKey = "categoryId";
    public static final int createRequestCode = 1;
    public static final int editRequestCode = 2;
    public static final int changedResultCode = 1;
    public static final int unchangedResultCode = 0;

    private int editMode;
    private boolean dbChanged = false;
    private CostItem costItem;
    private DatabaseHandler dbHandler;

    EditText subjectTextBox;
    EditText valueTextBox;
    CalendarView calendarView;
    TextView categoryTextView;
    Button confirmButton;

    private static String errorString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cost_item);

        Bundle extras = getIntent().getExtras();
        dbHandler = DatabaseHandler.getmInstance(null);

        subjectTextBox = findViewById(R.id.subjectTextBox);
        valueTextBox = findViewById(R.id.valueTextBox);
        calendarView = findViewById(R.id.calendarView);
        categoryTextView = findViewById(R.id.categoryTextView);
        confirmButton = findViewById(R.id.confirmItemButton2);

        if (extras.getInt(uidKey) != 0) {
            int uid = extras.getInt(uidKey);
            try {
                costItem = dbHandler.getById(uid);
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
                Log.d("ERROR", "CostItem with id " + uid + " could not be loaded.");
                finish();
            }
            subjectTextBox.setText(costItem.getSubject());
            valueTextBox.setText((costItem.getValue()+"").replace("+",""));
            editMode = editRequestCode;
        } else {
            int catId = extras.getInt(categoryKey);
            costItem = new CostItem("default", 0.0, Categories.getCategory(catId));
            editMode = createRequestCode;
            confirmButton.setEnabled(false);
            confirmButton.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        }
        String categoryName = "";
        if (costItem.getCategory().equals(Categories.OFFICIAL)) categoryName = getResources().getString(R.string.cat_key_official);
        else if (costItem.getCategory().equals(Categories.NECESSARY)) categoryName = getResources().getString(R.string.cat_key_necessary);
        else if (costItem.getCategory().equals(Categories.FUN)) categoryName = getResources().getString(R.string.cat_key_fun);

        categoryTextView.setText(errorString == null ? categoryName : errorString);
        calendarView.setDate(costItem.getDate().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                costItem.setDate(new GregorianCalendar(year, month, dayOfMonth).getTime());
                Log.d("DATE", costItem.dateString());
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(subjectTextBox.getText().toString().length() == 0 || valueTextBox.getText().toString().length() == 0){
                    confirmButton.setEnabled(false);
                    confirmButton.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                }else if (!confirmButton.isEnabled()){
                    confirmButton.setEnabled(true);
                    confirmButton.setTextColor(getResources().getColor(R.color.white));
                }
            }
        };
        subjectTextBox.addTextChangedListener(textWatcher);
        valueTextBox.addTextChangedListener(textWatcher);
    }

    public void onConfirmClick(View view) {
        costItem.setSubject(subjectTextBox.getText().toString());
        costItem.setValue(Double.parseDouble(valueTextBox.getText().toString()));

        if (editMode == editRequestCode) {
            try {
                dbHandler.update(costItem);
            }catch (Exception e){
                errorString = e.getMessage();
            }
        }
        else {
            dbHandler.insertCostItem(costItem);
        }
        dbChanged = true;
        sendOffResults();
    }

    public void onAbortClick(View view){
        sendOffResults();
    }

    public void sendOffResults() {
        Intent intent = new Intent();
        //intent.putExtra("MESSAGE",message);
        //setResult(2,intent);
        setResult(dbChanged ? changedResultCode : unchangedResultCode);
        Log.d("Tag", "Finishing with changed mode = " + dbChanged);
        if (dbChanged) Toast.makeText(this, getResources().getString(R.string.disclaimer_saved_changes), Toast.LENGTH_SHORT).show();
        finish();
    }
}
