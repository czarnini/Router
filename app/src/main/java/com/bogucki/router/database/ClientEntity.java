package com.bogucki.router.database;

import android.provider.BaseColumns;

import org.chalup.microorm.annotations.Column;

import java.util.Locale;

import static com.bogucki.router.database.DbHelper.CLIENT_COLUMN_ADDRESS;
import static com.bogucki.router.database.DbHelper.CLIENT_COLUMN_ID;
import static com.bogucki.router.database.DbHelper.CLIENT_COLUMN_NAME;

/**
 * Created by Michał Bogucki
 */

public class ClientEntity implements BaseColumns{
    @Column(CLIENT_COLUMN_ID) private long mId;
    @Column(CLIENT_COLUMN_NAME)    private String mName;
    @Column(CLIENT_COLUMN_ADDRESS) private String mAddress;


    public ClientEntity(String name, String address) {

        mId = -1;
        this.mName = name;
        this.mAddress = address;
    }

    public ClientEntity(long mId, String mName, String mAddress) {
        this.mId = mId;
        this.mName = mName;
        this.mAddress = mAddress;
    }

    public ClientEntity(int mId) {
        this.mId = mId;
        this.mAddress = this.mName = "";
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "mId = %d, mName = %s, mAddress = %s",
                mId, mName, mAddress);
    }

    public long getId() {
        return mId;
    }

    /*public void setId(int mId) {
        this.mId = mId;
    }
*/
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setId(long mId) {
        this.mId = mId;
    }


}
