
package com.example.mustafa.bloodbank.data.models.userData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientAllData {

    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("client")
    @Expose
    private ClientData client;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public ClientData getClient() {
        return client;
    }

    public void setClient(ClientData client) {
        this.client = client;
    }

}
