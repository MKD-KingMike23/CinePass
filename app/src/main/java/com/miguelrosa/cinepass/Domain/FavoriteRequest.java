package com.miguelrosa.cinepass.Domain;

// FavoriteRequest.java
public class FavoriteRequest {
    private String media_type;
    private int media_id;
    private boolean favorite;

    public FavoriteRequest(String mediaType, int mediaId, boolean favorite) {
        this.media_type = mediaType;
        this.media_id = mediaId;
        this.favorite = favorite;
    }
    // Getters and setters...
}
