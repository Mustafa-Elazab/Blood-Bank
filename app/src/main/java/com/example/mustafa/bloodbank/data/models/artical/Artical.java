
package com.example.mustafa.bloodbank.data.models.artical;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artical {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ArticalPagination data;

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

    public ArticalPagination getData() {
        return data;
    }

    public void setData(ArticalPagination data) {
        this.data = data;
    }

}
