package com.example.handshake;

// This class is used to store all relevant information for the search page
public class Donation {
    String name, info, donorName;

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }



    public void setUsername(String username) {
        this.donorName = username;
    }
}
