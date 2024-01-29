package com.example.handshake;

// This class is used to store all relevant information for the search page
public class Donation {
    String donationName, donationInfo, donorID, donorName, donorPhone, donorInfo, donorRate, key;

    public String getName() {
        return donationName;
    }

    public String getInfo() {
        return donationInfo;
    }

    public String getDonorID(){return donorID;}

    public String getDonorName() {
        return donorName;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public String getDonorInfo() {
        return donorInfo;
    }

    public String getDonorRate() {
        return donorRate;
    }

    public String getKey() {
        return this.key;
    }

    public void setName(String name) {
        this.donationName = name;
    }

    public void setInfo(String info) {
        this.donationInfo = info;
    }

    public void setDonorID(String donorID){ this.donorID = donorID;
    }

    public void setUsername(String username) {
        this.donorName = username;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public void setDonorInfo(String donorInfo) {
        this.donorInfo = donorInfo;
    }

    public void setDonorRate(String donorRate) {
        this.donorRate = donorRate;
    }


    public String getDonationId() {
        return this.key;
    }
}