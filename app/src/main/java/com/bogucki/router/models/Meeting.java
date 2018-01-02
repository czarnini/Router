package com.bogucki.router.models;


/**
 * Created by Michał Bogucki
 */

public class Meeting {

    private String pushId;
    private String client;
    private String address;
    private String reason;
    private long earliestTimeOfDelivery;
    private long latestTimeOfDelivery;
    private int meetingOrder = -1;


    public Meeting(String pushId, String client, String address, String reason,
                   long earliestTimeOfDelivery, long latestTimeOfDelivery) {
        this.pushId = pushId;
        this.client = client;
        this.address = address;
        this.reason = reason;
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
        this.latestTimeOfDelivery = latestTimeOfDelivery;
        meetingOrder = -1;
    }

    public Meeting(String pushId, String client, String address, String reason, long earliestTimeOfDelivery, long latestTimeOfDelivery, int meetingOrder) {
        this.pushId = pushId;
        this.client = client;
        this.address = address;
        this.reason = reason;
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
        this.latestTimeOfDelivery = latestTimeOfDelivery;
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

    public long getEarliestTimeOfDelivery() {
        return earliestTimeOfDelivery;
    }

    public void setEarliestTimeOfDelivery(long earliestTimeOfDelivery) {
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
    }

    public long getLatestTimeOfDelivery() {
        return latestTimeOfDelivery;
    }

    public void setLatestTimeOfDelivery(long latestTimeOfDelivery) {
        this.latestTimeOfDelivery = latestTimeOfDelivery;
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
}
