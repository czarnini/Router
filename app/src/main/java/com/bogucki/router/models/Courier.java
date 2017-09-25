package com.bogucki.router.models;

/**
 * Created by Michał Bogucki
 */


public class Courier {
    private int id;
    private String name;
    private String surname;

    //Firebase real-time database needs empty constructor
    public Courier() {
    }

    public Courier(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
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
