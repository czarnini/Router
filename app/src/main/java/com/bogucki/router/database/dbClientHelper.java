package com.bogucki.router.database;

import org.chalup.microorm.annotations.Column;

import static com.bogucki.router.database.dbHelper.CLIENT_COLUMN_ADDRESS;
import static com.bogucki.router.database.dbHelper.CLIENT_COLUMN_ID;
import static com.bogucki.router.database.dbHelper.CLIENT_COLUMN_NAME;

/**
 * Created by Michał on 19.08.2017.
 */

public class dbClientHelper implements Client {

    @Column(CLIENT_COLUMN_ID)      private int mId;
    @Column(CLIENT_COLUMN_NAME)    private int mName;
    @Column(CLIENT_COLUMN_ADDRESS) private int mAddress;

    @Override
    public long addClient() {
        return 0;
    }
}
