
package com.example.mustafa.bloodbank.data.model.donationrequestnotifi;

import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DonationRequestNotifi {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DonationRequest data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DonationRequest getData() {
        return data;
    }

    public void setData(DonationRequest data) {
        this.data = data;
    }

}
