package com.miguelrosa.cinepass.Domain.Responses;

import com.google.gson.annotations.SerializedName;

public class AccountDetailsResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("avatar")
    private Avatar avatar;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public static class Avatar {
        @SerializedName("tmdb")
        private TmdbAvatar tmdb;

        public TmdbAvatar getTmdb() {
            return tmdb;
        }
    }

    public static class TmdbAvatar {
        @SerializedName("avatar_path")
        private String avatarPath;

        public String getAvatarPath() {
            return avatarPath;
        }
    }
}
