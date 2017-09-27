package com.bogucki.router.models;

import java.util.HashMap;

/**
 * Created by Michał Bogucki
 */


public class    Courier {
    private int id;
    private String name;
    private String surname;

    public HashMap<Integer, Boolean> getCourierMeetings() {
        return courierMeetings;
    }

    public void setCourierMeetings(HashMap<Integer, Boolean> courierMeetings) {
        this.courierMeetings = courierMeetings;
    }

    private HashMap<Integer, Boolean> courierMeetings;

    //Firebase real-time database needs empty constructor
    public Courier() {
    }

    public Courier(int id, String name, String surname, HashMap<Integer, Boolean> courierMeetings) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.courierMeetings = courierMeetings;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
