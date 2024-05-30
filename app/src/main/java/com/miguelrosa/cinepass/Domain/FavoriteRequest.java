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

    public String getMediaType() {
        return media_type;
    }

    public void setMediaType(String mediaType) {
        this.media_type = mediaType;
    }

    public int getMediaId() {
        return media_id;
    }

    public void setMediaId(int mediaId) {
        this.media_id = mediaId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
