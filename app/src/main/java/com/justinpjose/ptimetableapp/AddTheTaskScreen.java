package com.justinpjose.ptimetableapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTheTaskScreen extends AppCompatActivity {
    EditText tasktitle_et;
    TextView taskday_et;
    TextView tasktime_et;
    TextView tasknote_et;
    TextView tvErrorMessage;
    Button sendTaskData;
    String sTitle, sDate, sTime, sNote;
    mDbHelper mDbHelper;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_the_task_screen);
        setTitle("Add The Task");

        count = getIntent().getIntExtra("Dbcount",0);
        count += 1;

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        tasktitle_et = findViewById(R.id.tasktitle_et);
        taskday_et = findViewById(R.id.taskday_et);
        tasktime_et = findViewById(R.id.tasktime_et);
        tasknote_et = findViewById(R.id.tasknote_et);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        sendTaskData = findViewById(R.id.sendTaskData);



        String email = mFirebaseUser.getEmail();
        mDbHelper = new mDbHelper(this, email, null, 1);

        sendTaskData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle = tasktitle_et.getText().toString();
                sDate = taskday_et.getText().toString();
                sTime = tasktime_et.getText().toString();
                sNote = tasknote_et.getText().toString();
                if (sTitle.isEmpty() || sDate.isEmpty() || sTime.isEmpty()) {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (sNote.isEmpty()) {
                    sNote = "Note was left blank.";
                }
                mDbHelper.insertData(sTitle, sDate, sTime, sNote);
                Log.d("justinerror1","\n"+sTitle+"\n"+sDate+"\n"+sTime+"\n"+sNote);
                Intent intent = new Intent(AddTheTaskScreen.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }

    public void datePickerDialog(View view) {
        Toast.makeText(this, "Pick your day", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddTheTaskScreen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_pickdate, viewGroup, false);
        DatePicker simpleDatePicker = dialogView.findViewById(R.id.datepicker);
        Button setdate_btn = dialogView.findViewById(R.id.setdate_btn);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Date");
        setdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String newDate = "", newMonth = "";
                int date = simpleDatePicker.getDayOfMonth();
                int month = simpleDatePicker.getMonth();
                int year = simpleDatePicker.getYear();
                month += 1;
                newDate = String.valueOf(date);
                newMonth = String.valueOf(month);
                if (date < 10) {
                    newDate = "0"+date;
                }
                if (month < 10) {
                    newMonth = "0"+month;
                }
                String s = newDate + "/" + newMonth + "/" + year;
                taskday_et.setText(s);
            }
        });
        alertDialog.show();
    }

    public void timePickerDialog(View view) {
        Toast.makeText(this, "Pick your time", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddTheTaskScreen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_picktime, viewGroup, false);
        TimePicker timepicker = dialogView.findViewById(R.id.timepicker);
        Button settime_btn = dialogView.findViewById(R.id.settime_btn);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Time");
        settime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String postfix = "AM";
                String newHour = "";
                int hour = timepicker.getHour();
                String minute = String.valueOf(timepicker.getMinute());
                if (minute.equals("0")) {
                    minute = "00";
                }
                if (hour > 12) {
                    hour -= 12;
                    postfix = "PM";
                }
                if (hour == 0) {
                    hour = 12;
                    postfix = "AM";
                }
                if (minute.length()==1) {
                    minute = "0"+minute;
                }
                newHour = String.valueOf(hour);
                if (hour < 10) {
                    newHour = "0"+hour;
                }
                String s = newHour + ":" + minute + " " + postfix;
                tasktime_et.setText(s);
            }
        });
        alertDialog.show();
    }
}