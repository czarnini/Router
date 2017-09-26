package com.bogucki.router.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michał Bogucki
 */


public class Client {
    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    private String pushID;
    private String name;
    private String address;

    //Firebase real-time database needs empty constructor
    public Client() {
    }

    public Client(String pushID, String name, String address) {
        this.pushID = pushID;
        this.name = name;
        this.address = address;
    }

    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        return result;
    }
}
