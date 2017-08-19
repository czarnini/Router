package com.bogucki.router.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Michał Bogucki
 */

public class dbHelper extends SQLiteOpenHelper {
    private static final String TAG = dbHelper.class.getSimpleName();

    static final String CLIENT_TABLE_NAME = "Clients";
    static final String CLIENT_COLUMN_ID = "id";
    static final String CLIENT_COLUMN_NAME = "name";
    static final String CLIENT_COLUMN_ADDRESS = "address";
    static final String MEETING_TABLE_NAME = "Meetings";
    static final String MEETING_COLUMN_ID = "id";
    static final String MEETING_COLUMN_CLIENT = "client_id";
    static final String MEETING_COLUMN_EARLIEST_DATE = "earliest_hour";
    static final String MEETING_COLUMN_LATEST_DATE = "latest_hour";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ROUTER_LOCAL_DB";
    static private final String CREATE_CLIENT_TABLE =
            "CREATE TABLE " + CLIENT_TABLE_NAME + "( " +
                    CLIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CLIENT_COLUMN_NAME + " TEXT NOT NULL, " +
                    CLIENT_COLUMN_ADDRESS + " TEXT NOT NULL); ";

    static private final String CREATE_MEETING_TABLE =
            "CREATE TABLE " + MEETING_TABLE_NAME + "( " +
                    MEETING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    MEETING_COLUMN_CLIENT + " INTEGER, " +
                    "FOREIGN KEY (" + MEETING_COLUMN_CLIENT + ") " +
                    "REFERENCES " + CLIENT_TABLE_NAME + "(" + CLIENT_COLUMN_ID + ")," +

                    MEETING_COLUMN_EARLIEST_DATE + " TEXT NOT NULL, " +
                    MEETING_COLUMN_LATEST_DATE + " TEXT NOT NULL ); ";

    static private final String CREATE_DATABASE = CREATE_CLIENT_TABLE + CREATE_MEETING_TABLE;

    private static final String UPGRADE_SCRIPT = " DROP TABLE IF EXISTS " + MEETING_TABLE_NAME + "; " +
                                                 " DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME + "; " ;


    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: DB CREATED WITH FOLOWING SCRIPT:" + CREATE_DATABASE);
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Upgrading DB to " + newVersion);
        sqLiteDatabase.execSQL(UPGRADE_SCRIPT);
        onCreate(sqLiteDatabase);
    }

}
