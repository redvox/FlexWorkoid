package com.flexworkoid.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;

/**
 * Created by red on 27.01.14.
 */
public class SQHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "flexworkoid";
    private static final int DATABASE_VERSION = 1;

    // TABLE Dates
    public static final String TABLE_DATES = "dates";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE_DAY = "day";
    public static final String KEY_DATE_MONTH = "month";
    public static final String KEY_DATE_YEAR = "year";
    public static final String KEY_DATE_WEEKDAY = "weekday";
    public static final String KEY_DATE_WEEKNUM = "weeknum";
    public static final String KEY_DATE_START_HOUR = "starttime_hour";
    public static final String KEY_DATE_START_MINUTE = "starttime_minute";
    public static final String KEY_DATE_END_HOUR = "endtime_hour";
    public static final String KEY_DATE_END_MINUTE = "endtime_minute";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_STARTTIMEMILLIES = "starttimemillies";
    public static final String KEY_ENDTIMEMILLIES = "endtimemillies";
    public static final String KEY_TYPE = "type";
    public static final String KEY_PROJECT = "theme";

    static final String CREATE_DATES_TABLE = "create table " + TABLE_DATES
            + "("
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_DATE_DAY + " integer not null, "
            + KEY_DATE_MONTH + " integer not null,"
            + KEY_DATE_YEAR + " integer not null,"
            + KEY_DATE_WEEKDAY + " integer not null,"
            + KEY_DATE_WEEKNUM + " integer not null,"
            + KEY_DATE_START_HOUR + " integer not null,"
            + KEY_DATE_START_MINUTE + " integer not null,"
            + KEY_DATE_END_HOUR + " integer not null,"
            + KEY_DATE_END_MINUTE + " integer not null,"
            + KEY_DURATION + " integer not null,"
            + KEY_TYPE + " integer not null,"
            + KEY_PROJECT + " integer,"
            + KEY_STARTTIMEMILLIES + " text not null,"
            + KEY_ENDTIMEMILLIES + " text not null"
            + ");";

    SQLiteDatabase db;

    public SQHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        db = database;
        createTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        db = database;
        //Log.e(SQHelper.class.getName(), "Upgrading database from version " + oldVersion +" to "+newVersion);

//        if(oldVersion <= 5){
//            Log.e(SQHelper.class.getName(), "++ Datebase Upgrade 5 ++");
//            String sql2 = "ALTER TABLE "+TABLE_THEME+" "
//                    +"ADD "+KEY_OVERALLTIME_W_CHILDREN+" integer not null default 0";
//            db.execSQL(sql2);
//        }
    }

    public void createTables(SQLiteDatabase database) {
        database.execSQL(CREATE_DATES_TABLE);
    }

    public void insertDate(int tag, int minutesPast, String theme) {
        openDatabase();

        DateTime endDate = new DateTime();
        DateTime startDate = endDate.minusMinutes(minutesPast);
        ContentValues newContent = new ContentValues();
        newContent.put(KEY_DATE_DAY, startDate.getDayOfMonth());
        newContent.put(KEY_DATE_MONTH, startDate.getMonthOfYear());
        newContent.put(KEY_DATE_YEAR, startDate.getYear());
        newContent.put(KEY_DATE_WEEKDAY, startDate.getDayOfWeek());
        newContent.put(KEY_DATE_WEEKNUM, startDate.getWeekOfWeekyear());
        newContent.put(KEY_DATE_START_HOUR, startDate.getHourOfDay());
        newContent.put(KEY_DATE_START_MINUTE, startDate.getMinuteOfHour());
        newContent.put(KEY_DATE_END_HOUR, endDate.getHourOfDay());
        newContent.put(KEY_DATE_END_MINUTE, endDate.getMinuteOfHour());
        newContent.put(KEY_TYPE, tag);
        newContent.put(KEY_DURATION, minutesPast);
//        newContent.put(KEY_PROJECT, getTheme(theme));
        newContent.put(KEY_STARTTIMEMILLIES, startDate.getMillis());
        newContent.put(KEY_ENDTIMEMILLIES, endDate.getMillis());
        db.insert(TABLE_DATES, null, newContent);
    }

    public void renewTables() {
        openDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATES);
        createTables(db);
    }

    private void openDatabase() {
        if (db == null){
            db = getWritableDatabase();
        }
    }

    public Cursor getDatesCursor() {
        openDatabase();
        return db.query(TABLE_DATES, null, null, null, null, null,
                KEY_ROWID);
    }

//    public Cursor getThemeCursor() {
//        openDatabase();
//        return db.query(TABLE_THEME, null, null, null, null, null,
//                KEY_OVERALLTIME+ " DESC");
//    }

    public Cursor getCursorForDay(int day, int month, int year){
        openDatabase();
        return db.query(TABLE_DATES, null, KEY_DATE_DAY +" = "+day+" AND "+KEY_DATE_MONTH +" = "+month+" AND "+KEY_DATE_YEAR +" = "+year, null, null, null,
                null);
    }
}
