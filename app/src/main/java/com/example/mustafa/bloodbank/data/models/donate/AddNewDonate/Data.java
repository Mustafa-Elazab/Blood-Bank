package com.example.mustafa.bloodbank.data.models.donate.AddNewDonate;

import com.example.mustafa.bloodbank.data.models.donate.DonateData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("donationRequest")
    @Expose
    private DonateData donationRequest;

    public DonateData getDonationRequest() {
        return donationRequest;
    }

    public void setDonationRequest(DonateData donationRequest) {
        this.donationRequest = donationRequest;
    }

}
