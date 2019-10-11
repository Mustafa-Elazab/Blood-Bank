
package com.example.mustafa.bloodbank.data.models.donate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donate {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DonatePagination data;

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

    public DonatePagination getData() {
        return data;
    }

    public void setData(DonatePagination data) {
        this.data = data;
    }

}
