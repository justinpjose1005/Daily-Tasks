package com.justinpjose.ptimetableapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class mDbHelper extends SQLiteOpenHelper {
    public mDbHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks (id integer primary key, title text, date text, time text, note text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String sTitle, String sDate, String sTime, String sNote) {
        int count = getCount() + 1;
        SQLiteDatabase wSqDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("tableCounter", String.valueOf(count));
        contentValues.put("id", count);
        contentValues.put("title", sTitle);
        contentValues.put("date", sDate);
        contentValues.put("time", sTime);
        contentValues.put("note", sNote);
        wSqDb.insert("tasks", null, contentValues);
        wSqDb.close();
    }

    public int getCount() {
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        int count = (int) DatabaseUtils.queryNumEntries(rSqDb, "tasks");
        rSqDb.close();
        return count;
    }

    public void deleteTask(String title, String date, String time, String note) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "title=?";
        String whereArgs = title;
        db.delete("tasks", whereClause, new String[]{whereArgs});
        return;
    }

//    public String getData() {
//        String result = "";
//        SQLiteDatabase rSqDb = this.getReadableDatabase();
//        Cursor cursor = rSqDb.rawQuery("select * from tasks", null);
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String title = cursor.getString(1);
//            String date = cursor.getString(2);
//            String time = cursor.getString(3);
//            String note = cursor.getString(4);
//            result += title + date + time + note + "\n";
//        }
//        rSqDb.close();
//        return result;
//    }

    // Todays Task List
    public List<String> getTodaysTitles(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date=\""+ mDate +"\"", null);
        while (cursor.moveToNext()) {
            values.add(cursor.getString(1));
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTodaysDates(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            values.add(cursor.getString(2));
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTodaysTimes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            values.add(cursor.getString(3));
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTodaysNotes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            values.add(cursor.getString(4));
        }
        rSqDb.close();
        return values;
    }

    // Tomorrows Task List
    public List<String> getTomorrowsTitles(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (tomorrowsDate(d,mDate)) {
                values.add(cursor.getString(1));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTomorrowsDates(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (tomorrowsDate(d,mDate)) {
                values.add(cursor.getString(2));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTomorrowsTimes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (tomorrowsDate(d,mDate)) {
                values.add(cursor.getString(3));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getTomorrowsNotes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (tomorrowsDate(d,mDate)) {
                values.add(cursor.getString(4));
            }
        }
        rSqDb.close();
        return values;
    }

    // Tomorrows Task Finder
    private boolean tomorrowsDate(String d, String mDate) {
        String[] a = d.split("/");
        String[] b = mDate.split("/");
        int x = Integer.parseInt(a[0]);
        int y = Integer.parseInt(b[0]);
        int z = x - y;
        if (a[2].equals(b[2]) && a[1].equals(b[1]) && z == 1) {
            return true;
        }
        return false;
    }

    // Upcoming Task List
    public List<String> getUpcomingTitles(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (upcomingDates(d,mDate)) {
                values.add(cursor.getString(1));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getUpcomingDates(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (upcomingDates(d,mDate)) {
                values.add(cursor.getString(2));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getUpcomingTimes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (upcomingDates(d,mDate)) {
                values.add(cursor.getString(3));
            }
        }
        rSqDb.close();
        return values;
    }

    public List<String> getUpcomingNotes(String mDate) {
        List<String> values = new ArrayList<>();
        SQLiteDatabase rSqDb = this.getReadableDatabase();
        Cursor cursor = rSqDb.rawQuery("select * from tasks where date!=\""+mDate+"\"", null);
        while (cursor.moveToNext()) {
            String d = cursor.getString(2);
            if (upcomingDates(d,mDate)) {
                values.add(cursor.getString(4));
            }
        }
        rSqDb.close();
        return values;
    }

    // Upcoming Task Finder
    private boolean upcomingDates(String d, String mDate) {
        String[] a = d.split("/");
        String[] b = mDate.split("/");
        int m = Integer.parseInt(a[0]);
        int n = Integer.parseInt(a[1]);
        int o = Integer.parseInt(a[2]);
        int x = Integer.parseInt(b[0]);
        int y = Integer.parseInt(b[1]);
        int z = Integer.parseInt(b[2]);
        if ((m>x && n>=y && o>=z && x+1!=m) || (m<=x && n>y && o>=z)|| (m<=x && n<=y && o>z)) {
            return true;
        }
        return false;
    }

    // Delete Table
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tasks");
        db.close();
    }
}
