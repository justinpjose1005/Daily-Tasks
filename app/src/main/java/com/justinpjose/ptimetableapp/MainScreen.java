package com.justinpjose.ptimetableapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainScreen extends AppCompatActivity implements AdapterRv1.ItemClickListener, AdapterRv2.ItemClickListener, AdapterRv3.ItemClickListener {
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    AdapterRv1 adapter1;
    AdapterRv2 adapter2;
    AdapterRv3 adapter3;
    mDbHelper mDbHelper;
    List<String> todays_titles = new ArrayList<>();
    List<String> todays_dates = new ArrayList<>();
    List<String> todays_times = new ArrayList<>();
    List<String> todays_notes = new ArrayList<>();
    List<String> tomorrows_titles = new ArrayList<>();
    List<String> tomorrows_dates = new ArrayList<>();
    List<String> tomorrows_times = new ArrayList<>();
    List<String> tomorrows_notes = new ArrayList<>();
    List<String> upcoming_titles = new ArrayList<>();
    List<String> upcoming_dates = new ArrayList<>();
    List<String> upcoming_times = new ArrayList<>();
    List<String> upcoming_notes = new ArrayList<>();
    int mDbSize;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String mDate;
    LinearLayout ll1, ll2, ll3, ll4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // User is signed out
            Intent i = new Intent(MainScreen.this, LoginScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed in
            String email = mFirebaseUser.getEmail();
            mDbHelper = new mDbHelper(this, email, null, 1);
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            mDate = dateFormat.format(calendar.getTime());
            mDbSize = mDbHelper.getCount();
            callDbDataAndUpdateRecyclerView();
        }
    }

    private void callDbDataAndUpdateRecyclerView() {
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);

        todays_titles = mDbHelper.getTodaysTitles(mDate);
        todays_dates = mDbHelper.getTodaysDates(mDate);
        todays_times = mDbHelper.getTodaysTimes(mDate);
        todays_notes = mDbHelper.getTodaysNotes(mDate);
        int size1 = todays_titles.size();
        if (size1 == 0) {
            ll1.setVisibility(View.GONE);
        }
        else {
            ll1.setVisibility(View.VISIBLE);
            RecyclerView recyclerView1 = findViewById(R.id.recyclerview1);
            recyclerView1.setLayoutManager(new LinearLayoutManager(this));
            adapter1 = new AdapterRv1(this, todays_titles, todays_dates, todays_times, todays_notes);
            adapter1.setClickListener(this);
            recyclerView1.setAdapter(adapter1);
        }

        tomorrows_titles = mDbHelper.getTomorrowsTitles(mDate);
        tomorrows_dates = mDbHelper.getTomorrowsDates(mDate);
        tomorrows_times = mDbHelper.getTomorrowsTimes(mDate);
        tomorrows_notes = mDbHelper.getTomorrowsNotes(mDate);
        int size2 = tomorrows_titles.size();
        if (size2 == 0) {
            ll2.setVisibility(View.GONE);
        }
        else {
            ll2.setVisibility(View.VISIBLE);
            RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            adapter2 = new AdapterRv2(this, tomorrows_titles, tomorrows_dates, tomorrows_times, tomorrows_notes);
            adapter2.setClickListener(this);
            recyclerView2.setAdapter(adapter2);
        }

        upcoming_titles = mDbHelper.getUpcomingTitles(mDate);
        upcoming_dates = mDbHelper.getUpcomingDates(mDate);
        upcoming_times = mDbHelper.getUpcomingTimes(mDate);
        upcoming_notes = mDbHelper.getUpcomingNotes(mDate);
        int size3 = upcoming_titles.size();
        if (size3 == 0) {
            ll3.setVisibility(View.GONE);
        }
        else {
            ll3.setVisibility(View.VISIBLE);
            RecyclerView recyclerView3 = findViewById(R.id.recyclerview3);
            recyclerView3.setLayoutManager(new LinearLayoutManager(this));
            adapter3 = new AdapterRv3(this, upcoming_titles, upcoming_dates, upcoming_times, upcoming_notes);
            adapter3.setClickListener(this);
            recyclerView3.setAdapter(adapter3);
        }
    }

    @Override
    public void onItemClick1(View view, int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_viewtaskdetail, viewGroup, false);
        TextView titled = dialogView.findViewById(R.id.dTitle);
        TextView dated = dialogView.findViewById(R.id.dDate);
        TextView timed = dialogView.findViewById(R.id.dTime);
        TextView noted = dialogView.findViewById(R.id.dNote);
        ImageButton deleteTask1 = dialogView.findViewById(R.id.deleteTask);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        deleteTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mDbHelper.deleteTask(adapter1.getTitle(position), null, null, null);
                callDbDataAndUpdateRecyclerView();
            }
        });
        alertDialog.show();
        titled.setText(adapter1.getTitle(position));
        dated.setText(adapter1.getDate(position));
        timed.setText(adapter1.getTime(position));
        noted.setText(adapter1.getNote(position));
    }

    @Override
    public void onItemClick2(View view, int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_viewtaskdetail, viewGroup, false);
        TextView titled = dialogView.findViewById(R.id.dTitle);
        TextView dated = dialogView.findViewById(R.id.dDate);
        TextView timed = dialogView.findViewById(R.id.dTime);
        TextView noted = dialogView.findViewById(R.id.dNote);
        ImageButton deleteTask1 = dialogView.findViewById(R.id.deleteTask);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        deleteTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mDbHelper.deleteTask(adapter2.getTitle(adapterPosition), null, null, null);
                callDbDataAndUpdateRecyclerView();
            }
        });
        alertDialog.show();
        titled.setText(adapter2.getTitle(adapterPosition));
        dated.setText(adapter2.getDate(adapterPosition));
        timed.setText(adapter2.getTime(adapterPosition));
        noted.setText(adapter2.getNote(adapterPosition));
    }

    @Override
    public void onItemClick3(View view, int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_viewtaskdetail, viewGroup, false);
        TextView titled = dialogView.findViewById(R.id.dTitle);
        TextView dated = dialogView.findViewById(R.id.dDate);
        TextView timed = dialogView.findViewById(R.id.dTime);
        TextView noted = dialogView.findViewById(R.id.dNote);
        ImageButton deleteTask1 = dialogView.findViewById(R.id.deleteTask);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        deleteTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mDbHelper.deleteTask(adapter3.getTitle(adapterPosition), null, null, null);
                callDbDataAndUpdateRecyclerView();
            }
        });
        alertDialog.show();
        titled.setText(adapter3.getTitle(adapterPosition));
        dated.setText(adapter3.getDate(adapterPosition));
        timed.setText(adapter3.getTime(adapterPosition));
        noted.setText(adapter3.getNote(adapterPosition));
    }

    // INFLATES MENU FOR LOGOUT
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    // GIVES MENU LOGOUT THE FUNCTION TO LOGOUT
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(this, AddTheTaskScreen.class);
            intent.putExtra("Dbcount",mDbSize);
            startActivity(intent);
        }
        return true;
    }

    public void clearTable(View view) {
        mDbHelper.deleteTable();
        callDbDataAndUpdateRecyclerView();
    }
}