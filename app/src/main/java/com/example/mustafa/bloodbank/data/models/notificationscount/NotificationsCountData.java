
package com.example.mustafa.bloodbank.data.models.notificationscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsCountData {

    @SerializedName("notifications-count")
    @Expose
    private Integer notificationsCount;

    public Integer getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(Integer notificationsCount) {
        this.notificationsCount = notificationsCount;
    }

}
