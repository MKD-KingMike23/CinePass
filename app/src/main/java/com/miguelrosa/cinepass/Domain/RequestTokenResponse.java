package com.miguelrosa.cinepass.Domain;

import com.google.gson.annotations.SerializedName;

public class RequestTokenResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("request_token")
    private String requestToken;

    public boolean isSuccess() {
        return success;
    }

    public String getRequestToken() {
        return requestToken;
    }
}
