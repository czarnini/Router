package com.bogucki.router.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;


/**
 * Created by Michał Bogucki
 */

public class dbHelper extends SQLiteOpenHelper implements Client, Meeting {
    private static final String TAG = dbHelper.class.getSimpleName();

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "ROUTER_LOCAL_DB";

    static final String CLIENT_TABLE_NAME = "Clients";
    static final String CLIENT_COLUMN_ID = "id";
    static final String CLIENT_COLUMN_NAME = "name";
    static final String CLIENT_COLUMN_ADDRESS = "address";
    static final String[] CLIENT_COLUMNS = {CLIENT_COLUMN_ID, CLIENT_COLUMN_NAME, CLIENT_COLUMN_ADDRESS};


    static final String MEETING_TABLE_NAME = "Meetings";
    static final String MEETING_COLUMN_ID = "id";
    static final String MEETING_COLUMN_CLIENT = "client_id";
    static final String MEETING_COLUMN_EARLIEST_DATE = "earliest_hour";
    static final String MEETING_COLUMN_LATEST_DATE = "latest_hour";
    static final String[] MEETINGS_COLUMNS = {MEETING_COLUMN_ID, MEETING_COLUMN_CLIENT, MEETING_COLUMN_EARLIEST_DATE, MEETING_COLUMN_LATEST_DATE};


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

    private static final String DROP_DB_SCRIPT = /*" DROP TABLE IF EXISTS " + MEETING_TABLE_NAME + "; " +*/
                                                 " DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME + "; " ;

    private MicroOrm uOrm = new MicroOrm();
    private SQLiteDatabase database;


    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: DB CREATED WITH FOLLOWING SCRIPT:" + CREATE_DATABASE);
        getWritableDatabase().execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Upgrading DB to " + newVersion);
        database.execSQL(DROP_DB_SCRIPT);
        onCreate(sqLiteDatabase);
    }

    public void dropDB(){
        database.execSQL(DROP_DB_SCRIPT);

    }

    //Create
    @Override
    public long addClient(ClientEntity client) {
        long insertedID = getWritableDatabase().insertWithOnConflict(CLIENT_TABLE_NAME, null, uOrm.toContentValues(client), SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "addClient: inserted client row: " + client + " into db, id = " + insertedID);
        return insertedID;
    }



    //Read
    @Override
    public ClientEntity getClientById(long id) {
        String selcetion = CLIENT_COLUMN_ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        try (Cursor cursor = getWritableDatabase().query(CLIENT_TABLE_NAME, CLIENT_COLUMNS, selcetion, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                ClientEntity selectedClient = uOrm.fromCursor(cursor, ClientEntity.class);
                Log.d(TAG, "getClientById: id = " + id + "\nClient: " + selectedClient);
                return selectedClient;
            } else {
                return new ClientEntity(-1);
            }
        }
    }

    @Override
    public ArrayList<ClientEntity> getClients() {
        return null;
    }

    //Update
    @Override
    public int updateClientById(long id) {
        return 0;
    }


    //Delete
    @Override
    public int deleteClientById(long id) {
        return 0;
    }

}
