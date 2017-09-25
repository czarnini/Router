package com.bogucki.router.models;

/**
 * Created by Michał Bogucki
 */

public class Meeting {
    private int clientID;
    private int courierID;
    private long earliestTimeOfDelivery;
    private long latestTimeOfDelivery;

    //Firebase real-time database needs empty constructor
    public Meeting() {
    }

    public Meeting(int clientID, int courierID, long earliestTimeOfDelivery, long latestTimeOfDelivery) {
        this.clientID = clientID;
        this.courierID = courierID;
        this.earliestTimeOfDelivery = earliestTimeOfDelivery;
        this.latestTimeOfDelivery = latestTimeOfDelivery;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getCourierID() {
        return courierID;
    }

    public void setCourierID(int courierID) {
        this.courierID = courierID;
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
}
