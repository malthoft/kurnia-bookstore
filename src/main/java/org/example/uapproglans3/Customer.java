package org.example.uapproglans3;

import java.io.Serializable;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String gender;
    private String address;

    public Customer(String id, String name, String gender, String address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
