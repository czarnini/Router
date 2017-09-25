package com.bogucki.router.models;

/**
 * Created by Michał Bogucki
 */


public class Client {
    private long id;
    private String name;
    private int defaultAddress;

    //Firebase real-time database needs empty constructor
    public Client() {
    }

    public Client(long id, String name, int address) {
        this.id = id;
        this.name = name;
        this.defaultAddress = address;
    }

    public long getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddress() {
        return defaultAddress;
    }

    public void setAddress(int address) {
        this.defaultAddress = address;
    }

    public void setId(long mId) {
        this.id = mId;
    }

}
