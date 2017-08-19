package com.bogucki.router.database;

import org.chalup.microorm.annotations.Column;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Michał Bogucki
 */

public class MeetingEntity {
    @Column(dbHelper.MEETING_COLUMN_ID)
    private int mId;

    @Column(dbHelper.MEETING_COLUMN_CLIENT)
    private int mClientId;

    @Column(dbHelper.MEETING_COLUMN_EARLIEST_DATE)
    private Date mEarliestDate;

    @Column(dbHelper.MEETING_COLUMN_LATEST_DATE)
    private Date mLatestDate;

    public MeetingEntity(int clientId, Date earliestDate, Date latestDate) {
        this.mClientId = clientId;
        this.mEarliestDate = earliestDate;
        this.mLatestDate = latestDate;
        this.mId = -1;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                " mId = %d, mClientId = %d, mEarliestDate = %s, mLatestDate = %s",
                mId, mClientId, mEarliestDate, mLatestDate);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getClientId() {
        return mClientId;
    }

    public void setClientId(int clientId) {
        this.mClientId = clientId;
    }

    public Date getEarliestDate() {
        return mEarliestDate;
    }

    public void setEarliestDate(Date earliestDate) {
        this.mEarliestDate = earliestDate;
    }

    public Date getLatestDate() {
        return mLatestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.mLatestDate = latestDate;
    }
}
