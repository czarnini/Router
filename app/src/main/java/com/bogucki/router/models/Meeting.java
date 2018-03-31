package com.bogucki.router.models;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michał Bogucki
 */

public class Meeting {

    private String pushId;
    private String client;
    private String address;
    private String reason;
    private long earliestTimePossible;
    private long latestTimePossible;
    private int meetingOrder = -1;


    public Meeting(String pushId, String client, String address, String reason,
                   long earliestTimePossible, long latestTimePossible) {
        this.pushId = pushId;
        this.client = client;
        this.address = address;
        this.reason = reason;
        this.earliestTimePossible = earliestTimePossible;
        this.latestTimePossible = latestTimePossible;
        meetingOrder = -1;
    }

    public Meeting(String pushId, String client, String address, String reason, long earliestTimePossible, long latestTimePossible, int meetingOrder) {
        this.pushId = pushId;
        this.client = client;
        this.address = address;
        this.reason = reason;
        this.earliestTimePossible = earliestTimePossible;
        this.latestTimePossible = latestTimePossible;
        this.meetingOrder = meetingOrder;
    }

    public String getClient() {

        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getEarliestTimePossible() {
        return earliestTimePossible;
    }

    public void setEarliestTimePossible(long earliestTimePossible) {
        this.earliestTimePossible = earliestTimePossible;
    }

    public long getLatestTimePossible() {
        return latestTimePossible;
    }

    public void setLatestTimePossible(long latestTimePossible) {
        this.latestTimePossible = latestTimePossible;
    }


    //Firebase real-time database needs empty constructor
    public Meeting() {
    }


    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getMeetingOrder() {
        return meetingOrder;
    }

    public void setMeetingOrder(int meetingOrder) {
        this.meetingOrder = meetingOrder;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pushId",                   pushId);
        result.put("client",                   client);
        result.put("address",                  address);
        result.put("reason",                   reason);
        result.put("earliestTimePossible", earliestTimePossible);
        result.put("latestTimePossible", latestTimePossible);
        result.put("meetingOrder",             meetingOrder);
        return result;
    }
}
