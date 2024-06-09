package com.miguelrosa.cinepass.Domain.Responses;

public class FavoriteResponse {
    private int status_code;
    private String status_message;

    public int getStatusCode() {
        return status_code;
    }

    public void setStatusCode(int statusCode) {
        this.status_code = statusCode;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String statusMessage) {
        this.status_message = statusMessage;
    }
}
