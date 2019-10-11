
package com.example.mustafa.bloodbank.data.models.donationrequestnotifi;
import com.example.mustafa.bloodbank.data.models.donate.DonateData;
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
    private DonateData data;

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

    public DonateData getData() {
        return data;
    }

    public void setData(DonateData data) {
        this.data = data;
    }

}
