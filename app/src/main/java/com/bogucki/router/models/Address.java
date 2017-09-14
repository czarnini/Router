package com.bogucki.router.models;

/**
 * Created by Michał Bogucki
 */

public class Address {
    private int     id;
    private String  street;
    private String  buildingNo;
    private String  localNo;

    //Firebase real-time database needs empty constructor
    public Address() {
    }

    public Address(int id, String street, String buildingNo, String localNo, String city, String postalCode) {
        this.id = id;
        this.street = street;
        this.buildingNo = buildingNo;
        this.localNo = localNo;
        this.city = city;
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getLocalNo() {
        return localNo;
    }

    public void setLocalNo(String localNo) {
        this.localNo = localNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    private String  city;
    private String  postalCode;
}
