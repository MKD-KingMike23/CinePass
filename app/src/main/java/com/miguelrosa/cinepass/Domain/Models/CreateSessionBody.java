package com.miguelrosa.cinepass.Domain.Models;

import com.google.gson.annotations.SerializedName;

public class CreateSessionBody {
    @SerializedName("request_token")
    private String requestToken;

    public CreateSessionBody(String requestToken) {
        this.requestToken = requestToken;
    }
}
