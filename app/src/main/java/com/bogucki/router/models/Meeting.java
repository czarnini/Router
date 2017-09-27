package com.bogucki.router.models;

/**
 * Created by Michał Bogucki
 */

public class Meeting {


    //TODO courierID?
    private String pushId;
    private String client;
    private String address;
    private String reason; //TODO czy to na pewno zostaje?
    private String earliestTimeOfDelivery;
    private String latestTimeOfDelivery;


    public Meeting(String pushId, String client, String address, String reason,
                   String earliestTimeOfDelivery, String latestTimeOfDelivery) {
        this.pushId = pushId;
        this.client = client;
        this.address = address;
        this.reason = reason;
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
        this.latestTimeOfDelivery = latestTimeOfDelivery;
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

    public String getEarliestTimeOfDelivery() {
        return earliestTimeOfDelivery;
    }

    public void setEarliestTimeOfDelivery(String earliestTimeOfDelivery) {
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
    }

    public String getLatestTimeOfDelivery() {
        return latestTimeOfDelivery;
    }

    public void setLatestTimeOfDelivery(String latestTimeOfDelivery) {
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
}
